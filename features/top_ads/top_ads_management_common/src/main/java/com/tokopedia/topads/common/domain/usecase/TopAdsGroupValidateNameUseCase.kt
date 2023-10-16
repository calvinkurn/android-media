package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.query.GetTopadsGroupValidateNameV2
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGroupValidateNameUseCase @Inject constructor(graphqlRepository: GraphqlRepository
                                                         , val userSession: UserSessionInterface)
    : GraphqlUseCase<ResponseGroupValidateName>(graphqlRepository) {

    init {
        setTypeClass(ResponseGroupValidateName::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsGroupValidateNameV2)

    }

    fun setParams(groupName: String, source: String) {
        val params = mutableMapOf(
            ParamObject.SHOP_ID to userSession.shopId,
            ParamObject.GROUP_NAME to groupName,
            ParamObject.SOURCE to source
        )
        setRequestParams(params)
    }
}
