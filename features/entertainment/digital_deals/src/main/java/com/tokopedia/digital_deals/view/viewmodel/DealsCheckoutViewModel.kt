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

      fun mapCheckoutDeals(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse):
              DealCheckoutGeneral {
            val gson = Gson()
            val checkoutGeneral = DealCheckoutGeneral()
            val cartInfo = CartInfo(gson.toJson(verify.metadata), dealsDetail.checkoutDataType)
            checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
            return checkoutGeneral
      }

      fun mapCheckoutDealsInstant(dealsDetail: DealsDetailsResponse, verify: EventVerifyResponse):
              DealCheckoutGeneralInstant {
            val gson = Gson()
            val checkoutGeneral = DealCheckoutGeneralInstant()
            val cartInfo = CartInfo(gson.toJson(verify.metadata), dealsDetail.checkoutDataType)
            checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
            checkoutGeneral.carts.cartInfo.add(0, cartInfo)
            checkoutGeneral.gatewayCode = verify.gatewayCode
            return checkoutGeneral
      }

      companion object {
            const val PARAM = "params"
      }
}