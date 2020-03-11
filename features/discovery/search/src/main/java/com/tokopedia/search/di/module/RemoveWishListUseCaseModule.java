package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.search.di.qualifier.SearchContext;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class RemoveWishListUseCaseModule {

    @SearchScope
    @Provides
    RemoveWishListUseCase provideWishlistUseCase(@SearchContext Context context) {
        return new RemoveWishListUseCase(context);
    }
}