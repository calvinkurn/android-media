package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderErrorFavoriteShopBinding
import com.tokopedia.homenav.databinding.HolderErrorWishlistBinding
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ErrorStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class ErrorFavoriteShopViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<ErrorStateFavoriteShopDataModel>(itemView) {
    private var binding: HolderErrorFavoriteShopBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_error_favorite_shop
    }

    override fun bind(errorStateFavoriteShopDataModel: ErrorStateFavoriteShopDataModel) {
        binding?.localloadErrorStateFavoriteShop?.refreshBtn?.setOnClickListener {
            mainNavListener.onErrorFavoriteShopClicked()
        }
    }
}