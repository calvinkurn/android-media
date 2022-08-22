package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchDoubleLineRelatedLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.model.RelatedItemDataView
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

internal class DoubleLineRelatedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_search_double_line_related_layout
    }

    private var binding: UniversalSearchDoubleLineRelatedLayoutBinding? by viewBinding()

    fun bind(data: RelatedItemDataView) {
        bindImage(data)
        bindTitle(data)
    }

    private fun bindTitle(data: RelatedItemDataView) {
        binding?.universalSearchRelatedTitle?.text = data.title
    }

    private fun bindImage(data: RelatedItemDataView) {
        binding?.universalSearchRelatedImage?.loadImageRounded(
            data.imageUrl,
            4.toPx().toFloat()
        )
    }
}