package com.tokopedia.campaign.entity

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Failure<T>(val error: Throwable) : Result<T>()
}