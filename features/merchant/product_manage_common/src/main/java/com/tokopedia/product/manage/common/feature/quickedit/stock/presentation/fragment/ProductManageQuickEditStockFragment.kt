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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.model.ProductViewModel
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*
import javax.inject.Inject

class ProductManageQuickEditStockFragment(private var onFinishedListener: OnFinishedListener? = null,
                                          private var product: ProductViewModel? = null) : BottomSheetUnify(),
        HasComponent<ProductManageQuickEditStockComponent> {

    companion object {
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"

        private const val KEY_CACHE_MANAGER_ID = "cache_manager_id"
        private const val KEY_PRODUCT = "product"

        private const val KEY_PRODUCT_ID = "product_id"
        private const val KEY_PRODUCT_NAME = "product_name"
        private const val KEY_PRODUCT_STATUS = "product_status"
        private const val KEY_STOCK = "stock"

        fun createInstance(product: ProductViewModel, onFinishedListener: OnFinishedListener) : ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment(onFinishedListener, product)
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

    private var productId: String? = null
    private var productName: String? = null
    private var productStatus: ProductStatus? = null
    private var productStock: Int = 0

    private var firstStateChecked = false

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
                    productStatus = ProductStatus.valueOf(status)
                }
                getInt(KEY_STOCK).let { stock ->
                    productStock = stock
                }
            }
        }

        savedInstanceState?.let {
            val cacheManagerId = it.getString(KEY_CACHE_MANAGER_ID).orEmpty()
            val cacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
            product = cacheManager?.get<ProductViewModel>(KEY_PRODUCT, ProductViewModel::class.java, null)

            productId = it.getString(KEY_PRODUCT_ID)
            productName = it.getString(KEY_PRODUCT_NAME)
            it.getString(KEY_PRODUCT_STATUS)?.run {
                productStatus = ProductStatus.valueOf(this)
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
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_PRODUCT, product)
        outState.run {
            putString(KEY_CACHE_MANAGER_ID, cacheManager?.id.orEmpty())
            if (product == null) {
                putString(KEY_PRODUCT_ID, productId)
                putString(KEY_PRODUCT_NAME, productName)
                putString(KEY_PRODUCT_STATUS, productStatus?.name)
                putInt(KEY_STOCK, productStock.orZero())
            }
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        quickEditStockQuantityEditor.apply {
            maxValue = MAXIMUM_STOCK
            minValue = MINIMUM_STOCK
            editText.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_STOCK_LENGTH))
            editText.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(quickEditStockQuantityEditor.editText.text.isEmpty()) {
                        quickEditStockQuantityEditor.setValue(MINIMUM_STOCK)
                    }
                    quickEditStockQuantityEditor.clearFocus()
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(quickEditStockQuantityEditor.editText.windowToken, 0)
                }
                true
            }
            editText.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) {
                    activity?.let { KeyboardHandler.showSoftKeyboard(it) }
                } else {
                    context?.let { KeyboardHandler.DropKeyboard(it, view) }
                }
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s.toString()
                    if(newValue.isNotEmpty()) {
                        val stock = newValue.toInt()
                        when {
                            stock >= MAXIMUM_STOCK -> setMaxStockBehavior()
                            stock <= MINIMUM_STOCK -> setZeroStockBehavior()
                            else -> setNormalBehavior()
                        }
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

        quickEditStockSaveButton.setOnClickListener {
            product?.run {
                if(quickEditStockQuantityEditor.editText.text.isEmpty()) {
                    quickEditStockQuantityEditor.setValue(MINIMUM_STOCK)
                }
                viewModel.updateStock(quickEditStockQuantityEditor.getValue())
                onFinishedListener?.onFinishEditStock(this)
                removeObservers()
                super.dismiss()
                ProductManageTracking.eventEditStockSave(id)
                return@setOnClickListener
            }

            productId?.let { id ->
                productName?.let { name ->
                    productStatus?.let { status ->
                        productStock.let { stock ->
                            if(quickEditStockQuantityEditor.editText.text.isEmpty()) {
                                quickEditStockQuantityEditor.setValue(MINIMUM_STOCK)
                                viewModel.updateStock(quickEditStockQuantityEditor.getValue())
                                onFinishedListener?.onFinishEditStock(id, name, status, stock)
                            }
                        }
                    }
                }
            }
        }

        quickEditStockActivateSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateStatus(ProductStatus.ACTIVE)
            } else {
                viewModel.updateStatus(ProductStatus.INACTIVE)
            }

            if(firstStateChecked) {
                if(isChecked) {
                    ProductManageTracking.eventEditStockToggle(TOGGLE_ACTIVE, product?.id.orEmpty())
                } else {
                    ProductManageTracking.eventEditStockToggle(TOGGLE_NOT_ACTIVE, product?.id.orEmpty())
                }
            }
        }
        quickEditStockActivateSwitch.isChecked = product?.isActive() ?: false
        product?.stock?.let { stock ->
            quickEditStockQuantityEditor.setValue(stock)
            viewModel.updateStock(stock)
        }
        product?.status?.let { status ->
            viewModel.updateStatus(status)
        }

        // Set value if not using ProductViewModel
        if (product == null) {
            quickEditStockQuantityEditor.setValue(productStock)
            viewModel.updateStock(productStock)
        }
        productStatus?.run {
            viewModel.updateStatus(this)
        }

        observeStatus()
        observeStock()
        quickEditStockQuantityEditor.editText.requestFocus()
        setAddButtonClickListener()
        setSubtractButtonClickListener()
    }

    private fun observeStock() {
        viewModel.stock.observe(this, Observer {
            if (product != null) {
                product = product?.copy(stock = it)
            } else {
                productStock = it
            }
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(this, Observer {
            if (product != null) {
                product = product?.copy(status = it)
                quickEditStockActivateSwitch.isChecked = product?.isActive() ?: false
            } else {
                productStatus = it
                quickEditStockActivateSwitch.isChecked = productStatus == ProductStatus.ACTIVE
            }
        })
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
        fun onFinishEditStock(modifiedProduct: ProductViewModel)
        fun onFinishEditStock(productId: String,
                              productName: String,
                              productStatus: ProductStatus,
                              stock: Int)
    }
}