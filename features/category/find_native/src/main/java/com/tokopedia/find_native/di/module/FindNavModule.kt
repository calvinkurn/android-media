package com.tokopedia.find_native.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.find_native.di.scope.FindNavScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module
class FindNavModule {

    @FindNavScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @FindNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FindNavScope
    @Provides
    fun provideGcmHandler(@ApplicationContext context: Context): GCMHandler {
        return GCMHandler(context)
    }

    @FindNavScope
    @Provides
    fun getAddWishListUseCase(context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @FindNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

}