package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */
@Parcelize
data class AddToCartDataModel(
    var errorMessage: ArrayList<String> = arrayListOf(),
    var status: String = "",
    var data: DataModel = DataModel(),
    var errorReporter: ErrorReporterModel = ErrorReporterModel(),
    var responseJson: String = ""
) : Parcelable {

    /*
     This method is for checking if ATC error and has message from backend
     Note: This is useful for ATC OCS when OVO validation failed and need custom error handling
     For tracking, it is recommended to use isStatusError() instead
     */
    fun isDataError(): Boolean {
        return (data.success == 0 || !status.equals("OK", true)) && (data.message.isNotEmpty() || errorMessage.isNotEmpty())
    }

    /*
     This method is for checking if ATC error without capability of custom error handling when no message provided from backend
     This is recommended for sending tracking
     */
    fun isStatusError(): Boolean {
        return (data.success == 0 || !status.equals("OK", true))
    }

    fun getAtcErrorMessage(): String? {
        return errorMessage.firstOrNull() ?: data.message.firstOrNull()
    }

    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"
    }
}