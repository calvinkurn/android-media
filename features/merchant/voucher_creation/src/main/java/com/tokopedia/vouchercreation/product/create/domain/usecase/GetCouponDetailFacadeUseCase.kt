package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetCouponDetailFacadeUseCase @Inject constructor(
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase
) {

    companion object {
        private const val IS_UPDATE_MODE = false
    }

    suspend fun execute(scope: CoroutineScope, couponId : Long): CouponDetailWithMetadata {
        val detailDeferred = scope.async { getCouponDetail(couponId) }
        val initiateVoucherDeferred = scope.async { initiateVoucher(IS_UPDATE_MODE) }

        val detail = detailDeferred.await()
        val metadata = initiateVoucherDeferred.await()

        return CouponDetailWithMetadata(detail, metadata.maxProducts)
    }


    private suspend fun getCouponDetail(couponId: Long): CouponUiModel {
        getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
        return getCouponDetailUseCase.executeOnBackground()
    }

    private suspend fun initiateVoucher(isUpdateMode: Boolean): InitiateVoucherUiModel {
        initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
        initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(isUpdateMode, false)
        return initiateCouponUseCase.executeOnBackground()
    }

}