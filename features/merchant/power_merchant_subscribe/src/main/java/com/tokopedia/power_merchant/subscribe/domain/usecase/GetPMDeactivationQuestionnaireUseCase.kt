package com.tokopedia.power_merchant.subscribe.domain.usecase

import com.tokopedia.gm.common.domain.interactor.BaseGqlUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.domain.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.domain.mapper.PMDeactivationQuestionnaireMapper
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/21
 */

@GqlQuery("GetPMDeactivationQuestionnaireGqlQuery", GetPMDeactivationQuestionnaireUseCase.QUERY)
class GetPMDeactivationQuestionnaireUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: PMDeactivationQuestionnaireMapper
) : BaseGqlUseCase<DeactivationQuestionnaireUiModel>() {

    override suspend fun executeOnBackground(): DeactivationQuestionnaireUiModel {
        val gqlRequest = GraphqlRequest(
            GetPMDeactivationQuestionnaireGqlQuery(),
            GoldCancellationsQuestionaire::class.java,
            params.parameters
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(GoldCancellationsQuestionaire::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<GoldCancellationsQuestionaire>(GoldCancellationsQuestionaire::class.java)
            if (data != null) {
                return mapper.mapRemoteModelToUiModel(data.result)
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        const val QUERY = """
            query goldCancellationsQuestionaire(${'$'}source: String, ${'$'}pm_tier: Int) {
              goldCancellationsQuestionaire(source: ${'$'}source, pm_tier:${'$'}pm_tier) {
                data {
                  question_list {
                    question_type
                    question
                    option {
                      value
                      image_url
                    }
                  }
                }
              }
            }
        """
        private const val KEY_SOURCE = "source"
        private const val KEY_PM_TIER = "pm_tier"

        fun createParams(source: String, pmTire: Int): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_SOURCE, source)
                putInt(KEY_PM_TIER, pmTire)
            }
        }

        fun getCacheStrategy(shouldFromCache: Boolean): GraphqlCacheStrategy {
            val cacheType = if (shouldFromCache) {
                CacheType.CACHE_FIRST
            } else {
                CacheType.ALWAYS_CLOUD
            }
            return GraphqlCacheStrategy.Builder(cacheType).build()
        }
    }
}
