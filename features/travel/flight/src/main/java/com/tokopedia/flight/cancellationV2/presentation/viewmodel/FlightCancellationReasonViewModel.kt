package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerAttachmentModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.user.session.UserSessionInterface
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
    private val attachmentModelList: MutableList<FlightCancellationAttachmentModel> = arrayListOf()

    private val mutableViewAttachmentModelList = MutableLiveData<MutableList<FlightCancellationAttachmentModel>>()
    val viewAttachmentModelList: LiveData<MutableList<FlightCancellationAttachmentModel>>
        get() = mutableViewAttachmentModelList

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

}