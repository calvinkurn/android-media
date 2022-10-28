package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.helper

import android.content.Context
import com.tokopedia.product.manage.databinding.ItemCampaignStockInfoCardBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.unifycomponents.Label

object VariantReservedViewHolderHelper {

    fun ItemCampaignStockInfoCardBinding.bind(context: Context,
                                              uiModel: ReservedEventInfoUiModel) {
        ivCampaignInfoIcon.setImageUrl(uiModel.campaignIconUrl)
        tvCampaignInfoTitle.text = uiModel.eventType
        labelCampaignInfoStatus.run {
            if (uiModel.periodStatus == ReservedEventInfoUiModel.PeriodStatus.ONGOING) {
                setLabel(context.getString(com.tokopedia.product.manage.R.string.product_manage_campaign_stock_ongoing))
                setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            } else {
                setLabel(context.getString(com.tokopedia.product.manage.R.string.product_manage_campaign_stock_upcoming))
                setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
        }
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