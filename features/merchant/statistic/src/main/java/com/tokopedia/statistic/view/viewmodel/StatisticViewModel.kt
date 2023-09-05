package com.tokopedia.statistic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.ParamTableWidgetModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.GetAnnouncementDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetBarChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCarouselDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiComponentDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiLineGraphUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPieChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPostDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetProgressDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTableDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTickerUseCase
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.Flow
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getTickerUseCase: Lazy<GetTickerUseCase>,
    private val getLayoutUseCase: Lazy<GetLayoutUseCase>,
    private val getCardDataUseCase: Lazy<GetCardDataUseCase>,
    private val getLineGraphDataUseCase: Lazy<GetLineGraphDataUseCase>,
    private val getMultiLineGraphUseCase: Lazy<GetMultiLineGraphUseCase>,
    private val getProgressDataUseCase: Lazy<GetProgressDataUseCase>,
    private val getPostDataUseCase: Lazy<GetPostDataUseCase>,
    private val getCarouselDataUseCase: Lazy<GetCarouselDataUseCase>,
    private val getTableDataUseCase: Lazy<GetTableDataUseCase>,
    private val getPieChartDataUseCase: Lazy<GetPieChartDataUseCase>,
    private val getBarChartDataUseCase: Lazy<GetBarChartDataUseCase>,
    private val getAnnouncementDataUseCase: Lazy<GetAnnouncementDataUseCase>,
    private val getMultiComponentDataUseCase: Lazy<GetMultiComponentDataUseCase>,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
    val tickers: LiveData<Result<List<TickerItemUiModel>>>
        get() = _tickers
    val cardWidgetData: LiveData<Result<List<CardDataUiModel>>>
        get() = _cardWidgetData
    val lineGraphWidgetData: LiveData<Result<List<LineGraphDataUiModel>>>
        get() = _lineGraphWidgetData
    val multiLineGraphWidgetData: LiveData<Result<List<MultiLineGraphDataUiModel>>>
        get() = _multiLineGraphWidgetData
    val progressWidgetData: LiveData<Result<List<ProgressDataUiModel>>>
        get() = _progressWidgetData
    val postListWidgetData: LiveData<Result<List<PostListDataUiModel>>>
        get() = _postListWidgetData
    val carouselWidgetData: LiveData<Result<List<CarouselDataUiModel>>>
        get() = _carouselWidgetData
    val tableWidgetData: LiveData<Result<List<TableDataUiModel>>>
        get() = _tableWidgetData
    val pieChartWidgetData: LiveData<Result<List<PieChartDataUiModel>>>
        get() = _pieChartWidgetData
    val barChartWidgetData: LiveData<Result<List<BarChartDataUiModel>>>
        get() = _barChartWidgetData
    val announcementWidgetData: LiveData<Result<List<AnnouncementDataUiModel>>>
        get() = _announcementWidgetData
    val multiComponentWidgetData: LiveData<Result<List<MultiComponentDataUiModel>>>
        get() = _multiComponentWidgetData

    private val shopId by lazy { userSession.shopId }
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _tickers = MutableLiveData<Result<List<TickerItemUiModel>>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _multiLineGraphWidgetData =
        MutableLiveData<Result<List<MultiLineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()
    private val _announcementWidgetData = MutableLiveData<Result<List<AnnouncementDataUiModel>>>()
    private val _multiComponentWidgetData = MutableLiveData<Result<List<MultiComponentDataUiModel>>>()
    private val _multiComponentTabsData = MutableSharedFlow<Map<MultiComponentTab, List<BaseDataUiModel>>>()

    private var dynamicParameter = ParamCommonWidgetModel()

    fun setDateFilter(pageSource: String, startDate: Date, endDate: Date, filterType: String) {
        val startDateFmt = DateTimeUtil.format(startDate.time, DATE_FORMAT)
        val endDateFmt = DateTimeUtil.format(endDate.time, DATE_FORMAT)
        this.dynamicParameter = ParamCommonWidgetModel(
            startDate = startDateFmt,
            endDate = endDateFmt,
            pageSource = pageSource,
            dateType = filterType
        )
    }

    fun getWidgetLayout(pageSource: String) {
        launchCatchError(context = dispatcher.io, block = {
            val result = Success(withContext(dispatcher.io) {
                getLayoutUseCase.get().params =
                    GetLayoutUseCase.getRequestParams(shopId, pageSource)
                return@withContext getLayoutUseCase.get().executeOnBackground().widgetList
            })
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getTickers(tickerPageName: String) {
        launchCatchError(block = {
            val result: Success<List<TickerItemUiModel>> = Success(withContext(dispatcher.io) {
                getTickerUseCase.get().params = GetTickerUseCase.createParams(tickerPageName)
                return@withContext getTickerUseCase.get().executeOnBackground()
            })
            _tickers.postValue(result)
        }, onError = {
            _tickers.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CardDataUiModel>> = Success(withContext(dispatcher.io) {
                getCardDataUseCase.get().params =
                    GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getCardDataUseCase.get().executeOnBackground()
            })
            _cardWidgetData.postValue(result)
        }, onError = {
            _cardWidgetData.postValue(Fail(it))
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<LineGraphDataUiModel>> = Success(withContext(dispatcher.io) {
                getLineGraphDataUseCase.get().params = GetLineGraphDataUseCase.getRequestParams(
                    dataKey = dataKeys,
                    dynamicParam = dynamicParameter
                )
                return@withContext getLineGraphDataUseCase.get().executeOnBackground()
            })
            _lineGraphWidgetData.postValue(result)
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getMultiLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<MultiLineGraphDataUiModel>> =
                Success(withContext(dispatcher.io) {
                    val useCase = getMultiLineGraphUseCase.get()
                    useCase.params = GetMultiLineGraphUseCase.getRequestParams(
                        dataKey = dataKeys,
                        dynamicParam = dynamicParameter
                    )
                    return@withContext useCase.executeOnBackground()
                })
            _multiLineGraphWidgetData.value = result
        }, onError = {
            _multiLineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<ProgressDataUiModel>> = Success(withContext(dispatcher.io) {
                val today: String = DateTimeUtil.format(Date().time, DATE_FORMAT)
                getProgressDataUseCase.get().params =
                    GetProgressDataUseCase.getRequestParams(today, dataKeys)
                return@withContext getProgressDataUseCase.get().executeOnBackground()
            })
            _progressWidgetData.postValue(result)
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<TableAndPostDataKey>) {
        launchCatchError(block = {
            val result: Success<List<PostListDataUiModel>> = Success(withContext(dispatcher.io) {
                getPostDataUseCase.get().params = GetPostDataUseCase.getRequestParams(
                    dataKey = dataKeys,
                    startDate = dynamicParameter.startDate,
                    endDate = dynamicParameter.endDate
                )
                return@withContext getPostDataUseCase.get().executeOnBackground()
            })
            _postListWidgetData.postValue(result)
        }, onError = {
            _postListWidgetData.postValue(Fail(it))
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CarouselDataUiModel>> = Success(withContext(dispatcher.io) {
                getCarouselDataUseCase.get().params =
                    GetCarouselDataUseCase.getRequestParams(dataKeys)
                return@withContext getCarouselDataUseCase.get().executeOnBackground()
            })
            _carouselWidgetData.postValue(result)
        }, onError = {
            _carouselWidgetData.postValue(Fail(it))
        })
    }

    fun getTableWidgetData(dataKeys: List<TableAndPostDataKey>) {
        launchCatchError(block = {
            val result: Success<List<TableDataUiModel>> = Success(withContext(dispatcher.io) {
                return@withContext getTableData(dataKeys)
            })
            _tableWidgetData.postValue(result)
        }, onError = {
            _tableWidgetData.postValue(Fail(it))
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<PieChartDataUiModel>> = Success(withContext(dispatcher.io) {
                return@withContext getPieChartData(dataKeys)
            })
            _pieChartWidgetData.postValue(result)
        }, onError = {
            _pieChartWidgetData.postValue(Fail(it))
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<BarChartDataUiModel>> = Success(withContext(dispatcher.io) {
                return@withContext getBarChartData(dataKeys)
            })
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }

    fun getAnnouncementWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getAnnouncementDataUseCase.get().params =
                GetAnnouncementDataUseCase.createRequestParams(dataKeys)
            val result = Success(withContext(dispatcher.io) {
                return@withContext getAnnouncementDataUseCase.get().executeOnBackground()
            })
            _announcementWidgetData.postValue(result)
        }, onError = {
            _announcementWidgetData.postValue(Fail(it))
        })
    }

    fun getMultiComponentWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getMultiComponentDataUseCase.get().params =
                GetMultiComponentDataUseCase.createRequestParams(dataKeys)
            val result = Success(withContext(dispatcher.io) {
                return@withContext getMultiComponentDataUseCase.get().executeOnBackground()
            })
            _multiComponentWidgetData.postValue(result)
        }, onError = {
            _multiComponentWidgetData.postValue(Fail(it))
        })
    }

    fun getMultiComponentInfoWidgetData(
        tab: MultiComponentTab,
        components: List<MultiComponentData>
    ) {
        launchCatchError(block = {
            val 
            _multiComponentTabsData.emit(
                mapOf(tab, )
            )
        })
    }

    private suspend fun getComponentData(component: MultiComponentData): List<BaseDataUiModel>? {
        return when (component.componentType) {
            WidgetType.BAR_CHART -> getBarChartData(listOf(component.dataKey))
            WidgetType.PIE_CHART -> getPieChartData(listOf(component.dataKey))
            WidgetType.TABLE -> {
                val dataKeys = getPieChartDataKeys(component)
                getTableData(dataKeys)
            }
            else -> null
        }
    }

    private suspend fun getBarChartData(dataKeys: List<String>): List<BarChartDataUiModel> {
        getBarChartDataUseCase.get().params =
            GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        return getBarChartDataUseCase.get().executeOnBackground()
    }

    private suspend fun getTableData(dataKeys: List<TableAndPostDataKey>): List<TableDataUiModel> {
        val dynamicParam = ParamTableWidgetModel(
            startDate = dynamicParameter.startDate,
            endDate = dynamicParameter.endDate,
            pageSource = dynamicParameter.pageSource
        )
        val param = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParam)
        getTableDataUseCase.get().params = param
        return getTableDataUseCase.get().executeOnBackground()
    }

    private suspend fun getPieChartData(dataKeys: List<String>): List<PieChartDataUiModel> {
        getPieChartDataUseCase.get().params =
            GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        return getPieChartDataUseCase.get().executeOnBackground()
    }

    private fun getPieChartDataKeys(component: MultiComponentData): List<TableAndPostDataKey> {
        // TODO: get from configuration
        return listOf(
            TableAndPostDataKey(
                component.dataKey,
                String.EMPTY,
                0,
                0
            )
        )
    }

}
