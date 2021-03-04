package com.tokopedia.statistic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getTickerUseCase: Lazy<GetTickerUseCase>,
        private val getUserRoleUseCase: Lazy<GetUserRoleUseCase>,
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
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
    val tickers: LiveData<Result<List<TickerItemUiModel>>>
        get() = _tickers
    val userRole: LiveData<Result<List<String>>>
        get() = _userRole
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

    private val shopId by lazy { userSession.shopId }
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _tickers = MutableLiveData<Result<List<TickerItemUiModel>>>()
    private val _userRole = MutableLiveData<Result<List<String>>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _multiLineGraphWidgetData = MutableLiveData<Result<List<MultiLineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()

    private var dynamicParameter = DynamicParameterModel()

    fun setDateFilter(pageSource: String, startDate: Date, endDate: Date, filterType: String) {
        val startDateFmt = DateTimeUtil.format(startDate.time, DATE_FORMAT)
        val endDateFmt = DateTimeUtil.format(endDate.time, DATE_FORMAT)
        this.dynamicParameter = DynamicParameterModel(
                startDate = startDateFmt,
                endDate = endDateFmt,
                pageSource = pageSource,
                dateType = filterType
        )
    }

    fun getWidgetLayout(pageSource: String) {
        launchCatchError(context = dispatcher.io, block = {
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(withContext(dispatcher.io) {
                getLayoutUseCase.get().params = GetLayoutUseCase.getRequestParams(shopId, pageSource)
                return@withContext getLayoutUseCase.get().executeOnBackground()
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

    fun getUserRole() {
        launchCatchError(block = {
            val result: Success<List<String>> = Success(withContext(dispatcher.io) {
                getUserRoleUseCase.get().params = GetUserRoleUseCase.createParam(userSession.userId.toIntOrZero())
                return@withContext getUserRoleUseCase.get().executeOnBackground()
            })
            _userRole.postValue(result)
        }, onError = {
            _userRole.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CardDataUiModel>> = Success(withContext(dispatcher.io) {
                getCardDataUseCase.get().params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
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
                getLineGraphDataUseCase.get().params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getLineGraphDataUseCase.get().executeOnBackground()
            })
            _lineGraphWidgetData.postValue(result)
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getMultiLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<MultiLineGraphDataUiModel>> = Success(withContext(dispatcher.io) {
                getMultiLineGraphUseCase.get().params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getMultiLineGraphUseCase.get().executeOnBackground()
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
                getProgressDataUseCase.get().params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
                return@withContext getProgressDataUseCase.get().executeOnBackground()
            })
            _progressWidgetData.postValue(result)
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<Pair<String, String>>) {
        launchCatchError(block = {
            val result: Success<List<PostListDataUiModel>> = Success(withContext(dispatcher.io) {
                getPostDataUseCase.get().params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
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
                getCarouselDataUseCase.get().params = GetCarouselDataUseCase.getRequestParams(dataKeys)
                return@withContext getCarouselDataUseCase.get().executeOnBackground()
            })
            _carouselWidgetData.postValue(result)
        }, onError = {
            _carouselWidgetData.postValue(Fail(it))
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<TableDataUiModel>> = Success(withContext(dispatcher.io) {
                getTableDataUseCase.get().params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getTableDataUseCase.get().executeOnBackground()
            })
            _tableWidgetData.postValue(result)
        }, onError = {
            _tableWidgetData.postValue(Fail(it))
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<PieChartDataUiModel>> = Success(withContext(dispatcher.io) {
                getPieChartDataUseCase.get().params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getPieChartDataUseCase.get().executeOnBackground()
            })
            _pieChartWidgetData.postValue(result)
        }, onError = {
            _pieChartWidgetData.postValue(Fail(it))
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<BarChartDataUiModel>> = Success(withContext(dispatcher.io) {
                getBarChartDataUseCase.get().params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getBarChartDataUseCase.get().executeOnBackground()
            })
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}