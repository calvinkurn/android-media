package com.tokopedia.media.loader.data

import com.bumptech.glide.load.Key
import com.bumptech.glide.signature.ObjectKey

object Signature {

    fun adaptiveSignature(url: String, connectionType: String): Key {
        return ObjectKey("$url&connection=$connectionType")
    }

}