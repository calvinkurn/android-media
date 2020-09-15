package com.tokopedia.digital.home.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication


object RechargeHomepageComponentInstance {
    private lateinit var rechargeHomepageComponent: RechargeHomepageComponent

    fun getRechargeHomepageComponent(application: Application): RechargeHomepageComponent {
        if (!::rechargeHomepageComponent.isInitialized) {
            rechargeHomepageComponent =  DaggerRechargeHomepageComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return rechargeHomepageComponent
    }

}
