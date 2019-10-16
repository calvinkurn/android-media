package com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list.CornerListFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

@CornerScope
@Component(modules = [CornerModule::class], dependencies = [BaseAppComponent::class])
interface CornerComponent {
    fun inject(cornerListFragment: CornerListFragment)
}