package com.tokopedia.kyc_centralized.view.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by alvinatin on 21/11/18.
 */
@Parcelize
data class AttachmentImageModel (
    @SerializedName("picture_obj")
    @Expose
    val pictureObj: String = ""
): Parcelable