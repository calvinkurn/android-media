package com.tokopedia.topupbills.telco.common.di

import com.tokopedia.topupbills.common.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoProductFragment
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoPromoFragment
import com.tokopedia.topupbills.telco.common.fragment.DigitalTelcoRecommendationFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTelcoScope
@Component(modules = [DigitalTelcoModule::class, DigitalTelcoViewModelModule::class], dependencies = [DigitalTopupComponent::class])
interface DigitalTelcoComponent {

    fun inject(digitalBaseTelcoFragment: DigitalBaseTelcoFragment)

    fun inject(digitalTelcoProductFragment: DigitalTelcoProductFragment)

    fun inject(digitalTelcoPromoFragment: DigitalTelcoPromoFragment)

    fun inject(digitalTelcoRecommendationFragment: DigitalTelcoRecommendationFragment)

    fun inject(baseTelcoActivity: BaseTelcoActivity)

}