package com.tokopedia.review.feature.createreputation.domain

import java.io.Serializable

sealed interface RequestState<out T : Any?, out P : Any?> : Serializable {
    interface CompleteRequestState<out T : Any?, out P : Any?> : RequestState<T, P>
    object Idle : RequestState<Nothing, Nothing>
    data class Requesting<P : Any?>(
        val params: P? = null
    ) : RequestState<Nothing, P>
    data class Success<T : Any?, P : Any?>(
        val result: T,
        val params: P? = null
    ) : CompleteRequestState<T, P>
    data class Error<P : Any?>(
        val throwable: Throwable,
        val params: P? = null
    ) : CompleteRequestState<Nothing, P>
}