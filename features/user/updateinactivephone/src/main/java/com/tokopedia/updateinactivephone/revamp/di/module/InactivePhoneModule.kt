package com.tokopedia.updateinactivephone.revamp.di.module

import android.content.Context
import android.service.autofill.UserData
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.updateinactivephone.revamp.common.UserDataTemporary
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneContext
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApi
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class InactivePhoneModule(private val context: Context) {

    @InactivePhoneScope
    @InactivePhoneContext
    @Provides
    fun provideInactivePhoneContext(): Context = context

    @InactivePhoneScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @InactivePhoneScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @InactivePhoneScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @InactivePhoneScope
    @Provides
    fun provideUserDataTemporary(): UserDataTemporary = UserDataTemporary(context)

    @InactivePhoneScope
    @Provides
    fun provideApiClient(): InactivePhoneApiClient<InactivePhoneApi> {
        return InactivePhoneApiClient(InactivePhoneApi::class.java)
    }

}