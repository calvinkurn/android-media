package com.tokopedia.feedback_form.feedbackpage.domain.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedbackFormRequest(
        @SerializedName("platform_id")
        var platformID: Long?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("app_version")
        var appVersion: String?,
        @SerializedName("bundle_version")
        var bundleVersion: String?,
        @SerializedName("device")
        var device: String?,
        @SerializedName("os")
        var os: String?,
        @SerializedName("tokopedia_userid")
        var tokopediaUserID: String?,
        @SerializedName("tokopedia_email")
        var tokopediaEmail: String?,
        @SerializedName("session_token")
        var sessionToken: String?,
        @SerializedName("fcm_token")
        var fcmToken: String?,
        @SerializedName("login_state")
        var loginState: String?,
        @SerializedName("last_accessed_page")
        var lastAccessedPage: String?,
        @SerializedName("category")
        var category: Int?,
        @SerializedName("journey")
        var journey: String?,
        @SerializedName("expected")
        var expected: String?,
        @SerializedName("labelIDs")
        var labelsId: ArrayList<Long>,
        @SerializedName("type")
        var type: Int?,
        @SerializedName("detail")
        var detail: String?
) : Parcelable
