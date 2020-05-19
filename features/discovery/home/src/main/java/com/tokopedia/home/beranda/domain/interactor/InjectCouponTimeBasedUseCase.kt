package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Ade Fulki on 11/05/20.
 */

class InjectCouponTimeBasedUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SetInjectCouponTimeBased>
): UseCase<SetInjectCouponTimeBased>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SetInjectCouponTimeBased::class.java)
    }

    private fun getQuery(): String = """
        query{
            SetInjectCouponTimeBased {
                is_success
                error_message
            }
        }
    """.trimIndent()

    override suspend fun executeOnBackground(): SetInjectCouponTimeBased {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(getQuery())
        return graphqlUseCase.executeOnBackground()
    }
}