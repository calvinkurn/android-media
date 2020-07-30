package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.EmptySearchWishlistDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.unifyprinciples.Typography

class EmptySearchWishlistViewHolder(
        view: View
) : SmartAbstractViewHolder<EmptySearchWishlistDataModel>(view) {

    private val description: Typography by lazy { view.findViewById<Typography>(R.id.description) }

    override fun bind(element: EmptySearchWishlistDataModel, listener: SmartListener) {
        description.text = String.format(getString(R.string.wishlist_search_not_found_description), element.keyword)
    }

    companion object{
        val LAYOUT = R.layout.layout_new_wishlist_empty_search_state
    }
}