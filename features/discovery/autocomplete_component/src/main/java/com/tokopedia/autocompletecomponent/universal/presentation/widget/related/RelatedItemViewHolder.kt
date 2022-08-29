package com.tokopedia.autocompletecomponent.universal.presentation.widget.related

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchRelatedItemLayoutBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class RelatedItemViewHolder(
    itemView: View,
    private val relatedItemListener: RelatedItemListener,
): RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_search_related_item_layout
    }

    private var binding: UniversalSearchRelatedItemLayoutBinding? by viewBinding()

    fun bind(data: RelatedItemDataView) {
        bindImage(data)
        bindTitle(data)
        bindItemClick(data)
    }

    private fun bindTitle(data: RelatedItemDataView) {
        binding?.universalSearchRelatedTitle?.text = data.title
    }

    private fun bindImage(data: RelatedItemDataView) {
        binding?.universalSearchRelatedImage?.let {
            it.loadImageRounded(
                data.imageUrl,
                4.toPx().toFloat()
            )

            it.addOnImpressionListener(data) {
                relatedItemListener.onRelatedItemImpressed(data)
            }
        }
    }

    private fun bindItemClick(data: RelatedItemDataView) {
        binding?.universalSearchRelatedContainer?.setOnClickListener {
            relatedItemListener.onRelatedItemClick(data)
        }
    }
}