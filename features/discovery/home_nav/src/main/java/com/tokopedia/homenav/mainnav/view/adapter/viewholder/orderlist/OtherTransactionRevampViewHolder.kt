package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.util.TypedValue
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderViewAllRevampBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.track.TrackApp
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OtherTransactionRevampViewHolder (itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherTransactionRevampModel>(itemView) {
    private var binding: HolderViewAllRevampBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_view_all_revamp
    }

    private fun setForegroundClickViewAllCard() {
        val outValue = TypedValue()
        itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        binding?.cardViewAll?.cardView?.foreground = itemView.context.getDrawable(outValue.resourceId)
    }

    override fun bind(otherTransactionRevampModel: OtherTransactionRevampModel) {
        setForegroundClickViewAllCard()
        binding?.cardViewAll?.description = itemView.resources.getString(R.string.transaction_view_all_desc)
        binding?.cardViewAll?.setCta(itemView.resources.getString(R.string.transaction_view_all))
        binding?.cardViewAll?.cardView?.setOnClickListener {
            val tracking = TrackingTransactionSection.getClickViewAllTransaction()
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
            RouteManager.route(itemView.context, ApplinkConst.PURCHASE_ORDER)
        }
    }
}
