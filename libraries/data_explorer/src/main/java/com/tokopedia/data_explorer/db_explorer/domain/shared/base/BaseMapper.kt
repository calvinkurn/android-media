package com.tokopedia.data_explorer.db_explorer.domain.shared.base

internal interface BaseMapper<Input, Output> {

    suspend operator fun invoke(model: Input): Output = throw NotImplementedError()
}
