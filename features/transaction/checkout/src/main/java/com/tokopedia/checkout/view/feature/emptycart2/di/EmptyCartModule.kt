package com.tokopedia.checkout.view.feature.emptycart2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

@Module
@EmptyCartScope
class EmptyCartModule {

    @Provides
    @EmptyCartScope
    fun providesGetWishListUseCase(@ApplicationContext context: Context): GetWishlistUseCase {
        return GetWishlistUseCase(context)
    }

}