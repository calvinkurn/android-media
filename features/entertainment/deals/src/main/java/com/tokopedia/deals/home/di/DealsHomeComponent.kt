package com.tokopedia.deals.home.di

import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.home.ui.fragment.DealsHomeFragment
import dagger.Component

/**
 * @author by jessica on 11/06/20
 */

@DealsHomeScope
@Component(modules = [DealsHomeModule::class, DealsHomeViewModelModule::class],
        dependencies = [DealsComponent::class])
interface DealsHomeComponent {

    fun inject(dealsHomeFragment: DealsHomeFragment)
}