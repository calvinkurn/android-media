package com.tokopedia.vouchercreation.create.view.interfaces

import android.graphics.Bitmap

interface SetVoucherPeriodListener: CreateMerchantVoucherListener {

    fun onSetVoucherPeriod(dateStart: String, dateEnd: String, hourStart: String, hourEnd: String)
    fun onSuccessGetBannerBitmap(bitmap: Bitmap)

}