package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.topupbills.telco.view.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.view.fragment.*
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = [DigitalTopupModule::class, DigitalTopupViewModelModule::class], dependencies = [CommonTopupBillsComponent::class])
interface DigitalTopupComponent {

    fun inject(digitalBaseTelcoFragment: DigitalBaseTelcoFragment)

    fun inject(digitalTelcoProductFragment: DigitalTelcoProductFragment)

    fun inject(digitalSearchNumberFragment: DigitalSearchNumberFragment)

    fun inject(digitalTelcoPromoFragment: DigitalTelcoPromoFragment)

    fun inject(digitalTelcoRecommendationFragment: DigitalTelcoRecommendationFragment)

    fun inject(baseTelcoActivity: BaseTelcoActivity)

}