package com.tokopedia.product.addedit.category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module(includes = [AddEditProductCategoryViewModelModule::class])
class AddEditProductCategoryModule {

    @AddEditProductCategoryScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}