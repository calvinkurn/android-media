package com.tokopedia.productcard_compact.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.productcard_compact.common.di.module.CommonModule
import com.tokopedia.productcard_compact.common.di.module.CommonViewModelModule
import com.tokopedia.productcard_compact.common.di.scope.CommonScope
import com.tokopedia.productcard_compact.productcard.presentation.customview.TokoNowWishlistButtonView
import com.tokopedia.productcard_compact.similarproduct.presentation.fragment.TokoNowSimilarProductFragment
import dagger.Component

@CommonScope
@Component(modules = [CommonModule::class, CommonViewModelModule::class], dependencies = [BaseAppComponent::class])
internal interface CommonComponent {
    fun inject(view: TokoNowWishlistButtonView)
    fun inject(fragment: TokoNowSimilarProductFragment)
}
