package com.tokopedia.play.broadcaster.util.cover

import android.net.Uri

/**
 * Created by jegul on 18/06/20
 */
interface ImageTransformer {

    fun transformImageFromUri(uri: Uri): Uri

    fun parseToUri(id: String): Uri
}
