package com.tokopedia.product.detail.postatc.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import dagger.Component

@PostAtcScope
@Component(
    modules = [
        PostAtcViewModelModule::class,
        PostAtcModule::class
    ],
    dependencies = [BaseAppComponent::class])
interface PostAtcComponent {
    fun inject(fragment: PostAtcBottomSheet)
}
