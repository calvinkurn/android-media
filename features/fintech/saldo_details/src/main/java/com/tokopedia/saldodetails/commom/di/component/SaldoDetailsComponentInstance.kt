package com.tokopedia.saldodetails.commom.di.component

import android.content.Context
import com.tokopedia.saldodetails.commom.di.module.ContextModule

object SaldoDetailsComponentInstance {

    fun getComponent(context: Context): SaldoDetailsComponent {

        val saldoDetailsComponent = DaggerSaldoDetailsComponent.builder()
                .contextModule(ContextModule(context))
                .build()

        return saldoDetailsComponent
    }
}
