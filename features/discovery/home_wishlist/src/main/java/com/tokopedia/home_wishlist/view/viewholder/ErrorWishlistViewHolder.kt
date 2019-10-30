package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.ErrorWishlistDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener

class ErrorWishlistViewHolder(
        view: View
) : SmartAbstractViewHolder<ErrorWishlistDataModel>(view) {

    private val emptyState: EmptyStateUnify by lazy { view.findViewById<EmptyStateUnify>(R.id.error_state) }

    override fun bind(element: ErrorWishlistDataModel, listener: SmartListener) {
        element.error?.let {
            emptyState.emptyStateDescription = element.error
            emptyState.setPrimaryCTAClickListener {
                (listener as WishlistListener).onTryAgainClick()
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_error_state
    }
}