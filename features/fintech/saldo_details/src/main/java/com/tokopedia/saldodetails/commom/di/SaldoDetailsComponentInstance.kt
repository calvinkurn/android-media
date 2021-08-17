package com.tokopedia.saldodetails.commom.di

import android.content.Context

object SaldoDetailsComponentInstance {

    fun getComponent(context: Context): SaldoDetailsComponent {

        val saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                .contextModule(ContextModule(context))
                .build()

        return saldoDetailsComponent
    }
}
