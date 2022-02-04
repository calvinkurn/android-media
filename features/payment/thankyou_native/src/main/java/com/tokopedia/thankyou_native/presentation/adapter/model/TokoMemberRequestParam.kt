package com.tokopedia.thankyou_native.presentation.adapter.model

import android.annotation.SuppressLint
import com.tokopedia.thankyou_native.data.mapper.PageType

@SuppressLint("ResponseFieldAnnotation")
data class TokoMemberRequestParam(
    var shopID: Int = 0,
    var amount: Float = 0F,
    var pageType: PageType?,
    var paymentID: String,
    var source: Int
)