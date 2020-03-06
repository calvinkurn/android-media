package com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_quick_edit_price.*
import kotlinx.android.synthetic.main.fragment_quick_edit_stock.*

class ProductManageQuickEditStockFragment : BottomSheetUnify() {

    companion object {

        const val EDIT_STOCK_CACHE_ID = "edit_stock_cache_id"
        const val EDIT_STOCK_PRODUCT = "edit_stock_product"
        private const val MAXIMUM_STOCK = 999999

        fun createInstance(context: Context, cacheManagerId: String) : ProductManageQuickEditStockFragment {
            return ProductManageQuickEditStockFragment().apply{
                val view = View.inflate(context, R.layout.fragment_quick_edit_stock,null)
                setChild(view)
                setTitle(context.resources.getString(R.string.quick_edit_stock_title))
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                arguments =  Bundle().apply{
                    putString(EDIT_STOCK_CACHE_ID, cacheManagerId)
                }
            }
        }
    }

    var editStockSuccess = false

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
            if(it.isActive()) {
                quick_edit_stock_activate_switch.isEnabled = true
            }
            it.stock?.let { stock -> quick_edit_quantity_editor.setValue(stock) }
            quick_edit_quantity_editor.maxValue = MAXIMUM_STOCK
        }
        quick_edit_save_button.setOnClickListener {
            super.dismiss()
        }

    }

}