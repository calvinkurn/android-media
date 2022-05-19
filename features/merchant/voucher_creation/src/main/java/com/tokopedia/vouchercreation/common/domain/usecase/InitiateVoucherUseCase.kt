package com.tokopedia.vouchercreation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.consts.InitiateAction
import com.tokopedia.vouchercreation.shop.create.domain.model.InitiateVoucherResponse
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import javax.inject.Inject

class InitiateVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<InitiateVoucherUiModel>() {

    companion object {
        const val QUERY = "query InitiateVoucher(\$action: String, \$targetBuyer: Int, \$couponType: String){\n" +
                "\tgetInitiateVoucherPage(Action: \$action, TargetBuyer: \$targetBuyer ,CouponType: \$couponType){\n" +
                "\t\theader{\n" +
                "          process_time\n" +
                "          messages\n" +
                "          reason\n" +
                "          error_code\n" +
                "        }\n" +
                "        data{\n" +
                "          shop_id\n" +
                "          token\n" +
                "          user_id\n" +
                "          access_token\n" +
                "          upload_app_url\n" +
                "          img_banner_base\n" +
                "          img_banner_ig_post\n" +
                "          img_banner_ig_story\n" +
                "          img_banner_label_gratis_ongkir\n" +
                "          img_banner_label_cashback\n" +
                "          img_banner_label_cashback_hingga\n" +
                "          prefix_voucher_code\n" +
                "          is_eligible\n" +
                "          max_product\n" +
                "        }\n" +
                "    }\n" +
                "}"

        // keys
        private const val ACTION_KEY = "action"
        private const val TARGET_BUYER_KEY = "targetBuyer"
        private const val COUPON_TYPE_KEY = "couponType"

        // values
        private const val ELIGIBLE_VALUE = 1
        private const val ALL_USER = 0
        private const val COUPON_TYPE_CASHBACK = "cashback"

        @JvmStatic
        fun createRequestParam(isUpdate: Boolean): RequestParams = RequestParams.create().apply {
            val action =
                    if (isUpdate) {
                        InitiateAction.UPDATE
                    } else {
                        InitiateAction.CREATE
                    }
            putString(ACTION_KEY, action)
            putInt(TARGET_BUYER_KEY, ALL_USER)
            putString(COUPON_TYPE_KEY, COUPON_TYPE_CASHBACK)
        }
    }

    var query: String = QUERY

    override suspend fun executeOnBackground(): InitiateVoucherUiModel {
        val request = GraphqlRequest(QUERY, InitiateVoucherResponse::class.java, params.parameters)
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