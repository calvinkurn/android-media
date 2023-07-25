package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessItemKeywordBinding
import com.tokopedia.utils.view.binding.viewBinding

class InspirationKeywordItemViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationKeywordListener,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_semless_item_keyword
    }
    private var binding: SearchInspirationSemlessItemKeywordBinding? by viewBinding()

    fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView,
    ) {
        val binding = binding ?: return
        binding.ivSearchKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        binding.textViewKeyword.text = inspirationKeywordDataView.keyword
        binding.root.setOnClickListener {
            inspirationCarouselListener.onBroadMatchItemClicked(inspirationKeywordDataView)
        }
    }
}
