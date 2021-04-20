package com.tokopedia.feedback_form.feedbackpage.domain.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedbackFormRequest(
        @SerializedName("platformID")
        var platformID: Int?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("appVersion")
        var appVersion: String?,
        @SerializedName("bundleVersion")
        var bundleVersion: String?,
        @SerializedName("device")
        var device: String?,
        @SerializedName("os")
        var os: String?,
        @SerializedName("tokopediaUserID")
        var tokopediaUserID: String?,
        @SerializedName("tokopediaEmail")
        var tokopediaEmail: String?,
        @SerializedName("sessionToken")
        var sessionToken: String?,
        @SerializedName("fcmToken")
        var fcmToken: String?,
        @SerializedName("loginState")
        var loginState: String?,
        @SerializedName("lastAccessedPage")
        var lastAccessedPage: String?,
        @SerializedName("category")
        var category: Int?,
        @SerializedName("journey")
        var journey: String?,
        @SerializedName("expected")
        var expected: String?,
        @SerializedName("labelIDs")
        var labelsId: ArrayList<Int>,
        @SerializedName("type")
        var type: Int?,
        @SerializedName("detail")
        var detail: String?
) : Parcelable