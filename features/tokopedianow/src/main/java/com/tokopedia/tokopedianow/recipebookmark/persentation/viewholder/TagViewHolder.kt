package com.tokopedia.tokopedianow.recipebookmark.persentation.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.util.getHexColorFromIdColor
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowTagBinding
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_GREY

class TagViewHolder(
    private val binding: ItemTokopedianowTagBinding,
    private val listener: TagListener? = null
): RecyclerView.ViewHolder(binding.root) {

    private fun ItemTokopedianowTagBinding.adjustLabelText(tag: String, shouldFormatTag: Boolean) {
        lblTag.apply {
            if (shouldFormatTag) {
                text = context.getString(R.string.tokopedianow_recipe_other_label, tag.toIntSafely())

                setOnClickListener {
                    listener?.onClickOtherTags()
                }

                listener?.onImpressOtherTags()
            } else {
                text = tag
            }
        }
    }

    private fun ItemTokopedianowTagBinding.adjustLabelColor(shouldUseStaticBackgroundColor: Boolean) {
        lblTag.apply {
            unlockFeature = true
            if (shouldUseStaticBackgroundColor) {
                setLabelType(
                    getHexColorFromIdColor(
                        context = binding.root.context,
                        idColor = R.color.tokopedianow_recipe_label_text_background_dms_color
                    )
                )
                setTextColor(ContextCompat.getColor(binding.root.context, R.color.tokopedianow_recipe_label_text_dms_color))
            } else {
                setLabelType(HIGHLIGHT_LIGHT_GREY)
            }
        }
    }

    fun bind(tagUiModel: TagUiModel) {
        binding.adjustLabelText(
            tag = tagUiModel.tag,
            shouldFormatTag = tagUiModel.shouldFormatTag
        )
        binding.adjustLabelColor(
            shouldUseStaticBackgroundColor = tagUiModel.shouldUseStaticBackgroundColor
        )
    }

    interface TagListener {
        fun onImpressOtherTags()
        fun onClickOtherTags()
    }
}
