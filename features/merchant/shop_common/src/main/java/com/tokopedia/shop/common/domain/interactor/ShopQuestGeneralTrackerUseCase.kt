package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ShopQuestGeneralTrackerUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopQuestGeneralTracker>(gqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopQuestGeneralTracker {
        val request = GraphqlRequest(MUTATION, ShopQuestGeneralTracker::class.java, params.parameters)
        val gqlResponse = gqlRepository.getReseponse(listOf(request))
        return gqlResponse.getData<ShopQuestGeneralTracker>(
                ShopQuestGeneralTracker::class.java
        )
    }

    companion object {

        private const val KEY_ACTION_NAME = "action_name"
        private const val KEY_SOURCE = "source"
        private const val KEY_CHANNEL = "channel"
        private const val KEY_LANGUAGE = "lang"
        private const val KEY_INPUT = "input"

        fun createRequestParams(
            actionName: String,
            source: String,
            channel: String,
            lang: String = "",
            input: ShopQuestGeneralTrackerInput
        ) : RequestParams = RequestParams.create().apply {
            putString(KEY_ACTION_NAME, actionName)
            putString(KEY_SOURCE, source)
            putString(KEY_CHANNEL, channel)
            putString(KEY_LANGUAGE, lang)
            putObject(KEY_INPUT, input)
        }

        /**
         * Mutation to track shop general tracker
         * See docs : https://tokopedia.atlassian.net/wiki/spaces/OS/pages/1182305693/GQL+General+Tracker+Contract
         */
        private const val MUTATION = "mutation shopQuestGeneralTracker(\n" +
                "  \$action_name: String!,\n" +
                "  \$source: String!,\n" +
                "  \$channel: String!,\n" +
                "  \$lang: String,\n" +
                "  \$input: SQTrackerParam!\n" +
                ") {\n" +
                "  shopQuestGeneralTracker(\n" +
                "    action_name: \$action_name,\n" +
                "    source: \$source,\n" +
                "    channel: \$channel,\n" +
                "    lang: \$lang,\n" +
                "    input: \$input\n" +
                "  ) {\n" +
                "    message\n" +
                "    is_success\n" +
                "  }\n" +
                "}"
    }

}