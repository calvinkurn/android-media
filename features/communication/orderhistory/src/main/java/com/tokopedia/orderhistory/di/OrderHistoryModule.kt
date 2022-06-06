package com.tokopedia.orderhistory.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class OrderHistoryModule {

    @OrderHistoryScope
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @OrderHistoryScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @OrderHistoryScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @OrderHistoryScope
    @Provides
    internal fun provideAddWishListUseCase(@OrderHistoryContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @OrderHistoryScope
    @Provides
    internal fun provideAddWishListV2UseCase(@ApplicationContext graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }
}