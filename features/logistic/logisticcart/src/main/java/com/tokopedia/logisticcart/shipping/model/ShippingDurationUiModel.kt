package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
@Parcelize
class ShippingDurationUiModel() : Parcelable, RatesViewModelType {
    var serviceData: ServiceData = ServiceData()
    var shippingCourierViewModelList: List<ShippingCourierUiModel>? = null
    var isSelected: Boolean = false
    var isShowShowCase: Boolean = false
    var errorMessage: String? = null
    var isCodAvailable: Boolean = false
    var codText: String? = null
    var isShowShippingInformation: Boolean = false
    var merchantVoucherModel: MerchantVoucherModel? = null
    var etaErrorCode: Int = 0
    var dynamicPriceModel: DynamicPriceModel? = null
}