package com.tokopedia.common.topupbills.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common.topupbills.view.fragment.TopupBillsCheckoutFragment
import dagger.Component

/**
 * Created by resakemal on 12/08/19.
 */
@CommonTopupBillsScope
@Component(modules = arrayOf(CommonTopupBillsModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface CommonTopupBillsComponent {

    fun inject(topupBillsCheckoutFragment: TopupBillsCheckoutFragment)

}