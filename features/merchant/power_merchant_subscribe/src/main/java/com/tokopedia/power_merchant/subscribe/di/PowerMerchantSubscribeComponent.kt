package com.tokopedia.power_merchant.subscribe.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.power_merchant.subscribe.view.activity.MembershipDetailActivity
import com.tokopedia.power_merchant.subscribe.view.activity.SubscriptionActivity
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.DeactivationQuestionnaireBottomSheet
import com.tokopedia.power_merchant.subscribe.view.bottomsheet.OptOutConfirmationBottomSheet
import com.tokopedia.power_merchant.subscribe.view.fragment.MembershipDetailFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscriptionFragment
import dagger.Component

@PowerMerchantSubscribeScope
@Component(
    modules = [
        PowerMerchantSubscribeModule::class,
        ViewModelModule::class
    ], dependencies = [BaseAppComponent::class]
)
interface PowerMerchantSubscribeComponent {

    fun inject(fragment: PowerMerchantSubscriptionFragment)

    fun inject(deactivationBottomSheet: DeactivationQuestionnaireBottomSheet)

    fun inject(subscriptionActivity: SubscriptionActivity)

    fun inject(activity: MembershipDetailActivity)

    fun inject(fragment: MembershipDetailFragment)

    fun inject(optOutConfirmationBottomSheet: OptOutConfirmationBottomSheet)
}
