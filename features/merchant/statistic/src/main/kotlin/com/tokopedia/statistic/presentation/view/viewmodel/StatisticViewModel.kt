package com.tokopedia.statistic.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.common.coroutine.DispatchersProvider
import com.tokopedia.statistic.presentation.domain.usecase.GetUserRoleUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getUserRoleUseCase: GetUserRoleUseCase,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getCardDataUseCase: GetCardDataUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        private val getProgressDataUseCase: GetProgressDataUseCase,
        private val getPostDataUseCase: GetPostDataUseCase,
        private val getCarouselDataUseCase: GetCarouselDataUseCase,
        private val getTableDataUseCase: GetTableDataUseCase,
        private val getPieChartDataUseCase: GetPieChartDataUseCase,
        private val getBarChartDataUseCase: GetBarChartDataUseCase,
        private val dispatcher: DispatchersProvider
) : BaseViewModel(dispatcher.main()) {

    companion object {
        private const val STATISTIC_PAGE_NAME = "shop-insight"
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
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

    fun setDateRange(startDate: Date, endDate: Date, filterType: String) {
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
            val result: Success<List<BaseWidgetUiModel<*>>> = Success(withContext(dispatcher.io()) {
                getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId, STATISTIC_PAGE_NAME)
                return@withContext getLayoutUseCase.executeOnBackground()
            })
            _widgetLayout.postValue(result)
        }, onError = {
            _widgetLayout.postValue(Fail(it))
        })
    }

    fun getUserRole() {
        launchCatchError(block = {
            val result: Success<List<String>> = Success(withContext(dispatcher.io()) {
                getUserRoleUseCase.params = GetUserRoleUseCase.createParam(userSession.userId.toIntOrZero())
                return@withContext getUserRoleUseCase.executeOnBackground()
            })
            _userRole.postValue(result)
        }, onError = {
            _userRole.postValue(Fail(it))
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<CardDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<LineGraphDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<ProgressDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<PostListDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<CarouselDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<TableDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<PieChartDataUiModel>> = Success(withContext(dispatcher.io()) {
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
            val result: Success<List<BarChartDataUiModel>> = Success(withContext(dispatcher.io()) {
                getBarChartDataUseCase.params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
                return@withContext getBarChartDataUseCase.executeOnBackground()
            })
            _barChartWidgetData.postValue(result)
        }, onError = {
            _barChartWidgetData.postValue(Fail(it))
        })
    }
}