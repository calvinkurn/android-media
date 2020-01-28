package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhome.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhome.domain.usecase.GetTickerUseCase
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val getTickerUseCase: GetTickerUseCase,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val homeTicker = MutableLiveData<Result<List<TickerUiModel>>>()
    val widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    val lineGraphData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()

    fun getTicker() {
        launchCatchError(block = {
            homeTicker.value = Success(withContext(Dispatchers.IO) {
                getTickerUseCase.executeOnBackground()
            })
        }, onError = {
            homeTicker.value = Fail(it)
        })
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            widgetLayout.value = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.executeOnBackground()
            })
        }, onError = {
            widgetLayout.value = Fail(it)
        })
    }

    fun getLineGraphDataUseCase(shopId: String, dataKeys: List<String>,
                                startDate: String, endDate: String) {
        launchCatchError(block = {
            lineGraphData.value = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)
                val graphData: List<LineGraphDataUiModel> = getLineGraphDataUseCase.executeOnBackground()
                return@withContext graphData
            })
        }, onError = {
            lineGraphData.value = Fail(it)
        })
    }
}