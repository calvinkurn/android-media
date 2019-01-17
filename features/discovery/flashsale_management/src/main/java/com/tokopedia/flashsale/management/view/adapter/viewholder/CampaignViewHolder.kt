package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.graphics.drawable.GradientDrawable
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.ekstension.loadUrl
import com.tokopedia.flashsale.management.ekstension.setTextDrawableColor
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import kotlinx.android.synthetic.main.item_campaign.view.*

class CampaignViewHolder(itemView: View) : AbstractViewHolder<CampaignViewModel>(itemView) {

    override fun bind(campaignViewModel: CampaignViewModel) {
        itemView.tvCampaignType.text = campaignViewModel.campaignType
        itemView.tvCampaignName.text = campaignViewModel.name
        itemView.tvCampaignDate.text = campaignViewModel.campaignPeriod
        with(itemView.tvStatus){
            text = campaignViewModel.status
            val (colorText, colorBg) = FlashSaleConstant.statusColorList[campaignViewModel.status.toLowerCase()] ?:
                    FlashSaleConstant.defaultPairColor
            setTextDrawableColor(ContextCompat.getColor(context, colorText))

            val bgDrawable = background.mutate() as GradientDrawable
            bgDrawable.setColor(ContextCompat.getColor(context, colorBg))
            bgDrawable.invalidateSelf()

        }
        itemView.tvStatus.text = campaignViewModel.status
        itemView.ivImageCampaign.loadUrl(campaignViewModel.cover, 20f)

    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_campaign
    }
}
