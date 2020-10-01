package com.tokopedia.buyerorder.unifiedhistory.list.di

import com.tokopedia.buyerorder.unifiedhistory.common.di.UohComponent
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import dagger.Component

/**
 * Created by fwidjaja on 04/07/20.
 */

@UohListScope
@Component(modules = [UohListViewModelModule::class], dependencies = [UohComponent::class])
interface UohListComponent {

    fun inject(uohListFragment: UohListFragment)
}