package com.tokopedia.power_merchant.subscribe.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.power_merchant.subscribe.view.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantTermsFragment
import dagger.Component

@PowerMerchantSubscribeScope
@Component(modules = [
    PowerMerchantSubscribeModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface PowerMerchantSubscribeComponent {

    fun inject(fragment: PowerMerchantSubscribeFragment)

    fun inject(fragment: PowerMerchantTermsFragment)

    fun inject(activity: PMCancellationQuestionnaireActivity)

}