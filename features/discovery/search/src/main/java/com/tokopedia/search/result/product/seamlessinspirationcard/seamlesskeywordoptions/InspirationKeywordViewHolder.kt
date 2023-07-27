package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessKeywordBinding
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.INDEX_IMPRESISON_KEYWORD_TO_BE_TRACK
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class InspirationKeywordViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationKeywordListener
) : AbstractViewHolder<InspirationKeyboardCardView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_semless_keyword
    }

    private var binding: SearchInspirationSemlessKeywordBinding? by viewBinding()

    override fun bind(element: InspirationKeyboardCardView?) {
        if (binding == null || element == null) return

        initCardView()
        val optionItem = element.optionsItems
        optionItem.getItemOptionsOn(INDEX_IMPRESISON_KEYWORD_TO_BE_TRACK).doImpressedTracker()
        initRecyclerView(element.optionsItems)
    }

    private fun initCardView() {
        val cardView = binding?.cardViewInspirationKeywordOptions
        cardView?.cardType = CardUnify2.TYPE_SHADOW
        cardView?.useCompatPadding = true
        cardView?.setMargin(0, 0, 0, 0)
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    }

    private fun InspirationKeywordDataView.doImpressedTracker() {
        binding?.cardViewInspirationKeywordOptions?.addOnImpressionListener(this) {
            inspirationCarouselListener.onInspirationKeywordImpressed(this)
        }
    }

    private fun initRecyclerView(inspirationKeywords: List<InspirationKeywordDataView>) {
        binding?.inspirationKeywordOptionList?.let {
            it.layoutManager = createLayoutManager()
            it.adapter =
                InspirationKeywordAdapter(inspirationKeywords, inspirationCarouselListener)
        }
    }

    private fun List<InspirationKeywordDataView>.getItemOptionsOn(position: Int) =
        this[position]
}
