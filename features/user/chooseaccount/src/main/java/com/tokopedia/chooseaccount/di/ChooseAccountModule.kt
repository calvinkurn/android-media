package com.tokopedia.chooseaccount.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 10/22/18.
 */
@Module
class ChooseAccountModule {

//    @ActivityScope
//    @Provides
//    @ApplicationContext
//    fun provideContext(): Context? {
//	return context?.applicationContext
//    }

    @ActivityScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
	return context.resources
    }

    @ActivityScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
	return getInstance().graphqlRepository
    }

    @ActivityScope
    @Provides
    fun provideFingerprintPreferenceManager(@ApplicationContext context: Context?): FingerprintPreference {
	return FingerprintPreferenceManager(context!!)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
	return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideLoginTokenUseCase(resources: Resources, graphqlUseCase: GraphqlUseCase, userSession: UserSessionInterface): LoginTokenUseCase {
	return LoginTokenUseCase(resources, graphqlUseCase, userSession)
    }

}