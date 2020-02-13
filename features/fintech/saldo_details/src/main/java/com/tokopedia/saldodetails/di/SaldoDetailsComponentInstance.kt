package com.tokopedia.saldodetails.di

import android.app.Application

import com.tokopedia.abstraction.base.app.BaseMainApplication

object SaldoDetailsComponentInstance {

    fun getComponent(application: Application): SaldoDetailsComponent {

        val saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

        return saldoDetailsComponent
    }
}
