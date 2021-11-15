package com.tokopedia.db_inspector.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.db_inspector.di.modules.*
import com.tokopedia.db_inspector.presentation.databases.DatabaseListFragment
import com.tokopedia.db_inspector.presentation.schema.SchemaFragment
import dagger.Component

@DbScope
@Component(
    modules = [
        DatabaseModule::class,
        ConnectionModule::class,
        PragmaModule::class,
        ContentModule::class,
        ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DbInspectorComponent {
    //fun inject(dbInspectorActivity: DbInspectorActivity)
    fun inject(databaseListFragment: DatabaseListFragment)
    fun inject(schemaFragment: SchemaFragment)
}