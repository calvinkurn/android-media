package com.tokopedia.loginphone.addname.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.loginphone.choosetokocashaccount.di.ChooseAccountScope
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 23/04/19.
 */
@Module
class AddNameModule {

    @ChooseAccountScope
    @Provides
    internal fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

}
