package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCardOptionViewModel
import com.tokopedia.search.result.presentation.model.InspirationCardViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.InspirationCardOptionAdapter
import com.tokopedia.search.result.presentation.view.listener.InspirationCardListener
import com.tokopedia.search.utils.ChipSpacingItemDecoration
import com.tokopedia.search.utils.addItemDecorationIfNotExists
import kotlinx.android.synthetic.main.search_result_product_big_grid_inspiration_card_layout.view.*
import kotlinx.android.synthetic.main.search_result_product_inspiration_card_layout.view.*

class BigGridInspirationCardViewHolder(
        itemView: View,
        private val inspirationCardListener: InspirationCardListener
) : AbstractViewHolder<InspirationCardViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_big_grid_inspiration_card_layout
    }

    override fun bind(element: InspirationCardViewModel) {
        bindTitle(element)
        bindContent(element)
    }

    private fun bindTitle(element: InspirationCardViewModel) {
        itemView.bigGridCardViewInspirationCard?.inspirationCardTitle?.shouldShowWithAction(element.title.isNotEmpty()) {
            itemView.bigGridCardViewInspirationCard?.inspirationCardTitle?.text = element.title
        }
    }

    private fun bindContent(element: InspirationCardViewModel) {
        val spacingItemDecoration = ChipSpacingItemDecoration(
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                itemView.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8)
        )

        itemView.bigGridCardViewInspirationCard?.recyclerViewInspirationCardOptionList?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.options)
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return ChipsLayoutManager.newBuilder(itemView.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
    }

    private fun createAdapter(
            inspirationCarouselProductList: List<InspirationCardOptionViewModel>
    ): RecyclerView.Adapter<InspirationCardOptionChipViewHolder> {
        val inspirationCardOptionAdapter = InspirationCardOptionAdapter(inspirationCardListener)
        inspirationCardOptionAdapter.setItemList(inspirationCarouselProductList)

        return inspirationCardOptionAdapter
    }

}