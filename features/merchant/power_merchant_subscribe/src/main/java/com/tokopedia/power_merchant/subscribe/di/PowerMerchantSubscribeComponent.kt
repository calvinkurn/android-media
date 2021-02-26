package com.tokopedia.power_merchant.subscribe.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.SubscriptionFragment
import com.tokopedia.power_merchant.subscribe.view_old.activity.PMCancellationQuestionnaireActivity
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantCancellationQuestionnaireIntroFragment
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantCancellationQuestionnaireMultipleOptionFragment
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantSubscribeFragment
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantTermsFragment
import dagger.Component

@PowerMerchantSubscribeScope
@Component(modules = [
    PowerMerchantSubscribeModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface PowerMerchantSubscribeComponent {

    fun inject(fragment: PowerMerchantSubscribeFragment)

    fun inject(fragment: PowerMerchantTermsFragment)

    fun inject(fragment: PowerMerchantCancellationQuestionnaireIntroFragment)

    fun inject(fragment: PowerMerchantCancellationQuestionnaireMultipleOptionFragment)

    fun inject(activity: PMCancellationQuestionnaireActivity)

    fun inject(subscriptionFragment: SubscriptionFragment)
}