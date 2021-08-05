package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.domain.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.model.listtravelcollectivebanner.PromoChekoutDealsBannerModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.util.HashMap
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class PromoCheckoutListDealsViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                          private val graphqlRepository: GraphqlRepository,
                                                          val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase
                                                          ): BaseViewModel(dispatcher.io) {

    private val _dealsCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val dealsCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _dealsCheckVoucherResult

    private val _listTravelCollectiveBanner = MutableLiveData<Result<List<PromoCheckoutLastSeenModel>>>()
    val listTravelCollectiveBanner: LiveData<Result<List<PromoCheckoutLastSeenModel>>>
        get() = _listTravelCollectiveBanner

    val showLoadingPromoDeals = MutableLiveData<Boolean>()

    fun processCheckDealPromoCode(flag: Boolean, requestParams: JsonObject){
        showLoadingPromoDeals.postValue(true)
        launchCatchError(block = {
            showLoadingPromoDeals.postValue( false)
            val data = withContext(dispatcher.io){
                dealsCheckVoucherUseCase.execute(dealsCheckVoucherUseCase.createMapParam(flag),
                    dealsCheckVoucherUseCase.setDealsVerifyBody(requestParams))
            }
            _dealsCheckVoucherResult.postValue(data)
        }){
            showLoadingPromoDeals.postValue( false)
            _dealsCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getListTravelCollectiveBanner(){
        launchCatchError(block = {
            val data = withContext(dispatcher.io){
                graphqlRepository.getReseponse(listOf(createRequest()))
            }.getSuccessData<PromoChekoutDealsBannerModel.Response>()
            _listTravelCollectiveBanner.postValue(Success(mapToLastSeen(data.response.banners)))
        }){
            _listTravelCollectiveBanner.postValue(Fail(it))
        }

    }

    private fun createRequest(): GraphqlRequest {
        val variables = HashMap<String, Any>()
        return GraphqlRequest(PromoQuery.promoDealsQuery(), PromoChekoutDealsBannerModel.Response::class.java, variables)
    }

    companion object {
        fun mapToLastSeen(data: List<PromoChekoutDealsBannerModel.Banner>?): List<PromoCheckoutLastSeenModel> {
            val mapResult = mutableListOf<PromoCheckoutLastSeenModel>()
            data?.forEachIndexed { index, banner ->
                if (!banner.attribute.promoCode.isBlank() && !banner.attribute.promoCode.equals("-")) {
                    val dataMapper = PromoCheckoutLastSeenModel(
                        id = banner.id.toInt(),
                        title = banner.attribute.description,
                        promoCode = banner.attribute.promoCode,
                        subtitle = banner.product
                    )
                    mapResult.add(dataMapper)
                }
            }
            return mapResult
        }
    }
}