package com.tokopedia.epharmacy.usecase

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailHeaderDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailInfoDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyOrderDetailPaymentDataModel
import com.tokopedia.epharmacy.network.gql.EPharmacyGetConsultationOrderDetailsQuery
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.epharmacy.network.response.OrderButtonData
import com.tokopedia.epharmacy.network.response.OrderTrackingData
import com.tokopedia.epharmacy.utils.ORDER_HEADER_COMPONENT
import com.tokopedia.epharmacy.utils.ORDER_INFO_COMPONENT
import com.tokopedia.epharmacy.utils.ORDER_PAYMENT_COMPONENT
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyConsultationOrderDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyOrderDetailResponse>(graphqlRepository) {

    fun getEPharmacyOrderDetail(
        onSuccess: (EPharmacyDataModel, OrderButtonData) -> Unit,
        onError: (Throwable) -> Unit,
        tConsultationId: Long,
        orderUUId: String,
        waitingInvoice: Boolean
    ) {
        try {
            this.setTypeClass(EPharmacyOrderDetailResponse::class.java)
            this.setGraphqlQuery(EPharmacyGetConsultationOrderDetailsQuery)
            this.setRequestParams(createRequestParams(tConsultationId, orderUUId, waitingInvoice))
            this.execute(
                { result ->
                    onSuccess(
                        mapResponseToOrderDetail(result.getConsultationOrderDetail?.ePharmacyOrderData),
                        mapResponseToOrderButton(result.getConsultationOrderDetail?.ePharmacyOrderData)
                    )
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun mapResponseToOrderDetail(orderData: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderData?): EPharmacyDataModel {
        val listOfComponents = arrayListOf<BaseEPharmacyDataModel>()
        listOfComponents.add(
            EPharmacyOrderDetailHeaderDataModel(
                ORDER_HEADER_COMPONENT, ORDER_HEADER_COMPONENT,
                orderData?.orderStatusDesc,
                orderData?.ticker?.typeInt,
                orderData?.ticker?.message,
                orderData?.invoiceNumber,
                orderData?.invoiceUrl,
                orderData?.paymentDate,
                orderData?.orderExpiredDate,
                orderData?.consultationData?.startTime,
                orderData?.orderIndicatorColor
            )
        )
        listOfComponents.add(
            EPharmacyOrderDetailInfoDataModel(
                ORDER_INFO_COMPONENT,
                ORDER_INFO_COMPONENT,
                orderData?.consultationSource?.serviceName,
                orderData?.consultationSource?.enablerName,
                orderData?.consultationSource?.operatingSchedule?.duration,
                orderData?.consultationSource?.priceStr,
                orderData?.consultationData?.prescription?.firstOrNull()?.expiryDate.orEmpty()
            )
        )
        listOfComponents.add(
            EPharmacyOrderDetailPaymentDataModel(
                ORDER_PAYMENT_COMPONENT,
                ORDER_PAYMENT_COMPONENT,
                orderData?.paymentMethod,
                orderData?.itemPriceStr,
                orderData?.paymentAmountStr,
                orderData?.helpButton
            )
        )
        return EPharmacyDataModel(listOfComponents)
    }

    private fun mapResponseToOrderButton(orderData: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderData?): OrderButtonData {
        return OrderButtonData(
            orderData?.cta,
            orderData?.triDots,
            OrderTrackingData(
                orderData?.consultationSource?.enablerName,
                orderData?.groupId,
                orderData?.consultationData?.tokoConsultationId,
                orderData?.orderId,
                orderData?.orderStatus,
                orderData?.ticker?.message
            )
        )
    }

    private fun createRequestParams(tConsultationId: Long, orderUUId: String, waitingInvoice: Boolean): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_TOKO_CONSULTATION_ID to tConsultationId,
            PARAM_ORDER_UUID to orderUUId,
            PARAM_SOURCE to PARAM_SOURCE_ANDROID,
            PARAM_WAITING_INVOICE to waitingInvoice
        )
    }

    companion object {
        const val PARAM_TOKO_CONSULTATION_ID = "toko_consultation_id"
        const val PARAM_ORDER_UUID = "order_uuid"
        const val PARAM_SOURCE = "source"
        const val PARAM_WAITING_INVOICE = "waiting_invoice"
        const val PARAM_SOURCE_ANDROID = "ANDROID"
    }
}
