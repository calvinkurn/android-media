package com.tokopedia.saldodetails.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication

object SaldoDetailsComponentInstance {

    fun getComponent(activity: Activity): SaldoDetailsComponent {

        val saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
                .gqlQueryModule(GqlQueryModule(activity))
                .build()

        return saldoDetailsComponent
    }
}
