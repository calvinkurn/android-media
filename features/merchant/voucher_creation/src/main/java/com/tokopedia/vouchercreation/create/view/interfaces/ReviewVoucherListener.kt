package com.tokopedia.vouchercreation.create.view.interfaces

import android.graphics.Bitmap
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel

interface ReviewVoucherListener: CreateMerchantVoucherListener {

    fun getToken(): String
    fun getPostBaseUiModel(): PostBaseUiModel
    fun onReturnToStep(@VoucherCreationStep step: Int)
    fun getBannerBitmap(): Bitmap?
    fun getVoucherId(): Int?

}