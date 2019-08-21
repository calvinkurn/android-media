package com.tokopedia.search.result.domain.usecase

class TestSearchUseCase<T: Any>(
        var data: T
): SearchUseCase<T>() {

    var numberOfExecution = 0

    override suspend fun executeOnBackground(): T {
        numberOfExecution++
        return data
    }
}