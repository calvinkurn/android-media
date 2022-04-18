package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshoplist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderOtherFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class OtherFavoriteShopViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherFavoriteShopModel>(itemView) {
    private var binding: HolderOtherFavoriteShopBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_other_favorite_shop
    }

    override fun bind(otherFavoriteShopModel: OtherFavoriteShopModel) {
        val context = itemView.context
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding?.holderView?.setBackgroundResource(R.drawable.bg_transaction_other)
        }
        binding?.favoriteShopOthersCount?.text = String.format(
            context.getString(R.string.transaction_others_count),
            otherFavoriteShopModel.otherShopsCount
        )

        itemView.setOnClickListener {
            //click tracker to be added
            //routing to be added
        }
    }
}