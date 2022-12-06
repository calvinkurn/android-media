package com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadPrescriptionUiModel(
    var showImageUpload: Boolean = false,
    var uploadImageText: String = "",
    var leftIconUrl: String = "",
    var checkoutId: String = "",

    var epharmacyGroupIds: ArrayList<String> = arrayListOf(),
    var prescriptionIds: ArrayList<String> = arrayListOf(),
    var uploadedImageCount: Int = 0,
    var descriptionText: String = "",
    var isError: Boolean = false,
    var frontEndValidation: Boolean = false,
    var consultationFlow: Boolean = false,
    var rejectedWording: String = "",
    var hasInvalidPrescription: Boolean = false,
    var isOcc: Boolean = false,
) : Parcelable {

    fun getWidgetState(): String {
        return if (uploadedImageCount == 0) {
            if (hasInvalidPrescription) {
                "failed"
            } else {
                "empty"
            }
        } else {
            "success"
        }
    }

    fun getPayState(hasCompletePrescription: Boolean): String {
        return if (hasCompletePrescription) {
            "success state"
        } else {
            if (uploadedImageCount == 0) {
                "failed state no prescription"
            } else {
                "failed state with prescription"
            }
        }
    }
}
