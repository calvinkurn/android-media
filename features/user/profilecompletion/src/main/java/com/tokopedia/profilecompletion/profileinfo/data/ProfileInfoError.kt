package com.tokopedia.profilecompletion.profileinfo.data

/* use ErrorSavePhoto to handle error when save the phot, otherwise can use general error */
sealed class ProfileInfoError {
    data class ErrorSavePhoto(val errorMsg: String?): ProfileInfoError()
    data class GeneralError(val errorMsg: String?): ProfileInfoError()
}