package com.tokopedia.find_native.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.find_native.di.scope.FindNavScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
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
    fun addWishListV2UseCase(@ApplicationContext graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @FindNavScope
    @Provides
    fun deleteWishListV2UseCase(@ApplicationContext graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

}