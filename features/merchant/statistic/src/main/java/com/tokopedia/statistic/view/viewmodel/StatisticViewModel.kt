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
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
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
        private val getProgressDataUseCase: Lazy<GetProgressDataUseCase>,
        private val getPostDataUseCase: Lazy<GetPostDataUseCase>,
        private val getCarouselDataUseCase: Lazy<GetCarouselDataUseCase>,
        private val getTableDataUseCase: Lazy<GetTableDataUseCase>,
        private val getPieChartDataUseCase: Lazy<GetPieChartDataUseCase>,
        private val getBarChartDataUseCase: Lazy<GetBarChartDataUseCase>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val STATISTIC_PAGE_NAME = "shop-insight"
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val TICKER_PAGE_NAME = "seller-statistic"
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
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()

    private var dynamicParameter = DynamicParameterModel()

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                resultFlow
                        .asFlow()
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

    fun setDateFilter(startDate: Date, endDate: Date, filterType: String) {
        val startDateFmt = DateTimeUtil.format(startDate.time, DATE_FORMAT)
        val endDateFmt = DateTimeUtil.format(endDate.time, DATE_FORMAT)
        this.dynamicParameter = DynamicParameterModel(
                startDate = startDateFmt,
                endDate = endDateFmt,
                pageSource = STATISTIC_PAGE_NAME,
                dateType = filterType
        )
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            val params = GetLayoutUseCase.getRequestParams(shopId, STATISTIC_PAGE_NAME)
            getLayoutUseCase.get().run {
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getTickers() {
        launchCatchError(block = {
            val params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
            getTickerUseCase.get().run {
                startCollectingResult(_tickers)
                executeOnBackground(params, firstLoad)
            }
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
            val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getCardDataUseCase.get().run {
                startCollectingResult(_cardWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _cardWidgetData.postValue(Fail(it))
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getLineGraphDataUseCase.get().run {
                startCollectingResult(_lineGraphWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _lineGraphWidgetData.postValue(Fail(it))
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val today: String = DateTimeUtil.format(Date().time, DATE_FORMAT)
            val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
            getProgressDataUseCase.get().run {
                startCollectingResult(_progressWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _progressWidgetData.postValue(Fail(it))
        })
    }

    fun getPostWidgetData(dataKeys: List<Pair<String, String>>) {
        launchCatchError(block = {
            val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getPostDataUseCase.get().run {
                startCollectingResult(_postListWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _postListWidgetData.postValue(Fail(it))
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
            getCarouselDataUseCase.get().run {
                startCollectingResult(_carouselWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _carouselWidgetData.postValue(Fail(it))
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getTableDataUseCase.get().run {
                startCollectingResult(_tableWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _tableWidgetData.postValue(Fail(it))
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getPieChartDataUseCase.get().run {
                startCollectingResult(_pieChartWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _pieChartWidgetData.postValue(Fail(it))
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getBarChartDataUseCase.get().run {
                startCollectingResult(_barChartWidgetData)
                executeOnBackground(params, firstLoad)
            }
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}