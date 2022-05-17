package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderOtherFavoriteShopBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OtherTransactionRevampViewHolder (itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherTransactionRevampModel>(itemView) {
    private var binding: HolderOtherFavoriteShopBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_other_favorite_shop
        private const val OTHER_TRACKING_LABEL = "other"
    }

    private fun setForegroundClickViewAllCard() {
        val outValue = TypedValue()
        itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        binding?.cardViewAllFavshop?.cardView?.foreground = itemView.context.getDrawable(outValue.resourceId)
    }

    override fun bind(otherTransactionRevampModel: OtherTransactionRevampModel) {
        setForegroundClickViewAllCard()
        binding?.cardViewAllFavshop?.setCta(itemView.resources.getString(R.string.transaction_view_all))
        binding?.cardViewAllFavshop?.cardView?.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    OTHER_TRACKING_LABEL)
            RouteManager.route(itemView.context, ApplinkConst.PURCHASE_ORDER)
        }
    }
}