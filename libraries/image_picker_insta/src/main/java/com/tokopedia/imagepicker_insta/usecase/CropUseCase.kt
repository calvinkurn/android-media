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
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.min

class CropUseCase @Inject constructor() {

     fun cropPhotos(
        context: Context,
        width: Int,
        height: Int,
        pairList: List<Pair<ImageAdapterData, ZoomInfo>>
    ): List<Uri> {
        val uriList = arrayListOf<Uri>()
        pairList.forEach {
            if (it.second.hasChanged()) {
                val asset = it.first.asset
                if (asset is PhotosData) {

                    val w = it.second.bmpWidth!!
                    val h = it.second.bmpHeight!!

                    var mBmp = Glide.with(context)
                        .asBitmap()
                        .load(asset.contentUri)
                        .apply(RequestOptions().override(w, h))
                        .submit()
                        .get()


                    if (mBmp.width != w || mBmp.height != h) {
                        val tmpBitmap = Bitmap.createScaledBitmap(mBmp,w,h,false)
                        mBmp = tmpBitmap

                        if (mBmp.width != w || mBmp.height != h) {
                            throw Exception("Unable to get specified bitmap")
                        }
                    }

                    Timber.d(
                        "createTempBitmap: uri = ${asset?.contentUri}," +
                                "scale=${it.second?.scale}," +
                                "matrix=${it.second?.matrix}," +
                                "bmpW=${it.second?.bmpWidth}," +
                                "bmpH=${it.second?.bmpHeight}," +
                                "left=${it.second?.rectF?.left}," +
                                "top=${it.second?.rectF?.top}," +
                                "dw = ${mBmp?.width}," +
                                " dh = ${mBmp?.height}",
                    )

                    val bmp = createTempBitmap(mBmp, it.second, width, height)
                    val file =
                        CameraUtil.createMediaFile(context, isImage = true, storeInCache = true)
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
    private fun createTempBitmap(
        bmp: Bitmap?,
        zoomInfo: ZoomInfo,
        width: Int,
        height: Int
    ): Bitmap {
        if (bmp != null && zoomInfo.hasData()) {
            val top = -min(0f, (zoomInfo.rectF!!.top / zoomInfo.scale!!)).toInt()
            val left = -min(0f, (zoomInfo.rectF!!.left / zoomInfo.scale!!)).toInt()

            val w = min(bmp.width.toFloat(), width / zoomInfo.scale!!)
            val h = min(bmp.height.toFloat(), height / zoomInfo.scale!!)

            return Bitmap.createBitmap(bmp, left, top, w.toInt(), h.toInt())
        }
        throw Exception("Unable to create bitmap")
    }
}