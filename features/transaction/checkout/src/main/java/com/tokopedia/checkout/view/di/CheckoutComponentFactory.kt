package com.tokopedia.checkout.view.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.checkout.view.ShipmentFragment

open class CheckoutComponentFactory {

    open fun createComponent(application: Application, fragment: ShipmentFragment): CheckoutComponent {
        return DaggerCheckoutComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .checkoutModule(CheckoutModule(fragment))
            .build()
    }

    companion object {
        private var sInstance: CheckoutComponentFactory? = null

        var instance: CheckoutComponentFactory
            get() {
                if (sInstance == null) {
                    sInstance = CheckoutComponentFactory()
                }
                return sInstance!!
            }
            set(value) {
                sInstance = value
            }
    }
}
