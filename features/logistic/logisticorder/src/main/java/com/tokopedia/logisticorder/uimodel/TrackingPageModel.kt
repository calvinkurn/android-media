package com.tokopedia.logisticorder.uimodel

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tokopedia.logisticorder.utils.TippingConstant
import kotlinx.parcelize.Parcelize

data class TrackingPageModel(
    var trackingDataModel: TrackingDataModel = TrackingDataModel()
)

@Parcelize
data class TrackingDataModel(
    var trackOrder: TrackOrderModel = TrackOrderModel(),
    var page: PageModel = PageModel(),
    var tipping: TippingModel = TippingModel(),
    var lastDriver: LastDriverModel = LastDriverModel()
) : Parcelable

@Parcelize
data class TrackOrderModel(
    var detail: DetailModel = DetailModel(),
    var trackHistory: List<TrackHistoryModel> = listOf(),
    var change: Int = -1,
    var status: String = "",
    var orderStatus: Int = -1,
    var noHistory: Int = -1,
    var receiverName: String = "",
    var shippingRefNum: String = "",
    var invalid: Boolean = false
) : Parcelable

@Parcelize
data class DetailModel(
    var shipperCity: String = "",
    var shipperName: String = "",
    var receiverCity: String = "",
    var sendDateTime: String = "",
    var sendDate: String = "",
    var sendTime: String = "",
    var receiverName: String = "",
    var serviceCode: String = "",
    var trackingUrl: String = "",
    var eta: EtaModel = EtaModel()
) : Parcelable

@Parcelize
data class ProofModel(
    var imageId: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var accessToken: String = ""
) : Parcelable

@Parcelize
data class TrackHistoryModel(
    var dateTime: String = "",
    var date: String = "",
    var status: String = "",
    var city: String = "",
    var time: String = "",
    var partnerName: String = "",
    var proof: ProofModel = ProofModel()
) : Parcelable

@Parcelize
data class PageModel(
    var additionalInfo: List<AdditionalInfoModel> = listOf(),
    var contactUsUrl: String = "",
    var targetedTickerParam: TargetedTickerParamModel = TargetedTickerParamModel()
) : Parcelable

@Parcelize
data class TargetedTickerParamModel(
    @SuppressLint("ParamFieldAnnotation")
    val page: String = "",
    @SuppressLint("ParamFieldAnnotation")
    val target: List<Target> = listOf(),
    @SuppressLint("ParamFieldAnnotation")
    val template: Template = Template()
) : Parcelable {

    @Parcelize
    data class Template(
        @SuppressLint("ParamFieldAnnotation")
        val contents: List<Content> = listOf()
    ) : Parcelable {
        @Parcelize
        data class Content(
            val key: String = "",
            val value: String = ""
        ) : Parcelable
    }

    @Parcelize
    data class Target(
        val type: String = "",
        val values: List<String> = listOf()
    ) : Parcelable
}

@Parcelize
data class AdditionalInfoModel(
    var title: String = "",
    var notes: String = "",
    var urlDetail: String = "",
    var urlText: String = ""
) : Parcelable

@Parcelize
data class EtaModel(
    var userInfo: String = "",
    var userUpdatedInfo: String = "",
    var isChanged: Boolean = false
) : Parcelable

@Parcelize
data class TippingModel(
    var status: Int = 0,
    var statusTitle: String = "",
    var statusSubtitle: String = "",
    var refNumber: String = ""
) : Parcelable {
    val eligibleForTipping: Boolean
        get() = status == TippingConstant.OPEN || status == TippingConstant.WAITING_PAYMENT || status == TippingConstant.SUCCESS_PAYMENT || status == TippingConstant.SUCCESS_TIPPING || status == TippingConstant.REFUND_TIP
}

@Parcelize
data class LastDriverModel(
    var photo: String = "",
    var name: String = "",
    var phone: String = "",
    var licenseNumber: String = "",
    var isChanged: Boolean = false
) : Parcelable
