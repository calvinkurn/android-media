package com.tokopedia.data_explorer.db_explorer.di.modules

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.sources.cursor.SchemaSource
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.schema.TableRepository
import com.tokopedia.data_explorer.db_explorer.domain.schema.control.SchemaControl
import com.tokopedia.data_explorer.db_explorer.domain.schema.control.converter.ContentConverter
import com.tokopedia.data_explorer.db_explorer.domain.schema.control.mapper.ContentMapper
import dagger.Module
import dagger.Provides

@Module
internal class ContentModule {

    @Provides
    fun provideContentMappers(): Mappers.Content = ContentMapper()

    @Provides
    fun provideContentConverter(): Converters.Content = ContentConverter()

    @Provides
    fun provideContentControl(
        mappers: Mappers.Content,
        converter: Converters.Content
    ): Control.Content = SchemaControl(mappers, converter)

    @Provides
    fun provideContentRepository(
        source: Sources.Local.Schema,
        control: Control.Content
    ): Repositories.Schema = TableRepository(source, control)

    @Provides
    fun provideSchemaSource(): Sources.Local.Schema = SchemaSource()
}