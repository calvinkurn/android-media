package com.tokopedia.tokofood.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class TokoFoodComponentBuilder {
    companion object {
        private var tokoFoodComponent: TokoFoodComponent? = null

        fun getComponent(application: Application): TokoFoodComponent {
            return tokoFoodComponent?.run { tokoFoodComponent }
                    ?: DaggerTokoFoodComponent.builder()
                            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                            .build()
        }
    }
}