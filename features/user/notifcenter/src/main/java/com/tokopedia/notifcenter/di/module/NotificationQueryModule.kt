package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
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
    @Named(NotificationQueriesConstant.MUTATION_NOTIF_CENTER_PUSH_NOTIF)
    fun provideRawQuerySendNotif(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_notif_center_send_notif)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
    fun provideRawQueryDrawerNotification(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_drawer_notification)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.TRANSACTION_NOTIFICATION)
    fun provideNotificationViewBean(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_notification_update)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.QUERY_IS_TAB_UPDATE)
    fun provideRawProductInfo(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_is_tab_update)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.FILTER_NOTIFICATION)
    fun provideFilterNotification(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_notification_update_filter)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.PRODUCT_STOCK_HANDLER)
    fun provideProductStockHandler(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_product_stock_handler)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.PRODUCT_STOCK_REMINDER)
    fun provideProductStockReminder(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_product_stock_reminder)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.SINGLE_NOTIFICATION_UPDATE)
    fun provideSingleNotificationUpdate(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_notif_center_single)

    @Provides
    @NotificationScope
    @Named(NotificationQueriesConstant.PRODUCT_HIGHLIGHT)
    fun provideProductHighlight(@NotificationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_ace_search_product)

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
