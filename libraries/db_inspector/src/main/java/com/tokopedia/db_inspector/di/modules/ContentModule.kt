package com.tokopedia.db_inspector.di.modules

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.sources.cursor.SchemaSource
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.schema.TableRepository
import com.tokopedia.db_inspector.domain.schema.control.SchemaControl
import com.tokopedia.db_inspector.domain.schema.control.converter.ContentConverter
import com.tokopedia.db_inspector.domain.schema.control.mapper.ContentMapper
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