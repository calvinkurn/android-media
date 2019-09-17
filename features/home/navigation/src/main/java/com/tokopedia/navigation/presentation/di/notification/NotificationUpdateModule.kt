package com.tokopedia.navigation.presentation.di.notification

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.navigation.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationUpdateModule {

    @NotificationUpdateScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
    }

}
