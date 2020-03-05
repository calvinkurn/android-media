package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.di.scope.NotificationUpdateScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class NotificationUpdateModule {

    @Provides
    @Named("atcMutation")
    @NotificationUpdateScope
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_product_to_cart)
    }

}
