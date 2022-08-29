package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
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
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * @author by nisie on 13/11/18.
 */
@Module
open class UserIdentificationCommonModule {

    private val sharedPreferenceName = "kyc_centralized"

    @ActivityScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @ActivityScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    open fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    @IntoMap
    @StringKey(KYCConstant.QUERY_GET_KYC_PROJECT_INFO)
    fun provideRawQueryGetKycProjectInfo(@ActivityScope context: Context): String =
            GraphqlHelper.loadRawString(context.resources, com.tokopedia.kyc_centralized.R.raw.query_get_kyc_project_info)

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