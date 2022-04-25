package com.tokopedia.search.result.product.chooseaddress

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class ChooseAddressViewModule {

    @Provides
    @SearchScope
    fun provideChooseAddressModule(@SearchContext context: Context): ChooseAddressView {
        return ChooseAddressViewDelegate(context)
    }
}