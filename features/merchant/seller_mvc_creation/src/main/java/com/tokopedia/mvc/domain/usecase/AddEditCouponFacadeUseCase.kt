package com.tokopedia.mvc.domain.usecase

import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AddEditCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val updateCouponUseCase: UpdateCouponUseCase,
    private val initiateCouponUseCase: GetInitiateVoucherPageUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase
) {
    companion object {
        private const val FIRST_IMAGE_URL_INDEX = 0
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
    }

    suspend fun executeAdd(
        configuration: VoucherConfiguration,
        allProducts: List<SelectedProduct>,
        warehouseId: String
    ): Int {
        return coroutineScope {
            val imageRatios = listOf(ImageRatio.HORIZONTAL, ImageRatio.SQUARE, ImageRatio.VERTICAL)
            val initiateCouponDeferred = async { initiateCoupon(true, configuration.promoType, configuration.isVoucherProduct) }
            val generatedImagesDeferred = imageRatios.map {
                async {
                    getCouponImagePreviewUseCase.executeGetImageUrl(
                        isCreateMode = true,
                        voucherConfiguration = configuration,
                        parentProductId = allProducts.map { it.parentProductId },
                        imageRatio = it
                    )
                }
            }

            val coupon = initiateCouponDeferred.await()
            val imageUrl = generatedImagesDeferred.getOrNull(FIRST_IMAGE_URL_INDEX)?.await().orEmpty()
            val squareImageUrl = generatedImagesDeferred.getOrNull(SECOND_IMAGE_URL_INDEX)?.await().orEmpty()
            val portraitImageUrl = generatedImagesDeferred.getOrNull(THIRD_IMAGE_URL_INDEX)?.await().orEmpty()

            val createCouponDeferred = async {
                val useCaseParam = CreateCouponProductUseCase.CreateCouponUseCaseParam(
                    configuration,
                    allProducts,
                    coupon.token,
                    imageUrl,
                    squareImageUrl,
                    portraitImageUrl,
                    warehouseId
                )
                createCouponProductUseCase.executeCreation(useCaseParam)
            }
            return@coroutineScope createCouponDeferred.await()
        }
    }

    suspend fun executeEdit(
        configuration: VoucherConfiguration,
        allProducts: List<SelectedProduct>
    ): Boolean {
        return coroutineScope {
            val imageRatios = listOf(ImageRatio.HORIZONTAL, ImageRatio.SQUARE, ImageRatio.VERTICAL)
            val initiateCouponDeferred = async { initiateCoupon(true, configuration.promoType, configuration.isVoucherProduct) }
            val generatedImagesDeferred = imageRatios.map {
                async {
                    getCouponImagePreviewUseCase.executeGetImageUrl(
                        isCreateMode = false,
                        voucherConfiguration = configuration,
                        parentProductId = allProducts.map { it.parentProductId },
                        imageRatio = it
                    )
                }
            }

            val coupon = initiateCouponDeferred.await()
            val imageUrl = generatedImagesDeferred.getOrNull(FIRST_IMAGE_URL_INDEX)?.await().orEmpty()
            val squareImageUrl = generatedImagesDeferred.getOrNull(SECOND_IMAGE_URL_INDEX)?.await().orEmpty()
            val portraitImageUrl = generatedImagesDeferred.getOrNull(THIRD_IMAGE_URL_INDEX)?.await().orEmpty()

            val createCouponDeferred = async {
                val useCaseParam = UpdateCouponUseCase.UpdateCouponUseCaseParam(
                    couponId = configuration.voucherId,
                    voucherConfiguration = configuration,
                    couponProducts = allProducts,
                    token = coupon.token,
                    imageUrl = imageUrl,
                    imageSquare = squareImageUrl,
                    imagePortrait = portraitImageUrl,
                    warehouseId = configuration.warehouseId.toString()
                )
                updateCouponUseCase.executeUpdate(useCaseParam)
            }
            return@coroutineScope createCouponDeferred.await()
        }
    }

    private suspend fun initiateCoupon(
        isCreateMode: Boolean,
        promoType: PromoType,
        isVoucherProduct: Boolean
    ): VoucherCreationMetadata {
        val param = GetInitiateVoucherPageUseCase.Param(
            action = if (isCreateMode) VoucherAction.CREATE else VoucherAction.UPDATE,
            promoType = promoType,
            isVoucherProduct = isVoucherProduct
        )
        return initiateCouponUseCase.execute(param)
    }
}
