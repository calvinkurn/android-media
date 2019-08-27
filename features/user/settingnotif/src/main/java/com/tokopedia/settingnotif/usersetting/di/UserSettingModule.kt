package com.tokopedia.settingnotif.usersetting.di

import android.content.Context
import android.support.annotation.RawRes
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.presenter.SettingFieldPresenter
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import dagger.Module
import dagger.Provides


@UserSettingScope
@Module
class UserSettingModule(var context: Context?, @RawRes val gqlQueryRaw: Int) {

    @UserSettingScope
    @Provides
    fun provideSettingFieldPresenter(
            getUserSettingUseCase: GetUserSettingUseCase,
            setUserSettingUseCase: SetUserSettingUseCase
    ): SettingFieldContract.Presenter {
        return SettingFieldPresenter(getUserSettingUseCase, setUserSettingUseCase)
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