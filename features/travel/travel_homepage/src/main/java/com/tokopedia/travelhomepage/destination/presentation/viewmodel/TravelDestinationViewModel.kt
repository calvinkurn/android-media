package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.destination.model.*
import com.tokopedia.travelhomepage.destination.model.mapper.TravelDestinationMapper
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyModelsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 2020-01-02
 */

class TravelDestinationViewModel  @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val getEmptyModelsUseCase: GetEmptyModelsUseCase,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    private val travelDestinationItemListMutable = MutableLiveData<List<TravelDestinationItemModel>>()
    val travelDestinationItemList: LiveData<List<TravelDestinationItemModel>>
        get() = travelDestinationItemListMutable

    private val travelDestinationCityModelMutable = MutableLiveData<Result<TravelDestinationCityModel>>()
    val travelDestinationCityModel: LiveData<Result<TravelDestinationCityModel>>
        get() = travelDestinationCityModelMutable

    private val isAllErrorMutable = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = isAllErrorMutable

    private val mapper = TravelDestinationMapper()

    fun getInitialList() {
        travelDestinationItemListMutable.postValue(getEmptyModelsUseCase.requestEmptyViewModels())
    }

    fun getAllContent(summaryQuery: String, recommQuery: String, orderQuery: String, articleQuery: String, cityId: String) {
        launch {
            val list: MutableList<TravelDestinationItemModel> = mutableListOf()

            getDestinationSummaryData(summaryQuery, cityId)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
                travelDestinationItemListMutable.postValue(list)
            }

            getOrderList(orderQuery, cityId)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
            }

            getCityRecommendationData(recommQuery, cityId, "ALL", CITY_RECOMMENDATION_ORDER)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
            }

            getCityRecommendationData(recommQuery, cityId, "EVENTS", CITY_EVENT_ORDER)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
            }

            getCityRecommendationData(recommQuery, cityId, "DEALS", CITY_DEALS_ORDER)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
            }

            getCityArticles(articleQuery, cityId)?.let {
                it.isLoaded = true
                it.isSuccess = true
                list.add(it)
            }

            travelDestinationItemListMutable.postValue(list)
        }
    }

    fun getDestinationCityData(query: String, webUrl: String) {
        launchCatchError( block = {
            val data = withContext(dispatcherProvider.main) {
                val param = mapOf(PARAM_WEBURLS to webUrl)
                val graphqlRequest = GraphqlRequest(query, TravelDestinationCityModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelDestinationCityModel.Response>().response

            travelDestinationCityModelMutable.postValue(Success(data))
        }) {
            travelDestinationCityModelMutable.postValue(Fail(it))
        }
    }

    suspend fun getDestinationSummaryData(query: String, cityId: String): TravelDestinationSummaryModel? {
        return try {
            val summaryData = async {
                val data = withContext(dispatcherProvider.main) {
                    val param = mapOf(PARAM_CITY_ID to cityId)
                    val graphqlRequest = GraphqlRequest(query, TravelDestinationSummaryModel.Response::class.java, param)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<TravelDestinationSummaryModel.Response>().response
                data
            }

            summaryData.await()
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun getCityRecommendationData(query: String, cityId: String, product: String, order: Int): TravelDestinationSectionModel? {
        return try {
            val recommendationData = async {
                val data = withContext(dispatcherProvider.main) {
                    val param = mapOf(PARAM_PRODUCT to product, PARAM_CITY_ID to cityId.toInt())
                    val graphqlRequest = GraphqlRequest(query, TravelHomepageRecommendationModel.Response::class.java, param)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<TravelHomepageRecommendationModel.Response>()
                data
            }

            mapper.mapToSectionViewModel(recommendationData.await().response, order)
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun getOrderList(query: String, cityId: String): TravelDestinationSectionModel? {
        return try {
            val orderData = async {
                val data = withContext(dispatcherProvider.main) {
                    val param = mapOf(PARAM_PAGE to 1, PARAM_PER_PAGE to 10, PARAM_FILTER_STATUS to "success", PARAM_CITY_ID to cityId.toInt())
                    val graphqlRequest = GraphqlRequest(query, TravelHomepageOrderListModel.Response::class.java, param)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<TravelHomepageOrderListModel.Response>()
                data
            }

            mapper.mapToSectionViewModel(orderData.await().response)
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun getCityArticles(query: String, cityId: String): TravelArticleModel? {
        return try {
            val articlesData = async {
                val data = withContext(dispatcherProvider.main) {
                    val param = mapOf(PARAM_CITY_ID to cityId.toInt())
                    val graphqlRequest = GraphqlRequest(query, TravelArticleModel.Response::class.java, param)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<TravelArticleModel.Response>()
                data
            }
            articlesData.await().response
        } catch (t: Throwable) {
            null
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