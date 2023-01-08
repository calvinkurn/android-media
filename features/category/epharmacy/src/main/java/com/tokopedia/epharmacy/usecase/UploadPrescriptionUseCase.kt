package com.tokopedia.epharmacy.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.epharmacy.network.request.UploadPrescriptionRequest
import com.tokopedia.epharmacy.network.response.EPharmacyPrescriptionUploadResponse
import com.tokopedia.epharmacy.utils.EPharmacyImageQuality
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.url.TokopediaUrl
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionUseCase @Inject constructor(
        private val repository: RestRepository): RestRequestUseCase(repository) {


    suspend fun executeOnBackground(id : Long, localFilePath: String): Map<Type, RestResponse> {
        val base64ImageString = getBase64OfPrescriptionImage(localFilePath)
        val restRequest = RestRequest.Builder(getEndpoint(), EPharmacyPrescriptionUploadResponse::class.java)
            .setBody(getUploadPrescriptionBody(id,base64ImageString))
            .setRequestType(RequestType.POST)
            .build()
        return repository.getResponses(arrayListOf(restRequest))
    }

    private fun getUploadPrescriptionBody(id : Long , base64ImageString : String): UploadPrescriptionRequest{
        return UploadPrescriptionRequest(arrayListOf(
            UploadPrescriptionRequest.PrescriptionRequest(
                base64ImageString, KEY_FORMAT_VALUE, id, KEY_SOURCE_VALUE
        )))
    }

    private fun getBase64OfPrescriptionImage(localFilePath: String, compress : Boolean = false, compressCounter : Int = 0): String {
        var prescriptionImageBitmap: Bitmap? = null
        val options: BitmapFactory.Options
        var finalEncodedString = ""
        return try {
            prescriptionImageBitmap = if(compress){
                options = BitmapFactory.Options()
                options.inSampleSize = 2 + compressCounter
                logBreadCrumb("$EPharmacyModuleName,Options={2 + ${compressCounter}},Path=${localFilePath}")
                BitmapFactory.decodeFile(localFilePath, options)
            }else {
                logBreadCrumb("$EPharmacyModuleName,Normal,Path=${localFilePath}")
                BitmapFactory.decodeFile(localFilePath)
            }
            val prescriptionByteArrayOutputStream = ByteArrayOutputStream()
            prescriptionImageBitmap?.compress(
                Bitmap.CompressFormat.JPEG,
                getImageQualitySafeFix(),
                prescriptionByteArrayOutputStream
            )
            val byteArrayImage = prescriptionByteArrayOutputStream.toByteArray()
            prescriptionImageBitmap?.recycle()

            val encodedString = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
            finalEncodedString = "${IMAGE_DATA_PREFIX}${encodedString}"
            logBreadCrumb("$EPharmacyModuleName,Return Main String = ${finalEncodedString.length}}")
            finalEncodedString
        }catch (e : Exception){
            prescriptionImageBitmap?.recycle()
            logBreadCrumb("$EPharmacyModuleName,Exception,isCompress=$compress}")
            when(e){
                is NullPointerException -> {
                    if((!compress) || (compress && (compressCounter < MAX_COMPRESSIONS))){
                        finalEncodedString = if (compress && localFilePath.isNotBlank()){
                            logBreadCrumb("$EPharmacyModuleName,Exception,isCompress=$compress}compressCounter=${compressCounter},path=${localFilePath}")
                            getBase64OfPrescriptionImage(localFilePath, true,compressCounter + 1)
                        }else {
                            logBreadCrumb("$EPharmacyModuleName,Exception,isCompress=$compress}compressCounter=${compressCounter},path=${localFilePath}")
                            getBase64OfPrescriptionImage(localFilePath, true,compressCounter +  1)
                        }
                    }
                    EPharmacyUtils.logException(NullPointerException("${e.message} filePath : $localFilePath"))
                }
                else -> {
                    EPharmacyUtils.logException(e)
                }
            }
            logBreadCrumb("$EPharmacyModuleName,Return Catch String = ${finalEncodedString.length}}")
            finalEncodedString
        }
    }

    private fun getImageQualitySafeFix() : Int {
        return EPharmacyImageQuality
    }

    private fun getEndpoint() : String {
        return if(TokopediaUrl.getInstance().GQL.contains("staging"))
            ENDPOINT_URL_STAGING
        else
            ENDPOINT_URL_LIVE
    }

    private fun logBreadCrumb(message : String){
        try {
            EmbraceMonitoring.logBreadcrumb(message)
        }catch (e:Exception){ }
    }

    companion object {
        private const val ENDPOINT_URL_LIVE = "https://api.tokopedia.com/epharmacy/prescription/upload"
        private const val ENDPOINT_URL_STAGING = "https://api-staging.tokopedia.com/epharmacy/prescription/upload"
        private const val KEY_FORMAT_VALUE="FILE"
        private const val KEY_SOURCE_VALUE="buyer"
        private const val IMAGE_DATA_PREFIX = "data:image/jpeg;base64,"

        private const val EPharmacyModuleName = "epharmacy"
        private const val MAX_COMPRESSIONS = 5

    }
}
