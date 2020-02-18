package com.tokopedia.mediauploader

abstract class BaseUseCase<in T, P> {

    /**
     * Override this to set the code to be executed.
     */
    abstract suspend fun execute(params: T): P

    /** Executes the use case synchronously
     *
     * @param params the input parameters to run the use case with
     * @return an generic class comes from P
     *
     */
    suspend operator fun invoke(params: T): P {
        return execute(params)
    }

}