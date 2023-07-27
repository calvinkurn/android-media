package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessItemKeywordBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.utils.view.binding.viewBinding

class SmallInspirationKeywordItemViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener
) : AbstractViewHolder<InspirationKeywordDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_semless_item_keyword
    }
    private var binding: SearchInspirationSemlessItemKeywordBinding? by viewBinding()

    override fun bind(
        inspirationKeywordDataView: InspirationKeywordDataView,
    ) {
        val binding = binding ?: return
        binding.ivSearchKeyword.loadImage(inspirationKeywordDataView.imageKeyword)
        binding.textViewKeyword.text = inspirationKeywordDataView.keyword
        binding.root.setOnClickListener {
            inspirationKeywordListener.onInspirationKeywordItemClicked(inspirationKeywordDataView)
        }
    }
}
