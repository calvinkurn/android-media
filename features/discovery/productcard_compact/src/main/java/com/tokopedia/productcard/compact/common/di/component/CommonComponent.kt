package com.tokopedia.productcard.compact.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.productcard.compact.common.di.module.CommonModule
import com.tokopedia.productcard.compact.common.di.module.CommonViewModelModule
import com.tokopedia.productcard.compact.common.di.scope.CommonScope
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactWishlistButtonView
import dagger.Component

@CommonScope
@Component(modules = [CommonModule::class, CommonViewModelModule::class], dependencies = [BaseAppComponent::class])
internal interface CommonComponent {
    fun inject(view: ProductCardCompactWishlistButtonView)
}
