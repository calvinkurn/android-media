package com.tokopedia.play.broadcaster.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import java.io.ByteArrayOutputStream

/**
 * @author by furqan on 12/06/2020
 */
object PlayBroadcastCoverTitleUtil {
    const val MAX_LENGTH_LIVE_TITLE = 38
    private const val COVER_TITLE = "PlayCover"

    fun getBitmapFromImageView(imageView: ImageView): Bitmap =
            (imageView.drawable as BitmapDrawable).bitmap

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                COVER_TITLE,
                null
        )
        return Uri.parse(path.toString())
    }
}