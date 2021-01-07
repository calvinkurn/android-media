package com.tokopedia.play.broadcaster.util.cover

import android.graphics.Bitmap
import android.net.Uri
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import com.tokopedia.utils.image.ImageUtil
import java.io.File

/**
 * Created by jegul on 18/06/20
 */
class PlayMinimumCoverImageTransformer : ImageTransformer {

    override fun transformImageFromUri(uri: Uri): Uri {
        val imageFile = File(uri.path)
        val isPng = ImageUtil.isPng(imageFile.absolutePath)
        val imageBitmap = ImageUtil.getBitmapFromPath(imageFile.absolutePath, ImageUtil.DEF_WIDTH,
                ImageUtil.DEF_HEIGHT, false) ?: return uri
        var newBitmap: Bitmap? = null
        val newImageFile = try {
            if (imageBitmap.width < PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_WIDTH || imageBitmap.height < PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_HEIGHT) {
                newBitmap = Bitmap.createScaledBitmap(imageBitmap, PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_WIDTH, PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_HEIGHT, false)
            }
            ImageUtil.writeImageToTkpdPath(
                    newBitmap ?: imageBitmap, isPng)
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        } finally {
            imageBitmap.recycle()
            newBitmap?.recycle()
            System.gc()
        }

        return newImageFile?.let {
            imageFile.delete()
            Uri.fromFile(it)
        } ?: uri
    }
}