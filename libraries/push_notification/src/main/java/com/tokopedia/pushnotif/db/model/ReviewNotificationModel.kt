package com.tokopedia.pushnotif.db.model

import android.net.Uri
import com.tokopedia.pushnotif.model.ApplinkNotificationModel

data class ReviewNotificationModel(
        val applinkNotificationModel: ApplinkNotificationModel = ApplinkNotificationModel(),
        val notificationType: Int = 0,
        var notificationId: Int = 0
) {
    companion object {
        private const val PARAM_RATING = "rating"
    }

    fun getApplinkWithRating(rating: String): String {
        return Uri.parse(applinkNotificationModel.applinks)
                .buildUpon()
                .appendQueryParameter(PARAM_RATING, rating)
                .build()
                .toString()
    }
}