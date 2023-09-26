package com.tokopedia.minicart.bmgm.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.minicart.bmgm.presentation.bottomsheet.BmgmMiniCartDetailBottomSheet
import com.tokopedia.minicart.bmgm.presentation.customview.BmgmMiniCartView
import dagger.Component

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

@ActivityScope
@Component(
    modules = [BmgmViewModelModule::class, BmgmModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BmgmComponent {

    fun inject(view: BmgmMiniCartView)
    fun inject(bottomSheet: BmgmMiniCartDetailBottomSheet)
}