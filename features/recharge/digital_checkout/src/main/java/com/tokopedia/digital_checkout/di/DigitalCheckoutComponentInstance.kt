package com.tokopedia.digital_checkout.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent
import com.tokopedia.digital_checkout.di.DaggerDigitalCheckoutComponent.builder

/**
 * @author by jessica on 07/01/21
 */

object DigitalCheckoutComponentInstance {
    private lateinit var digitalCheckoutComponent: DigitalCheckoutComponent

    fun getDigitalCheckoutComponent(application: Application): DigitalCheckoutComponent {
        if (!::digitalCheckoutComponent.isInitialized) {

            val digitalCommonComponent = DaggerDigitalCommonComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
            digitalCheckoutComponent = builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build()
        }

        return digitalCheckoutComponent
    }
}