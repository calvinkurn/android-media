package com.tokopedia.vouchercreation.common.base

import com.tokopedia.usecase.RequestParams

object VoucherSource {

    private const val SOURCE_KEY = "source"
    const val SELLERAPP = "android-sellerapp"

    @JvmStatic
    internal fun getVoucherRequestParams() =
            RequestParams().apply {
                putString(SOURCE_KEY, SELLERAPP)
            }

}