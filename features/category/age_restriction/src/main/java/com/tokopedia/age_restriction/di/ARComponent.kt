package com.tokopedia.age_restriction.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.age_restriction.viewcontroller.AgeRestrictionHomeActivity
import com.tokopedia.age_restriction.viewcontroller.VerifyDOBActivity
import dagger.Component


@ARScope
@Component(modules = [ARModule::class, ARViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ARComponent {

    fun inject(ageRestrictionHomeActivity: AgeRestrictionHomeActivity)
    fun inject(verifyDOBActivity: VerifyDOBActivity)

}