package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyFavoriteShopBinding
import com.tokopedia.homenav.databinding.HolderEmptyWishlistBinding
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class EmptyFavoriteShopViewHolder(itemView: View): AbstractViewHolder<OtherFavoriteShopModel>(itemView) {
    private var binding: HolderEmptyFavoriteShopBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_favorite_shop
    }

    override fun bind(otherFavoriteShopModel: OtherFavoriteShopModel) {
    }
}