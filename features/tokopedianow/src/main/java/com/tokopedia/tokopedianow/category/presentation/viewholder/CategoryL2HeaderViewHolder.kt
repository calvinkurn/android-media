package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryL2HeaderBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryL2HeaderViewHolder(
    itemView: View,
    private val listener: CategoryL2HeaderListener? = null
): AbstractViewHolder<CategoryL2HeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_l2_header
    }

    private val binding: ItemTokopedianowCategoryL2HeaderBinding? by viewBinding()

    override fun bind(header: CategoryL2HeaderUiModel) {
        binding?.apply {
            textTitle.text = header.title
            textOtherCategory.setOnClickListener {
                listener?.onClickOtherCategory()
            }
        }
    }

    interface CategoryL2HeaderListener {
        fun onClickOtherCategory()
    }
}
