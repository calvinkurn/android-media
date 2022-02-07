package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FintechRedirectionWidgetDataClass(
    var cta: Int = 0,
    var redirectionUrl: String? = null,
    var tenure:Int = 1,
    var productUrl:String? = null,
    var gatewayCode:String? = null,
    var widgetBottomSheet: WidgetBottomsheet? = null
): Parcelable
