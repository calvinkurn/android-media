package com.tokopedia.buy_more_get_more.minicart.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

@Module
class BmgmModule {

    @ActivityScope
    @Provides
    fun provideUseSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
