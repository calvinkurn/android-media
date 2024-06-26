package com.tokopedia.seller.menu.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.seller.menu.data.model.SellerMenuNotificationResponse
import com.tokopedia.seller.menu.domain.query.SellerMenuNotification
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetSellerNotificationUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    userSession: UserSessionInterface
): GraphqlUseCase<SellerMenuNotificationResponse>(graphqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)
        setRequestParams(SellerMenuNotification.getNotificationParam(userSession.shopId))
        setGraphqlQuery(SellerMenuNotification.QUERY)
        setTypeClass(SellerMenuNotificationResponse::class.java)
    }
}