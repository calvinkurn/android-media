package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val getEmptyModelsUseCase: GetEmptyModelsUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var travelItemList = mutableListOf<TravelHomepageItemModel>()
    val travelItemListLiveData = MutableLiveData<MutableList<TravelHomepageItemModel>>()
    val isAllError = MutableLiveData<Boolean>()
    private val mapper = TravelHomepageMapper()

    private var calledApiList: HashMap<String, String> = hashMapOf()

    fun getListFromCloud(rawQuery: String, isLoadFromCloud: Boolean) {
        calledApiList = hashMapOf()
        launchCatchError(block = {
            val layoutResult = getEmptyModelsUseCase.getTravelLayoutSubhomepage(rawQuery, isLoadFromCloud)
            if (layoutResult is Success) {
                travelItemList = layoutResult.data.toMutableList()
                travelItemListLiveData.postValue(travelItemList)
            } else isAllError.postValue(true)
        }) {
            isAllError.postValue(true)
        }
    }

    fun getTravelUnifiedData(rawQuery: String, layoutData: TravelLayoutSubhomepage.Data, isFromCloud: Boolean) {
        launch {
            delay(200)
            if (!calledApiList.containsKey(layoutData.position.toString())) {
                calledApiList[layoutData.position.toString()] = layoutData.position.toString()

                val param = mapOf(DATA_TYPE_PARAM to layoutData.dataType,
                        WIDGET_TYPE_PARAM to layoutData.widgetType, "data" to ParamData())

                try {
                    getSubhomepageData(rawQuery, param, isFromCloud).let {
                        val item = mapper.mapToViewModel(layoutData, it)
                        item.isLoaded = true
                        item.isSuccess = true
                        item.isLoadFromCloud = false
                        travelItemList[layoutData.position] = item
                        travelItemListLiveData.postValue(travelItemList)
                    }

                } catch (e: Throwable) {
                    travelItemList[layoutData.position].isLoaded = true
                    travelItemList[layoutData.position].isSuccess = false
                    if (checkIfAllError()) isAllError.postValue(true)
                    else travelItemListLiveData.postValue(travelItemList)
                }
            }
        }
    }

    private suspend fun getSubhomepageData(rawQuery: String, param: Map<String, Any>, isFromCloud: Boolean): List<TravelUnifiedSubhomepageData> {

        delay(300)
        val graphQlCacheStrategy = if (isFromCloud) GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        else GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()

        return withContext(dispatcherProvider.ui()) {
            val response = withContext(dispatcherProvider.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, TravelUnifiedSubhomepageData.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
                        .getSuccessData<TravelUnifiedSubhomepageData.Response>()
            }
            response.response
        }
    }

    private fun checkIfAllError(): Boolean {
        travelItemList.let {
            for (item in it) {
                if (item.isSuccess || !item.isLoaded) return false
            }
            return true
        }
    }

    companion object {
        const val DATA_TYPE_PARAM = "dataType"
        const val WIDGET_TYPE_PARAM = "widgetType"
    }
}