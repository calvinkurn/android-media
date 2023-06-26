package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig
import com.tokopedia.contactus.inboxtickets.data.model.PicObjUpload
import com.tokopedia.contactus.inboxtickets.data.model.SecureImageResponse
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

private const val SERVER_ID = "server_id"

class SecureUploadUseCase @Inject constructor(private val repository: ContactUsRepository) {

    companion object {
        private const val MEDIA_TYPE = "image/*"
        private const val FORM_DATA_KEY = "file_upload"
    }

    /**
     * Upload Image into server using secure method
     * @param files is absolute uri of image photo
     * @param chipUploadHostConfig is config secure from gql chipUploadHostConfig
     * @param uniqIDs is local generate uniqId as id attribut for reference attachment, the uniq id is the same id value in pPhotoAll on ticket_reply gql
     *
     * @return JSON consisting of encoded base64 from url_path + file_name with the parameter name is uniqIDs
     * */
    suspend fun uploadSecureImage(
        files: List<String>,
        chipUploadHostConfig: ChipUploadHostConfig,
        uniqIDs: List<String>
    ): String {
        val list = arrayListOf<SecureImageResponse>()
        files.forEach {file ->
            val body = getMultiPartObject(file)
            val uploadResponses : SecureImageResponse = repository.postMultiRestData(
                chipUploadHostConfig.getUploadHostConfig().getUploadHostConfigData().getHost()
                    .getSecureHost(),
                object : TypeToken<SecureImageResponse>() {}.type,
                queryMap = mapOf(
                    SERVER_ID to chipUploadHostConfig.getUploadHostConfig().getUploadHostConfigData()
                        .getHost().getSecureHost()
                ),
                body = body
            )

            if (uploadResponses.getImage().isSuccess() == InboxDetailConstanta.SUCCESS_KEY_SECURE_IMAGE_PARAMETER) {
                list.add(uploadResponses)
            } else {
                return@forEach
            }
        }

        if (list.isEmpty() || files.size != list.size) {
            return ""
        }
        val encodeImageUrl = encodeImageUrl(list)
        return makeUploadedUrlAsJSON(encodeImageUrl, uniqIDs)
    }

    private fun getMultiPartObject(pathFile: String): MultipartBody.Part {
        val file = File(pathFile)
        val reqFile = file.asRequestBody(MEDIA_TYPE.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(FORM_DATA_KEY, file.name, reqFile)
    }

    private fun encode(dataToEncode : String): String {
        return android.util.Base64.encodeToString(
            dataToEncode.toByteArray(charset("UTF-8")),
            android.util.Base64.DEFAULT
        )
    }

    private fun encodeImageUrl(listOfSecureImageResponse: ArrayList<SecureImageResponse>) : ArrayList<String> {
        val imagesUrlEncode = arrayListOf<String>()
        val gson = Gson()
        listOfSecureImageResponse.forEach {
            val imageDataSecureResponse = it.getImage().imageDataValues
            val uploadImageParam = PicObjUpload(imageDataSecureResponse?.fileName, imageDataSecureResponse?.filePath)
            imagesUrlEncode.add(encode(gson.toJson(uploadImageParam)))
        }
        return imagesUrlEncode
    }

    private fun makeUploadedUrlAsJSON(attachment: List<String>, uniqIDs : List<String>): String {
        var reviewPhotos: JSONObject? = null
        try {
            attachment.forEachIndexed { index, imageUrlEncode ->
                if (index == 0) {
                    reviewPhotos = JSONObject()
                }
                reviewPhotos?.put(uniqIDs[index], imageUrlEncode)
            }
        } catch (e: JSONException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            return ""
        }
        return reviewPhotos?.toString()?.replace("\\n", "").orEmpty()
    }
}
