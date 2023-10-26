package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationKeywordReimagineBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.INDEX_IMPRESSION_KEYWORD_TO_BE_TRACK
import com.tokopedia.utils.view.binding.viewBinding

class InspirationKeywordReimagineViewHolder(
    itemView: View,
    private val inspirationKeywordListener: InspirationKeywordListener,
) : AbstractViewHolder<InspirationKeywordCardView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_keyword_reimagine
    }

    private var binding: SearchInspirationKeywordReimagineBinding? by viewBinding()

    override fun bind(element: InspirationKeywordCardView?) {
        if (binding == null || element == null) return

        binding?.searchInspirationKeywordReimagineTitle?.text = element.title
        element.optionsItems.getItemOptionsOn(INDEX_IMPRESSION_KEYWORD_TO_BE_TRACK)?.doImpressedTracker()
        initRecyclerView(element)
    }

    private fun InspirationKeywordDataView.doImpressedTracker() {
        binding?.searchInspirationKeywordReimagineList?.addOnImpressionListener(this) {
            inspirationKeywordListener.onInspirationKeywordImpressed(this)
        }
    }

    private fun initRecyclerView(
        inspirationKeywords: InspirationKeywordCardView,
    ) {
        binding?.searchInspirationKeywordReimagineList?.run {
            this.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            this.adapter = InspirationKeywordReimagineAdapter(inspirationKeywords.optionsItems, inspirationKeywordListener)
        }
    }

    private fun List<InspirationKeywordDataView>.getItemOptionsOn(position: Int) =
        this.getOrNull(position)
}
