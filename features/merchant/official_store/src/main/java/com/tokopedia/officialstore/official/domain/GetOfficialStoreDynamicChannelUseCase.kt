package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.officialstore.GQLQueryConstant
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetOfficialStoreDynamicChannelUseCase @Inject constructor(
        private val graphlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL) val query: String
): UseCase<DynamicChannel>() {
    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): DynamicChannel {
        val gqlRequest = GraphqlRequest(query, DynamicChannel.Response::class.java, params)

        graphlUseCase.clearRequest()
        graphlUseCase.addRequest(gqlRequest)

        return graphlUseCase.executeOnBackground().run {
            this.getData<DynamicChannel.Response>(DynamicChannel.Response::class.java).dynamicHomeChannel
        }
    }

    companion object {
        private const val CHANNEL_TYPE = "type"

        fun setupParams(channelType: String): MutableMap<String, String> {
            val params = mutableMapOf<String, String>()

            params[CHANNEL_TYPE] = channelType

            return params
        }
    }
}
