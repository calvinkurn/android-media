package com.tokopedia.core.drawer2.domain.interactor

import com.tkpd.library.utils.LocalCacheHandler
import com.tokopedia.core.drawer2.data.mapper.TopChatNotificationMapper
import com.tokopedia.core.drawer2.data.pojo.ChatNotification
import com.tokopedia.core.drawer2.data.viewmodel.ChatNotificationModel
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel
import com.tokopedia.core.drawer2.domain.GqlQueryConstant.GET_CHAT_NOTIFICATION_QUERY
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Action1
import javax.inject.Inject
import javax.inject.Named

class GetChatNotificationUseCase @Inject constructor(
        @Named(GET_CHAT_NOTIFICATION_QUERY)
        val query: String,
        val graphqlUseCase: GraphqlUseCase,
        val drawerCache: LocalCacheHandler
) : UseCase<ChatNotificationModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<ChatNotificationModel> {
        val graphqlRequest = GraphqlRequest(query, ChatNotification::class.java)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            val response: ChatNotification = it.getData(ChatNotification::class.java)
            ChatNotificationModel(
                    response.notifications.chat.unreads,
                    response.notifications.chat.unreadsSeller,
                    response.notifications.chat.unreadsUser
            )
        }.doOnNext(saveToCache())
    }

    private fun saveToCache(): Action1<ChatNotificationModel> {
        return Action1 {
            drawerCache.putInt(DrawerNotification.CACHE_INBOX_MESSAGE, it.notifUnreadsBuyer)
            drawerCache.putInt(
                    DrawerNotification.CACHE_TOTAL_NOTIF,
                    drawerCache.getInt(DrawerNotification.CACHE_TOTAL_NOTIF, 0) + it.notifUnreadsBuyer
            )
            drawerCache.applyEditor()
        }
    }
}
