package com.tokopedia.revamp_changepassword.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.revamp_changepassword.di.ChangePasswordScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.revamp_changepassword.di.ChangePasswordContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ChangePasswordModule(private val context: Context) {

    @ChangePasswordScope
    @ChangePasswordContext
    @Provides
    fun provideChangePasswordContext(): Context = context

    @ChangePasswordScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ChangePasswordScope
    @Provides
    fun provideUserSession(@ChangePasswordContext context: Context): UserSessionInterface = UserSession(context)

    @ChangePasswordScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}