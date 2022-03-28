package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCouponDetailFacadeUseCase @Inject constructor(
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase,
    private val dispatcher: CoroutineDispatchers
) {

    suspend fun execute(couponId : Long) = withContext(dispatcher.io) {
        val detailDeferred = async { getCouponDetail(couponId) }
        val initiateVoucherDeferred = async { initiateVoucher(isUpdateMode = false) }

        val detail = detailDeferred.await()
        val metadata = initiateVoucherDeferred.await()

        return@withContext CouponDetailWithMetadata(detail, metadata.maxProducts)
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