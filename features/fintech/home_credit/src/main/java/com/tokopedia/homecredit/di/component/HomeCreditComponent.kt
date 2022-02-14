package com.tokopedia.homecredit.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.homecredit.di.module.HomeCreditModule
import com.tokopedia.homecredit.di.module.HomeCreditViewModelModule
import com.tokopedia.homecredit.di.scope.HomeCreditScope
import com.tokopedia.homecredit.view.activity.HomeCreditRegisterActivity
import com.tokopedia.homecredit.view.fragment.HomeCreditBaseCameraFragment
import dagger.Component

@HomeCreditScope
@Component(
    modules = [HomeCreditModule::class, HomeCreditViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface HomeCreditComponent {

    fun inject(homeCreditBaseCameraFragment: HomeCreditBaseCameraFragment)

    fun inject(homeCreditRegisterActivity: HomeCreditRegisterActivity)
}