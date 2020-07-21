package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerAttachmentModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 17/07/2020
 */
class FlightCancellationReasonViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    lateinit var cancellationWrapperModel: FlightCancellationWrapperModel
    var selectedReason: FlightCancellationPassengerEntity.Reason? = null
    var editedAttachmentPosition: Int = -1

    private val attachmentModelList: MutableList<FlightCancellationAttachmentModel> = arrayListOf()

    private val mutableAttachmentErrorStringRes = MutableLiveData<Int>()
    val attachmentErrorStringRes: LiveData<Int>
        get() = mutableAttachmentErrorStringRes

    private val mutableViewAttachmentModelList = MutableLiveData<MutableList<FlightCancellationAttachmentModel>>()
    val viewAttachmentModelList: LiveData<MutableList<FlightCancellationAttachmentModel>>
        get() = mutableViewAttachmentModelList

    init {
        mutableAttachmentErrorStringRes.value = DEFAULT_STRING_RES_ERROR
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

    companion object {
        const val DEFAULT_STRING_RES_ERROR = -1

        private const val DEFAULT_ONE_MEGABYTE = 1024
        private const val MINIMUM_HEIGHT = 100
        private const val MINIMUM_WIDTH = 300
        private const val MAX_FILE_SIZE = 15360
    }
}