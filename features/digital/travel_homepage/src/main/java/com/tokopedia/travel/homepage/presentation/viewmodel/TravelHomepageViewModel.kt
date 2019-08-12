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

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val travelItemList = MutableLiveData<Pair<List<TravelHomepageItemModel>, Boolean>>()

    fun getIntialList() {
        val list: List<TravelHomepageItemModel> = listOf(TravelHomepageBannerModel(),
                TravelHomepageCategoryListModel(), TravelHomepageOrderListModel(), TravelHomepageRecentSearchModel(),
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
                    updatedList[0] = data.response
                    updatedList[0].isLoaded = true
                    travelItemList.value = Pair(updatedList, true)
            }
        }) {
            travelItemList.value = travelItemList.value?.copy(second = false)
        }
    }

}