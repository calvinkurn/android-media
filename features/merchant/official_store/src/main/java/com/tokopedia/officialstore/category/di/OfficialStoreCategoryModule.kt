package com.tokopedia.officialstore.category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [OfficialStoreCategoryViewModelModule::class])
class OfficialStoreCategoryModule {

    @OfficialStoreCategoryScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

}
