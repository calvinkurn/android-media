package com.tokopedia.instantdebitbca.data.view.utils

import android.app.Application

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.instantdebitbca.data.di.DaggerInstantDebitBcaComponent
import com.tokopedia.instantdebitbca.data.di.InstantDebitBcaComponent

/**
 * Created by nabillasabbaha on 25/03/19.
 */
object InstantDebitBcaInstance {

    private var instantDebitBcaComponent: InstantDebitBcaComponent? = null

    fun getComponent(application: Application): InstantDebitBcaComponent {
        if (instantDebitBcaComponent == null) {
            instantDebitBcaComponent = DaggerInstantDebitBcaComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
        return this.instantDebitBcaComponent!!
    }
}
