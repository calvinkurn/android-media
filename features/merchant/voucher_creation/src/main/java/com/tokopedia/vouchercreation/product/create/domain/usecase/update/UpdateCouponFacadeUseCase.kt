package com.tokopedia.vouchercreation.product.create.domain.usecase.update

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.product.create.util.GenerateImageParamsBuilder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateCouponFacadeUseCase @Inject constructor(
    private val updateCouponUseCase: UpdateCouponUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder
) {

    companion object {
        private const val IS_UPDATE_MODE = true
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponId: Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
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
                couponProducts
            )
        }

        val generateSquareImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.SQUARE,
                couponInformation,
                couponSettings,
                shop,
                couponProducts
            )
        }

        val generatePortraitImage = scope.async {
            generateImage(
                sourceId,
                ImageRatio.VERTICAL,
                couponInformation,
                couponSettings,
                shop,
                couponProducts
            )
        }

        val imageUrl = generateImageDeferred.await()
        val squareImageUrl = generateSquareImageDeferred.await()
        val portraitImageUrl = generatePortraitImage.await()
        val voucher = initiateVoucherDeferred.await()

        val updateCouponDeferred = scope.async {
            updateCoupon(
                couponId,
                couponInformation,
                couponSettings,
                couponProducts,
                voucher.token,
                imageUrl,
                squareImageUrl,
                portraitImageUrl
            )
        }

        return updateCouponDeferred.await()
    }


    private suspend fun updateCoupon(
        couponId : Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl: String,
        imageSquare:String,
        imagePortrait:String
    ): Boolean {
        val params = updateCouponUseCase.createRequestParam(
            couponId,
            couponInformation,
            couponSettings,
            couponProducts,
            token,
            imageUrl,
            imageSquare,
            imagePortrait
        )
        updateCouponUseCase.params = params

        return updateCouponUseCase.executeOnBackground()

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
        initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
        initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdateMode)
        return initiateVoucherUseCase.executeOnBackground()
    }
}