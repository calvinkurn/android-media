package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderOtherFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

class OtherFavoriteShopViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherFavoriteShopModel>(itemView) {
    private var binding: HolderOtherFavoriteShopBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_other_favorite_shop
    }

    override fun bind(otherFavoriteShopModel: OtherFavoriteShopModel) {
        val context = itemView.context
        binding?.cardViewAllFavshop?.setCta(context.getString(R.string.global_view_all))
        binding?.cardViewAllFavshop?.descriptionView?.gone()
        binding?.cardViewAllFavshop?.titleView?.gone()

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnFavoriteShopViewAll()
            RouteManager.route(context, ApplinkConst.FAVORITE)
        }
    }
}