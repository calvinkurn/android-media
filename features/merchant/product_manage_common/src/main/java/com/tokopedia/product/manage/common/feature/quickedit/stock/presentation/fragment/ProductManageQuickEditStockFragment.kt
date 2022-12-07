package com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.databinding.FragmentQuickEditStockBinding
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.ext.*
import com.tokopedia.product.manage.common.feature.list.ext.hasEditProductAccess
import com.tokopedia.product.manage.common.feature.list.ext.isActive
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import javax.inject.Inject

class ProductManageQuickEditStockFragment(
    private var onFinishedListener: OnFinishedListener? = null,
    private var campaignListener: ProductCampaignInfoListener? = null
) : BottomSheetUnify(), HasComponent<ProductManageQuickEditStockComponent> {

    companion object {
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"
        private const val KEY_CACHE_MANAGER_ID = "product_edit_cache_manager"
        private const val KEY_PRODUCT = "product"

        fun createInstance(
            context: Context,
            product: ProductUiModel,
            onFinishedListener: OnFinishedListener,
            campaignListener: ProductCampaignInfoListener,
        ): ProductManageQuickEditStockFragment {
            SaveInstanceCacheManager(context, KEY_CACHE_MANAGER_ID).apply {
                put(KEY_PRODUCT, product)
            }
            return ProductManageQuickEditStockFragment(onFinishedListener, campaignListener)
        }
    }

    @Inject
    lateinit var viewModel: ProductManageQuickEditStockViewModel

    private val cacheManager by lazy {
        SaveInstanceCacheManager(requireContext(), KEY_CACHE_MANAGER_ID)
    }

    private var initialStock: Int? = null
    private var initialStatus: ProductStatus? = null
    private var product: ProductUiModel? = null

    private var binding: FragmentQuickEditStockBinding? = null

    private var firstStateChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
        binding = FragmentQuickEditStockBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        setChild(binding?.root)
        setTitle(getString(R.string.product_manage_quick_edit_stock_title))
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        initInjector()
    }

    private fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            cacheManager.get<ProductUiModel>(KEY_PRODUCT, ProductUiModel::class.java, null)?.apply {
                initialStock = stock
                initialStatus = status
                product = this
            }
        } else {
            product =
                cacheManager.get<ProductUiModel>(KEY_PRODUCT, ProductUiModel::class.java, null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        firstStateChecked = true
    }

    override fun getComponent(): ProductManageQuickEditStockComponent? {
        return activity?.run {
            DaggerProductManageQuickEditStockComponent
                .builder()
                .productManageCommonComponent(ProductManageCommonInstance.getComponent(application))
                .build()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager.put(KEY_PRODUCT, product)
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView(view: View) {
        observeStatus()
        observeStock()
        observeStockTicker()

        setupQuantityEditor()
        setStockAndStatus()
        setupSaveButton()
        setupStatusSwitch()
        setupBottomSheet()
        setupCampaignInfo(view)

        requestStockEditorFocus()
        setAddButtonClickListener()
        setSubtractButtonClickListener()
        getStockTicker()
        setMaxStock()
    }

    private fun setStockAndStatus() {
        val stock = product.getStock()
        val status = product.getStatus()

        binding?.quickEditStockQuantityEditor?.setValue(stock)
        viewModel.updateStock(stock)
        viewModel.updateStatus(status)
    }

    private fun requestStockEditorFocus() {
        binding?.quickEditStockQuantityEditor?.editText?.requestFocus()
    }

    private fun disableStockEditor(stock: Int) {
        binding?.quickEditStockQuantityEditor?.hide()
        binding?.textStock?.show()
        binding?.textStock?.text = stock.toString()
        setupIconInfo(product?.stock.orZero())
    }

    private fun setupStatusSwitch() {
        binding?.quickEditStockActivateSwitch?.isChecked = product.isActive()

        if (product.hasEditProductAccess()) {
            binding?.quickEditStockActivateSwitch?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.updateStatus(ProductStatus.ACTIVE)
                } else {
                    viewModel.updateStatus(ProductStatus.INACTIVE)
                }

                if (firstStateChecked) {
                    if (isChecked) {
                        ProductManageTracking.eventEditStockToggle(TOGGLE_ACTIVE, product.getId())
                    } else {
                        ProductManageTracking.eventEditStockToggle(
                            TOGGLE_NOT_ACTIVE,
                            product.getId()
                        )
                    }
                }
            }
        } else {
            binding?.quickEditStockActivateSwitch?.isEnabled = false
        }
    }

    private fun setupSaveButton() {
        if (product.hasEditStockAccess() || product.hasEditProductAccess()) {
            binding?.quickEditStockSaveButton?.setOnClickListener {
                onClickSaveBtn()
            }
            binding?.quickEditStockSaveButton?.show()
        } else {
            binding?.quickEditStockSaveButton?.hide()
        }
    }

    private fun onClickSaveBtn() {
        if (binding?.quickEditStockQuantityEditor?.editText?.text.isNullOrEmpty()) {
            binding?.quickEditStockQuantityEditor?.setValue(MINIMUM_STOCK)
        }
        val isStatusChecked = binding?.quickEditStockActivateSwitch?.isChecked == true
        val inputStock = getCurrentInputStock()
        val inputStatus = if (isStatusChecked) {
            ProductStatus.ACTIVE
        } else {
            ProductStatus.INACTIVE
        }

        val shouldSaveStock = inputStock != initialStock
        val shouldSaveStatus = inputStatus != initialStatus

        when {
            shouldSaveStock && shouldSaveStatus -> {
                saveStockAndStatus(inputStock, inputStatus)
            }
            shouldSaveStock -> saveProductStock(inputStock)
            shouldSaveStatus -> saveProductStatus(inputStatus)
        }

        removeObservers()
        dismiss()

        ProductManageTracking.eventEditStockSave(product.getId())
    }

    private fun saveStockAndStatus(stock: Int, status: ProductStatus) {
        onFinishedListener?.onFinishEditStock(product.getId(), product.getName(), stock, status)
    }

    private fun saveProductStock(stock: Int) {
        onFinishedListener?.onFinishEditStock(product.getId(), product.getName(), stock = stock)
    }

    private fun saveProductStatus(status: ProductStatus) {
        onFinishedListener?.onFinishEditStock(product.getId(), product.getName(), status = status)
    }

    private fun setupBottomSheet() {
        val horizontalSpacing =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .orZero()
        val topSpacing =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .orZero()
        val bottomSpacing =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
                .orZero()
        bottomSheetHeader.setMargin(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun setupCampaignInfo(view: View) {
        binding?.tvProductManageSingleStockCountVariant?.run {
            product?.campaignTypeList?.let { campaignList ->
                text = String.format(
                    getString(R.string.product_manage_campaign_count),
                    campaignList.count().orZero()
                )
                setOnClickListener {
                    campaignListener?.onClickCampaignInfo(campaignList)
                }
            }
            showWithCondition(product.isCampaign())
        }
    }

    private fun observeStock() {
        viewModel.stock.observe(viewLifecycleOwner, Observer {
            product = product?.copy(stock = it)
            setupStockEditor(it)
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            val isActive = it == ProductStatus.ACTIVE
            binding?.quickEditStockActivateSwitch?.isChecked = isActive
            product = product?.copy(status = it)
        })
    }

    private fun observeStockTicker() {
        observe(viewModel.stockTicker) {
            val ticker = binding?.tickerStockLayout?.root
            if (it.shouldShow()) {
                val tickerList = ProductManageTickerMapper.mapToTickerData(context, listOf(it))
                val adapter = TickerPagerAdapter(context, tickerList)
                ticker?.addPagerView(adapter, tickerList)
                ticker?.show()
            } else {
                ticker?.hide()
            }
        }
    }

    private fun setupQuantityEditor() {
        binding?.quickEditStockQuantityEditor?.run {
            maxValue = getMaxStock()
            minValue = MINIMUM_STOCK
            editText.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_STOCK_LENGTH))
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (binding?.quickEditStockQuantityEditor?.editText?.text.isNullOrEmpty()) {
                        binding?.quickEditStockQuantityEditor?.setValue(MINIMUM_STOCK)
                    }
                    binding?.quickEditStockQuantityEditor?.clearFocus()
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    binding?.quickEditStockQuantityEditor?.editText?.windowToken?.let { windowToken ->
                        imm.hideSoftInputFromWindow(windowToken, 0)
                    }
                }
                true
            }
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    activity?.let { KeyboardHandler.showSoftKeyboard(it) }
                } else {
                    context?.let { KeyboardHandler.DropKeyboard(it, view) }
                }
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s.toString()
                    if (newValue.isNotEmpty()) {
                        val stock = newValue.toInt()
                        viewModel.updateStock(stock)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No Op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No Op
                }
            })
        }
    }

    private fun getStockTicker() {
        val hasEditStockAccess = product.hasEditStockAccess()
        viewModel.getStockTicker(hasEditStockAccess)
    }

    private fun setMaxStock() {
        viewModel.setMaxStock(product?.maxStock)
    }

    private fun setupStockEditor(stock: Int) {
        when {
            !product.hasEditStockAccess() -> disableStockEditor(stock)
            stock > getMaxStock() -> setAboveMaxStockBehavior()
            stock == getMaxStock() -> setMaxStockBehavior()
            stock <= MINIMUM_STOCK -> setZeroStockBehavior()
            else -> setNormalBehavior()
        }
        setupIconInfo(stock)
    }

    private fun setZeroStockBehavior() {
        binding?.quickEditStockQuantityEditor?.subtractButton?.isEnabled = false
        binding?.quickEditStockSaveButton?.isEnabled = true
    }

    private fun setZeroStockInfo() {
        binding?.iconInfo?.run {
            if (product.suspendAccess()) {
                text = getString(
                    R.string.product_manage_suspend_stock_info_description
                )
                binding?.quickEditStockQuantityEditor?.subtractButton?.isEnabled = false
                binding?.quickEditStockSaveButton?.isEnabled = true
            } else {
                text = getString(
                    R.string.product_manage_zero_stock_info_in_edit_stock_variant
                ).parseAsHtml()
            }
            showWithCondition(!product.suspendAccess())
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_oos, 0, 0, 0)
        }

    }

    private fun setNotifyMeBuyerInfo() {
        binding?.iconInfo?.run {
            text = context?.getString(
                R.string.product_manage_notify_me_buyer_info_in_edit_stock,
                product?.notifyMeOOSCount.orEmpty()
            ).orEmpty().parseAsHtml()
            showWithCondition(!product.suspendAccess())
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_notify_me_buyer, 0, 0, 0)
        }
    }

    private fun setStockAlertActiveInfo() {
        binding?.iconInfo?.run {
            text = getString(
                R.string.product_manage_stock_alert_active_info_in_edit_stock_variant
            ).parseAsHtml()
            showWithCondition(!product.suspendAccess())
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_information_filled_yellow,
                0,
                0,
                0
            )
        }
    }

    private fun setHasStockAlertInfo() {
        binding?.iconInfo?.run {
            text = getString(
                R.string.product_manage_has_stock_alert_info_in_edit_stock_variant
            ).parseAsHtml()
            showWithCondition(!product.suspendAccess())
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bell_filled, 0, 0, 0)
        }
    }

    private fun setNormalBehavior() {
        binding?.quickEditStockQuantityEditor?.addButton?.isEnabled = true
        binding?.quickEditStockQuantityEditor?.subtractButton?.isEnabled = true
        binding?.quickEditStockQuantityEditor?.errorMessageText = String.EMPTY
        binding?.quickEditStockSaveButton?.isEnabled = true
    }

    private fun setMaxStockBehavior() {
        binding?.quickEditStockActivateSwitch?.isSelected = true
        binding?.quickEditStockQuantityEditor?.addButton?.isEnabled = false
        binding?.quickEditStockQuantityEditor?.errorMessageText = String.EMPTY
        binding?.quickEditStockSaveButton?.isEnabled = true
    }

    private fun setAboveMaxStockBehavior() {
        binding?.quickEditStockActivateSwitch?.isSelected = true
        binding?.quickEditStockQuantityEditor?.addButton?.isEnabled = false
        binding?.quickEditStockQuantityEditor?.errorMessageText =
            context?.getString(
                R.string.product_manage_quick_edit_stock_max_stock,
                getMaxStock().getNumberFormatted()
            ).orEmpty()
        binding?.quickEditStockSaveButton?.isEnabled = false
    }

    private fun setAddButtonClickListener() {
        binding?.quickEditStockQuantityEditor?.run {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock++

                if (stock <= getMaxStock()) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        binding?.quickEditStockQuantityEditor?.run {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if (input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock--

                if (stock >= MINIMUM_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setupIconInfo(currentStock: Int) {
        when {
            product.haveNotifyMeBuyer() && currentStock == Int.ZERO -> {
                setNotifyMeBuyerInfo()
            }
            product?.isEmptyStock.orFalse() || currentStock == Int.ZERO -> {
                setZeroStockInfo()
            }
            product?.stockAlertActive.orFalse() ||
                    (currentStock < product?.stockAlertCount.orZero() && product?.hasStockAlert.orFalse()) -> {
                setStockAlertActiveInfo()
            }
            product?.hasStockAlert.orFalse() -> {
                setHasStockAlertInfo()
            }
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }

    private fun getMaxStock(): Int {
        return product?.maxStock ?: MAXIMUM_STOCK
    }

    private fun getCurrentInputStock(): Int {
        return binding?.quickEditStockQuantityEditor?.getValue().orZero()
    }

    private fun removeObservers() {
        removeObservers(viewModel.status)
        removeObservers(viewModel.stock)
    }

    fun setOnFinishedListener(onFinishedListener: OnFinishedListener) {
        this.onFinishedListener = onFinishedListener
    }

    interface OnFinishedListener {
        fun onFinishEditStock(
            productId: String,
            productName: String,
            stock: Int? = null,
            status: ProductStatus? = null
        )
    }
}
