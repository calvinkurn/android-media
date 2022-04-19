package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderOtherTransactionProductBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.utils.view.binding.viewBinding

class OtherTransactionViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OtherTransactionModel>(itemView) {
    private var binding: HolderOtherTransactionProductBinding? by viewBinding()
    val otherTrackingLabel = "other"
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_other_transaction_product
    }

    override fun bind(otherTransactionModel: OtherTransactionModel) {
        val context = itemView.context
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding?.holderView?.setBackgroundResource(R.drawable.bg_transaction_other)
        }
        binding?.transactionOthersCount?.text = context.getString(R.string.transaction_others_count)

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    otherTrackingLabel)
            RouteManager.route(context, ApplinkConst.PURCHASE_ORDER)
        }
    }
}