package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchRelatedItemLayoutBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class RelatedItemViewHolder(
    itemView: View,
    private val relatedItemListener: RelatedItemListener,
): RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_search_related_item_layout

        const val TYPE_DOUBLE_LINE = 0
        const val TYPE_LIST_GRID = 1

        private const val IMAGE_LARGE_DP = 72
        private const val IMAGE_SMALL_DP = 32
        private const val CONTAINER_SHORT_DP = 232
        private const val TEXT_MARGIN_LEFT_12_DP = 12
        private const val TEXT_MARGIN_LEFT_8_DP = 8
        private const val TEXT_MAXLINES_1 = 1
        private const val TEXT_MAXLINES_2 = 2
    }

    private var binding: UniversalSearchRelatedItemLayoutBinding? by viewBinding()

    fun bind(data: RelatedItemDataView, type: Int) {
        configContainerWidth(type)
        bindImage(data, type)
        bindTitle(data, type)
        bindItemClick(data)
    }

    private fun configContainerWidth(type: Int) {
        binding?.universalSearchRelatedContainer?.layoutParams?.width = getContainerWidth(type)
    }

    private fun bindTitle(data: RelatedItemDataView, type: Int) {
        binding?.universalSearchRelatedTitle?.let {
            it.shouldShowWithAction(data.title.isNotEmpty()) {
                it.configMarginLeft(type)
                it.configMaxLines(type)
                it.text = data.title
            }
        }
    }

    private fun bindImage(data: RelatedItemDataView, type: Int) {
        binding?.universalSearchRelatedImage?.let {
            it.configImageDimension(type)
            it.loadImageRounded(
                data.imageUrl,
                4.toPx().toFloat()
            )
            it.addOnImpressionListener(data) {
                relatedItemListener.onRelatedItemImpressed(data)
            }
        }
    }

    private fun AppCompatImageView.configImageDimension(type: Int) {
        layoutParams.width = getImageDimension(type)
        layoutParams.height = getImageDimension(type)
    }

    private fun Typography.configMarginLeft(type: Int) {
        setMargin(getTextMarginLeft(type), 0, 0, 0)
    }

    private fun Typography.configMaxLines(type: Int) {
        maxLines = getTextMaxLines(type)
    }

    private fun getImageDimension(type: Int): Int {
        return if (type == TYPE_LIST_GRID)
            IMAGE_LARGE_DP.toPx()
        else
            IMAGE_SMALL_DP.toPx()
    }

    private fun getContainerWidth(type: Int): Int {
        return if (type == TYPE_LIST_GRID)
            CONTAINER_SHORT_DP.toPx()
        else
            ConstraintLayout.LayoutParams.MATCH_PARENT
    }

    private fun getTextMarginLeft(type: Int): Int {
        return if (type == TYPE_LIST_GRID)
            TEXT_MARGIN_LEFT_12_DP.toPx()
        else
            TEXT_MARGIN_LEFT_8_DP.toPx()
    }

    private fun getTextMaxLines(type: Int): Int {
        return if (type == TYPE_LIST_GRID)
            TEXT_MAXLINES_2
        else
            TEXT_MAXLINES_1
    }

    private fun bindItemClick(data: RelatedItemDataView) {
        binding?.universalSearchRelatedContainer?.setOnClickListener {
            relatedItemListener.onRelatedItemClick(data)
        }
    }
}