package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.UploadImageContactUs
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImpl
import com.tokopedia.contactus.orderquery.data.ImageUploadResult
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils
import com.tokopedia.core.network.v4.NetworkConfig
import com.tokopedia.core.util.ImageUploadHandler
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

const val IMAGE_UPLOAD_URL = "https://u12.tokopedia.net"

class UploadImageUseCase @Inject constructor(private val context: Context) {

    suspend fun uploadFile(imageUploads: List<ImageUpload>?,
                           networkCalculators: List<NetworkCalculator>,
                           files: List<File>,
                           isLoggedIn: Boolean): List<ImageUpload> {
        val list = arrayListOf<ImageUpload>()
        imageUploads?.forEachIndexed { index, imageUpload ->

            val upload: ImageUploadResult = getImageUploadresult(networkCalculators[index], files[index],isLoggedIn)

            if (upload.data != null) {
                imageUpload.picSrc = upload.data.picSrc
                imageUpload.picObj = upload.data.picObj
            } else if (upload.messageError != null) {
                throw RuntimeException(upload.messageError)
            }
            list.add(imageUpload)

        }
        return list
    }

    @Throws(IOException::class)
    fun getFile(imageUpload: List<ImageUpload>?): List<File> {
        val list = ArrayList<File>()
        imageUpload?.forEach {
            list.add(try {
                ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(it.fileLoc))
            } catch (e: IOException) {
                throw IOException(context.getString(R.string.contact_us_error_upload_image))
            })
        }
        return list
    }

    suspend fun getImageUploadresult(networkCalculator: NetworkCalculator, file: File, isLoggedIn: Boolean): ImageUploadResult {
        val userId = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[NetworkCalculator.USER_ID] ?: "")
        val deviceId = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[NetworkCalculator.DEVICE_ID] ?: "")
        val hash = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[NetworkCalculator.HASH] ?: "")
        val deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[NetworkCalculator.DEVICE_TIME] ?: "")
        val fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                file)
        val imageId = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[InboxDetailPresenterImpl.PARAM_IMAGE_ID] ?: "")
        val web_service = RequestBody.create(MediaType.parse("text/plain"),
                networkCalculator.content[InboxDetailPresenterImpl.PARAM_WEB_SERVICE] ?: "")
        return if (isLoggedIn) {
            RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                    .create(UploadImageContactUs::class.java)
                    .uploadImage(
                            networkCalculator.header[NetworkCalculator.CONTENT_MD5],  // 1
                            networkCalculator.header[NetworkCalculator.DATE],  // 2
                            networkCalculator.header[NetworkCalculator.AUTHORIZATION],  // 3
                            networkCalculator.header[NetworkCalculator.X_METHOD],  // 4
                            userId,
                            deviceId,
                            hash,
                            deviceTime,
                            fileToUpload,
                            imageId, web_service
                    )
        } else {
            RetrofitUtils.createRetrofit(IMAGE_UPLOAD_URL)
                    .create(UploadImageContactUs::class.java)
                    .uploadImagePublic(
                            networkCalculator.header[NetworkCalculator.CONTENT_MD5],  // 1
                            networkCalculator.header[NetworkCalculator.DATE],  // 2
                            networkCalculator.header[NetworkCalculator.AUTHORIZATION],  // 3
                            networkCalculator.header[NetworkCalculator.X_METHOD],  // 4
                            userId,
                            deviceId,
                            hash,
                            deviceTime,
                            fileToUpload,
                            imageId,
                            web_service)
        }
    }

     fun getNetworkCalculatorList(imageUpload: List<ImageUpload>?): List<NetworkCalculator> {
        val list = ArrayList<NetworkCalculator>()
        imageUpload?.forEach {
            list.add(
                    getNetworkCalculator()
                            .setIdentity()
                            .addParam(InboxDetailPresenterImpl.PARAM_IMAGE_ID, it.imageId)
                            .addParam(InboxDetailPresenterImpl.PARAM_WEB_SERVICE, "1")
                            .compileAllParam()
                            .finish()
            )
        }
        return list
    }

    fun getNetworkCalculator(): NetworkCalculator {
        return NetworkCalculator(
                NetworkConfig.POST, context,
                IMAGE_UPLOAD_URL)

    }
}