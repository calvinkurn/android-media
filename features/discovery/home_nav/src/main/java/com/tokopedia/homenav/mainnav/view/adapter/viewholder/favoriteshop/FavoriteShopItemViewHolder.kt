package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.utils.view.binding.viewBinding

class FavoriteShopItemViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<FavoriteShopModel>(itemView) {
    private var binding: HolderFavoriteShopBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_favorite_shop
    }

    override fun bind(element: FavoriteShopModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(favoriteShopModel: FavoriteShopModel) {
        val context = itemView.context

        //impression tracker to be added

        binding?.textShopName?.text = favoriteShopModel.navFavoriteShopModel.name

        if (!favoriteShopModel.navFavoriteShopModel.imageUrl.isNullOrEmpty()) {
            binding?.imageShop?.loadImageCircle(favoriteShopModel.navFavoriteShopModel.imageUrl)
        }

        binding?.textShopLocation?.text = favoriteShopModel.navFavoriteShopModel.location
        if(!favoriteShopModel.navFavoriteShopModel.badgeImageUrl.isNullOrEmpty()){
            binding?.iconShopBadge?.setImageUrl(favoriteShopModel.navFavoriteShopModel.badgeImageUrl)
        } else binding?.iconShopBadge?.gone()

        binding?.containerFavshop?.setOnClickListener {
            //tracker to be added
            //routing to be added
        }
    }
}