package com.tokopedia.search.result.product.filter.bottomsheetfilter

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.filter.dynamicfilter.DynamicFilterModelProviderModule
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        DynamicFilterModelProviderModule::class,
    ]
)
abstract class BottomSheetFilterModule {
    @SearchScope
    @Binds
    abstract fun bindBottomSheetFilterView(
        bottomSheetFilterViewDelegate: BottomSheetFilterViewDelegate
    ): BottomSheetFilterView

    @SearchScope
    @Binds
    abstract fun bindBottomSheetFilterPresenter(
        presenterDelegate: BottomSheetFilterPresenterDelegate
    ): BottomSheetFilterPresenter
}
