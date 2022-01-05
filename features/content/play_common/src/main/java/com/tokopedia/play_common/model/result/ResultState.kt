package com.tokopedia.play_common.model.result

enum class ResultState {
    Success,
    Loading,
    Fail;

    val isSuccess: Boolean
        get() = this == Success

    val isLoading: Boolean
        get() = this == Loading

    val isFail: Boolean
        get() = this == Fail
}