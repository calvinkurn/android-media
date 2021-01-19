package com.tokopedia.digital_checkout.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.digital_checkout.di.DaggerDigitalCheckoutComponent.*

/**
 * @author by jessica on 07/01/21
 */

object DigitalCheckoutComponentInstance {
    private lateinit var digitalCheckoutComponent: DigitalCheckoutComponent

    fun getDigitalCheckoutComponent(application: Application): DigitalCheckoutComponent {
        if (!::digitalCheckoutComponent.isInitialized) {
            digitalCheckoutComponent = builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return digitalCheckoutComponent
    }
}