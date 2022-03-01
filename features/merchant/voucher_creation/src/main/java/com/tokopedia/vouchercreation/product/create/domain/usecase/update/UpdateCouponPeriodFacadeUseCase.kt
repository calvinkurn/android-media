package com.tokopedia.vouchercreation.product.create.domain.usecase.update

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.util.GenerateImageParamsBuilder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateCouponPeriodFacadeUseCase @Inject constructor(
    private val updateCouponPeriodUseCase: UpdateCouponPeriodUseCase,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder
) {

    companion object {
        private const val IS_UPDATE_MODE = true
        private const val IS_TO_CREATE_NEW_COUPON = false
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponId: Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings
    ): Boolean {
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE) }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }

        val shop = shopDeferred.await()

        val generateImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.HORIZONTAL,
                couponInformation,
                couponSettings,
                shop,
                emptyList()
            )
        }

        val generateSquareImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.SQUARE,
                couponInformation,
                couponSettings,
                shop,
                emptyList()
            )
        }

        val imageUrl = generateImageDeferred.await()
        val squareImageUrl = generateSquareImageDeferred.await()
        val voucher = initiateVoucherDeferred.await()

        val updateCouponDeferred = scope.async {
            updateCoupon(
                couponId,
                couponInformation,
                couponSettings,
                voucher.token,
                imageUrl,
                squareImageUrl
            )
        }

        return updateCouponDeferred.await()
    }


    private suspend fun updateCoupon(
        couponId : Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        token: String,
        imageUrl: String,
        imageSquare:String
    ): Boolean {
        val params = updateCouponPeriodUseCase.createRequestParam(
            couponId,
            couponInformation,
            couponSettings,
            token,
            imageUrl,
            imageSquare
        )
        updateCouponPeriodUseCase.params = params

        return updateCouponPeriodUseCase.executeOnBackground()

    }

    private suspend fun generateImage(
        sourceId: String,
        imageRatio : ImageRatio,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        shop: ShopBasicDataResult,
        products : List<CouponProduct>
    ): String {
        val imageParams = imageBuilder.build(imageRatio, couponInformation, couponSettings, products, shop.logo, shop.shopName)

        val requestParams = arrayListOf(
            GenerateImageParams("platform", imageParams.platform),
            GenerateImageParams("is_public", imageParams.isPublic),
            GenerateImageParams("voucher_benefit_type", imageParams.voucherBenefitType),
            GenerateImageParams("voucher_cashback_type", imageParams.voucherCashbackType),
            GenerateImageParams("voucher_cashback_percentage", imageParams.voucherCashbackPercentage),
            GenerateImageParams("voucher_nominal_amount", imageParams.voucherNominalAmount),
            GenerateImageParams("voucher_nominal_symbol", imageParams.voucherNominalSymbol),
            GenerateImageParams("shop_logo", imageParams.shopLogo),
            GenerateImageParams("shop_name", imageParams.shopName),
            GenerateImageParams("voucher_code", imageParams.voucherCode),
            GenerateImageParams("voucher_start_time", imageParams.voucherStartTime),
            GenerateImageParams("voucher_finish_time", imageParams.voucherFinishTime),
            GenerateImageParams("product_count", imageParams.productCount),
            GenerateImageParams("product_image_1", imageParams.productImage1),
            GenerateImageParams("product_image_2", imageParams.productImage2),
            GenerateImageParams("product_image_3", imageParams.productImage3),
            GenerateImageParams("audience_target", imageParams.audienceTarget)
        )

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, requestParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateVoucher(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(isUpdateMode, IS_TO_CREATE_NEW_COUPON)
        return initiateCouponUseCase.executeOnBackground()
    }
}