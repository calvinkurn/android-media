package com.tokopedia.vouchercreation.product.create.data.source

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.product.create.data.service.ImageGeneratorService
import okhttp3.ResponseBody
import javax.inject.Inject

class ImageGeneratorRemoteDataSource @Inject constructor(
    private val imageGeneratorService: ImageGeneratorService,
    private val userSessionInterface: UserSessionInterface
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
        firstProductImageUrl: String,
        secondProductImageUrl: String,
        thirdProductImageUrl: String,
        audienceTarget: String
    ): ResponseBody {
        return imageGeneratorService.previewImage(
            userSessionInterface.accessToken,
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
            firstProductImageUrl,
            secondProductImageUrl,
            thirdProductImageUrl,
            audienceTarget
        )
    }
}