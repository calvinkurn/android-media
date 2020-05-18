package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.create.domain.model.InitiateVoucherResponse
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.InitiateVoucherUiModel
import javax.inject.Inject

class InitiateVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<InitiateVoucherUiModel>() {

    companion object {
        const val QUERY = "query InitiateVoucher{\n" +
                "\tgetInitiateVoucherPage{\n" +
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
    }

    override suspend fun executeOnBackground(): InitiateVoucherUiModel {
        val request = GraphqlRequest(QUERY, InitiateVoucherResponse::class.java, RequestParams.EMPTY.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(InitiateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<InitiateVoucherResponse>()
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