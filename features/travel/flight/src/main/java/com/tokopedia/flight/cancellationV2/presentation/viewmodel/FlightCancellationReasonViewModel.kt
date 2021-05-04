package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationAttachmentUploadUseCase
import com.tokopedia.flight.cancellationV2.presentation.model.*
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 17/07/2020
 */
class FlightCancellationReasonViewModel @Inject constructor(
        private val attachmentUploadUseCase: FlightCancellationAttachmentUploadUseCase,
        private val userSession: UserSessionInterface,
        private val flightAnalytics: FlightAnalytics,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    lateinit var cancellationWrapperModel: FlightCancellationWrapperModel
    var selectedReason: FlightCancellationPassengerEntity.Reason? = null
    var editedAttachmentPosition: Int = -1

    private val attachmentModelList: MutableList<FlightCancellationAttachmentModel> = arrayListOf()
    private val attachmentMandatory: Boolean
        get() {
            return selectedReason?.let {
                it.formattedRequiredDocs.size > 0 && attachmentModelList.size > 0
            } ?: false
        }

    private val mutableCanNavigateToNextStep = MutableLiveData<Boolean>()
    val canNavigateToNextStep: LiveData<Boolean>
        get() = mutableCanNavigateToNextStep

    private val mutableAttachmentErrorString = MutableLiveData<Result<Pair<Int, Any>>>()
    val attachmentErrorString: LiveData<Result<Pair<Int, Any>>>
        get() = mutableAttachmentErrorString

    private val mutableAttachmentErrorStringRes = MutableLiveData<Int>()
    val attachmentErrorStringRes: LiveData<Int>
        get() = mutableAttachmentErrorStringRes

    private val mutableViewAttachmentModelList = MutableLiveData<MutableList<FlightCancellationAttachmentModel>>()
    val viewAttachmentModelList: LiveData<MutableList<FlightCancellationAttachmentModel>>
        get() = mutableViewAttachmentModelList

    init {
        mutableCanNavigateToNextStep.postValue(false)
        mutableAttachmentErrorStringRes.postValue(DEFAULT_STRING_RES_ERROR)
    }

    fun trackOnNext() {
        flightAnalytics.eventClickNextOnCancellationReason(
                "${selectedReason?.title ?: ""} - ${cancellationWrapperModel.invoiceId}",
                userSession.userId
        )
    }

    fun buildAttachmentList() {
        for (passenger in getUniquePassenger()) {
            val passengerRelations = passenger.passengerRelation.split("-")
            attachmentModelList.add(FlightCancellationAttachmentModel(
                    filename = "",
                    filepath = "",
                    passengerId = passenger.passengerId,
                    passengerName = passenger.passengerName,
                    relationId = passenger.passengerRelation,
                    journeyId = passengerRelations[0],
                    docType = 0
            ))
        }
    }

    fun buildViewAttachmentList(docType: Int) {
        val viewAttachments = arrayListOf<FlightCancellationAttachmentModel>()
        for (attachment in attachmentModelList) {
            attachment.docType = docType
            if (!viewAttachments.contains(attachment)) {
                viewAttachments.add(attachment)
            }
        }
        mutableViewAttachmentModelList.postValue(viewAttachments)
    }

    fun getAttachments(): List<FlightCancellationAttachmentModel> = attachmentModelList

    fun onSuccessChangeAttachment(filePath: String) {
        if (validateAttachment(filePath)) {
            val attachmentModel = attachmentModelList[editedAttachmentPosition]
            attachmentModel.filepath = filePath
            attachmentModel.filename = Uri.parse(filePath).lastPathSegment ?: ""
            attachmentModel.isUploaded = false

            buildViewAttachmentList(attachmentModel.docType)
        }
    }

    fun onNextButtonClicked() {
        launchCatchError(dispatcherProvider.main, block = {
            if (attachmentMandatory) {
                val totalPassenger = calculateTotalPassenger()
                val attachmentToUpload = buildAttachmentForUpload()

                val isValidAttachment = !attachmentMandatory || (attachmentMandatory && attachmentToUpload.isNotEmpty())
                if (!isValidAttachment) mutableAttachmentErrorStringRes.postValue(R.string.flight_cancellation_attachment_required_error_message)

                val isValidAttachmentLength = !attachmentMandatory || (attachmentMandatory && totalPassenger > 0)
                if (!isValidAttachmentLength) {
                    mutableAttachmentErrorString.postValue(Success(Pair(
                            R.string.flight_cancellation_attachment_more_than_max_error_message,
                            totalPassenger + 1
                    )))
                }

                if (attachmentMandatory && isValidAttachmentLength) {
                    uploadAttachmentAndBuildModel()
                    mutableCanNavigateToNextStep.postValue(true)
                } else {
                    mutableCanNavigateToNextStep.postValue(false)
                }
            } else {
                buildCancellationWrapperModel()
                mutableCanNavigateToNextStep.postValue(true)
            }
        }) {
            it.printStackTrace()
            mutableAttachmentErrorString.postValue(Fail(it))
        }
    }

    private suspend fun uploadAttachmentAndBuildModel() {
        val unUploadedAttachmentList = buildUnUploadedAttachments(buildAttachmentForUpload())
        val invoiceId = cancellationWrapperModel.invoiceId

        unUploadedAttachmentList.map {
            val attachmentUpload = attachmentUploadUseCase.executeCoroutine(
                    attachmentUploadUseCase.createRequestParams(
                            it.filepath,
                            invoiceId,
                            it.journeyId,
                            it.passengerId,
                            it.docType
                    )
            )

            it.isUploaded = attachmentUpload.attributes.isUploaded

            val indexOfAttachment = attachmentModelList.indexOf(it)
            if (indexOfAttachment != -1) {
                attachmentModelList[indexOfAttachment].isUploaded = attachmentUpload.attributes.isUploaded
            }
        }.toList()

        buildCancellationWrapperModel()
    }

    private fun getUniquePassenger(): List<FlightCancellationPassengerAttachmentModel> {
        val uniquePassenger: MutableList<FlightCancellationPassengerAttachmentModel> = arrayListOf()

        for (cancellation in cancellationWrapperModel.cancellationList) {
            for (passenger in cancellation.passengerModelList) {
                val passengerAttachmentModel = FlightCancellationPassengerAttachmentModel(
                        passenger.passengerId, getPassengerName(passenger), passenger.relationId)
                if (!uniquePassenger.contains(passengerAttachmentModel) && passenger.type == FlightBookingPassenger.ADULT) {
                    uniquePassenger.add(passengerAttachmentModel)
                }
            }
        }

        return uniquePassenger
    }

    private fun getPassengerName(passenger: FlightCancellationPassengerModel): String =
            "${passenger.titleString} ${passenger.firstName.substring(0, 1).toUpperCase()}${passenger.firstName.substring(1)} " +
                    "${passenger.lastName.substring(0, 1).toUpperCase()}${passenger.lastName.substring(1)}"

    private fun validateAttachment(uri: String): Boolean {
        if (uri.isEmpty()) return false
        val file = File(uri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = file.length() / DEFAULT_ONE_MEGABYTE

        return if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            mutableAttachmentErrorStringRes.postValue(R.string.flight_cancellation_min_dimension_error)
            false
        } else if (fileSize >= MAX_FILE_SIZE) {
            mutableAttachmentErrorStringRes.postValue(R.string.flight_cancellation_max_error)
            false
        } else {
            mutableAttachmentErrorStringRes.postValue(DEFAULT_STRING_RES_ERROR)
            true
        }
    }

    private fun buildAttachmentForUpload(): List<FlightCancellationAttachmentModel> {
        val attachmentsToUpload = arrayListOf<FlightCancellationAttachmentModel>()

        viewAttachmentModelList.value?.let {
            for (attachment in attachmentModelList) {
                val indexInView = it.indexOf(attachment)
                if (indexInView != -1) {
                    val selectedViewAttachment = it[indexInView]
                    attachment.filename = selectedViewAttachment.filename
                    attachment.filepath = selectedViewAttachment.filepath
                    attachment.docType = selectedViewAttachment.docType
                    attachment.isUploaded = selectedViewAttachment.isUploaded

                    attachmentsToUpload.add(attachment)
                }
            }
        }

        return attachmentsToUpload
    }

    private fun buildUnUploadedAttachments(attachmentToUploads: List<FlightCancellationAttachmentModel>)
            : List<FlightCancellationAttachmentModel> {
        val unUploadedAttachments = arrayListOf<FlightCancellationAttachmentModel>()

        for (attachment in attachmentToUploads) {
            if (!attachment.isUploaded) {
                unUploadedAttachments.add(attachment)
            }
        }

        return unUploadedAttachments
    }

    private fun calculateTotalPassenger(): Int {
        val uniquePassenger = arrayListOf<String>()

        for (cancellation in cancellationWrapperModel.cancellationList) {
            for (passenger in cancellation.passengerModelList) {
                if (!uniquePassenger.contains(passenger.passengerId) &&
                        passenger.type == FlightBookingPassenger.ADULT) {
                    uniquePassenger.add(passenger.passengerId)
                }
            }
        }

        return uniquePassenger.size
    }

    private fun buildCancellationWrapperModel() {
        cancellationWrapperModel.cancellationReasonAndAttachmentModel =
                FlightCancellationReasonAndAttachmentModel(
                        attachmentList = attachmentModelList,
                        reason = selectedReason?.title ?: "",
                        reasonId = selectedReason?.id ?: "0"
                )
    }

    companion object {
        const val DEFAULT_STRING_RES_ERROR = -1

        private const val DEFAULT_ONE_MEGABYTE = 1024
        private const val MINIMUM_HEIGHT = 100
        private const val MINIMUM_WIDTH = 300
        private const val MAX_FILE_SIZE = 15360
    }
}