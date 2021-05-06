package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductNotifyMeDataModel(
        val type: String = "",
        val name: String = "",
        var campaignID: String = "",
        var campaignType: String = "",
        var campaignTypeName: String = "",
        var startDate: String = "",
        var notifyMe: Boolean = false,
        var upcomingNplData: UpcomingNplDataModel = UpcomingNplDataModel()
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductNotifyMeDataModel) {
            campaignID == newData.campaignID
                    && campaignType == newData.campaignType
                    && campaignTypeName == newData.campaignTypeName
                    && startDate == newData.startDate
                    && notifyMe == newData.notifyMe
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductNotifyMeDataModel) {
            if (campaignID != newData.campaignID && startDate != newData.startDate && campaignTypeName != newData.campaignTypeName && campaignType != newData.campaignType) {
                //Update the whole data
                return null
            }

            if (notifyMe != newData.notifyMe) {
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