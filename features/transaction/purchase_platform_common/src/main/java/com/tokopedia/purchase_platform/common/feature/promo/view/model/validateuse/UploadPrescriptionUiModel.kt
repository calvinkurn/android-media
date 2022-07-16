package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadPrescriptionUiModel(
        var showImageUpload : Boolean? = false,
        var uploadImageText : String? = "",
        var leftIconUrl : String? = "",
        var checkoutId : String? = "",

        var prescriptionIds : ArrayList<String>?,
        var uploadedImageCount : Int? = 0,
        var descriptionText : String? = ""
) : Parcelable
