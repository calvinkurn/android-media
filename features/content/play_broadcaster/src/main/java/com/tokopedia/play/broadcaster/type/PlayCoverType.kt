package com.tokopedia.play.broadcaster.type

import android.net.Uri

sealed class PlayCoverType {

    abstract val uri: Uri

    data class Camera(override val uri: Uri) : PlayCoverType()
    data class Gallery(override val uri: Uri) : PlayCoverType()
    data class Product(override val uri: Uri) : PlayCoverType()
}