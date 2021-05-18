package com.tokopedia.flight.cancellationV2.domain

import com.tokopedia.flight.cancellationV2.data.CancellationAttachmentUploadEntity
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 16/06/2020
 */
class FlightCancellationAttachmentUploadUseCase @Inject constructor(
        private val flightRepository: FlightRepository,
        private val userSession: UserSessionInterface) {

    suspend fun executeCoroutine(requestParams: RequestParams): CancellationAttachmentUploadEntity {
        val imageFile = File(requestParams.getString(PARAM_DOC, ""))
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
        val imageRequestToUpload = MultipartBody.Part.createFormData("doc", imageFile.name, requestFile)
        val requestBody: Map<String, RequestBody> = requestParams.getObject(PARAM_MAPS) as Map<String, RequestBody>

        return flightRepository.uploadCancellationAttachmentCoroutine(
                requestBody,
                imageRequestToUpload)
    }

    fun createRequestParams(pathFile: String,
                            invoiceId: String,
                            journeyId: String,
                            passengerId: String,
                            docTypeId: Int): RequestParams {
        val requestParams = RequestParams.create()
        val userId = RequestBody.create(MediaType.parse("multipart/form-data"), userSession.userId)
        val mInvoiceId = RequestBody.create(MediaType.parse("multipart/form-data"), invoiceId)
        val mJourneyId = RequestBody.create(MediaType.parse("multipart/form-data"), journeyId)
        val mPassengerId = RequestBody.create(MediaType.parse("multipart/form-data"), passengerId)
        val mDocTypeId = RequestBody.create(MediaType.parse("multipart/form-data"), docTypeId.toString())

        val paramsMap = mapOf(
                PARAM_USER_ID to userId,
                PARAM_INVOICE_ID to mInvoiceId,
                PARAM_JOURNEY_ID to mJourneyId,
                PARAM_PASSENGER_ID to mPassengerId,
                PARAM_DOC_TYPE to mDocTypeId
        )

        requestParams.putObject(PARAM_MAPS, paramsMap)
        requestParams.putString(PARAM_DOC, pathFile)

        return requestParams
    }

    companion object {
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_INVOICE_ID = "invoice_id"
        private const val PARAM_JOURNEY_ID = "journey_id"
        private const val PARAM_PASSENGER_ID = "passenger_id"
        private const val PARAM_DOC = "doc"
        private const val PARAM_DOC_TYPE = "doc_type_id"

        private const val PARAM_MAPS = "maps"
    }
}