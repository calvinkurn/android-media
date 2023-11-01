package com.tokopedia.catalogcommon.viewholder.comparison

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetItemComparisonContentSpecBinding
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.utils.view.binding.viewBinding

class ComparisonSpecItemViewHolder(
    itemView: View,
    isComparedItem: Boolean
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        private const val DIVIDER_MARGIN = 8
        private const val DIVIDER_DARK_ALPHA = 0.4F
        private const val DIVIDER_LIGHT_ALPHA = 0.2F

        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_item_comparison_content_spec, parent, false)
    }

    private val binding: WidgetItemComparisonContentSpecBinding? by viewBinding()

    init {
        if (isComparedItem) {
            binding?.apply {
                val margin = DIVIDER_MARGIN.dpToPx(itemView.resources.displayMetrics)
                divSpecCategory.setMargin(margin, Int.ZERO, Int.ZERO, Int.ZERO)
                divSpecValue.setMargin(margin, Int.ZERO, Int.ZERO, Int.ZERO)
            }
        }
    }

    fun bind(item: ComparisonUiModel.ComparisonSpec) {
        itemView.post {
            binding?.apply {
                tfSpecValue.ellipsize = TextUtils.TruncateAt.END
                tfSpecValue.maxLines = item.specHeight
                tfSpecValue.minLines = item.specHeight
                tfSpecCategoryTitle.text = item.specCategoryTitle
                tfSpecTitle.text = item.specTitle
                tfSpecValue.text = item.specValue

                tfSpecCategoryTitle.isVisible = item.isSpecCategoryTitle
                divSpecCategory.isVisible = item.isSpecCategoryTitle
                tfSpecTitle.isVisible = !item.isSpecCategoryTitle
                tfSpecValue.isVisible = !item.isSpecCategoryTitle
                divSpecValue.isVisible = !item.isSpecCategoryTitle

                divSpecValue.alpha = if (item.isDarkMode) DIVIDER_DARK_ALPHA else DIVIDER_LIGHT_ALPHA
                divSpecCategory.alpha = if (item.isDarkMode) DIVIDER_DARK_ALPHA else DIVIDER_LIGHT_ALPHA
                tfSpecTitle.setTextColor(item.specTextTitleColor ?: return@apply)
                tfSpecValue.setTextColor(item.specTextColor ?: return@apply)
            }
        }
    }
}
