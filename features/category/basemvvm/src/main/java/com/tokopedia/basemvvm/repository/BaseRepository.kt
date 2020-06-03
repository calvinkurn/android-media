package com.tokopedia.basemvvm.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type


open class BaseRepository {
    private val restRepository: RestRepository
    private val graphqlRepository: GraphqlRepository

    init {
        restRepository = RestRequestInteractor.getInstance().restRepository
        graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
    }

    suspend fun <T : Any> getRestData(url: String,
                                      typeOf: Type,
                                      requestType: RequestType,
                                      queryMap: MutableMap<String, Any> = RequestParams.EMPTY.parameters): T {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                    .setRequestType(requestType)
                    .setQueryParams(queryMap)
                    .build()
            return restRepository.getResponse(restRequest).getData()
        } catch (t: Throwable) {
            throw t
        }
    }

    suspend fun getGQLData(gqlQueryList: List<String>,
                           gqlResponseTypeList: List<Type>,
                           gqlParamList: List<Map<String, Any>>,
                           cacheType: CacheType = CacheType.CLOUD_THEN_CACHE): GraphqlResponse? {
        try {
            val gqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository)
            val gqlRequests = ArrayList<GraphqlRequest>()
            gqlQueryList.forEachIndexed { index, s ->
                gqlRequests.add(GraphqlRequest(s, gqlResponseTypeList[index], gqlParamList[index], false))
            }
            gqlUseCase.addRequests(gqlRequests)
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }

    }

    suspend fun <T : Any> getGQLData(gqlQuery: String,
                                     gqlResponseType: Class<T>,
                                     gqlParams: Map<String, Any>,
                                     cacheType: CacheType = CacheType.CLOUD_THEN_CACHE): T {
        try {
            val gqlUseCase = GraphqlUseCase<T>(graphqlRepository)
            gqlUseCase.setTypeClass(gqlResponseType)
            gqlUseCase.setGraphqlQuery(gqlQuery)
            gqlUseCase.setRequestParams(gqlParams)
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }

    suspend fun <T : Any> getGQLData(gqlQuery: String,
                                     gqlResponseType: Class<T>,
                                     gqlParams: Map<String, Any>, queryName: String): Any? {
        var jsonObject: JsonObject = getGQLData(gqlQuery, JsonObject::class.java, gqlParams)
        var jsonObject1 = jsonObject.get(queryName)
        return Gson().fromJson(jsonObject1, gqlResponseType)
    }

}
