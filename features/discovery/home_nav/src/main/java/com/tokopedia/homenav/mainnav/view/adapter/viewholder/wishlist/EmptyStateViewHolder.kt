package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.databinding.HolderEmptyStateBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateDataModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU

class EmptyStateViewHolder(itemView: View): AbstractViewHolder<EmptyStateDataModel>(itemView) {
    private var binding: HolderEmptyStateBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_state
        private const val TRANSACTION_EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_transaction.png"
        private const val FAVORITE_SHOP_EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_followed_shop.png"
        private const val WISHLIST_EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_wishlist.png"
    }

    override fun bind(emptyStateDataModel: EmptyStateDataModel) {
        val context = itemView.context
        binding?.cardEmptyState?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.emptyImage
        imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        when(emptyStateDataModel.id){
            ID_WISHLIST_MENU -> {
                imageView?.setImageUrl(WISHLIST_EMPTY_IMAGE_LINK)
                binding?.emptyText?.text = context.getText(R.string.empty_state_wishlist)
            }
            ID_FAVORITE_SHOP -> {
                imageView?.setImageUrl(FAVORITE_SHOP_EMPTY_IMAGE_LINK)
                binding?.emptyText?.text = context.getText(R.string.empty_state_favorite_shop)
            }
        }
    }
}