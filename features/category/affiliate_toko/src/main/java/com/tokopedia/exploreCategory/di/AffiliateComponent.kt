package com.tokopedia.exploreCategory.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.exploreCategory.ui.activity.AffiliateActivity
import com.tokopedia.exploreCategory.ui.fragment.AffiliateHomeFragment
import dagger.Component

@AffiliateScope
@Component(modules = [AffiliateModule::class, AffiliateVMModule::class], dependencies = [BaseAppComponent::class])
interface AffiliateComponent {

    @get:ApplicationContext
    val context: Context

    fun inject(affiliateHomeFragment: AffiliateHomeFragment)

}
