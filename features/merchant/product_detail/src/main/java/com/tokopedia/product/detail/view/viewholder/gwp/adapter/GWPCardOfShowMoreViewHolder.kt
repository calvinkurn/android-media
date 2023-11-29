package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.detail.databinding.GwpShowMoreBinding
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.viewallcard.ViewAllCard
import timber.log.Timber

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfShowMoreViewHolder(
    private val binding: GwpShowMoreBinding
) : GWPCardViewHolder<GWPWidgetUiModel.Card.LoadMore>(binding.root) {

    init {
        binding.gwpProductImageView.mode = ViewAllCard.MODE_NORMAL
    }

    override fun bind(data: GWPWidgetUiModel.Card.LoadMore) = with(binding.gwpProductImageView) {
        description = data.title
        cardView.setOnClickListener {
            Timber.d("loadmore click")
        }
    }

    companion object {
        val ID = ViewAllCard.hashCode()

        fun create(parent: ViewGroup): GWPCardOfShowMoreViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = GwpShowMoreBinding.inflate(inflater, parent, false)
            return GWPCardOfShowMoreViewHolder(binding)
        }
    }
}
