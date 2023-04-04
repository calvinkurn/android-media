package com.tokopedia.shop_nib.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_nib.di.module.ShopNibModule
import com.tokopedia.shop_nib.di.module.ShopNibViewModelModule
import com.tokopedia.shop_nib.di.scope.ShopNibScope
import com.tokopedia.shop_nib.presentation.submission.NibSubmissionActivity
import com.tokopedia.shop_nib.presentation.submission.NibSubmissionFragment
import dagger.Component

@ShopNibScope
@Component(
    modules = [ShopNibModule::class, ShopNibViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShopNibComponent {
    fun inject(activity: NibSubmissionActivity)
    fun inject(fragment: NibSubmissionFragment)

}
