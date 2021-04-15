package com.tokopedia.loginfingerprint.di

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class LoginFingerprintSettingModule(val context: Context) {

    @Provides
    @LoginFingerprintContext
    fun provideContext(): Context = context

    @LoginFingerprintSettingScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @LoginFingerprintSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @LoginFingerprintSettingScope
    @Provides
    fun providePreferenceHelper(@LoginFingerprintContext context: Context): FingerprintSetting = FingerprintPreferenceHelper(context)

    @LoginFingerprintSettingScope
    @Provides
    fun provideUserSessionInterface(@LoginFingerprintContext context: Context): UserSessionInterface = UserSession(context)

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    fun provideCryptography(): Cryptography?  {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptographyUtils()
        }
        else null
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}