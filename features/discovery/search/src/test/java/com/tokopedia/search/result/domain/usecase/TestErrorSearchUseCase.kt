package com.tokopedia.search.result.domain.usecase

class TestErrorSearchUseCase<T: Any, E: Throwable>(
        val error: E
): SearchUseCase<T>() {

    override suspend fun executeOnBackground(): T {
        throw error
    }
}