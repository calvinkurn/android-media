package com.tokopedia.vouchercreation.product.create.domain.usecase.create

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

class CreateCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder
) {

    companion object {
        private const val IS_UPDATE_MODE = false
        private const val IS_COUPON_PRODUCT = true
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
    ): Int {
        val initiateCouponDeferred = scope.async { initiateCoupon(IS_UPDATE_MODE) }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }

        val shop = shopDeferred.await()
        val coupon = initiateCouponDeferred.await()

        val generateImageDeferred = scope.async {
            generateImage(
                sourceId,
                coupon.voucherCodePrefix,
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
                coupon.voucherCodePrefix,
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
                coupon.voucherCodePrefix,
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

        val createCouponDeferred = scope.async {
            createCoupon(
                couponInformation,
                couponSettings,
                couponProducts,
                coupon.token,
                imageUrl,
                squareImageUrl,
                portraitImageUrl
            )
        }

        return createCouponDeferred.await()
    }

    private suspend fun createCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl: String,
        imageSquare:String,
        imagePortrait:String
    ): Int {
        val params = createCouponProductUseCase.createRequestParam(
            couponInformation,
            couponSettings,
            couponProducts,
            token,
            imageUrl,
            imageSquare,
            imagePortrait
        )
        createCouponProductUseCase.params = params
        return createCouponProductUseCase.executeOnBackground()
    }

    private suspend fun generateImage(
        sourceId: String,
        couponCodePrefix: String,
        imageRatio: ImageRatio,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        shop: ShopBasicDataResult,
        products : List<CouponProduct>
    ): String {
        val imageParams = imageBuilder.build(imageRatio, couponInformation, couponSettings, products, shop.logo, shop.shopName)

        val couponCode = if (couponInformation.target == CouponInformation.Target.PRIVATE) {
            couponCodePrefix + imageParams.voucherCode.uppercase()
        } else {
            couponInformation.code.uppercase()
        }

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
            GenerateImageParams("voucher_code", couponCode),
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

    private suspend fun initiateCoupon(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params =
            InitiateCouponUseCase.createRequestParam(isUpdateMode, IS_COUPON_PRODUCT)
        return initiateCouponUseCase.executeOnBackground()
    }
}