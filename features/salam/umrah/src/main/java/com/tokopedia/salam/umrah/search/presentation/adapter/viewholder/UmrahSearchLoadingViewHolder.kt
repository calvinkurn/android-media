package com.tokopedia.salam.umrah.search.presentation.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.R
/**
 * @author by M on 29/10/2019
 */
class UmrahSearchLoadingViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {
    override fun bind(element: LoadingModel) {
        itemView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }
    companion object {
        val LAYOUT = R.layout.item_umrah_search_shimmering
    }
}