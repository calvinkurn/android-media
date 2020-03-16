package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.common.utils.DateTimeUtil
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.view.model.*
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
    }

    private val shopId: String by lazy { userSession.shopId }
    private val startDate: String by lazy {
        val timeInMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7)
        return@lazy DateTimeUtil.format(timeInMillis, DATE_FORMAT)
    }
    private val endDate: String by lazy {
        val timeInMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1)
        return@lazy DateTimeUtil.format(timeInMillis, DATE_FORMAT)
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
            _homeTicker.value = Success(withContext(Dispatchers.IO) {
                getTickerUseCase.executeOnBackground()
            })
        }, onError = {
            _homeTicker.value = Fail(it)
        })
    }

    fun getShopStatus() = executeCall(_shopStatus) {
        getShopStatusUseCase.params = GetStatusShopUseCase.createRequestParams(userSession.shopId)
        getShopStatusUseCase.executeOnBackground()
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            _widgetLayout.value = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)
                return@withContext getLayoutUseCase.executeOnBackground()
            })
        }, onError = {
            _widgetLayout.value = Fail(it)
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            _shopLocation.value = Success(withContext(Dispatchers.IO) {
                getShopLocationUseCase.params = GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.executeOnBackground()
            })
        }, onError = {
            _shopLocation.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            _cardWidgetData.value = Success(withContext(Dispatchers.IO) {
                getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                return@withContext getCardDataUseCase.executeOnBackground()
            })
        }, onError = {
            _cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            _lineGraphWidgetData.value = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)
                return@withContext getLineGraphDataUseCase.executeOnBackground()
            })
        }, onError = {
            _lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            _progressWidgetData.value = Success(withContext(Dispatchers.IO) {
                val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
                getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(userSession.shopId, today, dataKeys)
                return@withContext getProgressDataUseCase.executeOnBackground()
            })
        }, onError = {
            _progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            _postListWidgetData.value = Success(withContext(Dispatchers.IO) {
                getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                return@withContext getPostDataUseCase.executeOnBackground()
            })
        }, onError = {
            _postListWidgetData.value = Fail(it)
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            _carouselWidgetData.value = Success(withContext(Dispatchers.IO) {
                getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys)
                return@withContext getCarouselDataUseCase.executeOnBackground()
            })
        }, onError = {
            _carouselWidgetData.value = Fail(it)
        })
    }
}