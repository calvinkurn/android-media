package com.tokopedia.vouchercreation.product.create.domain.usecase.create

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class CreateCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase
) {

    companion object {
        private const val IS_UPDATE_MODE = false
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
    ): Int {
        //val uploadImageDeferred = scope.async { uploadImage(sourceId, couponProducts) }
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE) }

       // val imageUrl = uploadImageDeferred.await()
        val voucher = initiateVoucherDeferred.await()

        val createCouponDeferred = scope.async {
            createCoupon(
                couponInformation,
                couponSettings,
                couponProducts,
                voucher.token,
                "https://images.tokopedia.net/img/VqbcmM/2021/8/19/78c9335e-e949-4549-9b31-e637eae9e1e0.png"
            )
        }

        val createdCouponId = createCouponDeferred.await()

        return createdCouponId
    }

    private suspend fun createCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl : String
    ): Int {

        val params = createCouponProductUseCase.createRequestParam(
            couponInformation,
            couponSettings,
            couponProducts,
            token,
            imageUrl
        )
        createCouponProductUseCase.params = params
        return createCouponProductUseCase.executeOnBackground()

    }

    private suspend fun uploadImage(sourceId: String, products: List<CouponProduct>): String {
        val product = products[0]

        val imageParams = arrayListOf(
            ImageGeneratorRequestData(
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL,
                product.imageUrl
            ),
            ImageGeneratorRequestData(
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE,
                product.price.toString()
            ),
            ImageGeneratorRequestData(
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_RATING,
                product.rating.toString()
            )
        )

        val imageGeneratorUseCase = ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val param = ImageGeneratorUseCase.createParam(sourceId, imageParams)
        imageGeneratorUseCase.params = param

        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateVoucher(isUpdateMode : Boolean): InitiateVoucherUiModel {
        initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
        initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdateMode)
        return initiateVoucherUseCase.executeOnBackground()
    }
}