package com.tokopedia.search.result

import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.*

internal fun UseCase<*>.stubExecute(): MockKStubScope<Any?, Any?> {
    val it = this

    stubExecuteToCallExecuteOnBackground()

    return coEvery { it.executeOnBackground() }
}

private fun UseCase<*>.stubExecuteToCallExecuteOnBackground() {
    val useCase = this

    every { useCase.execute(any(), any(), any()) } coAnswers {
        coAnswerExecute(useCase)
    }
}

private suspend fun MockKAnswerScope<Unit, Unit>.coAnswerExecute(useCase: UseCase<*>) {
    try {
        tryExecuteOnBackground(useCase)
    } catch (throwable: Throwable) {
        catchExecuteOnBackgroundException(throwable)
    }
}

private suspend fun MockKAnswerScope<Unit, Unit>.tryExecuteOnBackground(useCase: UseCase<*>) {
    val value = useCase.executeOnBackground()

    firstArg<(Any) -> Unit>().invoke(value)
}

private fun MockKAnswerScope<Unit, Unit>.catchExecuteOnBackgroundException(throwable: Throwable) {
    secondArg<(Throwable) -> Unit>().invoke(throwable)
}

internal fun UseCase<*>.isNeverExecuted() {
    return this.isExecuted(0)
}

internal fun UseCase<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.executeOnBackground() }
}