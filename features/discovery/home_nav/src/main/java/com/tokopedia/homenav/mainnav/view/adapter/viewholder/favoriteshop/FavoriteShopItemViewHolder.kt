package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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
        itemView.addOnImpressionListener(favoriteShopModel){
            mainNavListener.putEEToTrackingQueue(
                TrackingTransactionSection.getImpressionOnFavoriteShop(
                    userId = mainNavListener.getUserId(),
                    position = adapterPosition,
                    favoriteShopModel = favoriteShopModel.navFavoriteShopModel
                )
            )
        }

        binding?.textShopName?.text = favoriteShopModel.navFavoriteShopModel.name

        if (favoriteShopModel.navFavoriteShopModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.imageShop
            val shimmer = binding?.imageShopShimmer
            Glide.with(itemView.context)
                .load(favoriteShopModel.navFavoriteShopModel.imageUrl)
                .circleCrop()
                .placeholder(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                .dontAnimate()
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView?.setImageDrawable(resource)
                        shimmer?.gone()
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        shimmer?.visible()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        shimmer?.gone()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        shimmer?.gone()
                    }
                })
        }

        binding?.textShopLocation?.text = favoriteShopModel.navFavoriteShopModel.location
        if(favoriteShopModel.navFavoriteShopModel.badgeImageUrl.isNotEmpty()){
            binding?.iconShopBadge?.setImageUrl(favoriteShopModel.navFavoriteShopModel.badgeImageUrl)
        } else binding?.iconShopBadge?.gone()

        binding?.containerFavshop?.setOnClickListener {
            mainNavListener.onFavoriteShopItemClicked(favoriteShopModel.navFavoriteShopModel, adapterPosition)
        }
    }
}