package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotificationMarkAsSeenUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-09-13.
 * ade.hadian@tokopedia.com
 */

@Module class NotificationQueryModule {

    @Provides
    @NotificationScope
    @Named(NotificationMarkAsSeenUseCase.MUTATION_MARK_AS_SEEN)
    fun provideNotifMarkAsSeen(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, NotificationMarkAsSeenUseCase.query)

    @Provides
    @NotificationScope
    @Named(NotifcenterDetailUseCase.QUERY_NOTIFCENTER_DETAIL_V3)
    fun provideNotifcenterDetailV3(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, NotifcenterDetailUseCase.queryRes)
    @Provides
    @NotificationScope
    @Named(NotifOrderListUseCase.QUERY_ORDER_LIST)
    fun provideNotifcenterOrderList(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, NotifOrderListUseCase.queryRes)
}
