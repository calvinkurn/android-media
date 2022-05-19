package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderOtherWishlistBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

class OtherWishlistViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherWishlistModel>(itemView) {
    private var binding: HolderOtherWishlistBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_other_wishlist
    }

    override fun bind(otherWishlistModel: OtherWishlistModel) {
        val context = itemView.context
        binding?.cardViewAllWishlist?.setCta(context.getString(R.string.global_view_all))
        binding?.cardViewAllWishlist?.descriptionView?.gone()
        binding?.cardViewAllWishlist?.titleView?.gone()

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnWishlistViewAll()
        }
    }
}