package com.tokopedia.vouchercreation.create.view.interfaces

import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel

interface CreateMerchantVoucherListener {

    fun getPromoCodePrefix(): String
    fun getVoucherReviewUiModel(): VoucherReviewUiModel
    fun getBannerVoucherUiModel(): BannerVoucherUiModel
    fun getBannerBaseUiModel(): BannerBaseUiModel

}