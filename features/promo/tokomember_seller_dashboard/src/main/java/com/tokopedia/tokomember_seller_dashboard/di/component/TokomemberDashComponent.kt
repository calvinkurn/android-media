package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashIntroFragment
import dagger.Component

@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashIntroFragment: TokomemberDashIntroFragment)
}