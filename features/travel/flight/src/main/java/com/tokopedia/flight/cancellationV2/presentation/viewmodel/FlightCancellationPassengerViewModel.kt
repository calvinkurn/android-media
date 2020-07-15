package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.cancellationV2.data.FlightCancellationGQLQuery.CANCEL_PASSENGER
import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

/**
 * @author by furqan on 15/07/2020
 */
class FlightCancellationPassengerViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private val mutableCancellationPassengerList = MutableLiveData<List<FlightCancellationModel>>()
    val cancellationPassengerList: LiveData<List<FlightCancellationModel>>
        get() = mutableCancellationPassengerList

    fun getCancellablePassenger(invoiceId: String) {
        launchCatchError(dispatcherProvider.ui(), block = {
            val params = mapOf(PARAM_INVOICE_ID to invoiceId)
            val graphqlRequest = GraphqlRequest(CANCEL_PASSENGER, FlightCancellationPassengerEntity.Response::class.java, params)
            val data = graphqlRepository.getReseponse(listOf(graphqlRequest)).getSuccessData<FlightCancellationPassengerEntity.Response>().flightCancelPassenger

        }) {
            it.printStackTrace()
        }
    }

    companion object {
        private const val PARAM_INVOICE_ID = "invoiceID"
    }

}