package com.tokopedia.db_inspector.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.db_inspector.di.modules.ConnectionModule
import com.tokopedia.db_inspector.di.modules.DatabaseModule
import com.tokopedia.db_inspector.di.modules.ViewModelModule
import com.tokopedia.db_inspector.presentation.databases.DatabaseListFragment
import dagger.Component

@DbScope
@Component(
    modules = [
        DatabaseModule::class,
        ConnectionModule::class,
        ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DbInspectorComponent {
    //fun inject(dbInspectorActivity: DbInspectorActivity)
    fun inject(databaseListFragment: DatabaseListFragment)
}