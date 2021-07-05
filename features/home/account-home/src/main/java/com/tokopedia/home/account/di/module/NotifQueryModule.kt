package com.tokopedia.home.account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.account.R
import com.tokopedia.home.account.constant.NotificationQueriesConstant
import com.tokopedia.home.account.di.scope.AccountLogoutScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-09-13.
 * ade.hadian@tokopedia.com
 */

@Module
class NotifQueryModule {

    @Provides
    @IntoMap
    @StringKey(NotificationQueriesConstant.MUTATION_NOTIF_CENTER_PUSH_NOTIF)
    fun provideRawQuerySendNotif(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_notif_center_send_notif)

}
