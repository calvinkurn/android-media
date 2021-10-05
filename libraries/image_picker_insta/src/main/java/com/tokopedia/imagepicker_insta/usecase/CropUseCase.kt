package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.PhotosData
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import com.tokopedia.imagepicker_insta.util.CameraUtil
import javax.inject.Inject
import kotlin.math.roundToInt

class CropUseCase @Inject constructor() {

    suspend fun cropPhotos(context: Context, imageSize: Int, pairList: List<Pair<ImageAdapterData, ZoomInfo>>) :List<Uri>{
        val uriList = arrayListOf<Uri>()
        pairList.forEach {
            if (it.second.hasChanged()) {
                val asset = it.first.asset
                if (asset is PhotosData) {
                    val drawable = Glide.with(context)
                        .asBitmap()
                        .load(asset.contentUri)
                        .apply(RequestOptions().override(it.second.bmpWidth!!,it.second.bmpHeight!!))
                        .submit()
                        .get()
                    val bmp = createTempBitmap(drawable, it.second, imageSize)
                    val file = CameraUtil.createMediaFile(context, isImage = true, storeInCache = true)
                    val fos = file.outputStream()
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                    uriList.add(Uri.fromFile(file))
                } else {
                    uriList.add(it.first.asset.contentUri)
                }
            } else {
                uriList.add(it.first.asset.contentUri)
            }
        }
        return uriList
    }

    @Throws(Exception::class)
    private suspend fun createTempBitmap(bmp: Bitmap?, zoomInfo: ZoomInfo, size: Int): Bitmap {
        if (bmp != null && zoomInfo.hasData()) {
            val translationX: Float = zoomInfo.panX!! * zoomInfo.scale!!
            val translationY: Float = zoomInfo.panY!! * zoomInfo.scale!!

            val x: Float = if (translationX > 0f) 0f else -translationX
            val y: Float = if (translationY > 0f) 0f else -translationY
            val left: Int = (x / zoomInfo.scale!!).toInt()
            val top: Int = (y / zoomInfo.scale!!).toInt()
            var newWidth: Int = (size / zoomInfo.scale!!).roundToInt()
            var newHeight: Int = newWidth

            if (top == 0) {
                newHeight = bmp.height
            }

            //vertical image
            if (left == 0) {
                newHeight = (bmp.height / zoomInfo.scale!!).toInt()
                newWidth = bmp.width
            }

            //INVALID
            if ((left + newWidth > bmp.width) || top + newHeight > bmp.height) throw Exception("Invalid params")

            return Bitmap.createBitmap(bmp, left, top, newWidth, newHeight)

        }
        throw Exception("Unable to create bitmap")
    }
}