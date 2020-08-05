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

    fun isDataError(): Boolean {
        return (data.success == 0 || !status.equals("OK", true)) && (data.message.isNotEmpty() || errorMessage.isNotEmpty())
    }

    fun getAtcErrorMessage(): String? {
        return errorMessage.firstOrNull() ?: data.message.firstOrNull()
    }
    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"
    }
}