package com.tokopedia.digital_deals.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_deals.data.*
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealsQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DealsCheckoutViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.io) {

      private val errorGeneralValueMutable = MutableLiveData<Throwable>()
      val errorGeneralValue: LiveData<Throwable>
            get() = errorGeneralValueMutable

      private val dealsCheckoutResponseMutable = MutableLiveData<DealsCheckoutResponse>()
      val dealsCheckoutResponse: LiveData<DealsCheckoutResponse>
            get() = dealsCheckoutResponseMutable

      private val dealsCheckoutInstantResponseMutable = MutableLiveData<DealsCheckoutInstantResponse>()
      val dealsCheckoutInstantResponse: LiveData<DealsCheckoutInstantResponse>
            get() = dealsCheckoutInstantResponseMutable

      fun checkoutGeneral(checkoutParam: DealCheckoutGeneral) {
            launchCatchError(block = {
                  val params = mapOf(PARAM to checkoutParam)
                  val data = withContext(dispatcher.io) {
                        val graphqlRequest = GraphqlRequest(DealsQuery.mutationDealsCheckoutV2(), DealsCheckoutResponse::class.java, params)
                        graphqlRepository.response(listOf(graphqlRequest))
                  }.getSuccessData<DealsCheckoutResponse>()

                  dealsCheckoutResponseMutable.postValue(data)
            }) {
                  errorGeneralValueMutable.postValue(it)
            }
      }

      fun checkoutGeneralInstant(checkoutParam: DealCheckoutGeneralInstant) {
            launchCatchError(block = {
                  val params = mapOf(PARAM to checkoutParam)
                  val data = withContext(dispatcher.io) {
                        val graphqlRequest = GraphqlRequest(DealsQuery.mutationDealsCheckoutInstant(), DealsCheckoutInstantResponse::class.java, params)
                        graphqlRepository.response(listOf(graphqlRequest))
                  }.getSuccessData<DealsCheckoutInstantResponse>()

                  dealsCheckoutInstantResponseMutable.postValue(data)
            }) {
                  errorGeneralValueMutable.postValue(it)
            }
      }

      fun mapCheckoutDeals(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse, promoCodes: List<String>):
              DealCheckoutGeneral {
            val checkoutGeneral = DealCheckoutGeneral()
            val cartInfo = CartInfo(Gson().toJson(mapToIntMetaData(verify.metadata)), dealsDetail?.checkoutDataType ?: DEFAULT_CHECKOUT_DATA_TYPE)
            checkoutGeneral.carts.businessType = dealsDetail?.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
            checkoutGeneral.carts.promoCodes = promoCodes
            return checkoutGeneral
      }

      fun mapCheckoutDealsInstant(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse, promoCodes: List<String>):
              DealCheckoutGeneralInstant {
            val checkoutGeneral = DealCheckoutGeneralInstant()
            val cartInfo = CartInfo(Gson().toJson(mapToIntMetaData(verify.metadata)), dealsDetail?.checkoutDataType ?: DEFAULT_CHECKOUT_DATA_TYPE)
            checkoutGeneral.carts.businessType = dealsDetail?.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
            checkoutGeneral.carts.promoCodes = promoCodes
            checkoutGeneral.gatewayCode = verify.gatewayCode
            return checkoutGeneral
      }

      fun getMetaDataString(verify: EventVerifyResponse): String {
            return Gson().toJson(mapToIntMetaData(verify.metadata))
      }

      private fun mapToIntMetaData(metaDataResponse: MetaDataResponse): DealsMetaDataCheckout {
            metaDataResponse.apply {
                  return DealsMetaDataCheckout(
                          categoryName = categoryName,
                          error = error,
                          orderTitle = orderTitle,
                          orderSubTitle = orderSubTitle,
                          quantity = quantity,
                          totalPrice = totalPrice,
                          itemIds = convertStringListtoIntList(itemIds),
                          productNames = productNames,
                          productIds = convertStringListtoIntList(productIds),
                          itemMap = mapToItemMapCheckout(itemMap)
                  )
            }
      }

      private fun mapToItemMapCheckout(itemMapResponses: List<ItemMapResponse>): List<ItemMapCheckout> {
            return itemMapResponses.map {
                  ItemMapCheckout(
                          basePrice = it.basePrice.toInt(),
                          categoryId = it.categoryId.toInt(),
                          childCategoryIds = it.childCategoryIds,
                          commission = it.commission,
                          commissionType = it.commissionType,
                          currencyPrice = it.currencyPrice,
                          description = it.description,
                          email = it.email,
                          endTime = it.endTime,
                          error = it.error,
                          flagId = it.flagId.toInt(),
                          id = it.id.toInt(),
                          invoiceId = it.invoiceId.toInt(),
                          invoiceItemId = it.invoiceItemId.toInt(),
                          invoiceStatus = it.invoiceStatus,
                          locationName = it.locationName,
                          locationDesc = it.locationDesc,
                          mobile = it.mobile,
                          name = it.name,
                          orderTraceId = it.orderTraceId,
                          packageId = it.packageId.toIntOrZero(),
                          packageName = it.packageName,
                          paymentType = it.paymentType,
                          price = it.price,
                          productAppUrl = it.productAppUrl,
                          productId = it.productId.toInt(),
                          productImage = it.productImage,
                          productName = it.productName,
                          providerInvoiceCode = it.providerInvoiceCode,
                          providerPackageId = it.providerPackageId,
                          providerScheduleId = it.providerScheduleId,
                          providerTicketId = it.providerTicketId,
                          quantity = it.quantity,
                          scheduleTimestamp = it.scheduleTimestamp.toInt(),
                          startTime = it.startTime,
                          totalPrice = it.totalPrice,
                          productWebUrl = it.productWebUrl,
                          providerId = it.providerId.toInt(),
                          passengerForms = it.passengerForms
                  )
            }
      }

      private fun convertStringListtoIntList(listString: List<String>): List<Int> {
            return listString.map {
                  it.toIntOrZero()
            }
      }

      companion object {
            const val PARAM = "params"
            const val DEFAULT_CHECKOUT_DATA_TYPE = "foodvchr"
      }
}