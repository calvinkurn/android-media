package com.tokopedia.checkout.view.feature.emptycart2.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

@EmptyCartScope
@Component(modules = [EmptyCartModule::class], dependencies = [BaseAppComponent::class])
interface EmptyCartComponent {
}