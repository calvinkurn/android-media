package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhomecommon.domain.model.WidgetDataParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getCardDataUseCase: GetCardDataUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        private val getProgressDataUseCase: GetProgressDataUseCase,
        private val getPostDataUseCase: GetPostDataUseCase,
        private val getCarouselDataUseCase: GetCarouselDataUseCase,
        private val getTableDataUseCase: GetTableDataUseCase,
        private val getPieChartDataUseCase: GetPieChartDataUseCase,
        private val getBarChartDataUseCase: GetBarChartDataUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val STATISTIC_PAGE_NAME = "shop-insight"
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
    val cardWidgetData: LiveData<Result<List<CardDataUiModel>>>
        get() = _cardWidgetData
    val lineGraphWidgetData: LiveData<Result<List<LineGraphDataUiModel>>>
        get() = _lineGraphWidgetData
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

    private val shopId by lazy { userSession.shopId }
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()

    private var dynamicParameter = WidgetDataParameterModel()

    fun setDateRange(startDate: Date, endDate: Date) {
        val startDateFmt = DateTimeUtil.format(startDate.time, DATE_FORMAT)
        val endDateFmt = DateTimeUtil.format(endDate.time, DATE_FORMAT)
        this.dynamicParameter = WidgetDataParameterModel(
                startDate = startDateFmt,
                endDate = endDateFmt,
                pageSource = STATISTIC_PAGE_NAME
        )
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, STATISTIC_PAGE_NAME)
                return@withContext getLayoutUseCase.executeOnBackground()
            })
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CardDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getCardDataUseCase.executeOnBackground()
            })
            _cardWidgetData.postValue(result)
        }, onError = {
            _cardWidgetData.postValue(Fail(it))
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<LineGraphDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getLineGraphDataUseCase.executeOnBackground()
            })
            _lineGraphWidgetData.postValue(result)
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<ProgressDataUiModel>> = Success(withContext(Dispatchers.IO) {
                val today: String = DateTimeUtil.format(Date().time, DATE_FORMAT)
                getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
                return@withContext getProgressDataUseCase.executeOnBackground()
            })
            _progressWidgetData.postValue(result)
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<PostListDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getPostDataUseCase.executeOnBackground()
            })
            _postListWidgetData.postValue(result)
        }, onError = {
            _postListWidgetData.postValue(Fail(it))
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CarouselDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)
                return@withContext getCarouselDataUseCase.executeOnBackground()
            })
            _carouselWidgetData.postValue(result)
        }, onError = {
            _carouselWidgetData.postValue(Fail(it))
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<TableDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getTableDataUseCase.params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getTableDataUseCase.executeOnBackground()
            })
            _tableWidgetData.postValue(result)
        }, onError = {
            _tableWidgetData.postValue(Fail(it))
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<PieChartDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getPieChartDataUseCase.params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getPieChartDataUseCase.executeOnBackground()
            })
            _pieChartWidgetData.postValue(result)
        }, onError = {
            _pieChartWidgetData.postValue(Fail(it))
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<BarChartDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getBarChartDataUseCase.executeOnBackground()
            })
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}