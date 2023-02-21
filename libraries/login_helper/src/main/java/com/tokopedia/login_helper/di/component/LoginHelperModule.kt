package com.tokopedia.login_helper.di.component

import com.tokopedia.login_helper.di.scope.LoginHelperScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class LoginHelperModule {

    @LoginHelperScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}
