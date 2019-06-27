package com.tokopedia.settingnotif.usersetting.di

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.presenter.SettingFieldPresenter
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import dagger.Module
import dagger.Provides


@UserSettingScope
@Module
class UserSettingModule(var context: Context?) {

    @UserSettingScope
    @Provides
    fun provideSettingFieldPresenter(getUserSettingUseCase: GetUserSettingUseCase): SettingFieldContract.Presenter {
        return SettingFieldPresenter(getUserSettingUseCase)
    }

    @UserSettingScope
    @Provides
    fun providesGetUserSettingUseCase(gqlUserCase: GraphqlUseCase): GetUserSettingUseCase {
        return GetUserSettingUseCase(context, gqlUserCase)
    }

    @UserSettingScope
    @Provides
    fun providesGqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

}