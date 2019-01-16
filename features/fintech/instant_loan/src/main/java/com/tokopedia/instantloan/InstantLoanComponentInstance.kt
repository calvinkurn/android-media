package com.tokopedia.instantloan

import android.app.Application

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.instantloan.di.component.DaggerInstantLoanComponent
import com.tokopedia.instantloan.di.component.InstantLoanComponent
import com.tokopedia.instantloan.di.module.InstantLoanModule

object InstantLoanComponentInstance {

    var instantLoanComponent: InstantLoanComponent? = null

    @JvmStatic fun get(application: Application): InstantLoanComponent? {
        if (instantLoanComponent == null) {
            instantLoanComponent = DaggerInstantLoanComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .instantLoanModule(InstantLoanModule()).build()
        }
        return instantLoanComponent
    }
}
