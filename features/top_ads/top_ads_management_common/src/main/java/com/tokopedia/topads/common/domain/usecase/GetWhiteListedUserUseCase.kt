package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.domain.query.GetTopAdsGetShopWhitelistedFeature
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetWhiteListedUserUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                    val userSession: UserSessionInterface
)
    : GraphqlUseCase<WhiteListUserResponse>(graphqlRepository) {

    fun executeQuerySafeMode(onSuccess: (WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(WhiteListUserResponse::class.java)
        setGraphqlQuery(GetTopAdsGetShopWhitelistedFeature)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        execute({
            onSuccess(it.topAdsGetShopWhitelistedFeature)

        }, onError)
    }

    fun setParams() {
        val params = mutableMapOf(
            ParamObject.SHOP_ID to userSession.shopId,
        )
        setRequestParams(params)
    }
}
