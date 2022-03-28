package com.tokopedia.vouchercreation.product.create.domain.usecase.create

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CreateCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val imageBuilder: GenerateImageParamsBuilder,
    private val getMostSoldProductsUseCase: GetMostSoldProductsUseCase,
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
        private const val IS_UPDATE_MODE = false
        private const val IS_COUPON_PRODUCT = true
        private const val EMPTY_STRING = ""
    }

    suspend fun execute(
        isCreateMode: Boolean,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        allProducts: List<CouponProduct>,
        parentProductId: List<Long>
    ): Int {
        return coroutineScope {
            val initiateCouponDeferred = async { initiateCoupon(IS_UPDATE_MODE) }
            val shopDeferred = async { getShopBasicDataUseCase.executeOnBackground() }
            val topProductsDeferred = async { getMostSoldProducts(parentProductId) }

            val shop = shopDeferred.await()
            val coupon = initiateCouponDeferred.await()
            val topProducts = topProductsDeferred.await()

            val topProductImageUrls = topProducts.data.map { getImageUrlOrEmpty(it.pictures) }

            val generateImageDeferred = async {
                generateImage(
                    ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                    isCreateMode,
                    coupon.voucherCodePrefix,
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
                    isCreateMode,
                    coupon.voucherCodePrefix,
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
                    isCreateMode,
                    coupon.voucherCodePrefix,
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

            val createCouponDeferred = async {
                createCoupon(
                    couponInformation,
                    couponSettings,
                    allProducts,
                    coupon.token,
                    imageUrl,
                    squareImageUrl,
                    portraitImageUrl
                )
            }

            return@coroutineScope createCouponDeferred.await()
        }

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
        isCreateMode: Boolean,
        couponCodePrefix: String,
        imageRatio: ImageRatio,
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

        val couponCode = if (isCreateMode && couponInformation.target == CouponInformation.Target.PRIVATE) {
            couponCodePrefix + imageParams.voucherCode.uppercase()
        } else {
            couponInformation.code.uppercase()
        }

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
            GenerateImageParams(ImageGeneratorConstant.COUPON_PRODUCT_VOUCHER_CODE, couponCode),
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

        val imageGeneratorUseCase = GenerateImageUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val params = GenerateImageUseCase.createParam(sourceId, modifiedParams)
        imageGeneratorUseCase.params = params
        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateCoupon(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params =
            InitiateCouponUseCase.createRequestParam(isUpdateMode, IS_COUPON_PRODUCT)
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