package com.tokopedia.product.detail.view.viewholder.campaign.ui.model

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductNotifyMeUiModel(
    val type: String = "",
    val name: String = "",
    var shouldShow: Boolean = false,
    var data: UpcomingCampaignUiModel = UpcomingCampaignUiModel()
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductNotifyMeUiModel) {
            data == newData.data && shouldShow == newData.shouldShow
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductNotifyMeUiModel) {
            if (data.notifyMe != newData.data.notifyMe) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_NOTIFY_ME)
                bundle
            } else {
                null
            }
        } else {
            null
        }
    }
}
