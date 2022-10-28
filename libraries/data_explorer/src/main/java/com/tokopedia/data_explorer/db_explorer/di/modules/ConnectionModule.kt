package com.tokopedia.data_explorer.db_explorer.di.modules

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.sources.connection.AndroidConnectionSource
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.connection.ConnectionRepository
import com.tokopedia.data_explorer.db_explorer.domain.connection.control.ConnectionControl
import com.tokopedia.data_explorer.db_explorer.domain.connection.control.converters.ConnectionConverter
import com.tokopedia.data_explorer.db_explorer.domain.connection.control.mappers.ConnectionMapper
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import dagger.Module
import dagger.Provides

@Module
internal class ConnectionModule {

    @Provides
    fun provideConnectionMappers(): Mappers.Connection = ConnectionMapper()

    @Provides
    fun provideConnectionConverter(): Converters.Connection = ConnectionConverter()

    @Provides
    fun provideConnectionControl(
        mappers: Mappers.Connection,
        converter: Converters.Connection
    ): Control.Connection = ConnectionControl(mappers, converter)

    @Provides
    fun provideConnectionRepository(
        source: Sources.Memory,
        control: Control.Connection
    ): Repositories.Connection = ConnectionRepository(source, control)

    @Provides
    fun provideConnectionSource(): Sources.Memory = AndroidConnectionSource()
}