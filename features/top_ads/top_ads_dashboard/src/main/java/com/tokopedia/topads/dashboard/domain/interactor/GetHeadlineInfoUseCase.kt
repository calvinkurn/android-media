package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.HeadlineInfoResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetHeadlineInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<HeadlineInfoResponse>(graphqlRepository) {

    fun setParams(groupId: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.GROUP_ID] = groupId
        setRequestParams(queryMap)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
        .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (HeadlineInfoResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(HeadlineInfoResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}