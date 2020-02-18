package com.tokopedia.stickylogin.domain.usecase.coroutine

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.query.StickyLoginQuery
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StickyLoginUseCase @Inject constructor(
        val graphqlRepository: GraphqlRepository
)  {
    fun execute(page: String): Flow<StickyLoginTickerPojo.TickerResponse> = flow{
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val params = HashMap<String, Any>()
        params[StickyLoginConstant.PARAMS_PAGE] = page
        val gqlRecommendationRequest = GraphqlRequest(
                StickyLoginQuery.query,
                StickyLoginTickerPojo.TickerResponse::class.java,
                params
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(StickyLoginTickerPojo.TickerResponse::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        emit(response.getSuccessData())
    }
}