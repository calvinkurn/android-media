package com.tokopedia.db_inspector.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.sources.raw.DatabaseSourceImpl
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.databases.DatabaseRepository
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.databases.control.DatabaseControl
import com.tokopedia.db_inspector.domain.databases.control.converter.DatabaseConverter
import com.tokopedia.db_inspector.domain.databases.control.mapper.DatabaseMapper
import com.tokopedia.db_inspector.domain.databases.interactors.GetDatabaseInteractor
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
        interactors: Interactors.GetDatabases,
        control: Control.Database
    ): Repositories.Database = DatabaseRepository(interactors, control)

    @Provides
    fun provideDatabaseInteractor(source: Sources.Raw): Interactors.GetDatabases =
        GetDatabaseInteractor(source)

    @Provides
    fun provideDatabaseSource(@ApplicationContext context: Context): Sources.Raw =
        DatabaseSourceImpl(context)
}