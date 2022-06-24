package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.helper

import android.content.Context
import com.tokopedia.product.manage.databinding.ItemCampaignStockInfoCardBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel

object VariantReservedViewHolderHelper {

    fun ItemCampaignStockInfoCardBinding.bind(context: Context,
                                              uiModel: ReservedEventInfoUiModel) {
        ivCampaignInfoIcon.setImageUrl(uiModel.campaignIconUrl)
        tvCampaignInfoTitle.text = uiModel.eventType
        tvCampaignInfoName.text = uiModel.campaignName
        tvCampaignInfoStockCount.text = uiModel.stock
        tvCampaignInfoPeriod.text =
            context.getString(
                com.tokopedia.product.manage.R.string.product_manage_campaign_stock_period,
                uiModel.startTime,
                uiModel.endTime
            )
    }

}