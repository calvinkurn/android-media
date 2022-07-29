package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTagBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel

class TagViewHolder(
    private val binding: ItemTokopedianowTagBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(tagUiModel: TagUiModel) {
        binding.lblTag.text = if (tagUiModel.shouldFormatTag) {
            binding.root.context.getString(R.string.tokopedianow_recipe_other_label, tagUiModel.tag.toIntSafely())
        } else {
            tagUiModel.tag
        }
    }
}