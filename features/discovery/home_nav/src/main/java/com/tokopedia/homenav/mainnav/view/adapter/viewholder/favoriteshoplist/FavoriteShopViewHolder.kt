package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshoplist

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopBinding
import com.tokopedia.homenav.databinding.HolderTransactionProductBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.utils.view.binding.viewBinding

class FavoriteShopViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<FavoriteShopModel>(itemView) {
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

        itemView.setOnClickListener {
            //tracker to be added
            //routing to be added
        }
    }
}