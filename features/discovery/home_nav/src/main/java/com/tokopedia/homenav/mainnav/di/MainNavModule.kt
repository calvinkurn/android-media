package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [
    MainNavUseCaseModule::class
])
class MainNavModule {

    @MainNavScope
    @Provides
    fun provideAccountHeaderMapper(@ApplicationContext context: Context,
                                   userSession: UserSessionInterface
    ) = AccountHeaderMapper(
            userSession
    )

    @MainNavScope
    @Provides
    fun provideClientMenuGenerator(@ApplicationContext context: Context, userSession: UserSessionInterface) = ClientMenuGenerator(context, userSession)
}
