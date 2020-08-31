package com.tokopedia.loginfingerprint.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey


@Module
class LoginFingerprintQueryModule {

    @LoginFingerprintSettingScope
    @Provides
    @IntoMap
    @StringKey(LoginFingerprintQueryConstant.QUERY_REGISTER_FINGERPRINT)
    fun provideRawQueryRegisterFingerprint(@LoginFingerprintContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_register_fingerprint)

    @LoginFingerprintSettingScope
    @Provides
    @IntoMap
    @StringKey(LoginFingerprintQueryConstant.QUERY_VALIDATE_FINGERPRINT)
    fun provideRawQueryValidateFingerprint(@LoginFingerprintContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_validate_fingerprint)

    @LoginFingerprintSettingScope
    @Provides
    fun provideRegisterFingerprintUsecase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideValidateFingerprintUsecase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<ValidateFingerprintPojo> = GraphqlUseCase(graphqlRepository)

    @LoginFingerprintSettingScope
    @Provides
    fun provideLoginTokenUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<LoginTokenPojo> = GraphqlUseCase(graphqlRepository)
}