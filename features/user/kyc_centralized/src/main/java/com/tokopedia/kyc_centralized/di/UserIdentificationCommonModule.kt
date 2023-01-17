package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.domain.GetProjectInfoUseCase
import com.tokopedia.kyc_centralized.util.CipherProvider
import com.tokopedia.kyc_centralized.util.CipherProviderImpl
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceImpl
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
open class UserIdentificationCommonModule {

    private val sharedPreferenceName = "kyc_centralized"

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    open fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)

    @ActivityScope
    @Provides
    open fun provideKycPrefInterface(pref: SharedPreferences): KycSharedPreference {
        return KycSharedPreferenceImpl(pref)
    }

    @Provides
    open fun provideCipher(): CipherProvider {
        return CipherProviderImpl()
    }

    @Provides
    open fun provideGetUserProjectInfoUseCase(
        repository: GraphqlRepository,
        dispatchers: CoroutineDispatchers,
    ): GetProjectInfoUseCase {
        return GetProjectInfoUseCase(repository, dispatchers)
    }

}
