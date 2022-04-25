package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberDashModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.fragment.*
import dagger.Component

@TokomemberDashScope
@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class, TokomemberDashModule::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashHomeFragment: TokomemberDashHomeFragment)
    fun inject(tokomemberDashProgramDetailFragment: TokomemberDashProgramDetailFragment)
    fun inject(tokomemberDashProgramFragment: TokomemberDashProgramListFragment)
    fun inject(tokomemberDashCouponFragment: TokomemberDashCouponFragment)
    fun inject(tokomemberMainFragment: TokomemberMainFragment)
    fun inject(tokomemberDashIntroFragment: TokomemberDashIntroFragment)
    fun inject(tokomemberCreateCardFragment: TokomemberCreateCardFragment)
    fun inject(tokomemberProgramFragment: TokomemberProgramFragment)
    fun inject(tokomemberDashCreateProgramFragment: TokomemberDashPreviewFragment)
    fun inject(tokomemberKuponCreateFragment: TokomemberKuponCreateFragment)
}