package com.tokopedia.loginfingerprint.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.LoginFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named


@Module
class LoginFingerprintQueryModule {

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers): RegisterFingerprintUseCase {
        val useCase = GraphqlUseCase<RegisterFingerprintPojo>(graphqlRepository)
        return RegisterFingerprintUseCase(useCase, dispatchers)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideVerifyFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers): VerifyFingerprintUseCase {
        val useCase = GraphqlUseCase<VerifyFingerprintPojo>(graphqlRepository)
        return VerifyFingerprintUseCase(useCase, dispatchers)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideCheckFingerprintToggleUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers): CheckFingerprintToggleStatusUseCase {
        val useCase = GraphqlUseCase<CheckFingerprintPojo>(graphqlRepository)
        return CheckFingerprintToggleStatusUseCase(useCase, dispatchers)
    }

    @LoginFingerprintSettingScope
    @Provides
    fun provideLoginFingerprintUsecase(graphqlRepository: GraphqlRepository, dispatchers: CoroutineDispatchers, @Named(
        SessionModule.SESSION_MODULE) userSessionInterface: UserSessionInterface
    ): LoginFingerprintUseCase {
        val useCase = GraphqlUseCase<LoginTokenPojo>(graphqlRepository)
        return LoginFingerprintUseCase(useCase, dispatchers, userSessionInterface)
    }

    @Provides
    @IntoMap
    @StringKey(ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST)
    fun provideRawQueryGetAccountList(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(context.resources, R.raw.query_get_accounts_list)

    @LoginFingerprintSettingScope
    @Provides
    fun provideValidateFingerprintUsecase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ValidateFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideLoginTokenUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<LoginTokenPojo> = GraphqlUseCase(graphqlRepository)
}