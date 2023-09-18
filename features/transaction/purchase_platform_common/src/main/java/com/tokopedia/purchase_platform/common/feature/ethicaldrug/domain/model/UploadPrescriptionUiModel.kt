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
    val isIncompletePrescriptionError: Boolean = false,
    val productErrorCount: Int = 0,
    var frontEndValidation: Boolean = false,
    var consultationFlow: Boolean = false,
    var rejectedWording: String = "",
    var hasInvalidPrescription: Boolean = false,
    var isOcc: Boolean = false,

    // data for trackers
    var enablerNames: List<String> = emptyList(),
    var shopIds: List<String> = emptyList(),
    var cartIds: List<String> = emptyList(),

    var hasShowAnimation: Boolean = false
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
}
