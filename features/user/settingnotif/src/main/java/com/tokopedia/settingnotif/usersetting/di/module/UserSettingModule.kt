package com.tokopedia.settingnotif.usersetting.di.module

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.usersetting.di.UserSettingScope
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.SetUserSettingUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module class UserSettingModule(
        var context: Context?,
        @RawRes val gqlQueryRaw: Int
) {

    @UserSettingScope
    @Provides
    fun provideUserSession(): UserSessionInterface {
        return UserSession(context)
    }

    @UserSettingScope
    @Provides
    fun providesGetUserSettingUseCase(gqlUserCase: GraphqlUseCase): GetUserSettingUseCase {
        return GetUserSettingUseCase(context, gqlUserCase, gqlQueryRaw)
    }

    @UserSettingScope
    @Provides
    fun providesSetUserSettingUseCase(gqlUserCase: GraphqlUseCase): SetUserSettingUseCase {
        return SetUserSettingUseCase(context, gqlUserCase)
    }

    @UserSettingScope
    @Provides
    fun providesGqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

}