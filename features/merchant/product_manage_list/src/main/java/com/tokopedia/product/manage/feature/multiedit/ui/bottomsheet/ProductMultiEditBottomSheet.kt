package com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMultiEditAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MultiEditViewHolder.MenuClickListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage.view.*

class ProductMultiEditBottomSheet(
    container: View?,
    private val listener: MultiEditListener,
    private val fm: FragmentManager?
): BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage
        private val TAG: String = ProductMultiEditBottomSheet::class.java.simpleName
    }

    private val menuList = listOf(
        R.string.product_manage_edit_etalase,
        R.string.product_manage_edit_product_non_active,
        R.string.product_bs_delete_title
    )

    init {
        val itemView = LayoutInflater.from(container?.context)
            .inflate(LAYOUT, (container as ViewGroup), false)

        val adapter = ProductMultiEditAdapter(menuClickListener())
        itemView.menuList.adapter = adapter
        adapter.menuList = menuList

        showHeader = false
        showCloseIcon = false
        setChild(itemView)
    }

    private fun menuClickListener(): MenuClickListener {
        return object : MenuClickListener {
            override fun onClickMenuItem(menuId: Int) {
                when (menuId) {
                    R.string.product_manage_edit_etalase -> {
                        listener.editMultipleProductsEtalase()
                    }
                    R.string.product_manage_edit_product_non_active -> {
                        listener.editMultipleProductsInActive()
                    }
                    R.string.product_bs_delete_title -> {
                        listener.deleteMultipleProducts()
                    }
                }
                dismiss()
            }
        }
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    interface MultiEditListener{
        fun editMultipleProductsEtalase()
        fun editMultipleProductsInActive()
        fun deleteMultipleProducts()
    }
}