package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class AddWishListUseCaseModule {

    @SearchScope
    @Provides
    AddWishListUseCase provideWishlistUseCase(@ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }
}
