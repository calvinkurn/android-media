package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationKeywordReimagineItemBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.utils.view.binding.viewBinding

class InspirationKeywordReimagineItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_keyword_reimagine_item
    }
    private var binding: SearchInspirationKeywordReimagineItemBinding? by viewBinding()

    fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView,
    ) {
        val binding = binding ?: return
        binding.searchInspirationKeywordReimagineIcon.loadImage(inspirationKeywordDataView.imageKeyword)
        binding.searchInspirationKeywordReimagineKeyword.text = inspirationKeywordDataView.keyword
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }
}
