package com.tokopedia.travel.homepage.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travel.homepage.data.*
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val userSessionInterface: UserSessionInterface,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val travelItemList = MutableLiveData<Pair<List<TravelHomepageItemModel>, Boolean>>()

    fun getIntialList() {
        val list: List<TravelHomepageItemModel> =
                listOf(TravelHomepageBannerModel(),
                TravelHomepageCategoryListModel(),
                TravelHomepageOrderListModel(),
                TravelHomepageRecentSearchModel(),
                TravelHomepageRecommendationModel())

        travelItemList.value = Pair(list, true)
    }

    fun getBanner(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageBannerModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageBannerModel.Response>()

            travelItemList.value?.let {
                    val updatedList = it.first.toMutableList()
                    updatedList[BANNER_ORDER] = data.response
                    updatedList[BANNER_ORDER].isLoaded = true
                    travelItemList.value = Pair(updatedList, true)
            }
        }) {
            travelItemList.value = travelItemList.value?.copy(second = false)
        }
    }

    fun getCategories(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageCategoryListModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageCategoryListModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.first.toMutableList()
                updatedList[CATEGORIES_ORDER] = data.response
                updatedList[CATEGORIES_ORDER].isLoaded = true
                travelItemList.value = Pair(updatedList, true)
            }
        }) {
            travelItemList.value = travelItemList.value?.copy(second = false)
        }
    }

    fun getOrderList(rawQuery: String) {
        if (userSessionInterface.isLoggedIn) {
            launchCatchError(block = {
                val data = withContext(Dispatchers.Default) {
                    val param = mapOf(PARAM_PAGE to 1, PARAM_PER_PAGE to 10, PARAM_FILTER_STATUS to "success" )
                    val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageOrderListModel.Response::class.java, param)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData<TravelHomepageOrderListModel.Response>()

                travelItemList.value?.let {
                    val updatedList = it.first.toMutableList()
                    updatedList[ORDER_LIST_ORDER] = data.response
                    updatedList[ORDER_LIST_ORDER].isLoaded = true
                    travelItemList.value = Pair(updatedList, true)
                }
            }) {
                travelItemList.value = travelItemList.value?.copy(second = false)
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
                val updatedList = it.first.toMutableList()
                updatedList[RECENT_SEARCHES_ORDER] = data.response
                updatedList[RECENT_SEARCHES_ORDER].isLoaded = true
                travelItemList.value = Pair(updatedList, true)
            }
        }) {
            travelItemList.value = travelItemList.value?.copy(second = false)
        }
    }

    fun getRecommendation(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelHomepageRecommendationModel.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TravelHomepageRecommendationModel.Response>()

            travelItemList.value?.let {
                val updatedList = it.first.toMutableList()
                updatedList[RECOMMENDATION_ORDER] = data.response
                updatedList[RECOMMENDATION_ORDER].isLoaded = true
                travelItemList.value = Pair(updatedList, true)
            }
        }) {
            travelItemList.value = travelItemList.value?.copy(second = false)
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