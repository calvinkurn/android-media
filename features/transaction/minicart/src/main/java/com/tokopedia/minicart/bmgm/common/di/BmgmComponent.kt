package com.tokopedia.minicart.bmgm.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.minicart.bmgm.presentation.fragment.BmgmMiniCartFragment
import dagger.Component

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

@BmgmMiniCartScope
@Component(
    modules = [BmgmViewModelModule::class, BmgmModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BmgmComponent {

    fun inject(fragment: BmgmMiniCartFragment)
}