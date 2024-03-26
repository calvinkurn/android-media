package com.tokopedia.accountprofile.changephonenumber.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.accountprofile.changephonenumber.di.ChangePhoneNumberContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ChangePhoneNumberModule(
    private val context: Context
) {

    @ActivityScope
    @ChangePhoneNumberContext
    @Provides
    fun provideInactivePhoneContext(): Context = context

    @ActivityScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ActivityScope
    @Provides
    fun provideRestRepository(): RestRepository = RestRequestInteractor.getInstance().restRepository

}
