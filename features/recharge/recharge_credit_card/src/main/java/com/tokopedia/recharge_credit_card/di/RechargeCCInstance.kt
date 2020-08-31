package com.tokopedia.recharge_credit_card.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recharge_credit_card.di.RechargeCCComponent
import com.tokopedia.recharge_credit_card.di.DaggerRechargeCCComponent

object RechargeCCInstance {

    fun getComponent(application: Application): RechargeCCComponent {
        val rechargeCCComponent: RechargeCCComponent by lazy {
            DaggerRechargeCCComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return rechargeCCComponent
    }
}