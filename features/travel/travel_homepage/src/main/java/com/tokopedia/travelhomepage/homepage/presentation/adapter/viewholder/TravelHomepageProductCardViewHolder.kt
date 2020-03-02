package com.tokopedia.travelhomepage.homepage.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageProductCardModel
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener

/**
 * @author by jessica on 2020-03-02
 */

class TravelHomepageProductCardViewHolder(itemView: View, private val onItemBindListener: OnItemBindListener,
                                          private val onItemClickListener: OnItemClickListener) : AbstractViewHolder<TravelHomepageProductCardModel>(itemView) {
    override fun bind(element: TravelHomepageProductCardModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val LAYOUT = 0
    }

}