package com.tokopedia.travelhomepage.homepage.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travelhomepage.homepage.data.TravelUnifiedSubhomepageData
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author by jessica on 08/09/20
 */

class GetSubhomepageUnifiedDataUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {
    suspend fun execute(rawQuery: String, param: Map<String, Any>, isFromCloud: Boolean, type: Type):
            List<TravelUnifiedSubhomepageData> {

        val graphQlCacheStrategy = if (isFromCloud) GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        else GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        useCase.setCacheStrategy(graphQlCacheStrategy)
        useCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, type, param)
            useCase.addRequest(graphqlRequest)
            val graphqlResponse = useCase.executeOnBackground()
            val errors = graphqlResponse.getError(type)

            if (errors != null && errors.isNotEmpty() && errors[0].extensions != null) {
                throw Throwable()
            } else {
                val unifiedData =
                        graphqlResponse.getData<TravelUnifiedSubhomepageData.Response>(type)
                unifiedData.response
            }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}