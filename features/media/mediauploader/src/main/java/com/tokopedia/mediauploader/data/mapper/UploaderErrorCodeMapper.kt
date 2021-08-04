package com.tokopedia.mediauploader.data.mapper

import com.tokopedia.mediauploader.UploaderManager.Companion.FILE_NOT_FOUND
import com.tokopedia.mediauploader.UploaderManager.Companion.SOURCE_NOT_FOUND
import com.tokopedia.mediauploader.domain.UploaderUseCase.Companion.NETWORK_ERROR
import com.tokopedia.mediauploader.domain.UploaderUseCase.Companion.TIMEOUT_ERROR

/*
* This is class didn't used for local mediauploader,
* the reason created this class because some of feature module
* need to track the cause of error of image uploader,
* that's why we want to provide an unify errorCode along side
* the common state provided by mediauploader.
* */
object UploaderErrorCodeMapper {

    fun state(message: String): CommonErrorState {
        return when (message) {
            FILE_NOT_FOUND -> CommonErrorState.FileNotFound
            SOURCE_NOT_FOUND -> CommonErrorState.InvalidSourceId
            NETWORK_ERROR -> CommonErrorState.NetworkError
            TIMEOUT_ERROR -> CommonErrorState.TimeOut
            else -> CommonErrorState.UnknownError
        }
    }

    sealed class CommonErrorState {
        object FileNotFound : CommonErrorState()
        object TimeOut : CommonErrorState()
        object NetworkError : CommonErrorState()
        object InvalidSourceId : CommonErrorState()
        object UnknownError : CommonErrorState()
    }

}
