package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel
import kotlinx.android.synthetic.main.item_af_explore.view.*

/**
 * @author by yfsx on 24/09/18.
 */
class ExploreViewHolder(itemView: View, private val mainView: ExploreContract.View) : AbstractViewHolder<ExploreViewModel>(itemView) {
    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_explore
    }

    override fun bind(element: ExploreViewModel) {
        itemView.card.bind(element.exploreCardViewModel)
    }
}
