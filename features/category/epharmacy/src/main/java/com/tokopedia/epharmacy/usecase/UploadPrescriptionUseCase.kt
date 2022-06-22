package com.tokopedia.epharmacy.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.epharmacy.network.request.UploadPrescriptionRequest
import com.tokopedia.epharmacy.network.response.EPharmacyPrescriptionUploadResponse
import com.tokopedia.user.session.UserSessionInterface
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

    private var localFilePath: String = ""
    private var id : Long = 0L
    private var userAccessToken = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {
        val base64ImageString = getBase64OfPrescriptionImage(localFilePath)
        val restRequest = RestRequest.Builder(ENDPOINT_URL, EPharmacyPrescriptionUploadResponse::class.java)
            .setBody(getUploadPrescriptionBody(PREFIX_IMAGE + base64ImageString))
            .setRequestType(RequestType.POST)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    private fun getUploadPrescriptionBody(base64ImageString : String): UploadPrescriptionRequest{
        return UploadPrescriptionRequest(arrayListOf(
            UploadPrescriptionRequest.PrescriptionRequest(
                base64ImageString, KEY_FORMAT_VALUE, id, KEY_SOURCE_VALUE
        )))
    }

    fun setBase64Image(id : Long, localPath: String, accessToken : String) {
        try {
            this.id = id
            this.localFilePath = localPath
            this.userAccessToken = accessToken
        } catch (e: Exception) {

        }
    }

    private fun getBase64OfPrescriptionImage(localFilePath: String): String {

        // TODO File Size Calculations and Out Of Memory resolutions
        return try {
            val prescriptionImageBitmap: Bitmap = BitmapFactory.decodeFile(localFilePath)
            val prescriptionByteArrayOutputStream = ByteArrayOutputStream()
            prescriptionImageBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                prescriptionByteArrayOutputStream
            )
            val byteArrayImage = prescriptionByteArrayOutputStream.toByteArray()
            prescriptionImageBitmap.recycle()
            Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        }catch (e : Exception){
            ""
        }
    }

    companion object {
        private const val ENDPOINT_URL = "https://api-staging.tokopedia.com/epharmacy/prescription/upload"
        private const val KEY_FORMAT_VALUE="FILE"
        private const val KEY_SOURCE_VALUE="buyer"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val PREFIX_IMAGE = "data:image/jpeg;base64,"
        private const val BEARER = "Bearer "
        private const val MAX_BYTES = 4_000_000
    }
}