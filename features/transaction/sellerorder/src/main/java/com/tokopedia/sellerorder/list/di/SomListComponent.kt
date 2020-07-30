package com.tokopedia.sellerorder.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomListActivity
import com.tokopedia.sellerorder.list.presentation.fragment.SomFilterFragment
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-08-28.
 */

@SomListScope
@Component(modules = [SomListViewModelModule::class], dependencies = [SomComponent::class])
interface SomListComponent {

    fun inject(somListActivity: SomListActivity)
    fun inject(somListFragment: SomListFragment)
    fun inject(somFilterFragment: SomFilterFragment)
}