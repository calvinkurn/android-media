package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
@Parcelize
class ShippingCourierUiModel(
    var productData: ProductData = ProductData(),
    var serviceData: ServiceData = ServiceData(),
    var blackboxInfo: String? = null,
    var ratesId: String? = null,
    var additionalFee: Int = 0,
    var isAllowDropshipper: Boolean = false,
    var isSelected: Boolean = false,
    var preOrderModel: PreOrderModel? = null
) : Parcelable, RatesViewModelType
