package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.util.CipherProvider
import com.tokopedia.kyc_centralized.util.CipherProviderImpl
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceImpl
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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

}
