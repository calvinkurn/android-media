package com.tokopedia.tokopedianow.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.common.di.module.CommonModule
import com.tokopedia.tokopedianow.common.di.module.CommonViewModelModule
import com.tokopedia.tokopedianow.common.di.scope.CommonScope
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowWishlistButtonView
import dagger.Component

@CommonScope
@Component(modules = [CommonModule::class, CommonViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CommonComponent {
    fun inject(tokoNowWishlistButtonView: TokoNowWishlistButtonView)
}
