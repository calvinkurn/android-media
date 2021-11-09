package com.tokopedia.db_inspector.domain.shared.base

internal interface BaseInteractor<InputModel, OutputModel> {

    suspend operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()

}
