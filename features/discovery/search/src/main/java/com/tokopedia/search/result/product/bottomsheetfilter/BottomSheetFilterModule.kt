package com.tokopedia.search.result.product.bottomsheetfilter

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import dagger.Module
import dagger.Provides

@Module
object BottomSheetFilterModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideBottomSheetFilterView(
        bottomSheetFilterViewDelegate: BottomSheetFilterViewDelegate
    ) : BottomSheetFilterView {
        return bottomSheetFilterViewDelegate
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideBottomSheetFilterPresenter(
        bottomSheetFilterPresenterDelegate: BottomSheetFilterPresenterDelegate
    ) : BottomSheetFilterPresenter {
        return bottomSheetFilterPresenterDelegate
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideDynamicFilterModelProvider(
        bottomSheetFilterPresenterDelegate: BottomSheetFilterPresenterDelegate
    ) : DynamicFilterModelProvider {
        return bottomSheetFilterPresenterDelegate
    }
}
