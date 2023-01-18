package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderViewAllRevampBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class OtherWishlistViewHolder(itemView: View, val mainNavListener: MainNavListener) : AbstractViewHolder<OtherWishlistModel>(itemView) {
    private var binding: HolderViewAllRevampBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_view_all_revamp
    }

    override fun bind(otherWishlistModel: OtherWishlistModel) {
        val context = itemView.context
        setForegroundClickViewAllCard()
        binding?.cardViewAll?.description = itemView.resources.getString(R.string.wishlist_view_all_desc)
        binding?.cardViewAll?.setCta(context.getString(R.string.global_view_all))

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnWishlistViewAll()
            RouteManager.route(context, ApplinkConst.WISHLIST)
        }
    }

    private fun setForegroundClickViewAllCard() {
        val outValue = TypedValue()
        itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        binding?.cardViewAll?.cardView?.foreground = itemView.context.getDrawable(outValue.resourceId)
    }
}
