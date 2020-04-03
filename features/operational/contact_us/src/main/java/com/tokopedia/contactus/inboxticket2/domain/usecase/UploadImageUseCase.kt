package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.UploadImageContactUs
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImpl
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import com.tokopedia.contactus.orderquery.data.ImageUpload
import com.tokopedia.contactus.orderquery.data.ImageUploadResult
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils
import com.tokopedia.core.network.v4.NetworkConfig
import com.tokopedia.core.util.ImageUploadHandler
import com.tokopedia.core.util.SessionHandler
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(var context: Context) {

     suspend fun uploadFile(context: Context, imageUploads: List<ImageUpload>?): List<ImageUpload> {
        val list = arrayListOf<ImageUpload>()
        imageUploads?.forEach { imageUpload ->
            val uploadUrl = Utils(context).UPLOAD_URL
            val networkCalculator = NetworkCalculator(
                    NetworkConfig.POST, context,
                    uploadUrl)
                    .setIdentity()
                    .addParam(InboxDetailPresenterImpl.PARAM_IMAGE_ID, imageUpload.imageId)
                    .addParam(InboxDetailPresenterImpl.PARAM_WEB_SERVICE, "1")
                    .compileAllParam()
                    .finish()
            val file: File = try {
                ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.fileLoc))
            } catch (e: IOException) {
                throw RuntimeException(context.getString(R.string.contact_us_error_upload_image))
            }
            val userId = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[NetworkCalculator.USER_ID])
            val deviceId = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[NetworkCalculator.DEVICE_ID])
            val hash = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[NetworkCalculator.HASH])
            val deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[NetworkCalculator.DEVICE_TIME])
            val fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                    file)
            val imageId = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[InboxDetailPresenterImpl.PARAM_IMAGE_ID])
            val web_service = RequestBody.create(MediaType.parse("text/plain"),
                    networkCalculator.content[InboxDetailPresenterImpl.PARAM_WEB_SERVICE])


            val upload: ImageUploadResult = if (SessionHandler.isV4Login(context)) {
                RetrofitUtils.createRetrofit(uploadUrl)
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
                RetrofitUtils.createRetrofit(uploadUrl)
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
}