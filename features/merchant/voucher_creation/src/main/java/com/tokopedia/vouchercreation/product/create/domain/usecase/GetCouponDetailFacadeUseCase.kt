package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCouponDetailFacadeUseCase @Inject constructor(
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase
) {

    suspend fun execute(couponId: Long): CouponDetailWithMetadata {
        return coroutineScope {
            val couponDetailDeferred = async { getCouponDetail(couponId) }
            val initiateCouponDeferred = async { initiateCoupon(isUpdateMode = false) }

            val couponDetail = couponDetailDeferred.await()
            val metadata = initiateCouponDeferred.await()

            return@coroutineScope CouponDetailWithMetadata(couponDetail, metadata.maxProducts)
        }
    }


    private suspend fun getCouponDetail(couponId: Long): CouponUiModel {
        getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
        return getCouponDetailUseCase.executeOnBackground()
    }

    private suspend fun initiateCoupon(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(isUpdateMode, false)
        return initiateCouponUseCase.executeOnBackground()
    }

}