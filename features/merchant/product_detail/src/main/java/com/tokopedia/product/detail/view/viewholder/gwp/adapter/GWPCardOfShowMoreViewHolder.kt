package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.ViewGroup
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.viewallcard.ViewAllCard
import timber.log.Timber

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfShowMoreViewHolder(
    private val binding: ViewAllCard
) : GWPCardViewHolder<GWPWidgetUiModel.Card.LoadMore>(binding.rootView) {

    override fun bind(data: GWPWidgetUiModel.Card.LoadMore) {
        // binding.allcard1.title = "Title here"
        binding.description = data.title

        binding.cardView.setOnClickListener {
            Timber.d("loadmore click")
        }
    }

    companion object {
        val ID = ViewAllCard.hashCode()

        fun create(parent: ViewGroup): GWPCardOfShowMoreViewHolder {
            val card = ViewAllCard(parent.context)
            return GWPCardOfShowMoreViewHolder(card)
        }
    }
}
