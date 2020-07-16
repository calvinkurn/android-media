package com.tokopedia.oneclickcheckout.common.domain

import com.tokopedia.oneclickcheckout.common.view.model.preference.PreferenceListResponseModel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FakeGetPreferenceListUseCase : GetPreferenceListUseCase(TestCoroutineDispatcher(), TestCoroutineDispatcher()) {

    var onWait: (() -> Unit)? = null
    var onFinish: (() -> PreferenceListResponseModel)? = null

    private var continuation: Continuation<PreferenceListResponseModel>? = null

    override suspend fun executeOnBackground(): PreferenceListResponseModel {
        return suspendCoroutine {
            if (onFinish != null) {
                onWait?.invoke()
                onFinish?.invoke()
            } else {
                continuation = it
            }
        }
    }

    fun invokeOnSuccess(preferenceListResponseModel: PreferenceListResponseModel) {
        continuation?.resume(preferenceListResponseModel)
        continuation = null
    }

    fun invokeOnError(throwable: Throwable) {
        continuation?.resumeWithException(throwable)
        continuation = null
    }
}