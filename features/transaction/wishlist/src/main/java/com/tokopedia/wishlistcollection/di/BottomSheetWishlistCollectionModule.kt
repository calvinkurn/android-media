package com.tokopedia.wishlistcollection.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides


@Module
class BottomSheetWishlistCollectionModule() {
    @ActivityScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }
}
