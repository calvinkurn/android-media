package com.tokopedia.loginfingerprint.di

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.loginfingerprint.utils.crypto.KeyPairManager
import com.tokopedia.loginfingerprint.utils.crypto.RsaSignatureUtils
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
    fun provideUserSessionInterface(@LoginFingerprintContext context: Context): UserSessionInterface = UserSession(context)

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideBiometricTracker(): BiometricTracker = BiometricTracker()

    @LoginFingerprintSettingScope
    @Provides
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    fun provideRsaSignatureUtils(): RsaSignatureUtils?  {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RsaSignatureUtils()
        }
        else null
    }

    @LoginFingerprintSettingScope
    @Provides
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    fun provideKeyPairManager(@LoginFingerprintContext context: Context): KeyPairManager?  {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyPairManager(context)
        }
        else null
    }
}
