package com.tokopedia.digital_deals.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.common_entertainment.data.MetaDataResponse
import com.tokopedia.digital_deals.data.CartInfo
import com.tokopedia.digital_deals.data.DealCheckoutGeneral
import com.tokopedia.digital_deals.data.DealCheckoutGeneralInstant
import com.tokopedia.digital_deals.data.DealCheckoutGeneralInstantNoPromo
import com.tokopedia.digital_deals.data.DealCheckoutGeneralNoPromo
import com.tokopedia.digital_deals.data.DealsCheckoutInstantResponse
import com.tokopedia.digital_deals.data.DealsCheckoutResponse
import com.tokopedia.digital_deals.data.DealsGeneral
import com.tokopedia.digital_deals.data.DealsInstant
import com.tokopedia.digital_deals.data.DealsMetaDataCheckout
import com.tokopedia.digital_deals.data.ItemMapCheckout
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealsQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntSafely
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

      fun checkoutGeneral(checkoutParam: DealsGeneral) {
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

      fun checkoutGeneralInstant(checkoutParam: DealsInstant) {
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

        fun mapCheckoutDeals(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse): DealCheckoutGeneralNoPromo {
            val checkoutGeneral = DealCheckoutGeneralNoPromo()
            val cartInfo = CartInfo(Gson().toJson(mapToIntMetaData(verify.metadata)), dealsDetail?.checkoutDataType ?: DEFAULT_CHECKOUT_DATA_TYPE)
            checkoutGeneral.carts.businessType = dealsDetail?.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
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

        fun mapCheckoutDealsInstant(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse): DealCheckoutGeneralInstantNoPromo {
            val checkoutGeneral = DealCheckoutGeneralInstantNoPromo()
            val cartInfo = CartInfo(Gson().toJson(mapToIntMetaData(verify.metadata)), dealsDetail?.checkoutDataType ?: DEFAULT_CHECKOUT_DATA_TYPE)
            checkoutGeneral.carts.businessType = dealsDetail?.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
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
                          basePrice = it.basePrice.toIntSafely().toLong(),
                          categoryId = it.categoryId.toIntSafely().toLong(),
                          childCategoryIds = it.childCategoryIds,
                          commission = it.commission,
                          commissionType = it.commissionType,
                          currencyPrice = it.currencyPrice,
                          description = it.description,
                          email = it.email,
                          endTime = it.endTime,
                          error = it.error,
                          flagId = it.flagId.toIntSafely().toLong(),
                          id = it.id.toIntSafely().toLong(),
                          invoiceId = it.invoiceId.toIntSafely().toLong(),
                          invoiceItemId = it.invoiceItemId.toIntSafely().toLong(),
                          invoiceStatus = it.invoiceStatus,
                          locationName = it.locationName,
                          locationDesc = it.locationDesc,
                          mobile = it.mobile,
                          name = it.name,
                          orderTraceId = it.orderTraceId,
                          packageId = it.packageId.toIntSafely().toLong(),
                          packageName = it.packageName,
                          paymentType = it.paymentType,
                          price = it.price,
                          productAppUrl = it.productAppUrl,
                          productId = it.productId.toIntSafely().toLong(),
                          productImage = it.productImage,
                          productName = it.productName,
                          providerInvoiceCode = it.providerInvoiceCode,
                          providerPackageId = it.providerPackageId,
                          providerScheduleId = it.providerScheduleId,
                          providerTicketId = it.providerTicketId,
                          quantity = it.quantity,
                          scheduleTimestamp = it.scheduleTimestamp.toIntSafely(),
                          startTime = it.startTime,
                          totalPrice = it.totalPrice,
                          productWebUrl = it.productWebUrl,
                          providerId = it.providerId.toIntSafely().toLong(),
                          passengerForms = it.passengerForms
                  )
            }
      }

      private fun convertStringListtoIntList(listString: List<String>): List<Int> {
            return listString.map {
                  it.toIntSafely()
            }
      }

      companion object {
            const val PARAM = "params"
            const val DEFAULT_CHECKOUT_DATA_TYPE = "foodvchr"
      }
}
