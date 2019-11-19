package com.tokopedia.navigation.presentation.di.notification.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.navigation.R
import com.tokopedia.navigation.data.consts.NotificationQueriesConstant
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-09-13.
 * ade.hadian@tokopedia.com
 */

@Module class NotificationQueryModule {

    @Provides
    @IntoMap
    @StringKey(NotificationQueriesConstant.MUTATION_NOTIF_CENTER_PUSH_NOTIF)
    fun provideRawQuerySendNotif(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_notif_center_send_notif)

    @Provides
    @IntoMap
    @StringKey(NotificationQueriesConstant.DRAWER_PUSH_NOTIFICATION)
    fun provideRawQueryDrawerNotification(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_drawer_notification)

    @Provides
    @Named(NotificationQueriesConstant.QUERY_IS_TAB_UPDATE)
    fun provideRawProductInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_is_tab_update)

}
