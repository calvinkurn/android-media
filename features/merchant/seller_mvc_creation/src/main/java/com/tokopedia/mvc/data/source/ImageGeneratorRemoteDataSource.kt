package com.tokopedia.mvc.data.source

import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.mvc.data.service.ImageGeneratorService
import okhttp3.ResponseBody
import javax.inject.Inject

class ImageGeneratorRemoteDataSource @Inject constructor(
    private val imageGeneratorService: ImageGeneratorService,
    private val userSessionInterface: UserSessionInterface
) {

    suspend fun previewImage(
        param: PreviewImageParam
    ): ResponseBody {
        with(param) {
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
                voucherDiscountType,
                voucherDiscountPercentage,
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

    data class PreviewImageParam (
        val sourceId: String,
        val platform: String,
        val isPublic: String,
        val voucherBenefitType: String,
        val voucherCashbackType: String,
        val voucherCashbackPercentage: Int,
        val voucherNominalAmount: Int,
        val voucherNominalSymbol: String,
        val voucherDiscountType: String,
        val voucherDiscountPercentage: Int,
        val shopLogo: String,
        val shopName: String,
        val voucherCode: String,
        val voucherStartTime: String,
        val voucherFinishTime: String,
        val productCount: Int,
        val firstProductImageUrl: String,
        val secondProductImageUrl: String?,
        val thirdProductImageUrl: String?,
        val audienceTarget: String
    )
}
