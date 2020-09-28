package com.tokopedia.play.broadcaster.util.cover

import android.graphics.Bitmap
import android.net.Uri
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayGalleryImagePickerBottomSheet
import java.io.File

/**
 * Created by jegul on 18/06/20
 */
class PlayMinimumCoverImageTransformer : ImageTransformer {

    override fun transformImageFromUri(uri: Uri): Uri {
        val imageFile = File(uri.path)
        val isPng = ImageUtils.isPng(imageFile.absolutePath)
        val imageBitmap = ImageUtils.getBitmapFromPath(imageFile.absolutePath, ImageUtils.DEF_WIDTH,
                ImageUtils.DEF_HEIGHT, false)
        var newBitmap: Bitmap? = null
        val newImageFile = try {
            if (imageBitmap.width < PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_WIDTH || imageBitmap.height < PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_HEIGHT) {
                newBitmap = Bitmap.createScaledBitmap(imageBitmap, PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_WIDTH, PlayGalleryImagePickerBottomSheet.MINIMUM_COVER_HEIGHT, false)
            }
            ImageUtils.writeImageToTkpdPath(
                    ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA,
                    newBitmap ?: imageBitmap, isPng)
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        } finally {
            imageBitmap?.recycle()
            newBitmap?.recycle()
            System.gc()
        }

        return newImageFile?.let {
            imageFile.delete()
            Uri.fromFile(it)
        } ?: uri
    }
}