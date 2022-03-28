package com.tokopedia.data_explorer.db_explorer.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.sources.raw.DatabaseSourceImpl
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.databases.DatabaseRepository
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.databases.control.DatabaseControl
import com.tokopedia.data_explorer.db_explorer.domain.databases.control.converter.DatabaseConverter
import com.tokopedia.data_explorer.db_explorer.domain.databases.control.mapper.DatabaseMapper
import dagger.Module
import dagger.Provides

@Module
internal class DatabaseModule {

    @Provides
    fun provideDatabaseMappers(): Mappers.Database = DatabaseMapper()

    @Provides
    fun provideDatabaseConverter(): Converters.Database = DatabaseConverter()

    @Provides
    fun provideDatabaseControl(
        mappers: Mappers.Database,
        converter: Converters.Database
    ): Control.Database = DatabaseControl(mappers, converter)

    @Provides
    fun provideDatabaseRepository(
        source: Sources.Raw,
        control: Control.Database
    ): Repositories.Database = DatabaseRepository(source, control)

    @Provides
    fun provideDatabaseSource(@ApplicationContext context: Context): Sources.Raw =
        DatabaseSourceImpl(context)
}