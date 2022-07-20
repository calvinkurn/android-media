package com.tokopedia.picker.common.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository<P, R> constructor(
    private val dispatcher: CoroutineDispatcher
) {

    abstract fun execute(param: P): R

    suspend operator fun invoke(params: P): R {
        return withContext(dispatcher) {
            execute(params)
        }
    }

}