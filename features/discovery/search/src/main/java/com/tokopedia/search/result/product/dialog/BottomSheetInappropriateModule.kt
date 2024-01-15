package com.tokopedia.search.result.product.dialog

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class BottomSheetInappropriateModule {

    @SearchScope
    @Binds
    abstract fun bindBottomSheetInappropriateView(
        bottomSheetInappropriateViewDelegate: BottomSheetInappropriateViewDelegate
    ): BottomSheetInappropriateView
}
