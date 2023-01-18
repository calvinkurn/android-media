package com.tokopedia.contactus.inboxtickets.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.contactus.inboxtickets.data.UploadImageResponse
import com.tokopedia.contactus.inboxtickets.data.model.PicObjPojo
import com.tokopedia.contactus.inboxtickets.data.model.SecureImageParameter
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.utils.image.ImageProcessingUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

   suspend fun uploadFile(userId: String,
                          imageUploads: List<ImageUpload>?,
                          files: List<String>,
                          listOfSecureImageParmeter: ArrayList<SecureImageParameter>): List<ImageUpload>{
        val list = ArrayList<ImageUpload>()
        imageUploads?.forEachIndexed { index, imageUpload ->

            val params =getParams(userId,files[index])

            val response = uploadImageUseCase
                    .createObservable(params)
                    .toBlocking()
                    .first()
                    .dataResultImageUpload

            imageUpload.picObj = getModifiedPicObj(response.data.picObj, listOfSecureImageParmeter[index])
            list.add(imageUpload)
        }
        return list
    }

    fun getModifiedPicObj(picObj: String, secureImageParameter: SecureImageParameter): String {
        val picObjPojo = GsonBuilder().create()
                .fromJson(picObj.decode(),
                        PicObjPojo::class.java)
        picObjPojo.fileName = secureImageParameter.getImage().imageDataValues?.fileName.orEmpty()
        picObjPojo.filePath = secureImageParameter.getImage().imageDataValues?.filePath.orEmpty()

        return Gson().toJson(picObjPojo).encode();
    }

    private fun String.decode(): String {
        return android.util.Base64.decode(this, android.util.Base64.DEFAULT).toString(charset("UTF-8"))
    }

    private fun String.encode(): String {
        return android.util.Base64.encodeToString(this.toByteArray(charset("UTF-8")), android.util.Base64.DEFAULT)
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
        return content.toRequestBody("text/plain".toMediaTypeOrNull())
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
