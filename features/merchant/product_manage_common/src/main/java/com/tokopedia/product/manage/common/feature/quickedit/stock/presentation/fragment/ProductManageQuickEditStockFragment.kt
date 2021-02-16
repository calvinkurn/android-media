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
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*
import java.util.*
import javax.inject.Inject

class ProductManageQuickEditStockFragment(
    private var onFinishedListener: OnFinishedListener? = null
) : BottomSheetUnify(), HasComponent<ProductManageQuickEditStockComponent> {

    companion object {
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"

        private const val KEY_PRODUCT_ID = "extra_product_id"
        private const val KEY_PRODUCT_NAME = "extra_product_name"
        private const val KEY_PRODUCT_STATUS = "extra_product_status"
        private const val KEY_STOCK = "extra_product_stock"
        private const val KEY_EDIT_STOCK_ACCESS = "edit_stock_access"
        private const val KEY_EDIT_PRODUCT_ACCESS = "edit_product_access"

        fun createInstance(product: ProductUiModel, onFinishedListener: OnFinishedListener) : ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment(onFinishedListener).apply {
                Bundle().apply {
                    val access = product.access
                    val editStockAccess = access?.editStock ?: false
                    val editProductAccess = access?.editProduct ?: false
                    val productStock = product.stock.orZero()

                    putString(KEY_PRODUCT_ID, product.id)
                    putString(KEY_PRODUCT_NAME, product.title)
                    putString(KEY_PRODUCT_STATUS, product.status?.name)
                    putInt(KEY_STOCK, productStock)
                    putBoolean(KEY_EDIT_STOCK_ACCESS, editStockAccess)
                    putBoolean(KEY_EDIT_PRODUCT_ACCESS, editProductAccess)
                    arguments = this
                }
            }
        }

        fun createInstance(productId: String,
                           productName: String,
                           productStatus: String,
                           stock: Int,
                           onFinishedListener: OnFinishedListener): ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment(onFinishedListener).apply {
                Bundle().apply {
                    putString(KEY_PRODUCT_ID, productId)
                    putString(KEY_PRODUCT_NAME, productName)
                    putString(KEY_PRODUCT_STATUS, productStatus)
                    putInt(KEY_STOCK, stock)
                    arguments = this
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageQuickEditStockViewModel

    private var productId: String = ""
    private var productName: String = ""
    private var productStock: Int = 0
    private var productStatus = ProductStatus.INACTIVE

    private var hasEditStockAccess: Boolean = false
    private var hasEditProductAccess: Boolean = false

    private var firstStateChecked = false

    private var currentStock = 0
    private var currentStatus = ProductStatus.INACTIVE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            arguments?.run {
                getString(KEY_PRODUCT_ID)?.let { id ->
                    productId = id
                }
                getString(KEY_PRODUCT_NAME)?.let { name ->
                    productName = name
                }
                getString(KEY_PRODUCT_STATUS)?.let { status ->
                    productStatus = ProductStatus.valueOf(status.toUpperCase(Locale.ROOT))
                }
                productStock = getInt(KEY_STOCK)
                hasEditStockAccess = getBoolean(KEY_EDIT_STOCK_ACCESS)
                hasEditProductAccess = getBoolean(KEY_EDIT_PRODUCT_ACCESS)

                currentStock = productStock
                currentStatus = productStatus
            }
        }

        savedInstanceState?.let {
            productId = it.getString(KEY_PRODUCT_ID).orEmpty()
            productName = it.getString(KEY_PRODUCT_NAME).orEmpty()
            it.getString(KEY_PRODUCT_STATUS)?.run {
                productStatus = ProductStatus.valueOf(toUpperCase(Locale.ROOT))
            }
            productStock = it.getInt(KEY_STOCK)
        }
        val view = View.inflate(context, R.layout.fragment_quick_edit_stock,null)
        setChild(view)
        setTitle(getString(R.string.product_manage_quick_edit_stock_title))
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        initInjector()
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
        outState.run {
            putString(KEY_PRODUCT_ID, productId)
            putString(KEY_PRODUCT_NAME, productName)
            putString(KEY_PRODUCT_STATUS, productStatus.name)
            putInt(KEY_STOCK, productStock.orZero())
            putBoolean(KEY_EDIT_STOCK_ACCESS, hasEditStockAccess)
            putBoolean(KEY_EDIT_PRODUCT_ACCESS, hasEditProductAccess)
        }
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

        requestStockEditorFocus()
        setAddButtonClickListener()
        setSubtractButtonClickListener()
        getStockTicker()
    }

    private fun setStockAndStatus() {
        quickEditStockQuantityEditor.setValue(productStock)
        viewModel.updateStock(productStock)
        viewModel.updateStatus(productStatus)
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
        val isActive = productStatus == ProductStatus.ACTIVE
        quickEditStockActivateSwitch.isChecked = isActive

        if(hasEditProductAccess) {
            quickEditStockActivateSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.updateStatus(ProductStatus.ACTIVE)
                } else {
                    viewModel.updateStatus(ProductStatus.INACTIVE)
                }

                if (firstStateChecked) {
                    if (isChecked) {
                        ProductManageTracking.eventEditStockToggle(TOGGLE_ACTIVE, productId)
                    } else {
                        ProductManageTracking.eventEditStockToggle(TOGGLE_NOT_ACTIVE, productId)
                    }
                }
            }
        } else {
            quickEditStockActivateSwitch.isEnabled = false
        }
    }

    private fun setupSaveButton() {
        val shouldShow = hasEditStockAccess || hasEditProductAccess

        if(shouldShow) {
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

        val shouldSaveStock = inputStock != currentStock
        val shouldSaveStatus = inputStatus != currentStatus

        when {
            shouldSaveStock && shouldSaveStatus -> {
                saveStockAndStatus(inputStock, inputStatus)
            }
            shouldSaveStock -> saveProductStock(inputStock)
            shouldSaveStatus -> saveProductStatus(inputStatus)
        }

        removeObservers()
        dismiss()

        ProductManageTracking.eventEditStockSave(productId)
    }

    private fun saveStockAndStatus(stock: Int, status: ProductStatus) {
        onFinishedListener?.onFinishEditStock(productId, productName, stock, status)
    }

    private fun saveProductStock(stock: Int) {
        onFinishedListener?.onFinishEditStock(productId, productName, stock = stock)
    }

    private fun saveProductStatus(status: ProductStatus) {
        onFinishedListener?.onFinishEditStock(productId, productName, status = status)
    }

    private fun setupBottomSheet() {
        val horizontalSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val topSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val bottomSpacing = context?.resources?.getDimensionPixelSize(R.dimen.spacing_lvl3).orZero()
        bottomSheetHeader.setMargin(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun observeStock() {
        viewModel.stock.observe(viewLifecycleOwner, Observer {
            productStock = it
            setupStockEditor(it)
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            val isActive = productStatus == ProductStatus.ACTIVE
            quickEditStockActivateSwitch.isChecked = isActive
            productStatus = it
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
        viewModel.getStockTicker(hasEditStockAccess)
    }

    private fun setupStockEditor(stock: Int) {
        when {
            !hasEditStockAccess -> disableStockEditor(stock)
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