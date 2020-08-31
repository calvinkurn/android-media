package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.EmptyWishlistDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class EmptyWishlistViewHolder(
        view: View
) : SmartAbstractViewHolder<EmptyWishlistDataModel>(view) {

    private val retry: UnifyButton by lazy { view.findViewById<UnifyButton>(R.id.retry_button) }

    override fun bind(element: EmptyWishlistDataModel, listener: SmartListener) {
        retry?.setOnClickListener {
            RouteManager.route(it.context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE)
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_new_wishlist_empty_state
    }
}