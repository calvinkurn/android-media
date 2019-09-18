package com.tokopedia.similarsearch.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module
public class SimilarSearchModule {
    @ApplicationContext
    @Provides
    Context context(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    AddWishListUseCase providesTkpdAddWishListUseCase(
            @ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }

    @Provides
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase(
            @ApplicationContext Context context) {
        return new RemoveWishListUseCase(context);
    }
}
