package com.tokopedia.travel.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travel.homepage.data.*
import com.tokopedia.travel.homepage.data.mapper.TravelHomepageMapper
import com.tokopedia.travel.homepage.usecase.GetEmptyViewModelsUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val userSessionInterface: UserSessionInterface,
        private val getEmptyViewModelsUseCase: GetEmptyViewModelsUseCase,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val travelItemList = MutableLiveData<List<TravelHomepageItemModel>>()
    private val mapper = TravelHomepageMapper()

    fun getIntialList() {
        val list: List<TravelHomepageItemModel> = getEmptyViewModelsUseCase.requestEmptyViewModels()

        travelItemList.value = list
    }

    fun getBanner(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageBannerModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageBannerModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[BANNER_ORDER] = data.response
                updatedList[BANNER_ORDER].isLoaded = true
                updatedList[BANNER_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[BANNER_ORDER].isLoaded = true
                updatedList[BANNER_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    fun getCategories(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageCategoryListModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageCategoryListModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CATEGORIES_ORDER] = data.response
                updatedList[CATEGORIES_ORDER].isLoaded = true
                updatedList[CATEGORIES_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CATEGORIES_ORDER].isLoaded = true
                updatedList[CATEGORIES_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    fun getOrderList(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val param = mapOf(PARAM_PAGE to 1, PARAM_PER_PAGE to 10, PARAM_FILTER_STATUS to "success")
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageOrderListModel.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageOrderListModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[ORDER_LIST_ORDER] = mapper.mapToSectionViewModel(data.response)
                updatedList[ORDER_LIST_ORDER].isLoaded = true
                updatedList[ORDER_LIST_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[ORDER_LIST_ORDER].isLoaded = true
                updatedList[ORDER_LIST_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    fun getRecentSearch(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageRecentSearchModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecentSearchModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[RECENT_SEARCHES_ORDER] = mapper.mapToSectionViewModel(data.response)
                updatedList[RECENT_SEARCHES_ORDER].isLoaded = true
                updatedList[RECENT_SEARCHES_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[RECENT_SEARCHES_ORDER].isLoaded = true
                updatedList[RECENT_SEARCHES_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    fun getRecommendation(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageRecommendationModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecommendationModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[RECOMMENDATION_ORDER] = mapper.mapToSectionViewModel(data.response)
                updatedList[RECOMMENDATION_ORDER].isLoaded = true
                updatedList[RECOMMENDATION_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[RECOMMENDATION_ORDER].isLoaded = true
                updatedList[RECOMMENDATION_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    fun getDestination(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageDestinationModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageDestinationModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[DESTINATION_ORDER] = data.response
                updatedList[DESTINATION_ORDER].isLoaded = true
                updatedList[DESTINATION_ORDER].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[DESTINATION_ORDER].isLoaded = true
                updatedList[DESTINATION_ORDER].isSuccess = false
                travelItemList.value = updatedList
            }
        }
    }

    companion object {
        val BANNER_ORDER = 0
        val CATEGORIES_ORDER = 1
        val ORDER_LIST_ORDER = 2
        val RECENT_SEARCHES_ORDER = 3
        val RECOMMENDATION_ORDER = 4
        val DESTINATION_ORDER = 5

        val PARAM_PAGE = "page"
        val PARAM_PER_PAGE = "perPage"
        val PARAM_FILTER_STATUS = "filterStatus"
    }

}