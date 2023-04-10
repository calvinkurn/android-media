package com.tokopedia.vouchercreation.product.create.domain.usecase.update

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.product.create.data.request.GenerateImageParams
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.GenerateImageUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetMostSoldProductsUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.util.GenerateImageParamsBuilder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
        private const val IS_UPDATE_MODE = true
        private const val IS_TO_CREATE_NEW_COUPON = false
        private const val EMPTY_STRING = ""
    }

    suspend fun execute(
        couponId: Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        allProducts: List<CouponProduct>,
        parentProductId: List<Long>
    ): Boolean {
        return coroutineScope {
            val initiateVoucherDeferred = async { initiateCoupon(IS_UPDATE_MODE) }
            val shopDeferred = async { getShopBasicDataUseCase.executeOnBackground() }
            val topProductsDeferred = async { getMostSoldProducts(parentProductId) }

            val shop = shopDeferred.await()
            val topProducts = topProductsDeferred.await()
            val topProductImageUrls = topProducts.data.map { getImageUrlOrEmpty(it.pictures) }
            val warehouseId = topProducts.data.firstOrNull()?.warehouses?.firstOrNull()?.id.orEmpty()

            val generateImageDeferred = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    ImageRatio.HORIZONTAL,
                    couponInformation,
                    couponSettings,
                    shop,
                    topProductImageUrls
                )
            }

            val generateSquareImageDeferred = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    ImageRatio.SQUARE,
                    couponInformation,
                    couponSettings,
                    shop,
                    topProductImageUrls
                )
            }

            val generatePortraitImage = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
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

            val updateCouponDeferred = async {
                val useCaseParam = UpdateCouponUseCaseParam(
                    couponId,
                    couponInformation,
                    couponSettings,
                    allProducts,
                    voucher.token,
                    imageUrl,
                    squareImageUrl,
                    portraitImageUrl,
                    warehouseId
                )
                updateCoupon(useCaseParam)
            }

            return@coroutineScope updateCouponDeferred.await()
        }
    }


    private suspend fun updateCoupon(
        useCaseParam: UpdateCouponUseCaseParam
    ): Boolean {
        val params = updateCouponUseCase.createRequestParam(useCaseParam)
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
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_PLATFORM, imageParams.platform),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_IS_PUBLIC, imageParams.isPublic),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_BENEFIT_TYPE, imageParams.voucherBenefitType),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CASHBACK_TYPE, imageParams.voucherCashbackType),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CASHBACK_PERCENTAGE, imageParams.voucherCashbackPercentage),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_NOMINAL_AMOUNT, imageParams.voucherNominalAmount),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_NOMINAL_SYMBOL, imageParams.voucherNominalSymbol),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SHOP_LOGO, imageParams.shopLogo),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SHOP_NAME, imageParams.shopName),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CODE, imageParams.voucherCode),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_START_TIME, imageParams.voucherStartTime),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_FINISH_TIME, imageParams.voucherFinishTime),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_PRODUCT_COUNT, imageParams.productCount),
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_AUDIENCE_TARGET, imageParams.audienceTarget)
        )


        if (parentProductsImageUrls.isNotEmpty()) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_FIRST_PRODUCT_IMAGE, imageParams.productImage1))
        }

        if (parentProductsImageUrls.size >= SECOND_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_SECOND_PRODUCT_IMAGE, imageParams.productImage2))
        }

        if (parentProductsImageUrls.size >= THIRD_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_THIRD_PRODUCT_IMAGE, imageParams.productImage3))
        }

        val modifiedParams = arrayListOf<GenerateImageParams>()
        modifiedParams.addAll(requestParams)

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, requestParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground().imageUrl
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
