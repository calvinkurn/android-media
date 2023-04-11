package com.tokopedia.autocompletecomponent

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.*

internal fun UseCase<*>.stubExecute(
    requestParamsSlot: CapturingSlot<RequestParams> = slot()
): MockKStubScope<Any?, Any?> {
    val it = this

    stubExecuteToCallExecuteOnBackground(requestParamsSlot)

    return coEvery { it.executeOnBackground() }
}

private fun UseCase<*>.stubExecuteToCallExecuteOnBackground(requestParamsSlot: CapturingSlot<RequestParams>) {
    val useCase = this

    every { useCase.execute(any(), any(), capture(requestParamsSlot)) } coAnswers {
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