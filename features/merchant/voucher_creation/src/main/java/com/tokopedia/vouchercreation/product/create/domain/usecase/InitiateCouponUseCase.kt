package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
import com.tokopedia.vouchercreation.common.consts.InitiateAction
import com.tokopedia.vouchercreation.shop.create.domain.model.InitiateVoucherResponse
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import javax.inject.Inject

class InitiateCouponUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<InitiateVoucherUiModel>() {

    companion object {

        // keys
        private const val ACTION_KEY = "action"
        private const val TARGET_BUYER_KEY = "targetBuyer"
        private const val COUPON_TYPE_KEY = "couponType"
        private const val IS_VOUCHER_PRODUCT_KEY = "isVoucherProduct"

        // values
        private const val ELIGIBLE_VALUE = 1
        private const val ALL_USER = 0
        private const val COUPON_TYPE_CASHBACK = "cashback"
        private const val NON_VOUCHER_PRODUCT = 0
        private const val VOUCHER_PRODUCT = 1


        @JvmStatic
        fun createRequestParam(isUpdate: Boolean, isToCreateNewCoupon: Boolean): RequestParams = RequestParams.create().apply {
            val action =
                    if (isUpdate) {
                        InitiateAction.UPDATE
                    } else {
                        InitiateAction.CREATE
                    }
            putString(ACTION_KEY, action)
            putInt(TARGET_BUYER_KEY, ALL_USER)
            putString(COUPON_TYPE_KEY, COUPON_TYPE_CASHBACK)
            val couponProduct = if (isToCreateNewCoupon) VOUCHER_PRODUCT else NON_VOUCHER_PRODUCT
            putInt(IS_VOUCHER_PRODUCT_KEY, couponProduct)
        }
    }

    var query: String = INITIATE_COUPON_PRODUCT_QUERY

    override suspend fun executeOnBackground(): InitiateVoucherUiModel {
        val request = GraphqlRequest(INITIATE_COUPON_PRODUCT_QUERY, InitiateVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(InitiateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<InitiateVoucherResponse>()
            with(data.initiateVoucherPage.header) {
                if (!errorCode.isNullOrEmpty()) {
                    throw MessageErrorException(messages.first())
                }
            }
            return data.mapToUiModel()
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }

    private fun InitiateVoucherResponse.mapToUiModel(): InitiateVoucherUiModel {
        val data = initiateVoucherPage.initiateVoucherPageData
        with(data) {
            return InitiateVoucherUiModel(
                token = token,
                accessToken = accessToken,
                uploadAppUrl = uploadAppUrl,
                bannerBaseUrl = bannerBaseUrl,
                bannerIgPostUrl = bannerIgPostUrl,
                bannerIgStoryUrl = bannerIgStoryUrl,
                bannerFreeShippingLabelUrl = bannerFreeShippingLabelUrl,
                bannerCashbackLabelUrl = bannerCashbackLabelUrl,
                bannerCashbackUntilLabelUrl = bannerCashbackUntilLabelUrl,
                voucherCodePrefix = voucherCodePrefix,
                isCreateVoucherEligible = isEligible == ELIGIBLE_VALUE,
                maxProducts = maxProduct
            )
        }
    }
}