package com.tokopedia.tokomember_seller_dashboard.di.component

import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberActivityContextModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberDashModule
import com.tokopedia.tokomember_seller_dashboard.di.module.TokomemberViewmodelModule
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashHomeActivity
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmAddQuotaBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCouponFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashIntroFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashPreviewFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramDetailFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramListFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberKuponCreateFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberMainFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberProgramFragment
import dagger.Component

@TokomemberDashScope
@Component(modules = [TokomemberViewmodelModule::class, TokomemberActivityContextModule::class, TokomemberDashModule::class , MediaUploaderModule::class])
interface TokomemberDashComponent {
    fun inject(tokomemberDashHomeMainFragment: TokomemberDashHomeMainFragment)
    fun inject(tokomemberDashHomeFragment: TokomemberDashHomeFragment)
    fun inject(tokomemberDashProgramDetailFragment: TokomemberDashProgramDetailFragment)
    fun inject(tokomemberDashProgramFragment: TokomemberDashProgramListFragment)
    fun inject(tokomemberDashCouponFragment: TokomemberDashCouponFragment)
    fun inject(tmAddQuotaBottomsheet: TmAddQuotaBottomsheet)
    fun inject(tokomemberMainFragment: TokomemberMainFragment)
    fun inject(tokomemberDashIntroFragment: TokomemberDashIntroFragment)
    fun inject(tokomemberCreateCardFragment: TokomemberCreateCardFragment)
    fun inject(tokomemberProgramFragment: TokomemberProgramFragment)
    fun inject(tokomemberDashCreateProgramFragment: TokomemberDashPreviewFragment)
    fun inject(tokomemberKuponCreateFragment: TokomemberKuponCreateFragment)
    fun inject(tokomemberDashHomeActivity: TokomemberDashHomeActivity)
}