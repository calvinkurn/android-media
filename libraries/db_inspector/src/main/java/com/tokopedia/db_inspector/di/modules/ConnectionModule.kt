package com.tokopedia.db_inspector.di.modules

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.sources.connection.AndroidConnectionSource
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.connection.ConnectionRepository
import com.tokopedia.db_inspector.domain.connection.control.ConnectionControl
import com.tokopedia.db_inspector.domain.connection.control.converters.ConnectionConverter
import com.tokopedia.db_inspector.domain.connection.control.mappers.ConnectionMapper
import com.tokopedia.db_inspector.domain.databases.Repositories
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