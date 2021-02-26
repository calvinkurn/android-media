package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
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
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
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
        private val getMultiLineGraphUseCase: Lazy<GetMultiLineGraphUseCase>,
        private val getAnnouncementUseCase: Lazy<GetAnnouncementDataUseCase>,
        private val remoteConfig: SellerHomeRemoteConfig,
        private val dispatcher: CoroutineDispatchers
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
    private val _multiLineGraphWidgetData = MutableLiveData<Result<List<MultiLineGraphDataUiModel>>>()
    private val _announcementWidgetData = MutableLiveData<Result<List<AnnouncementDataUiModel>>>()

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
    val multiLineGraphWidgetData: LiveData<Result<List<MultiLineGraphDataUiModel>>>
        get() = _multiLineGraphWidgetData
    val announcementWidgetData: LiveData<Result<List<AnnouncementDataUiModel>>>
        get() = _announcementWidgetData

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                resultFlow.asFlow()
                        .collect {
                            withContext(dispatcher.main) {
                                liveData.value = Success(it)
                            }
                        }
            }, onError = {
                liveData.value = Fail(it)
            })
        }
    }

    fun getTicker() {
        launchCatchError(block = {
            val params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
            getTickerUseCase.get().run {
                startCollectingResult(_homeTicker)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _homeTicker.value = Fail(it)
        })
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            val params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
            getLayoutUseCase.get().run {
                startCollectingResult(_widgetLayout)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _widgetLayout.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getCardDataUseCase.get().run {
                startCollectingResult(_cardWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getLineGraphDataUseCase.get().run {
                startCollectingResult(_lineGraphWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
            val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
            getProgressDataUseCase.get().run {
                startCollectingResult(_progressWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<Pair<String, String>>) {
        launchCatchError(block = {
            val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getPostDataUseCase.get().run {
                startCollectingResult(_postListWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _postListWidgetData.value = Fail(it)
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
            getCarouselDataUseCase.get().run {
                startCollectingResult(_carouselWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _carouselWidgetData.value = Fail(it)
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getTableDataUseCase.get().run {
                startCollectingResult(_tableWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _tableWidgetData.value = Fail(it)
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getPieChartDataUseCase.get().run {
                startCollectingResult(_pieChartWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _pieChartWidgetData.value = Fail(it)
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getBarChartDataUseCase.get().run {
                startCollectingResult(_barChartWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _barChartWidgetData.value = Fail(it)
        })
    }

    fun getMultiLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
            getMultiLineGraphUseCase.get().run {
                startCollectingResult(_multiLineGraphWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _multiLineGraphWidgetData.value = Fail(it)
        })
    }

    fun getAnnouncementWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
            getAnnouncementUseCase.get().run {
                startCollectingResult(_announcementWidgetData)
                executeOnBackground(params)
            }
        }, onError = {
            _announcementWidgetData.value = Fail(it)
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            val result: Success<ShippingLoc> = Success(withContext(dispatcher.io) {
                getShopLocationUseCase.get().params = GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.get().executeOnBackground()
            })
            _shopLocation.value = result
        }, onError = {
            _shopLocation.value = Fail(it)
        })
    }
}