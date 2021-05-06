package com.tokopedia.contactus.inboxticket2.domain.usecase

import android.content.Context
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.UploadImageResponse
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.utils.image.ImageProcessingUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import javax.inject.Inject

const val IMAGE_UPLOAD_URL = "https://u12.tokopedia.net"
private const val IMAGE_UPLOAD_PATH = "/upload/attachment"
private const val ATTACHMENT_TYPE = "fileToUpload\"; filename=\"image.jpg"
const val IMAGE_QUALITY = 70
private const val PARAM_WEB_SERVICE = "web_service"
private const val PARAM_ID = "id"

class ContactUsUploadImageUseCase @Inject constructor(private val context: Context,
                                                      private val uploadImageUseCase: UploadImageUseCase<UploadImageResponse>) {

    fun uploadFile(userId: String,
                           imageUploads: List<ImageUpload>?,
                           files: List<String>): List<ImageUpload>{
        val list = ArrayList<ImageUpload>()
        imageUploads?.forEachIndexed { index, imageUpload ->

            val params =getParams(userId,files[index])

            val response = uploadImageUseCase
                    .createObservable(params)
                    .toBlocking()
                    .first()
                    .dataResultImageUpload

            imageUpload.picObj = response.data.picObj
            list.add(imageUpload)
        }
        return list
    }

    private fun getParams(userId: String, pathFile: String): RequestParams {
        val reqParam = HashMap<String, RequestBody>()
        reqParam[PARAM_WEB_SERVICE] = createRequestBody("1")
        reqParam[PARAM_ID] = createRequestBody(String.format("%s%s", userId, pathFile))

        return uploadImageUseCase.createRequestParam(pathFile,
                IMAGE_UPLOAD_PATH,
                ATTACHMENT_TYPE,
                reqParam)
    }


    private fun createRequestBody(content: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), content)
    }

    @Throws(IOException::class)
    fun getFile(imageUpload: List<ImageUpload>?): List<String> {
        val list = ArrayList<String>()
        imageUpload?.forEach {
            val s = ImageProcessingUtil.compressImageFile(it.fileLoc ?: "", IMAGE_QUALITY)
            list.add(try {
                s.absolutePath
            } catch (e: IOException) {
                throw IOException(context.getString(R.string.contact_us_error_upload_image))
            })
        }
        return list
    }
}