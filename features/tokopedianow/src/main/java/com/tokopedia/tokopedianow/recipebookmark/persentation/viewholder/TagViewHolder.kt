package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTagBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel

class TagViewHolder(
    private val binding: ItemTokopedianowTagBinding,
    private val listener: TagListener? = null
): RecyclerView.ViewHolder(binding.root) {

    fun bind(tagUiModel: TagUiModel) {
        if (tagUiModel.shouldFormatTag) {
            binding.lblTag.text = binding.lblTag.context.getString(
                R.string.tokopedianow_recipe_other_label, tagUiModel.tag.toIntSafely())

            binding.lblTag.setOnClickListener {
                listener?.onClickOtherTags()
            }

            listener?.onImpressOtherTags()
        } else {
            binding.lblTag.text = tagUiModel.tag
        }
    }

    interface TagListener {
        fun onImpressOtherTags()
        fun onClickOtherTags()
    }
}