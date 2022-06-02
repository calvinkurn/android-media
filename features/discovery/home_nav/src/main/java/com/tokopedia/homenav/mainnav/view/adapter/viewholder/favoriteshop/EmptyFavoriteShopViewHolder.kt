package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class EmptyFavoriteShopViewHolder(itemView: View): AbstractViewHolder<EmptyStateFavoriteShopDataModel>(itemView) {
    private var binding: HolderEmptyFavoriteShopBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_favorite_shop
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_followed_shop.png"
    }

    override fun bind(emptyStateFavoriteShopDataModel: EmptyStateFavoriteShopDataModel) {
        binding?.cardEmptyFavoriteShop?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.favoriteShopEmptyImage
        imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView?.setImageUrl(EMPTY_IMAGE_LINK)
    }
}