package com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.utils.ProductManageTracking
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*
import javax.inject.Inject

class ProductManageQuickEditStockFragment(private val onFinishedListener: OnFinishedListener,
                                          private var product: ProductViewModel) : BottomSheetUnify(),
        HasComponent<ProductManageQuickEditStockComponent> {

    companion object {

        const val MAXIMUM_STOCK = 999999
        const val MINIMUM_STOCK = 0
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"

        fun createInstance(context: Context, product: ProductViewModel, onFinishedListener: OnFinishedListener) : ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment(onFinishedListener, product).apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_stock,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_quick_edit_stock_title))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageQuickEditStockViewModel

    private var firstStateChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        quickEditStockQuantityEditor.apply {
            maxValue = MAXIMUM_STOCK
            minValue = MINIMUM_STOCK
            editText.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    quickEditStockQuantityEditor.clearFocus()
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(quickEditStockQuantityEditor.editText.windowToken, 0)
                }
                true
            }
            setValueChangedListener { newValue, _, _ ->
                if(newValue > MINIMUM_STOCK) {
                    setNormalBehavior()
                } else {
                    onZeroStock()
                }
                viewModel.updateStock(newValue)
            }
        }

        quickEditStockSaveButton.setOnClickListener {
            onFinishedListener.onFinishEditStock(product)
            removeObservers()
            super.dismiss()
            ProductManageTracking.eventEditStockSave(product.id)
        }

        quickEditStockActivateSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateStatus(ProductStatus.ACTIVE)
            } else {
                viewModel.updateStatus(ProductStatus.INACTIVE)
            }

            if(firstStateChecked) {
                if(isChecked) {
                    ProductManageTracking.eventEditStockToggle(TOGGLE_ACTIVE, product.id)
                } else {
                    ProductManageTracking.eventEditStockToggle(TOGGLE_NOT_ACTIVE, product.id)
                }
            }
        }
        quickEditStockActivateSwitch.isChecked = product.isActive()
        product.stock?.let { stock ->
            quickEditStockQuantityEditor.setValue(stock)
            viewModel.updateStock(stock)
        }
        product.status?.let { status ->
            viewModel.updateStatus(status)
        }
        observeStatus()
        observeStock()
        quickEditStockQuantityEditor.editText.requestFocus()
    }

    private fun observeStock() {
        viewModel.stock.observe(this, Observer {
            product = product.copy(stock = it)
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(this, Observer {
            product = product.copy(status = it)
        })
    }
    
    private fun onZeroStock() {
        zeroStockInfo.visibility = View.VISIBLE
        quickEditStockActivateSwitch.isEnabled = false
    }

    private fun setNormalBehavior() {
        zeroStockInfo.visibility = View.GONE
        quickEditStockActivateSwitch.isEnabled = true
    }

    private fun removeObservers() {
        removeObservers(viewModel.status)
        removeObservers(viewModel.stock)
    }

    interface OnFinishedListener {
        fun onFinishEditStock(modifiedProduct: ProductViewModel)
    }
}