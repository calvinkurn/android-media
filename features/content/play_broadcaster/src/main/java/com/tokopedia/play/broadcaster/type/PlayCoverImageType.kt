package com.tokopedia.play.broadcaster.type

import android.net.Uri

sealed class PlayCoverImageType {

    data class Camera(val uri: Uri) : PlayCoverImageType()
    data class Gallery(val uri: Uri) : PlayCoverImageType()
    data class Product(val productId: Long, val imageUrl: String) : PlayCoverImageType()
}