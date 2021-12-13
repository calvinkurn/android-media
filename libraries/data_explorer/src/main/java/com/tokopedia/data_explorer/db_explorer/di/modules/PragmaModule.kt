package com.tokopedia.data_explorer.db_explorer.di.modules

import com.tokopedia.data_explorer.db_explorer.data.Sources
import com.tokopedia.data_explorer.db_explorer.data.sources.cursor.PragmaSource
import com.tokopedia.data_explorer.db_explorer.domain.Control
import com.tokopedia.data_explorer.db_explorer.domain.Converters
import com.tokopedia.data_explorer.db_explorer.domain.Mappers
import com.tokopedia.data_explorer.db_explorer.domain.databases.Repositories
import com.tokopedia.data_explorer.db_explorer.domain.pragma.PragmaRepository
import com.tokopedia.data_explorer.db_explorer.domain.pragma.control.PragmaControl
import com.tokopedia.data_explorer.db_explorer.domain.pragma.control.converters.PragmaConverter
import com.tokopedia.data_explorer.db_explorer.domain.pragma.control.mappers.PragmaMapper
import dagger.Module
import dagger.Provides

@Module
internal class PragmaModule {

    @Provides
    fun providePragmaMappers(): Mappers.Pragma = PragmaMapper()

    @Provides
    fun providePragmaConverter(): Converters.Pragma = PragmaConverter()

    @Provides
    fun providePragmaControl(
        mappers: Mappers.Pragma,
        converter: Converters.Pragma
    ): Control.Pragma = PragmaControl(mappers, converter)

    @Provides
    fun providePragmaRepository(
        source: Sources.Pragma,
        control: Control.Pragma
    ): Repositories.Pragma = PragmaRepository(source, control)

    @Provides
    fun providePragmaSource(): Sources.Pragma = PragmaSource()
}