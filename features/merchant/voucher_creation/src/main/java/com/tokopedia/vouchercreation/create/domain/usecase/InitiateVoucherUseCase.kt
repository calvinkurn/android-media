package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.consts.InitiateAction
import com.tokopedia.vouchercreation.create.domain.model.InitiateVoucherResponse
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.InitiateVoucherUiModel
import javax.inject.Inject

class InitiateVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<InitiateVoucherUiModel>() {

    companion object {
        const val QUERY = "query InitiateVoucher(\$action: String){\n" +
                "\tgetInitiateVoucherPage(Action: \$action){\n" +
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
                "        }\n" +
                "    }\n" +
                "}"

        private const val ACTION_KEY = "action"

        @JvmStatic
        fun createRequestParam(isUpdate: Boolean): RequestParams = RequestParams.create().apply {
            val action =
                    if (isUpdate) {
                        InitiateAction.UPDATE
                    } else {
                        InitiateAction.CREATE
                    }
            putString(ACTION_KEY, action)
        }
    }

    override suspend fun executeOnBackground(): InitiateVoucherUiModel {
        val request = GraphqlRequest(QUERY, InitiateVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

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
            return InitiateVoucherUiModel(token, accessToken, uploadAppUrl, bannerBaseUrl, bannerIgPostUrl, bannerIgStoryUrl, bannerFreeShippingLabelUrl, bannerCashbackLabelUrl, bannerCashbackUntilLabelUrl, voucherCodePrefix)
        }
    }
}