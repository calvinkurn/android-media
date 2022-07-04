package com.tokopedia.wishlistcollection.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlist.di.WishlistV2Scope
import dagger.Module
import dagger.Provides


@Module
class WishlistCollectionHostBottomSheetModule(private val activity: Activity) {
    @WishlistV2Scope
    @Provides
    fun provideContext(): Context = activity

    @WishlistCollectionHostBottomSheetScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @WishlistCollectionHostBottomSheetScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
}
