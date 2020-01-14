package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationCityModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationItemModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import com.tokopedia.travelhomepage.destination.model.mapper.TravelDestinationMapper
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyViewModelsUseCase
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
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
        private val graphqlRepository: GraphqlRepository,
        private val getEmptyViewModelsUseCase: GetEmptyViewModelsUseCase,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _travelDestinationItemList = MutableLiveData<List<TravelDestinationItemModel>>()
    val travelDestinationItemList: LiveData<List<TravelDestinationItemModel>>
        get() = _travelDestinationItemList

    private val _travelDestinationCityModel = MutableLiveData<Result<TravelDestinationCityModel>>()
    val travelDestinationCityModel: LiveData<Result<TravelDestinationCityModel>>
        get() = _travelDestinationCityModel

    private val _isAllError = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = _isAllError

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
            _isAllError.postValue(true)
        }
    }

    fun getCityRecommendationData(query: String, cityId: String, product: String, order: Int) {
        launchCatchError(block ={
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_PRODUCT to product, PARAM_CITY_ID to cityId.toInt())
                val graphqlRequest = GraphqlRequest(query, TravelHomepageRecommendationModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecommendationModel.Response>()

            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[order] = mapper.mapToSectionViewModel(data.response, order)
                updatedList[order].isLoaded = true
                updatedList[order].isSuccess = true
                _travelDestinationItemList.postValue(updatedList)
            }

        }) {
            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[order].isLoaded = true
                updatedList[order].isSuccess = false
                _travelDestinationItemList.postValue(updatedList)
                checkIfAllError()
            }
        }

    }

    fun getOrderList(query: String, cityId: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_PAGE to 1, PARAM_PER_PAGE to 10, PARAM_FILTER_STATUS to "success", PARAM_CITY_ID to cityId.toInt())
                val graphqlRequest = GraphqlRequest(query, TravelHomepageOrderListModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageOrderListModel.Response>()

                travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[ORDER_LIST_ORDER] = mapper.mapToSectionViewModel(data.response)
                updatedList[ORDER_LIST_ORDER].isLoaded = true
                updatedList[ORDER_LIST_ORDER].isSuccess = true
                _travelDestinationItemList.value = updatedList
            }
        }) {
            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[ORDER_LIST_ORDER].isLoaded = true
                updatedList[ORDER_LIST_ORDER].isSuccess = false
                _travelDestinationItemList.value = updatedList
                checkIfAllError()
            }
        }
    }

    fun getCityArticles(query: String, cityId: String) {
        launchCatchError(block ={
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_CITY_ID to cityId.toInt())
                val graphqlRequest = GraphqlRequest(query, TravelArticleModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelArticleModel.Response>()

            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CITY_ARTICLE_ORDER] = data.response
                updatedList[CITY_ARTICLE_ORDER].isLoaded = true
                updatedList[CITY_ARTICLE_ORDER].isSuccess = true
                _travelDestinationItemList.postValue(updatedList)
            }

        }) {
            travelDestinationItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CITY_ARTICLE_ORDER].isLoaded = true
                updatedList[CITY_ARTICLE_ORDER].isSuccess = false
                _travelDestinationItemList.postValue(updatedList)
                checkIfAllError()
            }
        }
    }

    private fun checkIfAllError() {
        travelDestinationItemList.value?.let {
            var isSuccess = false
            for (item in it) {
                if (item.isSuccess || !item.isLoaded) {
                    isSuccess = true
                    break
                }
            }
            if (!isSuccess) _isAllError.postValue(true)
        }
    }

    companion object {
        const val PARAM_WEBURLS = "webURLs"
        const val PARAM_CITY_ID = "cityID"

        const val SUMMARY_ORDER = 0
        const val ORDER_LIST_ORDER = 1
        const val CITY_RECOMMENDATION_ORDER = 2
        const val CITY_EVENT_ORDER = 3
        const val CITY_DEALS_ORDER = 4
        const val CITY_ARTICLE_ORDER = 5

        const val PARAM_PAGE = "page"
        const val PARAM_PER_PAGE = "perPage"
        const val PARAM_FILTER_STATUS = "filterStatus"
        const val PARAM_PRODUCT = "product"
    }
}