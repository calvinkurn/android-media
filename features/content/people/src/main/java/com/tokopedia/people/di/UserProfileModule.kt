package com.tokopedia.people.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.people.data.UserProfileRepositoryImpl
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class UserProfileModule {

    @Provides
    @UserProfileScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
