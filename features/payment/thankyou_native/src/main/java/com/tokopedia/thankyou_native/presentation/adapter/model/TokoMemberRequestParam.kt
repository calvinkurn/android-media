package com.tokopedia.thankyou_native.presentation.adapter.model

import android.annotation.SuppressLint
import com.tokopedia.thankyou_native.data.mapper.PageType
import com.tokopedia.tokomember.model.ShopParams

@SuppressLint("ResponseFieldAnnotation")
data class TokoMemberRequestParam(
    var shopID: Int = 0,
    var amount: Float = 0F,
    var pageType: PageType?,
    var paymentID: String  = "",
    var source: Int = 0,
    var orderData : List<ShopParams> = arrayListOf()
)
