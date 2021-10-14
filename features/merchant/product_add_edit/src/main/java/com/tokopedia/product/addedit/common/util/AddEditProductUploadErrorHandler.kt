package com.tokopedia.product.addedit.common.util

import com.tokopedia.mediauploader.common.data.mapper.UploaderErrorCodeMapper.state as uploaderErrorState
import com.tokopedia.mediauploader.common.data.mapper.UploaderErrorCodeMapper.CommonErrorState
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import com.tokopedia.network.exception.MessageErrorException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object AddEditProductUploadErrorHandler {
    private const val ERROR_NO_INTERNET = "noInternet"
    private const val ERROR_PAGE_NOT_FOUND = "pageNotFound"
    private const val ERROR_UNDER_MAINTENANCE = "underMaintenance"
    private const val ERROR_FULL_VISITOR = "fullVisitor"
    private const val ERROR_UNKNOWN = "unknownError"

    private const val ERROR_UPLOADER_UPLOAD_FAILED = "uploadFailed"
    private const val ERROR_UPLOADER_FILE_UNAVAILABLE = "fileUnavailable"
    private const val ERROR_UPLOADER_SOURCE_UNAVAILABLE = "sourceUnavailable"
    private const val ERROR_UPLOADER_TIMEOUT = "timeout"
    private const val ERROR_UPLOADER_NETWORK_ERROR = "networkError"

    private const val MULTIPLE_DATA_SEPARATOR = "; "
    private const val MAX_LENGTH_ERROR_MESSAGE = 3

    fun getErrorName(e: Throwable?): String {
        return if (e is ResponseV4ErrorException) {
            e.errorList.firstOrNull() ?: ERROR_UNKNOWN
        } else if (e is MessageErrorException) {
            e.localizedMessage.orEmpty().replace("\n", MULTIPLE_DATA_SEPARATOR)
        } else if (e is UnknownHostException || e is SocketTimeoutException || e is ConnectException) {
            ERROR_NO_INTERNET
        } else if (e is RuntimeException && e.getLocalizedMessage() != null &&
                e.getLocalizedMessage() != "" && e.getLocalizedMessage().length <= MAX_LENGTH_ERROR_MESSAGE) {
            try {
                val code = e.getLocalizedMessage() ?: "0"
                when (code.toInt()) {
                    ResponseStatus.SC_NOT_FOUND -> {
                        ERROR_PAGE_NOT_FOUND
                    }
                    ResponseStatus.SC_SERVICE_UNAVAILABLE -> {
                        ERROR_UNDER_MAINTENANCE
                    }
                    ResponseStatus.SC_REQUEST_TIMEOUT, ResponseStatus.SC_GATEWAY_TIMEOUT ->{
                        ERROR_FULL_VISITOR
                    }
                    else -> ERROR_UNKNOWN
                }
            } catch (e1: NumberFormatException) {
                ERROR_UNKNOWN
            }
        } else {
            ERROR_UNKNOWN
        }
    }

    fun isValidationError(e: Throwable?): Boolean {
        return e is MessageErrorException
    }

    fun isServerTimeout(e: Throwable?): Boolean {
        return if (e is RuntimeException && e.getLocalizedMessage() != null &&
                e.getLocalizedMessage() != "" && e.getLocalizedMessage().length <= MAX_LENGTH_ERROR_MESSAGE) {
            try {
                val code = e.getLocalizedMessage() ?: "0"
                when (code.toInt()) {
                    ResponseStatus.SC_REQUEST_TIMEOUT ->{
                        true
                    }
                    else -> false
                }
            } catch (e1: NumberFormatException) {
                false
            }
        } else {
            false
        }
    }

    fun getUploadImageErrorName(messageFromUploader: String): String {
        return when (uploaderErrorState(messageFromUploader)) {
            is CommonErrorState.FileNotFound -> ERROR_UPLOADER_FILE_UNAVAILABLE
            is CommonErrorState.TimeOut -> ERROR_UPLOADER_TIMEOUT
            is CommonErrorState.NetworkError -> ERROR_UPLOADER_NETWORK_ERROR
            is CommonErrorState.InvalidSourceId -> ERROR_UPLOADER_SOURCE_UNAVAILABLE
            else -> {
                if (messageFromUploader.isEmpty()) {
                    ERROR_UPLOADER_UPLOAD_FAILED
                } else {
                    messageFromUploader
                }
            }
        }
    }
}