package com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageMultiEditBinding
import com.tokopedia.product.manage.feature.list.view.adapter.ProductMultiEditAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MultiEditViewHolder.MenuClickListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductMultiEditBottomSheet(
    private val listener: MultiEditListener,
    private val fm: FragmentManager?
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = ProductMultiEditBottomSheet::class.java.simpleName
    }

    private val menuList = listOf(
        R.string.product_manage_edit_etalase,
        R.string.product_manage_edit_product_non_active,
        R.string.product_bs_delete_title
    )

    private var binding by autoClearedNullable<BottomSheetProductManageMultiEditBinding>()

    init {
        showHeader = false
        showCloseIcon = false
    }

    private var isShopModerated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageMultiEditBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
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

    private fun setupView() {
        val adapter = ProductMultiEditAdapter(menuClickListener(), isShopModerated)
        binding?.rvProductManageMultiEdit?.adapter = adapter
        adapter.menuList = menuList
    }

    fun show(isShopModerated: Boolean) {
        this.isShopModerated = isShopModerated
        fm?.let { show(it, TAG) }
    }

    interface MultiEditListener {
        fun editMultipleProductsEtalase()
        fun editMultipleProductsInActive()
        fun deleteMultipleProducts()
    }
}
