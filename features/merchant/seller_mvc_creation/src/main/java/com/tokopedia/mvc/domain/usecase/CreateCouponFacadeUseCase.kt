package com.tokopedia.mvc.domain.usecase

import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CreateCouponFacadeUseCase @Inject constructor(
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateCouponUseCase: GetInitiateVoucherPageUseCase
) {
    suspend fun execute(
        configuration: VoucherConfiguration,
        allProducts: List<SelectedProduct>,
        warehouseId: String
    ): Int {
        return coroutineScope {
            val initiateCouponDeferred = async { initiateCoupon(true, configuration.promoType, configuration.isVoucherProduct) }
            val coupon = initiateCouponDeferred.await()
            val imageUrl = ""
            val squareImageUrl = ""
            val portraitImageUrl = ""

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
