package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.util.AppDispatcherProvider
import com.tokopedia.kyc_centralized.util.DispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.KYCConstant
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * @author by nisie on 13/11/18.
 */
@Module
class UserIdentificationCommonModule {

    @UserIdentificationCommonScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @UserIdentificationCommonScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @UserIdentificationCommonScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @UserIdentificationCommonScope
    @Provides
    @IntoMap
    @StringKey(KYCConstant.QUERY_GET_KYC_PROJECT_INFO)
    fun provideRawQueryGetKycProjectInfo(@UserIdentificationCommonScope context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_kyc_project_info)

    @UserIdentificationCommonScope
    @Provides
    fun provideMainDispatcher(): DispatcherProvider {
        return AppDispatcherProvider()
    }
}