package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
@Parcelize
class ShippingDurationUiModel(
    var serviceData: ServiceData = ServiceData(),
    var shippingCourierViewModelList: List<ShippingCourierUiModel> = emptyList(),
    var isSelected: Boolean = false,
    var isShowShowCase: Boolean = false,
    var errorMessage: String? = null,
    var isCodAvailable: Boolean = false,
    var codText: String? = null,
    var isShowShippingInformation: Boolean = false,
    var merchantVoucherModel: MerchantVoucherModel = MerchantVoucherModel(),
    var etaErrorCode: Int = 0,
    var dynamicPriceModel: DynamicPriceModel = DynamicPriceModel()
) : RatesViewModelType, Parcelable
