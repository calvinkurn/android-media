package com.tokopedia.search.result.domain.usecase

class TestSearchUseCase<T: Any>(
        var data: T
): SearchUseCase<T>() {

    override suspend fun executeOnBackground(): T {
        return data
    }
}