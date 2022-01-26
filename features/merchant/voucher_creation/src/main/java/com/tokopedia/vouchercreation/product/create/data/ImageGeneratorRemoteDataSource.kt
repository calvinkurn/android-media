package com.tokopedia.vouchercreation.product.create.data

import okhttp3.ResponseBody
import javax.inject.Inject

class ImageGeneratorRemoteDataSource @Inject constructor(
    private val imageGeneratorService: ImageGeneratorService
) {

    suspend fun previewImage(
        sourceId: String,
        platform: String,
        isPublic: String,
        voucherBenefitType: String,
        voucherCashbackType: String,
        voucherCashbackPercentage: Int,
        voucherNominalAmount: Int,
        voucherNominalSymbol: String,
        shopLogo: String,
        shopName: String,
        voucherCode: String,
        voucherStartTime: String,
        voucherFinishTime: String,
        productCount: Int,
        productImage1: String,
        audienceTarget: String
    ): ResponseBody {
        return imageGeneratorService.previewImage(
            sourceId,
            platform,
            isPublic,
            voucherBenefitType,
            voucherCashbackType,
            voucherCashbackPercentage,
            voucherNominalAmount,
            voucherNominalSymbol,
            shopLogo,
            shopName,
            voucherCode,
            voucherStartTime,
            voucherFinishTime,
            productCount,
            productImage1,
            audienceTarget
        )
    }
}