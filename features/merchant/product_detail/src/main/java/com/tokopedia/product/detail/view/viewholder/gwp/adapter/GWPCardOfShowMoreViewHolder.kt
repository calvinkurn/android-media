package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.setLayoutWidth
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.viewallcard.ViewAllCard
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.viewallcard.R as viewallcardR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfShowMoreViewHolder(
    private val binding: ViewAllCard,
    private val onEvent: (GWPEvent) -> Unit
) : GWPCardViewHolder<GWPWidgetUiModel.Card.LoadMore>(binding.rootView) {

    init {
        binding.mode = ViewAllCard.MODE_NORMAL
        setupCardView()
    }

    private fun setupCardView() {
        val context = binding.context
        val color = context.getColorChecker(unifyprinciplesR.color.Unify_NN0)

        binding.cardView.cardType = CardUnify2.TYPE_BORDER
        binding.cardView.setMargin(0, 0, 0, 0)
        binding.cardView.setLayoutHeight(CARD_HEIGHT)
        binding.cardView.setLayoutWidth(CARD_WIDTH)
        binding.cardView.setCardUnifyBackgroundColor(color)
    }

    override fun bind(data: GWPWidgetUiModel.Card.LoadMore) = with(binding) {
        setTitle(title = data.title)
        setEvent(data = data)
    }

    private fun ViewAllCard.setTitle(title: String) {
        description = title
    }

    private fun ViewAllCard.setEvent(data: GWPWidgetUiModel.Card.LoadMore) {
        cardView.setOnClickListener {
            onEvent(GWPEvent.OnClickShowMore(data = data))
        }
    }

    companion object {
        val ID = viewallcardR.layout.view_all_card_layout

        private val CARD_HEIGHT = 88.toPx()
        private val CARD_WIDTH = 99.toPx()

        fun create(
            parent: ViewGroup,
            onEvent: (GWPEvent) -> Unit
        ): GWPCardOfShowMoreViewHolder {
            val viewAllCard = ViewAllCard(parent.context)
            return GWPCardOfShowMoreViewHolder(binding = viewAllCard, onEvent = onEvent)
        }
    }
}
