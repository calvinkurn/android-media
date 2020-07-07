package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageMoreMenuAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductManageMoreMenuViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_more_menu.view.*

class ProductManageMoreMenuBottomSheet(
        context: Context? = null,
        private val listener: ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener? = null,
        private val fm: FragmentManager? = null
): BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage_more_menu
        private val TAG = ProductManageMoreMenuBottomSheet::class.java.simpleName
    }

    private var moreMenuAdapter: ProductManageMoreMenuAdapter? = null

    init {
        if (context != null && listener != null && fm != null) {
            moreMenuAdapter = context?.let { ProductManageMoreMenuAdapter(it, listener) }
            val itemView = View.inflate(context, LAYOUT, null).apply {
                moreMenuList.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = moreMenuAdapter
                }
            }
            setChild(itemView)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageMoreMenuBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

}