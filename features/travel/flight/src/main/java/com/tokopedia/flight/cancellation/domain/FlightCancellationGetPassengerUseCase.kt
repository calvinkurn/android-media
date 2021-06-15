package com.tokopedia.flight.cancellation.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationGQLQuery
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationReasonDataCacheSource
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.common.util.FlightPassengerTitleType
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 15/07/2020
 */
class FlightCancellationGetPassengerUseCase @Inject constructor(
        @ApplicationContext
        private val context: Context,
        private val useCase: MultiRequestGraphqlUseCase,
        private val cancellationReasonsCache: FlightCancellationReasonDataCacheSource) {

    suspend fun fetchCancellablePassenger(invoiceId: String): List<FlightCancellationModel> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_INVOICE_ID to invoiceId)
        val graphqlRequest = GraphqlRequest(FlightCancellationGQLQuery.CANCEL_PASSENGER, FlightCancellationPassengerEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val flightCancellablePassengerList = useCase.executeOnBackground().getSuccessData<FlightCancellationPassengerEntity.Response>().flightCancelPassenger
        val processedData = processIncludedData(flightCancellablePassengerList)

        cancellationReasonsCache.saveCache(processedData.formattedReasons)
        return transformAllPassengersData(processedData)
    }

    private fun transformAllPassengersData(cancellationPassengerEntity: FlightCancellationPassengerEntity): List<FlightCancellationModel> {
        val journeyMap = hashMapOf<String, MutableList<FlightCancellationPassengerModel>>()
        for (passenger in cancellationPassengerEntity.passengers) {
            if (journeyMap.containsKey(passenger.journeyId)) {
                journeyMap[passenger.journeyId]?.add(transformPassenger(passenger))
            } else {
                val passengerModelList = arrayListOf<FlightCancellationPassengerModel>()
                passengerModelList.add(transformPassenger(passenger))
                journeyMap[passenger.journeyId] = passengerModelList
            }
        }

        for (passenger in cancellationPassengerEntity.nonCancellablePassengers) {
            if (journeyMap.containsKey(passenger.journeyId)) {
                journeyMap[passenger.journeyId]?.add(transformPassenger(passenger))
            } else {
                val passengerModelList = arrayListOf<FlightCancellationPassengerModel>()
                passengerModelList.add(transformPassenger(passenger))
                journeyMap[passenger.journeyId] = passengerModelList
            }
        }

        val cancellationModelList = arrayListOf<FlightCancellationModel>()
        for ((key, value) in journeyMap) {
            cancellationModelList.add(FlightCancellationModel().apply {
                flightCancellationJourney = FlightCancellationJourney(journeyId = key)
                passengerModelList = value
            })
        }
        return cancellationModelList
    }

    private fun transformPassenger(passenger: FlightCancellationPassengerEntity.Passenger): FlightCancellationPassengerModel =
            FlightCancellationPassengerModel(
                    passengerId = passenger.passengerId,
                    type = passenger.type,
                    title = passenger.title,
                    titleString = getTitleString(passenger.title),
                    firstName = passenger.firstName,
                    lastName = passenger.lastName,
                    relationId = passenger.relationId,
                    relations = passenger.relations,
                    status = passenger.status,
                    statusString = passenger.statusString
            )

    private fun processIncludedData(flightCancellationPassengerEntity: FlightCancellationPassengerEntity): FlightCancellationPassengerEntity {
        val reasonList = arrayListOf<FlightCancellationPassengerEntity.Reason>()
        val docsMap = hashMapOf<String, FlightCancellationPassengerEntity.RequiredDoc>()
        for (item in flightCancellationPassengerEntity.included) {
            if (item.type == TYPE_REASON) {
                val reason = FlightCancellationPassengerEntity.Reason(
                        id = item.key,
                        title = item.attributes.title,
                        requiredDocs = item.attributes.requiredDocs
                )
                reasonList.add(reason)
            } else if (item.type == TYPE_DOCS) {
                docsMap[item.key] = FlightCancellationPassengerEntity.RequiredDoc(item.key, item.attributes.title)
            }
        }

        for (item in reasonList) {
            item.formattedRequiredDocs.addAll(processRequiredDocsData(docsMap, item.requiredDocs))
        }

        flightCancellationPassengerEntity.formattedReasons.addAll(reasonList)
        return flightCancellationPassengerEntity
    }

    private fun processRequiredDocsData(docsMap: Map<String, FlightCancellationPassengerEntity.RequiredDoc>,
                                        reasonRequiredDocList: List<String>)
            : List<FlightCancellationPassengerEntity.RequiredDoc> {

        val requiredDocsList = arrayListOf<FlightCancellationPassengerEntity.RequiredDoc>()
        for (docId in reasonRequiredDocList) {
            docsMap[docId]?.let {
                requiredDocsList.add(it)
            }
        }
        return requiredDocsList
    }

    private fun getTitleString(typeId: Int): String {
        return when (typeId) {
            FlightPassengerTitleType.TUAN -> context.getString(R.string.mister)
            FlightPassengerTitleType.NYONYA -> context.getString(R.string.misiz)
            FlightPassengerTitleType.NONA -> context.getString(R.string.miss)
            else -> context.getString(R.string.mister)
        }
    }

    companion object {
        private const val PARAM_INVOICE_ID = "invoiceID"

        private const val TYPE_REASON = "reason"
        private const val TYPE_DOCS = "docs"
    }

}