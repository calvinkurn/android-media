package com.tokopedia.sellerorder.oldlist.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.oldlist.presentation.activity.SomListActivity
import com.tokopedia.sellerorder.oldlist.presentation.fragment.SomFilterFragment
import com.tokopedia.sellerorder.oldlist.presentation.fragment.SomListFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-08-28.
 */

@SomListScope
@Component(modules = [SomListViewModelModule::class, SomListModule::class], dependencies = [SomComponent::class])
interface SomListComponent {

    fun inject(somListActivity: SomListActivity)
    fun inject(somListFragment: SomListFragment)
    fun inject(somFilterFragment: SomFilterFragment)
}