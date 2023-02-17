package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FintechRedirectionWidgetDataClass(
    var cta: Int = 0,
    var redirectionUrl: String? = null,
    var tenure: Int = 1,
    var gatewayCode: String? = null,
    var gatewayPartnerName: String? = null,
    var gatewayId: String,
    var userStatus: String? = null,
    var linkingStatus: String? = null,
    var widgetBottomSheet: WidgetBottomsheet? = null,
    var installmentAmout: String? = null,
    var promoName: String? = null,
) : Parcelable
