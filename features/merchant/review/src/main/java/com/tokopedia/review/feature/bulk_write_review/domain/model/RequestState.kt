package com.tokopedia.review.feature.bulk_write_review.domain.model

sealed interface RequestState<out Output : Any> {
    object Requesting : RequestState<Nothing>
    sealed interface Complete<out Output : Any> : RequestState<Output> {
        data class Success<out Output : Any>(
            val result: Output
        ) : Complete<Output>
        data class Error(val throwable: Throwable?) : Complete<Nothing>
    }
}

