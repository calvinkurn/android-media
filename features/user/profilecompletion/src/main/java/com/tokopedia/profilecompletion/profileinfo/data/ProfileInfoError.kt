package com.tokopedia.profilecompletion.profileinfo.data

sealed class ProfileInfoError {
    data class ErrorSavePhoto(val errorMsg: String?): ProfileInfoError()
    data class ErrorOthers(val errorMsg: String?): ProfileInfoError()
}