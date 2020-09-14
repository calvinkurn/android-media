package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetStatusShopUseCase
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val getShopStatusUseCase: Lazy<GetStatusShopUseCase>,
        private val userSession: Lazy<UserSessionInterface>,
        private val getTickerUseCase: Lazy<GetTickerUseCase>,
        private val getLayoutUseCase: Lazy<GetLayoutUseCase>,
        private val getShopLocationUseCase: Lazy<GetShopLocationUseCase>,
        private val getCardDataUseCase: Lazy<GetCardDataUseCase>,
        private val getLineGraphDataUseCase: Lazy<GetLineGraphDataUseCase>,
        private val getProgressDataUseCase: Lazy<GetProgressDataUseCase>,
        private val getPostDataUseCase: Lazy<GetPostDataUseCase>,
        private val getCarouselDataUseCase: Lazy<GetCarouselDataUseCase>,
        private val getTableDataUseCase: Lazy<GetTableDataUseCase>,
        private val getPieChartDataUseCase: Lazy<GetPieChartDataUseCase>,
        private val getBarChartDataUseCase: Lazy<GetBarChartDataUseCase>,
        @Named("Main") dispatcher: CoroutineDispatcher
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val SELLER_HOME_PAGE_NAME = "seller-home"
        private const val TICKER_PAGE_NAME = "seller"
    }

    private val shopId: String by lazy { userSession.get().shopId }
    private val dynamicParameter by lazy {
        val startDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7)
        val endDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1)
        return@lazy DynamicParameterModel(
                startDate = DateTimeUtil.format(startDateMillis, DATE_FORMAT),
                endDate = DateTimeUtil.format(endDateMillis, DATE_FORMAT),
                pageSource = SELLER_HOME_PAGE_NAME
        )
    }

    private val _homeTicker = MutableLiveData<Result<List<TickerItemUiModel>>>()
    private val _shopStatus = MutableLiveData<Result<GetShopStatusResponse>>()
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _shopLocation = MutableLiveData<Result<ShippingLoc>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()

    val homeTicker: LiveData<Result<List<TickerItemUiModel>>>
        get() = _homeTicker
    val shopStatus: LiveData<Result<GetShopStatusResponse>>
        get() = _shopStatus
    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
    val shopLocation: LiveData<Result<ShippingLoc>>
        get() = _shopLocation
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

    fun getTicker() {
        launchCatchError(block = {
            val result: Success<List<TickerItemUiModel>> = Success(withContext(Dispatchers.IO) {
                getTickerUseCase.get().params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
                return@withContext getTickerUseCase.get().executeOnBackground()
            })
            _homeTicker.postValue(result)
        }, onError = {
            _homeTicker.postValue(Fail(it))
        })
    }

    fun getShopStatus() = executeCall(_shopStatus) {
        getShopStatusUseCase.get().params = GetStatusShopUseCase.createRequestParams(userSession.get().shopId)
        getShopStatusUseCase.get().executeOnBackground()
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.get().params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
                return@withContext getLayoutUseCase.get().executeOnBackground()
            })
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            val result: Success<ShippingLoc> = Success(withContext(Dispatchers.IO) {
                getShopLocationUseCase.get().params = GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.get().executeOnBackground()
            })
            _shopLocation.postValue(result)
        }, onError = {
            _shopLocation.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CardDataUiModel>> = Success(withContext(Dispatchers.IO) {
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
            val result: Success<List<LineGraphDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.get().params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getLineGraphDataUseCase.get().executeOnBackground()
            })
            _lineGraphWidgetData.postValue(result)
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<ProgressDataUiModel>> = Success(withContext(Dispatchers.IO) {
                val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
                getProgressDataUseCase.get().params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
                return@withContext getProgressDataUseCase.get().executeOnBackground()
            })
            _progressWidgetData.postValue(result)
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<PostListDataUiModel>> = Success(withContext(Dispatchers.IO) {
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
            val result: Success<List<CarouselDataUiModel>> = Success(withContext(Dispatchers.IO) {
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
            val result: Success<List<TableDataUiModel>> = Success(withContext(Dispatchers.IO) {
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
            val result: Success<List<PieChartDataUiModel>> = Success(withContext(Dispatchers.IO) {
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
            val result: Success<List<BarChartDataUiModel>> = Success(withContext(Dispatchers.IO) {
                getBarChartDataUseCase.get().params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getBarChartDataUseCase.get().executeOnBackground()
            })
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}