package com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*

class ProductManageQuickEditStockFragment : BottomSheetUnify() {

    companion object {

        const val EDIT_STOCK_CACHE_ID = "edit_stock_cache_id"
        const val EDIT_STOCK_PRODUCT = "edit_stock_product"
        private const val MAXIMUM_STOCK = 999999
        private const val MINIMUM_STOCK = 1

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

    var editStockSuccess = false
    var stock = 1

    private var product: ProductViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun initView() {
        quick_edit_stock_activate_switch.setTextBold(true)
        product?.let {
            quick_edit_stock_activate_switch.isSelected = it.isActive()
            it.stock?.let { stock -> quick_edit_stock_quantity_editor.setValue(stock) }
            quick_edit_stock_quantity_editor.maxValue = MAXIMUM_STOCK
        }
        quick_edit_stock_quantity_editor.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                hideError()
                activity.let {
                    KeyboardHandler.showSoftKeyboard(it)
                }
            } else {
                activity.let {
                    KeyboardHandler.hideSoftKeyboard(it)
                }
            }
        }
        quick_edit_stock_save_button.setOnClickListener {
            stock = quick_edit_stock_quantity_editor.getValue()
            when {
                isStockTooHigh() -> showErrorStockTooHigh()
                isStockTooLow() -> showErrorStockTooLow()
                else -> onSuccessSetStock()
            }
        }
        quick_edit_stock_activate_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            product = if (isChecked) {
                product?.copy(status = ProductStatus.ACTIVE)
            } else {
                product?.copy(status = ProductStatus.INACTIVE)
            }
        }
        quick_edit_stock_quantity_editor.requestFocus()
    }

    private fun isStockTooLow(): Boolean {
        if(stock < MINIMUM_STOCK) {
            return true
        }
        return false
    }

    private fun isStockTooHigh(): Boolean {
        if(stock > MAXIMUM_STOCK) {
            return true
        }
        return false
    }

    private fun showErrorStockTooLow() {
        quick_edit_stock_error_message.text = context?.let { it.resources.getString(R.string.product_manage_quick_edit_stock_min_stock_error)}
        quick_edit_stock_error_message.visibility = View.VISIBLE
    }

    private fun showErrorStockTooHigh() {
        quick_edit_stock_error_message.text = context?.let { it.resources.getString(R.string.product_manage_quick_edit_stock_max_stock_error)}
        quick_edit_stock_error_message.visibility = View.VISIBLE
    }

    private fun hideError() {
        quick_edit_stock_error_message.visibility = View.GONE
    }

    private fun onSuccessSetStock() {
        editStockSuccess = true
        super.dismiss()
    }


}