package com.tokopedia.play.broadcaster.data.model

/**
 * Created by jegul on 06/07/20
 */
data class SerializableCoverData(
        val coverImageUriString: String,
        val coverTitle: String,
        val coverSource: String,
        val productId: Long?
)