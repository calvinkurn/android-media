package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.data.mapper.TravelHomepageMapper
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
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
                travelItemList.value = layoutResult.data
                isAllError.value = false
            } else isAllError.value = true
        }) {
            isAllError.value = true
        }
    }

    fun getTravelUnifiedData(rawQuery: String, layoutData: TravelLayoutSubhomepage.Data, position: Int, isFromCloud: Boolean) {

        launchCatchError(block = {
            val data = withContext(dispatcherProvider.ui()) {
                val param = mapOf("dataType" to layoutData.dataType, "widgetType" to layoutData.widgetType, "data" to ParamData())
                val graphqlRequest = GraphqlRequest(rawQuery, TravelUnifiedSubhomepageData.Response::class.java, param)
                var graphQlCacheStrategy = if (isFromCloud) GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                else GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<TravelUnifiedSubhomepageData.Response>()

            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[position] = mapper.mapToViewModel(layoutData, data.response)
                updatedList[position].isLoaded = true
                updatedList[position].isSuccess = true
                travelItemList.value = updatedList
            }
        }) {
            travelItemList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[position].isLoaded = true
                updatedList[position].isSuccess = false
                travelItemList.value = updatedList
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
            isAllError.value = !isSuccess
        }
    }

    data class ParamData(
            @SerializedName("product")
            @Expose
            val product: String = "SUBHOMEPAGE",

            @SerializedName("countryID")
            @Expose
            val countryID: String = "ID",

            @SerializedName("platform")
            @Expose
            val platform: String = "SUBHOMEPAGE"
    )

}