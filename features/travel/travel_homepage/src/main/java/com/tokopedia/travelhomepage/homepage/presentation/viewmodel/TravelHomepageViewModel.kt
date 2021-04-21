package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelhomepage.homepage.data.ParamData
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage
import com.tokopedia.travelhomepage.homepage.data.mapper.TravelHomepageMapper
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.homepage.usecase.GetSubhomepageUnifiedDataUseCase
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageViewModel @Inject constructor(
        private val getEmptyModelsUseCase: GetEmptyModelsUseCase,
        dispatcherProvider: CoroutineDispatchers,
        private val getSubhomepageUnifiedDataUseCase: GetSubhomepageUnifiedDataUseCase)
    : BaseViewModel(dispatcherProvider.io) {

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

    fun getTravelUnifiedData(rawQuery: String, layoutData: TravelLayoutSubhomepage.Data, isFromCloud: Boolean,
                             type: Type) {
        launch {
            delay(200)
            if (!calledApiList.containsKey(layoutData.position.toString())) {
                calledApiList[layoutData.position.toString()] = layoutData.position.toString()

                val param = mapOf(DATA_TYPE_PARAM to layoutData.dataType,
                        WIDGET_TYPE_PARAM to layoutData.widgetType, "data" to ParamData())

                try {
                    getSubhomepageUnifiedDataUseCase.execute(rawQuery, param, isFromCloud, type).let {
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