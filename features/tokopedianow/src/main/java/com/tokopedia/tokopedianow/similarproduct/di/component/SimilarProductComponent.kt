package com.tokopedia.tokopedianow.similarproduct.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.similarproduct.di.module.SimilarProductModule
import com.tokopedia.tokopedianow.similarproduct.di.module.SimilarProductViewModelModule
import com.tokopedia.tokopedianow.similarproduct.di.scope.SimilarProductScope
import com.tokopedia.tokopedianow.similarproduct.presentation.fragment.TokoNowSimilarProductBottomSheetFragment
import dagger.Component

@SimilarProductScope
@Component(
    modules = [
        SimilarProductModule::class,
        SimilarProductViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface SimilarProductComponent {
    fun inject(bottomSheetFragment: TokoNowSimilarProductBottomSheetFragment)
}
