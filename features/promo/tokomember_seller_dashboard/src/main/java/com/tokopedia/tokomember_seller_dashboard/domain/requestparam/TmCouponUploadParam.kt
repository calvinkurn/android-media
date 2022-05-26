package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import android.annotation.SuppressLint
import java.io.File

@SuppressLint("ParamFieldAnnotation")
data class TmCouponUploadParam(
    var filePath: File? = null,
    var sourceId: String = ""
)