package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.product.create.data.mapper.CouponDetailMapper
import com.tokopedia.vouchercreation.product.create.data.response.CouponDetailResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import javax.inject.Inject

class GetCouponDetailUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val couponDetailMapper: CouponDetailMapper
) : BaseGqlUseCase<CouponUiModel>() {

    companion object {
        private const val VOUCHER_ID_KEY = "voucher_id"
        private const val SOURCE_KEY = "source"

        @JvmStatic
        fun createRequestParam(voucherId: Int): RequestParams {
            return RequestParams.create().apply {
                putString(SOURCE_KEY, VoucherSource.SELLERAPP)
                putInt(VOUCHER_ID_KEY, voucherId)
            }
        }
    }

    override suspend fun executeOnBackground(): CouponUiModel {
        val request = GraphqlRequest(
            GqlQueryConstant.GET_COUPON_DETAIL_QUERY,
            CouponDetailResponse::class.java,
            params.parameters
        )
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(CouponDetailResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<CouponDetailResponse>()
            return couponDetailMapper.map(data.result.voucher)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}