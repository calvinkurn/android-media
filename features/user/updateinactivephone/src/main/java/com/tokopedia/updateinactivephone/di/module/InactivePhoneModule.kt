package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.updateinactivephone.di.InactivePhoneContext
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class InactivePhoneModule(private val context: Context) {

    @ActivityScope
    @InactivePhoneContext
    @Provides
    fun provideInactivePhoneContext(): Context = context

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ActivityScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @ActivityScope
    @Provides
    fun provideApiClient(): InactivePhoneApiClient<InactivePhoneApi> {
        return InactivePhoneApiClient(InactivePhoneApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideInactivePhoneTracker(): InactivePhoneTracker {
        return InactivePhoneTracker()
    }

    @ActivityScope
    @Provides
    fun provideInactivePhoneWithPinTracker(): InactivePhoneWithPinTracker {
        return InactivePhoneWithPinTracker()
    }

}