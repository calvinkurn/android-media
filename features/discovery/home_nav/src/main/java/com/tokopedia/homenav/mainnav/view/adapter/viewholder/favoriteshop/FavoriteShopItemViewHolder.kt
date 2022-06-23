package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.unifycomponents.ImageUnify
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

    override fun bind(element: FavoriteShopModel) {
        setLayoutFullWidth(element)

        itemView.addOnImpressionListener(element){
            mainNavListener.putEEToTrackingQueue(
                TrackingTransactionSection.getImpressionOnFavoriteShop(
                    userId = mainNavListener.getUserId(),
                    position = adapterPosition,
                    favoriteShopModel = element.navFavoriteShopModel
                )
            )
        }

        binding?.textShopName?.text = element.navFavoriteShopModel.name

        if (element.navFavoriteShopModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.imageShop
            imageView?.type = ImageUnify.TYPE_CIRCLE
            imageView?.setImageUrl(element.navFavoriteShopModel.imageUrl)
        }

        binding?.textShopLocation?.text = element.navFavoriteShopModel.location
        if(element.navFavoriteShopModel.badgeImageUrl.isNotEmpty()){
            binding?.iconShopBadge?.setImageUrl(element.navFavoriteShopModel.badgeImageUrl)
        } else binding?.iconShopBadge?.gone()

        binding?.containerFavshop?.setOnClickListener {
            mainNavListener.onFavoriteShopItemClicked(element.navFavoriteShopModel, adapterPosition)
        }
    }

    private fun setLayoutFullWidth(element: FavoriteShopModel) {
        val layoutParams = binding?.cardFavoriteShop?.layoutParams
        if (element.navFavoriteShopModel.fullWidth) {
            layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams?.width = itemView.resources.getDimension(R.dimen.nav_card_me_page_size).toInt()
        }
        binding?.cardFavoriteShop?.layoutParams = layoutParams
    }
}