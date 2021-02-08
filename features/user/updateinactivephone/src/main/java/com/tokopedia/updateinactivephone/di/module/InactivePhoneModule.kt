package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.updateinactivephone.common.UserDataTemporary
import com.tokopedia.updateinactivephone.di.InactivePhoneContext
import com.tokopedia.updateinactivephone.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
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

    @InactivePhoneScope
    @Provides
    fun provideInactivePhoneTracker(): InactivePhoneTracker {
        return InactivePhoneTracker()
    }

}