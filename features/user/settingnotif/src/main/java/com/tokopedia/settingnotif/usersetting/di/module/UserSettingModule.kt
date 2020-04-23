package com.tokopedia.settingnotif.usersetting.di.module

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.di.UserSettingScope
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.util.dispatcher.AppDispatcherProvider
import com.tokopedia.settingnotif.usersetting.util.dispatcher.DispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module class UserSettingModule(
        private val context: Context?,
        @RawRes private val gqlQueryRaw: Int
) {

    @Provides
    @UserSettingScope
    fun provideContext(): Context? {
        return context
    }

    @Provides
    @UserSettingScope
    fun provideUserSession(
            @UserSettingScope context: Context?
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @UserSettingScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @UserSettingScope
    fun provideGetUserSettingUseCase(
            @UserSettingScope context: Context?,
            repository: GraphqlRepository
    ): GetUserSettingUseCase {
        val query = GraphqlHelper.loadRawString(
                context?.resources,
                gqlQueryRaw
        )
        return GetUserSettingUseCase(repository, query)
    }

    @Provides
    @UserSettingScope
    fun provideSetUserSettingUseCase(
            @UserSettingScope context: Context?,
            repository: GraphqlRepository
    ): SetUserSettingUseCase {
        val query = GraphqlHelper.loadRawString(
                context?.resources,
                R.raw.query_set_user_setting
        )
        return SetUserSettingUseCase(repository, query)
    }

    @Provides
    @UserSettingScope
    fun provideGqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @UserSettingScope
    fun provideDispatcherProvider(): DispatcherProvider {
        return AppDispatcherProvider()
    }

}