package com.tokopedia.deals.ui.location_picker.di.component

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.location_picker.di.DealsLocationScope
import com.tokopedia.deals.ui.location_picker.di.module.DealsLocationModule
import com.tokopedia.deals.ui.location_picker.di.module.DealsLocationViewModelModule
import com.tokopedia.deals.ui.location_picker.ui.fragment.DealsSelectLocationFragment
import dagger.Component

@DealsLocationScope
@Component(
    modules = [
        DealsLocationModule::class,
        DealsLocationViewModelModule::class
    ],
    dependencies = [
        DealsComponent::class
    ]
)
interface DealsLocationComponent {
    fun inject(selectLocationFragment: DealsSelectLocationFragment)
}
