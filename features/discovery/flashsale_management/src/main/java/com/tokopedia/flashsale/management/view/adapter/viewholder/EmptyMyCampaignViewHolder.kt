package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.view.activity.FlashsaleWebViewActivity
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel
import kotlinx.android.synthetic.main.item_empty_my_campaign.view.*

class EmptyMyCampaignViewHolder(itemView: View) : AbstractViewHolder<EmptyMyCampaignViewModel>(itemView) {

    override fun bind(emptyMyCampaignViewModel: EmptyMyCampaignViewModel) {
        itemView.see_more.setOnClickListener {
            with(itemView.context){
                startActivity(FlashsaleWebViewActivity.createIntent(this, FlashSaleConstant.FLASHSALE_ABOUT_URL))
            }
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_empty_my_campaign
    }
}
