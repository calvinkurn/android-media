package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.destination.model.TravelDestinationCityModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.destination.model.mapper.TravelDestinationMapper
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyViewModelsUseCase
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.homepage.presentation.viewmodel.TravelHomepageViewModel.Companion.PARAM_PRODUCT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 2020-01-02
 */

class TravelDestinationViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        private val getEmptyViewModelsUseCase: GetEmptyViewModelsUseCase,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _travelDestinationItemList = MutableLiveData<List<TravelDestinationItemModel>>()
    val travelDestinationItemList: LiveData<List<TravelDestinationItemModel>>
        get() = _travelDestinationItemList

    private val _travelDestinationCityModel = MutableLiveData<Result<TravelDestinationCityModel>>()
    val travelDestinationCityModel: LiveData<Result<TravelDestinationCityModel>>
        get() = _travelDestinationCityModel

    private val mapper = TravelDestinationMapper()

    fun getInitialList() {
        _travelDestinationItemList.value = getEmptyViewModelsUseCase.requestEmptyViewModels()
    }

    fun getDestinationCityData(query: String, webUrl: String) {
        launchCatchError( block = {
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_WEBURLS to webUrl)
                val graphqlRequest = GraphqlRequest(query, TravelDestinationCityModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelDestinationCityModel.Response>().response

            _travelDestinationCityModel.postValue(Success(data))
        }) {
            _travelDestinationCityModel.postValue(Fail(it))
        }
    }


    fun getDestinationSummaryData(query: String, cityId: String) {
        launchCatchError( block = {
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_CITY_ID to cityId)
                val graphqlRequest = GraphqlRequest(query, TravelDestinationSummaryModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelDestinationSummaryModel.Response>()

           travelDestinationItemList.value?.let {
               val updatedList = it.toMutableList()
               data.response.isLoaded = true
               data.response.isSuccess = true
               updatedList[SUMMARY_ORDER] = data.response
               _travelDestinationItemList.postValue(updatedList)
           }
        }) {

        }
    }

    fun getCityRecommendationData(query: String, cityId: String) {
        launchCatchError(block ={
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_PRODUCT to "ALL", PARAM_CITY_ID to cityId.toInt())
                val graphqlRequest = GraphqlRequest(query, TravelHomepageRecommendationModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecommendationModel.Response>()

            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CITY_RECOMMENDATION_ORDER] = mapper.mapToSectionViewModel(data.response, CITY_RECOMMENDATION_ORDER)
                updatedList[CITY_RECOMMENDATION_ORDER].isLoaded = true
                updatedList[CITY_RECOMMENDATION_ORDER].isSuccess = true
                _travelDestinationItemList.postValue(updatedList)
            }

        }) {

        }

    }

    fun getCityDeals(query: String, cityId: String) {
        launchCatchError(block ={
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_PRODUCT to "DEALS", PARAM_CITY_ID to cityId.toInt())
                val graphqlRequest = GraphqlRequest(query, TravelHomepageRecommendationModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecommendationModel.Response>()

            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CITY_DEALS_ORDER] = mapper.mapToSectionViewModel(data.response, CITY_DEALS_ORDER)
                updatedList[CITY_DEALS_ORDER].isLoaded = true
                updatedList[CITY_DEALS_ORDER].isSuccess = true
                _travelDestinationItemList.postValue(updatedList)
            }

        }) {

        }
    }

    companion object {
        const val PARAM_WEBURLS = "webURLs"
        const val PARAM_CITY_ID = "cityID"

        val SUMMARY_ORDER = 0
        val CITY_RECOMMENDATION_ORDER = 1
        val CITY_DEALS_ORDER = 2
    }
}