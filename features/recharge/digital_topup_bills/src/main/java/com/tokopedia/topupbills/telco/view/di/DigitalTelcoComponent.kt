package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.topupbills.common.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.view.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoProductFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoPromoFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoRecommendationFragment
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