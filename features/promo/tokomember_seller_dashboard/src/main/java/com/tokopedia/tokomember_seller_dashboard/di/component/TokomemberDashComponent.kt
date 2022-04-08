package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberDashModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCouponFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCreateProgramFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashIntroFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberMainFragment
import dagger.Component

@TokomemberDashScope
@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class, TokomemberDashModule::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashHomeFragment: TokomemberDashHomeFragment)
    fun inject(tokomemberDashProgramFragment: TokomemberDashProgramFragment)
    fun inject(tokomemberDashCouponFragment: TokomemberDashCouponFragment)
    fun inject(tokomemberMainFragment: TokomemberMainFragment)
    fun inject(tokomemberDashIntroFragment: TokomemberDashIntroFragment)
    fun inject(tokomemberDashCreateCardFragment: TokomemberDashCreateCardFragment)
    fun inject(tokomemberDashCreateProgramFragment: TokomemberDashCreateProgramFragment)
}