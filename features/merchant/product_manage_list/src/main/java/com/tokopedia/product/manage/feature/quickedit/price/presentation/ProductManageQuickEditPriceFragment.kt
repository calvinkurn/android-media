package com.tokopedia.product.manage.feature.quickedit.price.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.product.manage.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductManageQuickEditPriceFragment : BottomSheetUnify() {

    companion object {
        fun createInstance(context: Context, cacheManagerId: String) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment().apply{
                val view = View.inflate(context, R.layout.fragment_filter,null)
                setChild(view)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        context?.let {
            this.setTitle(it.resources.getString(R.string.product_manage_menu_set_price))
        }
        // Product Name

    }
}