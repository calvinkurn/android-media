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
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.quickedit.stock.di.DaggerProductManageQuickEditStockComponent
import com.tokopedia.product.manage.feature.quickedit.stock.di.ProductManageQuickEditStockComponent
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*
import javax.inject.Inject

class ProductManageQuickEditStockFragment : BottomSheetUnify(),
        HasComponent<ProductManageQuickEditStockComponent> {

    companion object {

        const val EDIT_STOCK_CACHE_ID = "edit_stock_cache_id"
        const val EDIT_STOCK_PRODUCT = "edit_stock_product"
        const val MAXIMUM_STOCK = 999999
        const val MINIMUM_STOCK = 0

        fun createInstance(context: Context, cacheManagerId: String) : ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment().apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_stock,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.product_manage_quick_edit_stock_title))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                arguments =  Bundle().apply{
                    putString(EDIT_STOCK_CACHE_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ProductManageQuickEditStockViewModel

    var editStockSuccess = false
    var cacheManagerId: String? = ""

    private var product: ProductViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            val cacheManagerId: String? = it.getString(EDIT_STOCK_CACHE_ID)
            val cacheManager = context?.let {context -> SaveInstanceCacheManager(context, cacheManagerId) }
            product = cacheManager?.get(EDIT_STOCK_PRODUCT, ProductViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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
        quick_edit_stock_quantity_editor.apply {
            maxValue = MAXIMUM_STOCK
            minValue = MINIMUM_STOCK
            (editText as EditText).setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    quick_edit_stock_quantity_editor.clearFocus()
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(quick_edit_stock_quantity_editor.editText.windowToken, 0)
                }
                true
            }
            setValueChangedListener { newValue, _, _ ->
                viewModel.updateStock(newValue)
            }
        }

        quick_edit_stock_save_button.setOnClickListener {
            val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
            cacheManager?.let {
                cacheManagerId = it.id
                it.put(EDIT_STOCK_PRODUCT, product)
            }
            editStockSuccess = true
            super.dismiss()
        }
        quick_edit_stock_activate_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateStatus(ProductStatus.ACTIVE)
            } else {
                viewModel.updateStatus(ProductStatus.INACTIVE)
            }
        }
        product?.let {
            quick_edit_stock_activate_switch.isChecked = it.isActive()
            it.stock?.let { stock ->
                quick_edit_stock_quantity_editor.setValue(stock)
                viewModel.updateStock(stock)
            }
            it.status?.let { status ->
                viewModel.updateStatus(status)
            }
        }
        observeStatus()
        observeStock()
        quick_edit_stock_quantity_editor.editText.requestFocus()
    }

    private fun observeStock() {
        viewModel.stock.observe(this, Observer {
            product = product?.copy(stock = it)
        })
    }

    private fun observeStatus() {
        viewModel.status.observe(this, Observer {
            quick_edit_stock_activate_switch.isChecked = it == ProductStatus.ACTIVE
            product = product?.copy(status = it)
        })
    }


}