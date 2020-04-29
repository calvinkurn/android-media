package com.tokopedia.officialstore.official.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_component.data.DynamicHomeChannelCommon
import com.tokopedia.home_component.query.DynamicChannelQueryCommon
import com.tokopedia.officialstore.GQLQueryConstant
import com.tokopedia.officialstore.official.data.model.dynamic_channel.DynamicChannel
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class GetOfficialStoreDynamicChannelUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<DynamicHomeChannelCommon>() {
    private val paramChannelType = "type"

    var requestParams: MutableMap<String, Any> = mutableMapOf()
        private set

    override suspend fun executeOnBackground(): DynamicHomeChannelCommon {
        val responseType: Type = DynamicChannel.Response::class.java
        val requestInstance = GraphqlRequest(DynamicChannelQueryCommon.getQuery(), responseType, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(requestInstance)

        return graphqlUseCase.executeOnBackground().run {
            getData<DynamicHomeChannelCommon>(responseType)
        }
    }

    suspend operator fun invoke(): DynamicHomeChannelCommon = executeOnBackground()

    fun setupParams(channelType: String) {
        if (channelType.isNotEmpty()) {
            requestParams[paramChannelType] = channelType
        }
    }
}
