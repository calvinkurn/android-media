package com.tokopedia.vouchercreation.product.create.domain.usecase.update

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetMostSoldProductsUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.util.GenerateImageParamsBuilder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class UpdateCouponFacadeUseCase @Inject constructor(
    private val updateCouponUseCase: UpdateCouponUseCase,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder,
    private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val SECOND_IMAGE_URL = 1
        private const val THIRD_IMAGE_URL = 2
        private const val IS_UPDATE_MODE = true
        private const val IS_TO_CREATE_NEW_COUPON = false
        private const val EMPTY_STRING = ""
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponId: Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        allProducts: List<CouponProduct>,
        parentProductId: List<Long>
    ): Boolean {
        val initiateVoucherDeferred = scope.async { initiateCoupon(IS_UPDATE_MODE) }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val topProductsDeferred = scope.async { getMostSoldProducts(parentProductId) }

        val shop = shopDeferred.await()
        val topProducts = topProductsDeferred.await()
        val topProductImageUrls = topProducts.data.map { getImageUrlOrEmpty(it.pictures) }

        val generateImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.HORIZONTAL,
                couponInformation,
                couponSettings,
                shop,
                topProductImageUrls
            )
        }

        val generateSquareImageDeferred = scope.async {
            generateImage(
                sourceId,
                ImageRatio.SQUARE,
                couponInformation,
                couponSettings,
                shop,
                topProductImageUrls
            )
        }

        val generatePortraitImage = scope.async {
            generateImage(
                sourceId,
                ImageRatio.VERTICAL,
                couponInformation,
                couponSettings,
                shop,
                topProductImageUrls
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
                allProducts,
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
        parentProductsImageUrls : List<String>
    ): String {
        val imageParams = imageBuilder.build(
            imageRatio,
            couponInformation,
            couponSettings,
            parentProductsImageUrls,
            shop.logo,
            shop.shopName
        )

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
            GenerateImageParams("audience_target", imageParams.audienceTarget)
        )


        if (parentProductsImageUrls.isNotEmpty()) {
            requestParams.add(GenerateImageParams("product_image_1", imageParams.productImage1),)
        }

        if (parentProductsImageUrls.size >= SECOND_IMAGE_URL) {
            requestParams.add(GenerateImageParams("product_image_2", imageParams.productImage2),)
        }

        if (parentProductsImageUrls.size >= THIRD_IMAGE_URL) {
            requestParams.add(GenerateImageParams("product_image_3", imageParams.productImage3))
        }

        val modifiedParams = arrayListOf<GenerateImageParams>()
        modifiedParams.addAll(requestParams)

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, requestParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateCoupon(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(isUpdateMode, IS_TO_CREATE_NEW_COUPON)
        return initiateCouponUseCase.executeOnBackground()
    }

    private suspend fun getMostSoldProducts(productIds: List<Long>): GetProductsByProductIdResponse.GetProductListData {
        getMostSoldProductsUseCase.params = GetMostSoldProductsUseCase.createParams(userSession.shopId, productIds)
        return getMostSoldProductsUseCase.executeOnBackground()
    }

    private fun getImageUrlOrEmpty(pictures : List<GetProductsByProductIdResponse.Picture>): String {
        if (pictures.isEmpty()) {
            return EMPTY_STRING
        }

        return pictures[0].urlThumbnail
    }
}