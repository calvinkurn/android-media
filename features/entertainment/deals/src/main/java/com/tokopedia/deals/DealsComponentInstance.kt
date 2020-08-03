package com.tokopedia.deals

import android.app.Application
import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.deals.common.di.DaggerDealsComponent
import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.common.di.DealsModule
import com.tokopedia.deals.location_picker.di.component.DaggerDealsLocationComponent
import com.tokopedia.deals.location_picker.di.component.DealsLocationComponent
import com.tokopedia.deals.location_picker.di.module.DealsLocationModule

/**
 * @author by jessica on 11/06/20
 */

object DealsComponentInstance {
    private lateinit var dealsComponent: DealsComponent
    private lateinit var dealsLocationComponent: DealsLocationComponent

    fun getDealsComponent(application: Application, context: Context): DealsComponent {
        if (!::dealsComponent.isInitialized) {
            dealsComponent = DaggerDealsComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .dealsModule(DealsModule(context)).build()
        }
        
        return dealsComponent
    }

    fun getDealsLocationComponent(application: Application, context: Context): DealsLocationComponent {
        if (!::dealsLocationComponent.isInitialized) {
            dealsLocationComponent = DaggerDealsLocationComponent.builder()
                    .dealsComponent(getDealsComponent(application, context))
                    .dealsLocationModule(DealsLocationModule()).build()
        }

        return dealsLocationComponent
    }
}