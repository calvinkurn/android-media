package com.tokopedia.saldodetails.di

import android.app.Application

import com.tokopedia.abstraction.base.app.BaseMainApplication

object SaldoDetailsComponentInstance {

    private var saldoDetailsComponent: SaldoDetailsComponent? = null

    fun getComponent(application: Application): SaldoDetailsComponent? {
        if (saldoDetailsComponent == null) {
            saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return saldoDetailsComponent
    }
}
