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
import com.tokopedia.epharmacy.utils.EPharmacyImageQuality
import com.tokopedia.epharmacy.utils.EPharmacyImageQualityDecreaseFactor
import com.tokopedia.media.preview.managers.ImageCompressionManager
import okio.utf8Size
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionUseCase @Inject constructor(
        private val repository: RestRepository,
        private val imageCompressionManager: ImageCompressionManager
): RestRequestUseCase(repository) {

    private var localFilePath: String = ""
    private var id : Long = 0L

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {
        val base64ImageString = getBase64OfPrescriptionImage(localFilePath, EPharmacyImageQuality)
        val restRequest = RestRequest.Builder(ENDPOINT_URL, EPharmacyPrescriptionUploadResponse::class.java)
            .setBody(getUploadPrescriptionBody(base64ImageString))
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

    fun setBase64Image(id : Long, localPath: String) {
        this.id = (id + 1)
        this.localFilePath = localPath
    }

    private fun getBase64OfPrescriptionImage(localFilePath: String, quality : Int): String {
        return try {
            val prescriptionImageBitmap: Bitmap = BitmapFactory.decodeFile(localFilePath)
            val prescriptionByteArrayOutputStream = ByteArrayOutputStream()
            prescriptionImageBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                prescriptionByteArrayOutputStream
            )
            val byteArrayImage = prescriptionByteArrayOutputStream.toByteArray()
            prescriptionImageBitmap.recycle()

            val encodedString = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
            return if(encodedString.utf8Size(0,encodedString.length) >= MAX_BYTES && quality > MIN_QUALITY){
                getBase64OfPrescriptionImage(localFilePath , (quality * EPharmacyImageQualityDecreaseFactor).toInt())
            } else
                "${IMAGE_DATA_PREFIX}${encodedString}"
        }catch (e : Exception){
            // TODO Log in Crashlytics
            e.printStackTrace()
            ""
        }
    }

    companion object {
        private const val ENDPOINT_URL = "https://epharmacy-staging.tokopedia.com/prescription/upload"
        private const val KEY_FORMAT_VALUE="FILE"
        private const val KEY_SOURCE_VALUE="buyer"
        private const val MAX_BYTES = 4_000_000
        private const val MIN_QUALITY = 60
        private const val IMAGE_DATA_PREFIX = "data:image/jpeg;base64,"
    }
}