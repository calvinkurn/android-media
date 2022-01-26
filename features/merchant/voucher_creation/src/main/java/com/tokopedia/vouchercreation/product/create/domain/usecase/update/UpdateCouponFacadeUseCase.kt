package com.tokopedia.vouchercreation.product.create.domain.usecase.update

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

class UpdateCouponFacadeUseCase @Inject constructor(
    private val updateCouponUseCase: UpdateCouponUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase
) {

    companion object {
        private const val IS_UPDATE_MODE = true
    }

    suspend fun execute(
        scope: CoroutineScope,
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
    ): Boolean {
        val uploadImageDeferred = scope.async { uploadImage(sourceId, couponProducts) }
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE) }

        val imageUrl = uploadImageDeferred.await()
        val voucher = initiateVoucherDeferred.await()

        val updateCouponDeferred = scope.async {
            updateCoupon(
                couponInformation,
                couponSettings,
                couponProducts,
                voucher.token,
                imageUrl
            )
        }

        val isUpdateSuccess = updateCouponDeferred.await()

        return isUpdateSuccess
    }


    private suspend fun updateCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String,
        imageUrl: String
    ): Boolean {

        val params = updateCouponUseCase.createRequestParam(
            couponInformation,
            couponSettings,
            couponProducts,
            token,
            imageUrl
        )
        updateCouponUseCase.params = params

        return updateCouponUseCase.executeOnBackground()

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

        val imageGeneratorUseCase =
            ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        val param = ImageGeneratorUseCase.createParam(sourceId, imageParams)
        imageGeneratorUseCase.params = param

        return imageGeneratorUseCase.executeOnBackground()
    }

    private suspend fun initiateVoucher(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
        initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdateMode)
        return initiateVoucherUseCase.executeOnBackground()
    }
}