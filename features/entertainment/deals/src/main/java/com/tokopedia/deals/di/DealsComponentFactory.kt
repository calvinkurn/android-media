package com.tokopedia.deals.di

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.deals.ui.location_picker.di.component.DaggerDealsLocationComponent
import com.tokopedia.deals.ui.location_picker.di.component.DealsLocationComponent
import com.tokopedia.deals.ui.location_picker.di.module.DealsLocationModule

/**
 * @author by jessica on 11/06/20
 */

open class DealsComponentFactory {
    private lateinit var dealsLocationComponent: DealsLocationComponent

    open fun getDealsComponent(application: Application, context: Context): DealsComponent {
        return DaggerDealsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .dealsModule(DealsModule(context)).build()
    }

    open fun getDealsLocationComponent(
        application: Application,
        context: Context
    ): DealsLocationComponent {
        if (!::dealsLocationComponent.isInitialized) {
            dealsLocationComponent = DaggerDealsLocationComponent.builder()
                .dealsComponent(getDealsComponent(application, context))
                .dealsLocationModule(DealsLocationModule()).build()
        }

        return dealsLocationComponent
    }

    companion object {
        private var sInstance: DealsComponentFactory? = null

        @VisibleForTesting
        var instance: DealsComponentFactory
            get() {
                if (sInstance == null) sInstance = DealsComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
