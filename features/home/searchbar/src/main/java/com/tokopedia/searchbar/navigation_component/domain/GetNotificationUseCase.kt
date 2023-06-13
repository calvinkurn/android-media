package com.tokopedia.searchbar.navigation_component.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.searchbar.navigation_component.data.notification.NotificationResponse
import com.tokopedia.searchbar.navigation_component.data.notification.Param
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.mapper.NotificationRequestMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<NotificationResponse>,
        userSession: UserSessionInterface
) : UseCase<TopNavNotificationModel>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(QueryNotification.query)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(NotificationResponse::class.java)
        params.parameters[PARAM_INPUT] = Param(userSession.shopId)
    }

    override suspend fun executeOnBackground(): TopNavNotificationModel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val data = graphqlUseCase.executeOnBackground()
        return NotificationRequestMapper.mapToNotificationModel(data)
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}