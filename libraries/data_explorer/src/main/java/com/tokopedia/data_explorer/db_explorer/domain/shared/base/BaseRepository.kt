package com.tokopedia.data_explorer.db_explorer.domain.shared.base

internal interface BaseRepository<InputModel : BaseParameters, OutputModel> {

    suspend fun getPage(input: InputModel): OutputModel = throw NotImplementedError()
}
