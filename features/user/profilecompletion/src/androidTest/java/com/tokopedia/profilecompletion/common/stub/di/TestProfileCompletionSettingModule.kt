package com.tokopedia.profilecompletion.common.stub.di

import android.content.Context
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.common.stub.GraphqlRepositoryStub
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TestProfileCompletionSettingModule(private val context: Context): ProfileCompletionSettingModule(context) {

    @Provides
    override fun provideGraphQlRepository(): GraphqlRepository = GraphqlRepositoryStub()

    override fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return object: UserSession(context) {
            override fun getUserId(): String {
                return "14369519"
            }
        }
    }
}