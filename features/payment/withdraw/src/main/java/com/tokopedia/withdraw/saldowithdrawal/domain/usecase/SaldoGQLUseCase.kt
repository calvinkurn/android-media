package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

open class SaldoGQLUseCase<T : Any> @Inject constructor(
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<T>(graphqlRepository) {

    private var job: Job = Job()
    private val defaultDispatchers: CoroutineDispatcher = Dispatchers.Default

    private suspend fun executeOnIO(): Result<T> = withContext(defaultDispatchers +
            job + coroutineExceptionHandler) {
        try {
            Success(executeOnBackground())
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun cancelJob() {
        if (job.isActive) {
            job.cancel()
            job = Job()
        }
    }

    suspend fun executeUseCase(): Result<T> {
        cancelJob()
        return executeOnIO()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

}