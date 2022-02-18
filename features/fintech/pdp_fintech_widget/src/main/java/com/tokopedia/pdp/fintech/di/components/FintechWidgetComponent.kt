package com.tokopedia.pdp.fintech.di.components

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pdp.fintech.di.module.FintechViewModelModule
import com.tokopedia.pdp.fintech.di.module.FintechWidgetModule
import com.tokopedia.pdp.fintech.view.PdpFintechWidget
import com.tokopedia.pdp.fintech.view.activity.ActivationBottomSheetActivity
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet
import com.tokopedia.pdpsimulation.common.di.scope.FintechWidgetScope
import dagger.Component


@FintechWidgetScope
@Component(
    modules =
    [FintechWidgetModule::class,
        FintechViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface FintechWidgetComponent {

    fun inject(pdpFintechWidget: PdpFintechWidget)
    fun inject(activationBottomSheetActivity: ActivationBottomSheetActivity) {}

}