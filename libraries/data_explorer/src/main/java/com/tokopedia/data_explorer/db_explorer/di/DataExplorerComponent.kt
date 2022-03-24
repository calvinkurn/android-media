package com.tokopedia.data_explorer.db_explorer.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.data_explorer.db_explorer.di.modules.*
import com.tokopedia.data_explorer.db_explorer.presentation.content.bottomsheet.ColumnPickerBottomSheet
import com.tokopedia.data_explorer.db_explorer.presentation.content.ContentFragment
import com.tokopedia.data_explorer.db_explorer.presentation.databases.DatabaseListFragment
import com.tokopedia.data_explorer.db_explorer.presentation.schema.SchemaFragment
import dagger.Component

@DataExplorerScope
@Component(
    modules = [
        DatabaseModule::class,
        ConnectionModule::class,
        PragmaModule::class,
        ContentModule::class,
        ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DataExplorerComponent {
    fun inject(databaseListFragment: DatabaseListFragment)
    fun inject(schemaFragment: SchemaFragment)
    fun inject(contentFragment: ContentFragment)
    fun inject(columnPickerBottomSheet: ColumnPickerBottomSheet)

}