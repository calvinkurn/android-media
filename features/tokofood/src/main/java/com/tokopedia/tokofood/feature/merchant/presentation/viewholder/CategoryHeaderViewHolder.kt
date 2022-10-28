package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.TokofoodCategoryHeaderLayoutBinding

class CategoryHeaderViewHolder(
        private val binding: TokofoodCategoryHeaderLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(categoryName: String) {
        binding.tpgCategoryName.text = categoryName
    }
}