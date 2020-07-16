package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetStatusShopUseCase
import com.tokopedia.sellerhome.domain.usecase.GetTickerUseCase
import com.tokopedia.sellerhome.view.model.TickerUiModel
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
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val getShopStatusUseCase: GetStatusShopUseCase,
        private val userSession: UserSessionInterface,
        private val getTickerUseCase: GetTickerUseCase,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getShopLocationUseCase: GetShopLocationUseCase,
        private val getCardDataUseCase: GetCardDataUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        private val getProgressDataUseCase: GetProgressDataUseCase,
        private val getPostDataUseCase: GetPostDataUseCase,
        private val getCarouselDataUseCase: GetCarouselDataUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val SELLER_HOME_PAGE_NAME = "seller-home"
    }

    private val shopId: String by lazy { userSession.shopId }
    private val dynamicParameter by lazy {
        val startDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7)
        val endDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1)
        return@lazy WidgetDataParameterModel(
                startDate = DateTimeUtil.format(startDateMillis, DATE_FORMAT),
                endDate = DateTimeUtil.format(endDateMillis, DATE_FORMAT),
                pageSource = SELLER_HOME_PAGE_NAME
        )
    }

    private val _homeTicker = MutableLiveData<Result<List<TickerUiModel>>>()
    private val _shopStatus = MutableLiveData<Result<GetShopStatusResponse>>()
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _shopLocation = MutableLiveData<Result<ShippingLoc>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()

    val homeTicker: LiveData<Result<List<TickerUiModel>>>
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

    fun getTicker() {
        launchCatchError(block = {
            val result: Success<List<TickerUiModel>> = Success(withContext(Dispatchers.IO) {
                getTickerUseCase.executeOnBackground()
            })
            _homeTicker.postValue(result)
        }, onError = {
            _homeTicker.postValue(Fail(it))
        })
    }

    fun getShopStatus() = executeCall(_shopStatus) {
        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams(userSession.shopId)
        getShopStatusUseCase.executeOnBackground()
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
                return@withContext getLayoutUseCase.executeOnBackground()
            })
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            val result: Success<ShippingLoc> = Success(withContext(Dispatchers.IO) {
                getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.executeOnBackground()
            })
            _shopLocation.postValue(result)
        }, onError = {
            _shopLocation.postValue(Fail(it))
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
                val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
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
}