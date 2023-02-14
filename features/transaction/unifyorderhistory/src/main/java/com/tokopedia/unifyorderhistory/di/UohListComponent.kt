package com.tokopedia.unifyorderhistory.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.unifyorderhistory.view.activity.UohListActivity
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment
import dagger.Component

/**
 * Created by fwidjaja on 04/07/20.
 */

@UohListScope
@Component(modules = [UohListModule::class, UohListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface UohListComponent {

    fun inject(fragment: UohListFragment)
    fun inject(activity: UohListActivity)
}
