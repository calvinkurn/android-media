package com.tokopedia.db_inspector.domain.shared.base

internal interface BaseRepository<InputModel : BaseParameters, OutputModel> {

    suspend fun getPage(input: InputModel): OutputModel = throw NotImplementedError()
}
