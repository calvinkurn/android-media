package com.tokopedia.navigation.presentation.di.notification.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.navigation.util.NotifPreference
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.di.notification.scope.NotificationUpdateScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationUpdateModule {

    @NotificationUpdateScope
    @Provides
    fun provideNotifPreference(@ApplicationContext context: Context): NotifPreference = NotifPreference(context)
    
    @NotificationUpdateScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
    }

}
