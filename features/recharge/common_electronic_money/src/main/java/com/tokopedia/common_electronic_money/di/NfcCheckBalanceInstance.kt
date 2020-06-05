package com.tokopedia.common_electronic_money.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

object NfcCheckBalanceInstance {

    fun getNfcCheckBalanceComponent(application: Application): NfcCheckBalanceComponent {
        val nfcCheckBalanceComponent: NfcCheckBalanceComponent by lazy {
            DaggerNfcCheckBalanceComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return nfcCheckBalanceComponent
    }
}