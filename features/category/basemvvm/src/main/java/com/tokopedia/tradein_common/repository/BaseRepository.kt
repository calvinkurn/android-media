package com.tokopedia.tradein_common.repository

import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext


class BaseRepository private constructor() : CoroutineScope {
    private val restRepository: RestRepository
    private val graphqlRepository: GraphqlRepository
    private lateinit var userSession: UserSessionInterface

    init {
        restRepository = RestRequestInteractor.getInstance().restRepository
        graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
    }


    companion object {
        val repositoryInstance: BaseRepository
            get() {
                return BaseRepository()
            }
    }

    suspend fun getRestData(url: String, typeOf: Type,
                            requestType: RequestType,
                            queryMap: MutableMap<String, Any>): RestResponse? {
        try {
            val restRequest = RestRequest.Builder(url, typeOf)
                    .setRequestType(requestType)
                    .setQueryParams(queryMap)
                    .build()
            return restRepository.getResponse(restRequest)
        } catch (t: Throwable) {
            throw t
        }
    }

    suspend fun getGQLData(gqlQueryList: List<String>,
                           gqlResponseTypeList: List<Type>,
                           gqlParamList: List<Map<String, Object>>): GraphqlResponse? {
        try {
            val gqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository)
            val gqlRequests = ArrayList<GraphqlRequest>()
            gqlQueryList.forEachIndexed { index, s ->
                gqlRequests.add(GraphqlRequest(s, gqlResponseTypeList[index], gqlParamList[index], false))
            }
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }

    }

    suspend fun <T : Any> getGQLData(gqlQuery: String,
                                     gqlResponseType: Class<T>,
                                     gqlParams: Map<String, Any>): Any? {
        try {
            val gqlUseCase = GraphqlUseCase<T>(graphqlRepository)
            gqlUseCase.setTypeClass(gqlResponseType)
            gqlUseCase.setGraphqlQuery(gqlQuery)
            gqlUseCase.setRequestParams(gqlParams)
            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getUserLoginState(): UserSessionInterface {
        if (!(::userSession.isInitialized)) {
            userSession = NetworkClient.getsUserSession()
        }
        return userSession
    }
}
