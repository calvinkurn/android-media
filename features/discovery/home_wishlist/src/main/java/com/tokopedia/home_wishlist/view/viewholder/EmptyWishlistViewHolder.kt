package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.EmptyWishlistDataModel
import com.tokopedia.unifycomponents.EmptyState

class EmptyWishlistViewHolder(
        view: View
) : SmartAbstractViewHolder<EmptyWishlistDataModel>(view) {

    private val emptyState: EmptyState by lazy { view.findViewById<EmptyState>(R.id.empty_state) }

    override fun bind(element: EmptyWishlistDataModel, listener: SmartListener) {
        emptyState.setPrimaryCTAClickListener {
            RouteManager.route(emptyState.context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_empty_state
    }
}