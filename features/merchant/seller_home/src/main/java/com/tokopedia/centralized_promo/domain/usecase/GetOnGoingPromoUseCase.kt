package com.tokopedia.centralized_promo.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralized_promo.domain.mapper.PromotionMapper
import com.tokopedia.centralized_promo.domain.model.GetPromotionListResponseWrapper
import com.tokopedia.centralized_promo.view.model.OnGoingPromoListUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerhome.domain.usecase.BaseGqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetOnGoingPromoUseCase @Inject constructor(
        private val gqlRepository: GraphqlRepository,
        private val promotionMapper: PromotionMapper
) : BaseGqlUseCase<OnGoingPromoListUiModel>() {

    override suspend fun executeOnBackground(): OnGoingPromoListUiModel {
        val gqlRequest = GraphqlRequest(QUERY, GetPromotionListResponseWrapper::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetPromotionListResponseWrapper::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPromotionListResponseWrapper>()
            return promotionMapper.mapRemoteModelToUiModel(data.response)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val SHOW_EMPTY_COUNT = "showEmpty"

        fun getRequestParams(showEmpty: Boolean): RequestParams {
            return RequestParams.create().apply {
                putBoolean(SHOW_EMPTY_COUNT, showEmpty)
            }
        }

        const val QUERY = "query (\$showEmpty: Boolean!) {\n" +
                "    MerchantPromotionGetPromotionList(ShowEmptyCount: \$showEmpty) {\n" +
                "        header{\n" +
                "            process_time\n" +
                "            message\n" +
                "            reason\n" +
                "            error_code\n" +
                "        }\n" +
                "        data{\n" +
                "            title\n" +
                "            list{\n" +
                "                title\n" +
                "                status{\n" +
                "                    text\n" +
                "                    count\n" +
                "                    url\n" +
                "                    mobile_url\n" +
                "                }\n" +
                "                footer{\n" +
                "                    text\n" +
                "                    url\n" +
                "                    mobile_url\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}"
    }
}