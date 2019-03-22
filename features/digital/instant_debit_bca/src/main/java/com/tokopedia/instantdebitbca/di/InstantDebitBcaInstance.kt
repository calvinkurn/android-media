package com.tokopedia.instantdebitbca.di

import android.app.Application
import com.tokopedia.instantdebitbca.di.DaggerInstantDebitBcaComponent
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by nabillasabbaha on 21/03/19.
 */
object InstantDebitBcaInstance {

    fun getComponent(application: Application): InstantDebitBcaComponent {
        val instantDebitBcaComponent: InstantDebitBcaComponent by lazy {
            DaggerInstantDebitBcaComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return instantDebitBcaComponent;
    }
}