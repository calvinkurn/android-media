package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.homepage.data.ParamData
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage
import com.tokopedia.travelhomepage.homepage.data.TravelUnifiedSubhomepageData
import com.tokopedia.travelhomepage.homepage.data.mapper.TravelHomepageMapper
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val getEmptyModelsUseCase: GetEmptyModelsUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    val travelItemList = MutableLiveData<List<TravelHomepageItemModel>>()
    val isAllError = MutableLiveData<Boolean>()
    private val mapper = TravelHomepageMapper()

    fun getListFromCloud(rawQuery: String, isLoadFromCloud: Boolean) {
        launchCatchError(block = {
            val layoutResult = getEmptyModelsUseCase.getTravelLayoutSubhomepage(rawQuery, isLoadFromCloud)
            if (layoutResult is Success) {
                travelItemList.postValue(layoutResult.data)
                isAllError.postValue(false)
            } else checkIfAllError()
        }) {
            checkIfAllError()
        }
    }

    fun getTravelUnifiedData(rawQuery: String, layoutData: TravelLayoutSubhomepage.Data, position: Int, isFromCloud: Boolean) {

        launchCatchError(block = {
            val data = withContext(dispatcherProvider.ui()) {
                val param = mapOf(DATA_TYPE_PARAM to layoutData.dataType, WIDGET_TYPE_PARAM to layoutData.widgetType, "data" to ParamData())
                val graphqlRequest = GraphqlRequest(rawQuery, TravelUnifiedSubhomepageData.Response::class.java, param)
                var graphQlCacheStrategy = if (isFromCloud) GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                else GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<TravelUnifiedSubhomepageData.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[layoutData.position] = mapper.mapToViewModel(layoutData, data.response)
                updatedList[layoutData.position].isLoaded = true
                updatedList[layoutData.position].isSuccess = true
                travelItemList.postValue(updatedList)
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[layoutData.position].isLoaded = true
                updatedList[layoutData.position].isSuccess = false
                travelItemList.postValue(updatedList)
            }
            checkIfAllError()
        }
    }

    private fun checkIfAllError() {
        travelItemList.value?.let {
            var isSuccess = false
            for (item in it) {
                if (item.isSuccess || !item.isLoaded) {
                    isSuccess = true
                    break
                }
            }
            isAllError.postValue(!isSuccess)
        }
    }

    companion object {
        const val DATA_TYPE_PARAM = "dataType"
        const val WIDGET_TYPE_PARAM = "widgetType"
    }
}