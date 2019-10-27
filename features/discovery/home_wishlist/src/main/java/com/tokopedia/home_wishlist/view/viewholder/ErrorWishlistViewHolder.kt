package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.EmptyWishlistDataModel
import com.tokopedia.home_wishlist.model.datamodel.ErrorWishlistDataModel
import com.tokopedia.unifycomponents.EmptyState

class ErrorWishlistViewHolder(
        view: View
) : AbstractViewHolder<ErrorWishlistDataModel>(view) {

    private val emptyState: EmptyState by lazy { view.findViewById<EmptyState>(R.id.error_state) }

    override fun bind(element: ErrorWishlistDataModel?) {
        element?.error?.let {
            emptyState.emptyStateDescription = element.error
            emptyState.setPrimaryCTAClickListener {

            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_error_state
    }
}