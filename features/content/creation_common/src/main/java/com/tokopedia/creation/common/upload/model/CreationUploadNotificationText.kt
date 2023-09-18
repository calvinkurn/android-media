package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
data class CreationUploadNotificationText(
    val progressTitle: String,
    val progressDescription: String,
    val successTitle: String,
    val successDescription: String,
    val failTitle: String,
    val failDescription: String,
    val failRetryAction: String,
)
