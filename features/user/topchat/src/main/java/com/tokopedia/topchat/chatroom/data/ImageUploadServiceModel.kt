package com.tokopedia.topchat.chatroom.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUploadServiceModel (
        var messageId: String,
        var fromUid: String,
        var from: String,
        var fromRole: String,
        var attachmentId: String,
        var attachmentType: String,
        var replyTime: String,
        var startTime: String,
        var isRead: Boolean,
        var isDummy: Boolean,
        var isSender: Boolean,
        var message: String,
        var source: String,
        var imageUrl: String,
        var imageUrlThumbnail: String,
        var isRetry: Boolean
): Parcelable