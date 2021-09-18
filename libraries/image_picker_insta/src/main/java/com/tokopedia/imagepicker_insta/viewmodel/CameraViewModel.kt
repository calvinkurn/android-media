package com.tokopedia.imagepicker_insta.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileOutputStream

class CameraViewModel : BaseViewModel(Dispatchers.IO) {
    val liveDataCropPhoto = MutableLiveData<LiveDataResult<Uri>>()

    fun cropPhoto(srcBitmap: Bitmap, yOffset: Int, width: Int, dstFile: File) {
        launchCatchError(block = {
            liveDataCropPhoto.postValue(LiveDataResult.loading())
            val bmp: Bitmap = Bitmap.createBitmap(srcBitmap, 0, yOffset, width, width)

            val fos = FileOutputStream(dstFile)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            val uri = Uri.fromFile(dstFile)
            liveDataCropPhoto.postValue(LiveDataResult.success(uri))
        }, onError = {
            liveDataCropPhoto.postValue(LiveDataResult.error(it))
        })
    }

    fun deleteFile(file: File?) {
        launchCatchError(block = {
            file?.delete()
        }, onError = {})
    }
}