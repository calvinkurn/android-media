package com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageBinding
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMultiEditAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MultiEditViewHolder.MenuClickListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductMultiEditBottomSheet(
    private val listener: MultiEditListener,
    private val fm: FragmentManager?
): BottomSheetUnify() {

    companion object {
        private val TAG: String = ProductMultiEditBottomSheet::class.java.simpleName
    }

    private val menuList = listOf(
        R.string.product_manage_edit_etalase,
        R.string.product_manage_edit_product_non_active,
        R.string.product_bs_delete_title
    )

    init {
        val binding = BottomSheetProductManageBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        val adapter = ProductMultiEditAdapter(menuClickListener())
        binding.menuList.adapter = adapter
        adapter.menuList = menuList

        showHeader = false
        showCloseIcon = false
        setChild(binding.root)
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