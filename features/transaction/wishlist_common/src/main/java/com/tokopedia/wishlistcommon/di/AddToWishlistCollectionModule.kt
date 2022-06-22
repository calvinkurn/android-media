package com.tokopedia.wishlistcommon.di

import android.app.Activity
import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides


@Module
class AddToWishlistCollectionModule(private val activity: Activity) {
    @AddToWishlistCollectionScope
    @Provides
    fun provideContext(): Context = activity

    @AddToWishlistCollectionScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
}