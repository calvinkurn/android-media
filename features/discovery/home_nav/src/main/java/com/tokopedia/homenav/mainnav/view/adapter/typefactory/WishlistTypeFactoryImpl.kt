package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist.EmptyWishlistViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist.OtherWishlistViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist.WishlistItemViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistNavVisitable

@MePage(MePage.Widget.WISHLIST)
class WishlistTypeFactoryImpl(val mainNavListener: MainNavListener) : BaseAdapterTypeFactory(), WishlistTypeFactory {

    override fun type(wishlistModel: WishlistModel): Int {
        return WishlistItemViewHolder.LAYOUT
    }

    override fun type(otherWishlistModel: OtherWishlistModel): Int {
        return OtherWishlistViewHolder.LAYOUT
    }

    override fun type(emptyStateWishlistDataModel: EmptyStateWishlistDataModel): Int {
        return EmptyWishlistViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<WishlistNavVisitable> {
        return when (viewType) {
            WishlistItemViewHolder.LAYOUT -> WishlistItemViewHolder(view, mainNavListener)
            OtherWishlistViewHolder.LAYOUT -> OtherWishlistViewHolder(view, mainNavListener)
            EmptyWishlistViewHolder.LAYOUT -> EmptyWishlistViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        } as AbstractViewHolder<WishlistNavVisitable>
    }
}
