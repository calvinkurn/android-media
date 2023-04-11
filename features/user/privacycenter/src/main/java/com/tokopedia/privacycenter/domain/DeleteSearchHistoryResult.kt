package com.tokopedia.privacycenter.domain

sealed class DeleteSearchHistoryResult(
    val position: Int = -1,
    val isClearAll: Boolean = false,
    val throwable: Throwable? = null
) {
    class Success(position: Int, isClearAll: Boolean) : DeleteSearchHistoryResult(
        position = position,
        isClearAll = isClearAll
    )
    class Failed(position: Int, isClearAll: Boolean, throwable: Throwable? = null) : DeleteSearchHistoryResult(
        position = position,
        isClearAll = isClearAll,
        throwable = throwable
    )
}
