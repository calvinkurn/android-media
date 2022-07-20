package com.tokopedia.data_explorer.db_explorer.domain.shared.base

internal interface BaseConverter<Input : BaseParameters, Output> {

    suspend operator fun invoke(parameters: Input): Output = throw NotImplementedError()
}
