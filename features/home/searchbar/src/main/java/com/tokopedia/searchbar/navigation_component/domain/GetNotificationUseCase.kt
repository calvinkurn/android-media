package com.tokopedia.searchbar.navigation_component.domain

import android.util.Log
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.searchbar.navigation_component.data.notification.NotificationResponse
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.domain.mapper.NotificationRequestMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<NotificationResponse>
) : UseCase<TopNavNotificationModel>(){
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(QueryNotification.query)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(NotificationResponse::class.java)
    }

    override suspend fun executeOnBackground(): TopNavNotificationModel {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params.parameters)
        val data = graphqlUseCase.executeOnBackground()
        return NotificationRequestMapper.mapToNotificationModel(data)
    }
}