package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberDashModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashHomeActivity
import com.tokopedia.tokomember_seller_dashboard.view.fragment.*
import dagger.Component

@TokomemberDashScope
@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class, TokomemberDashModule::class,MediaUploaderModule::class ],dependencies  = [BaseAppComponent::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashHomeMainFragment: TokomemberDashHomeMainFragment)
    fun inject(tokomemberDashHomeFragment: TokomemberDashHomeFragment)
    fun inject(tokomemberDashProgramDetailFragment: TokomemberDashProgramDetailFragment)
    fun inject(tokomemberDashProgramFragment: TokomemberDashProgramListFragment)
    fun inject(tokomemberDashCouponFragment: TokomemberDashCouponFragment)
    fun inject(tmAddQuotaBottomsheet: TmAddQuotaBottomsheet)
    fun inject(tokomemberMainFragment: TokomemberMainFragment)
    fun inject(tmIntroFragment: TmIntroFragment)
    fun inject(tmCreateCardFragment: TmCreateCardFragment)
    fun inject(tmProgramFragment: TmProgramFragment)
    fun inject(tmDashCreateProgramFragment: TmDashPreviewFragment)
    fun inject(tmMultipleCuponCreateFragment: TmMultipleCuponCreateFragment)
    fun inject(tokomemberDashHomeActivity: TokomemberDashHomeActivity)
    fun inject(tmSingleCouponCreateFragment: TmSingleCouponCreateFragment)
    fun inject(tmDashCouponDetailFragment:TmDashCouponDetailFragment)
    fun inject(tmMemberListFragment: TokomemberMemberListFragment)
}
