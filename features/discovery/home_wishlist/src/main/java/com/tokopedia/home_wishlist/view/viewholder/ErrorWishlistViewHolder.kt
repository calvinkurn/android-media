package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.ErrorWishlistDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ErrorWishlistViewHolder(
        view: View
) : SmartAbstractViewHolder<ErrorWishlistDataModel>(view) {

    private val description: Typography by lazy { view.findViewById<Typography>(R.id.description) }
    private val retry: UnifyButton by lazy { view.findViewById<UnifyButton>(R.id.retry_button) }

    override fun bind(element: ErrorWishlistDataModel, listener: SmartListener) {
        element.error?.let {
            description.text = element.error
            retry.setOnClickListener {
                (listener as WishlistListener).onTryAgainClick()
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_wishlist_error_state
    }
}