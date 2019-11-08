package com.tokopedia.similarsearch.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.similarsearch.di.scope.SimilarSearchModuleScope;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;

@SimilarSearchModuleScope
@Module
public class SimilarSearchModule {

    @SimilarSearchModuleScope
    @Provides
    AddWishListUseCase providesTkpdAddWishListUseCase(
            @ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }

    @SimilarSearchModuleScope
    @Provides
    RemoveWishListUseCase providesTkpdRemoveWishListUseCase(
            @ApplicationContext Context context) {
        return new RemoveWishListUseCase(context);
    }

    @SimilarSearchModuleScope
    @Provides
    UserSessionInterface provideUserSession(
            @ApplicationContext Context context
    ) {
        return new UserSession(context);
    }
}
