package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS_DT
import kotlinx.parcelize.Parcelize

@Parcelize
data class FreeShippingGeneralData(
        var badgeUrl: String = "",
        var boType: Int = 0,
        var boName: String = "",
): Parcelable {

    fun isBoTypePlus(): Boolean {
        return boType == BO_TYPE_PLUS || boType == BO_TYPE_PLUS_DT
    }
}
