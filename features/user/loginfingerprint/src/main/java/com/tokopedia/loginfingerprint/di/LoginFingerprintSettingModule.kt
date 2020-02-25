package com.tokopedia.loginfingerprint.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.utils.AppDispatcherProvider
import com.tokopedia.loginfingerprint.utils.CryptographyUtils
import com.tokopedia.loginfingerprint.utils.DispatcherProvider


@LoginFingerprintSettingScope
@Module
class LoginFingerprintSettingModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @LoginFingerprintSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context): FingerprintPreferenceHelper = FingerprintPreferenceHelper(context)

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    fun provideRegisterFingerprintUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideCryptographyUtils(): CryptographyUtils = CryptographyUtils()

    @Provides
    fun provideDispatchers(): DispatcherProvider = AppDispatcherProvider()

}