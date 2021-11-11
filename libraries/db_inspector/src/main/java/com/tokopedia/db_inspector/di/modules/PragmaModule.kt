package com.tokopedia.db_inspector.di.modules

import com.tokopedia.db_inspector.data.Sources
import com.tokopedia.db_inspector.data.sources.cursor.PragmaSource
import com.tokopedia.db_inspector.domain.Control
import com.tokopedia.db_inspector.domain.Converters
import com.tokopedia.db_inspector.domain.Interactors
import com.tokopedia.db_inspector.domain.Mappers
import com.tokopedia.db_inspector.domain.databases.Repositories
import com.tokopedia.db_inspector.domain.pragma.PragmaRepository
import com.tokopedia.db_inspector.domain.pragma.control.PragmaControl
import com.tokopedia.db_inspector.domain.pragma.control.converters.PragmaConverter
import com.tokopedia.db_inspector.domain.pragma.control.mappers.PragmaMapper
import com.tokopedia.db_inspector.domain.pragma.interactors.GetUserVersionInteractor
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
        interactors: Interactors.GetUserVersion,
        control: Control.Pragma
    ): Repositories.Pragma = PragmaRepository(interactors, control)

    @Provides
    fun providePragmaInteractor(source: Sources.Pragma): Interactors.GetUserVersion =
        GetUserVersionInteractor(source)

    @Provides
    fun providePragmaSource(): Sources.Pragma = PragmaSource()
}