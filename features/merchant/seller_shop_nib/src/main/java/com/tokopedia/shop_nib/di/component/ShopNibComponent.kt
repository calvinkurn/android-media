package com.tokopedia.shop_nib.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_nib.di.module.ShopNibModule
import com.tokopedia.shop_nib.di.module.ShopNibNetworkModule
import com.tokopedia.shop_nib.di.module.ShopNibViewModelModule
import com.tokopedia.shop_nib.di.scope.ShopNibScope
import com.tokopedia.shop_nib.presentation.landing_page.LandingPageActivity
import com.tokopedia.shop_nib.presentation.submission.NibSubmissionFragment
import com.tokopedia.shop_nib.presentation.submission_success.NibSubmissionSuccessFragment
import com.tokopedia.shop_nib.presentation.submitted.NibAlreadySubmittedFragment
import dagger.Component

@ShopNibScope
@Component(
    modules = [ShopNibModule::class, ShopNibViewModelModule::class, ShopNibNetworkModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShopNibComponent {
    fun inject(activity: LandingPageActivity)
    fun inject(fragment: NibSubmissionFragment)
    fun inject(fragment: NibAlreadySubmittedFragment)
    fun inject(fragment: NibSubmissionSuccessFragment)
}
