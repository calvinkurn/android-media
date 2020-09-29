package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.menu.common.coroutine.SellerHomeCoroutineDispatcher
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
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
        private val dispatcher: SellerHomeCoroutineDispatcher
) : CustomBaseViewModel(dispatcher.io()) {

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
                pageSource = SELLER_HOME_PAGE_NAME,
                dateType = DateFilterType.DATE_TYPE_DAY
        )
    }

    private val _homeTicker = MutableLiveData<Result<List<TickerItemUiModel>>>()
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
            getTickerUseCase.get().params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
            val result: Success<List<TickerItemUiModel>> = Success(getTickerUseCase.get().executeOnBackground())
            _homeTicker.postValue(result)
        }, onError = {
            _homeTicker.postValue(Fail(it))
        })
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            getLayoutUseCase.get().params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(getLayoutUseCase.get().executeOnBackground())
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            getShopLocationUseCase.get().params = GetShopLocationUseCase.getRequestParams(shopId)
            val result: Success<ShippingLoc> = Success(getShopLocationUseCase.get().executeOnBackground())
            _shopLocation.postValue(result)
        }, onError = {
            _shopLocation.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getCardDataUseCase.get().params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<CardDataUiModel>> = Success(getCardDataUseCase.get().executeOnBackground())
            _cardWidgetData.postValue(result)
        }, onError = {
            _cardWidgetData.postValue(Fail(it))
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getLineGraphDataUseCase.get().params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<LineGraphDataUiModel>> = Success(getLineGraphDataUseCase.get().executeOnBackground())
            _lineGraphWidgetData.postValue(result)
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
            getProgressDataUseCase.get().params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
            val result: Success<List<ProgressDataUiModel>> = Success(getProgressDataUseCase.get().executeOnBackground())
            _progressWidgetData.postValue(result)
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getPostDataUseCase.get().params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<PostListDataUiModel>> = Success(getPostDataUseCase.get().executeOnBackground())
            _postListWidgetData.postValue(result)
        }, onError = {
            _postListWidgetData.postValue(Fail(it))
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getCarouselDataUseCase.get().params = GetCarouselDataUseCase.getRequestParams(dataKeys)
            val result: Success<List<CarouselDataUiModel>> = Success(getCarouselDataUseCase.get().executeOnBackground())
            _carouselWidgetData.postValue(result)
        }, onError = {
            _carouselWidgetData.postValue(Fail(it))
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getTableDataUseCase.get().params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<TableDataUiModel>> = Success(getTableDataUseCase.get().executeOnBackground())
            _tableWidgetData.postValue(result)
        }, onError = {
            _tableWidgetData.postValue(Fail(it))
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getPieChartDataUseCase.get().params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<PieChartDataUiModel>> = Success(getPieChartDataUseCase.get().executeOnBackground())
            _pieChartWidgetData.postValue(result)
        }, onError = {
            _pieChartWidgetData.postValue(Fail(it))
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            getBarChartDataUseCase.get().params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val result: Success<List<BarChartDataUiModel>> = Success(getBarChartDataUseCase.get().executeOnBackground())
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}