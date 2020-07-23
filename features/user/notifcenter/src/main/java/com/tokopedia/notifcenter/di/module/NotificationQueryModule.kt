package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
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

}
