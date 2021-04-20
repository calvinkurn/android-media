package com.tokopedia.ordermanagement.snapshot.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.ordermanagement.snapshot.view.fragment.SnapshotFragment
import dagger.Component

/**
 * Created by fwidjaja on 1/25/21.
 */

@SnapshotScope
@Component(modules = [SnapshotModule::class, SnapshotViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SnapshotComponent {
    fun context(): Context

    fun inject(uohListFragment: SnapshotFragment)
}