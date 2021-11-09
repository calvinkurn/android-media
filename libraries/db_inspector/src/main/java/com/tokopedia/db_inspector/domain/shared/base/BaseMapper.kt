package com.tokopedia.db_inspector.domain.shared.base

internal interface BaseMapper<Input, Output> {

    suspend operator fun invoke(model: Input): Output = throw NotImplementedError()
}
