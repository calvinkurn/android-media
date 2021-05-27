package com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.ext.getId
import com.tokopedia.product.manage.common.feature.list.ext.getName
import com.tokopedia.product.manage.common.feature.list.ext.getStatus
import com.tokopedia.product.manage.common.feature.list.ext.getStock
import com.tokopedia.product.manage.common.feature.list.ext.hasEditProductAccess
import com.tokopedia.product.manage.common.feature.list.ext.hasEditStockAccess
import com.tokopedia.product.manage.common.feature.list.ext.isActive
import com.tokopedia.product.manage.common.feature.list.ext.isCampaign
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*
import javax.inject.Inject

class ProductManageQuickEditStockFragment(
    private var onFinishedListener: OnFinishedListener? = null
) : BottomSheetUnify(), HasComponent<ProductManageQuickEditStockComponent> {

    companion object {
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"

        private const val KEY_CACHE_MANAGER_ID = "product_edit_cache_manager"
        private const val KEY_PRODUCT = "product"

        fun createInstance(
            context: Context,
            product: ProductUiModel,
            onFinishedListener: OnFinishedListener
        ): ProductManageQuickEditStockFragment {
            SaveInstanceCacheManager(context, KEY_CACHE_MANAGER_ID).apply {
                put(KEY_PRODUCT, product)
            }
            return ProductManageQuickEditStockFragment(onFinishedListener)
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

    private var firstStateChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
        val view = View.inflate(context, R.layout.fragment_quick_edit_stock, null)
        setChild(view)
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
            product = cacheManager.get<ProductUiModel>(KEY_PRODUCT, ProductUiModel::class.java, null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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

    private fun initView() {
        observeStatus()
        observeStock()
        observeStockTicker()

        setupQuantityEditor()
        setStockAndStatus()
        setupSaveButton()
        setupStatusSwitch()
        setupBottomSheet()
        setupCampaignLabel()

        requestStockEditorFocus()
        setAddButtonClickListener()
        setSubtractButtonClickListener()
        getStockTicker()
    }

    private fun setStockAndStatus() {
        val stock = product.getStock()
        val status = product.getStatus()

        quickEditStockQuantityEditor.setValue(stock)
        viewModel.updateStock(stock)
        viewModel.updateStatus(status)
    }

    private fun requestStockEditorFocus() {
        quickEditStockQuantityEditor.editText.requestFocus()
    }

    private fun disableStockEditor(stock: Int) {
        quickEditStockQuantityEditor.hide()
        textStock.show()
        textStock.text = stock.toString()
    }

    private fun setupStatusSwitch() {
        quickEditStockActivateSwitch.isChecked = product.isActive()

        if (product.hasEditProductAccess()) {
            quickEditStockActivateSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.updateStatus(ProductStatus.ACTIVE)
                } else {
                    viewModel.updateStatus(ProductStatus.INACTIVE)
                }

                if (firstStateChecked) {
                    if (isChecked) {
                        ProductManageTracking.eventEditStockToggle(TOGGLE_ACTIVE, product.getId())
                    } else {
                        ProductManageTracking.eventEditStockToggle(TOGGLE_NOT_ACTIVE, product.getId())
                    }
                }
            }
        } else {
            quickEditStockActivateSwitch.isEnabled = false
        }
    }

    private fun setupSaveButton() {
        if(product.hasEditStockAccess() || product.hasEditProductAccess()) {
            quickEditStockSaveButton.setOnClickListener {
                onClickSaveBtn()
            }
            quickEditStockSaveButton.show()
        } else {
            quickEditStockSaveButton.hide()
        }
    }

    private fun onClickSaveBtn() {
        if (quickEditStockQuantityEditor.editText.text.isEmpty()) {
            quickEditStockQuantityEditor.setValue(MINIMUM_STOCK)
        }
        val isStatusChecked = quickEditStockActivateSwitch.isChecked
        val inputStock = quickEditStockQuantityEditor.getValue()
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
        val horizontalSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val topSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val bottomSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).orZero()
        bottomSheetHeader.setMargin(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun setupCampaignLabel() {
        labelCampaign.showWithCondition(product.isCampaign())
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
            quickEditStockActivateSwitch.isChecked = isActive
            product = product?.copy(status = it)
        })
    }

    private fun observeStockTicker() {
        observe(viewModel.stockTicker) {
            if(it.shouldShow()) {
                val ticker = tickerStockLayout as? Ticker
                val tickerList = ProductManageTickerMapper.mapToTickerData(context, listOf(it))
                val adapter = TickerPagerAdapter(context, tickerList)
                ticker?.addPagerView(adapter, tickerList)
                tickerStockLayout.show()
            } else {
                tickerStockLayout.hide()
            }
        }
    }

    private fun setupQuantityEditor() {
        quickEditStockQuantityEditor.apply {
            maxValue = MAXIMUM_STOCK
            minValue = MINIMUM_STOCK
            editText.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_STOCK_LENGTH))
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (quickEditStockQuantityEditor.editText.text.isEmpty()) {
                        quickEditStockQuantityEditor.setValue(MINIMUM_STOCK)
                    }
                    quickEditStockQuantityEditor.clearFocus()
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(quickEditStockQuantityEditor.editText.windowToken, 0)
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

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
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

    private fun setupStockEditor(stock: Int) {
        when {
            !product.hasEditStockAccess() -> disableStockEditor(stock)
            stock >= MAXIMUM_STOCK -> setMaxStockBehavior()
            stock <= MINIMUM_STOCK -> setZeroStockBehavior()
            else -> setNormalBehavior()
        }
    }
    
    private fun setZeroStockBehavior() {
        if(zeroStockInfo != null
                && quickEditStockActivateSwitch != null
                && quickEditStockQuantityEditor != null) {
            zeroStockInfo.visibility = View.VISIBLE
            quickEditStockQuantityEditor.subtractButton.isEnabled = false
        }
    }

    private fun setNormalBehavior() {
        if(zeroStockInfo != null
                && quickEditStockActivateSwitch != null
                && quickEditStockQuantityEditor != null) {
            zeroStockInfo.visibility = View.GONE
            quickEditStockQuantityEditor.addButton.isEnabled = true
            quickEditStockQuantityEditor.subtractButton.isEnabled = true
        }
    }

    private fun setMaxStockBehavior() {
        if(quickEditStockQuantityEditor != null) {
            quickEditStockActivateSwitch.isSelected = true
            quickEditStockQuantityEditor.addButton.isEnabled = false
        }
    }

    private fun setAddButtonClickListener() {
        quickEditStockQuantityEditor.apply {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock++

                if(stock <= MAXIMUM_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        quickEditStockQuantityEditor.apply {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    MINIMUM_STOCK
                }

                stock--

                if(stock >= MINIMUM_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
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