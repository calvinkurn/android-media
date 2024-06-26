package com.tokopedia.accountprofile.settingprofile.profileinfo.data

/* use ErrorSavePhoto to handle error when save the phot, otherwise can use general error */
sealed class ProfileInfoError {
    data class ErrorSavePhoto(val errorMsg: String?) : ProfileInfoError()
    data class GeneralError(val error: Throwable) : ProfileInfoError()
}
