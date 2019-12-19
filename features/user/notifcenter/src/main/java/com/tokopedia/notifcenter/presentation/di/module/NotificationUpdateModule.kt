package com.tokopedia.notifcenter.presentation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.notifcenter.util.NotifPreference
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.presentation.di.scope.NotificationUpdateScope
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
