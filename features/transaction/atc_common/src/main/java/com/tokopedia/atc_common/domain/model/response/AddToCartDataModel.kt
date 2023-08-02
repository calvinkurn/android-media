package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class DataModel(
    var success: Int = 0,
    var cartId: String = "",
    var productId: String = "",
    var quantity: Int = 0,
    var notes: String = "",
    var shopId: String = "",
    var customerId: String = "",
    var warehouseId: String = "",
    var trackerAttribution: String = "",
    var trackerListName: String = "",
    var ucUtParam: String = "",
    var isTradeIn: Boolean = false,
    var message: ArrayList<String> = arrayListOf(),
    var ovoValidationDataModel: OvoValidationDataModel = OvoValidationDataModel(),
    var refreshPrerequisitePage: Boolean = false
) : Parcelable

@Parcelize
data class ErrorReporterModel(
    var eligible: Boolean = false,
    var texts: ErrorReporterTextModel = ErrorReporterTextModel()
) : Parcelable

@Parcelize
data class ErrorReporterTextModel(
    var submitTitle: String = "",
    var submitDescription: String = "",
    var submitButton: String = "",
    var cancelButton: String = ""
) : Parcelable

@Parcelize
data class OvoValidationDataModel(
    var status: Int = 0,
    var applink: String = "",
    var ovoInsufficientBalance: OvoInsufficientBalance = OvoInsufficientBalance()
) : Parcelable

@Parcelize
data class OvoInsufficientBalance(
    var title: String = "",
    var description: String = "",
    var details: OvoInsufficientDetails = OvoInsufficientDetails(),
    var buttons: OvoInsufficientButton = OvoInsufficientButton()
) : Parcelable

@Parcelize
data class OvoInsufficientDetails(
    var productPrice: Long = 0,
    var shippingEstimation: Int = 0,
    var ovoBalance: Int = 0,
    var topupBalance: Int = 0
) : Parcelable

@Parcelize
data class OvoInsufficientButton(
    var topupButton: OvoInsufficientTopup = OvoInsufficientTopup(),
    var otherMethodButton: OvoInsufficientTopup = OvoInsufficientTopup()
) : Parcelable

@Parcelize
data class OvoInsufficientTopup(
    var text: String = "",
    var applink: String = "",
    var enable: Boolean = false
) : Parcelable
