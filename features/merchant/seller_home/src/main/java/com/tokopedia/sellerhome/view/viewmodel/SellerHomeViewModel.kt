package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.util.TimeFormat
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getTickerUseCase: GetTickerUseCase,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getCardDataUseCase: GetCardDataUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        private val getProgressDataUseCase: GetProgressDataUseCase,
        private val getPostDataUseCase: GetPostDataUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val shopId: String by lazy { "479066" /*userSession.shopId*/ }
    private val now = Calendar.getInstance(Locale("id"))
    private val last8daysTimeMillis = now.timeInMillis.minus(TimeUnit.DAYS.toMillis(8))
    private val last1DayTimeMillis = now.timeInMillis.minus(TimeUnit.DAYS.toMillis(1))
    private val startDate = TimeFormat.format(last8daysTimeMillis, "dd-MM-yyyy")
    private val endDate = TimeFormat.format(last1DayTimeMillis, "dd-MM-yyyy")

    val homeTicker = MutableLiveData<Result<List<TickerUiModel>>>()
    val widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    val cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    val lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    val progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    val postWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()

    fun getTicker() {
        launchCatchError(block = {
            homeTicker.value = Success(withContext(Dispatchers.IO) {
                getTickerUseCase.executeOnBackground()
            })
        }, onError = {
            homeTicker.value = Fail(it)
        })
    }

    fun getWidgetLayout() {
        launchCatchError(block = {
            widgetLayout.value = Success(withContext(Dispatchers.IO) {
                getLayoutUseCase.executeOnBackground()
            })


        }, onError = {
            widgetLayout.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            cardWidgetData.value = Success(withContext(Dispatchers.IO) {
                getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                val widgetData: List<CardDataUiModel> = getCardDataUseCase.executeOnBackground()
                return@withContext widgetData
            })
        }, onError = {
            cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            lineGraphWidgetData.value = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)
                val widgetData: List<LineGraphDataUiModel> = getLineGraphDataUseCase.executeOnBackground()
                return@withContext widgetData
            })
        }, onError = {
            lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            progressWidgetData.value = Success(withContext(Dispatchers.IO) {
                getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(userSession.shopId, "2020-02-02", dataKeys)
                return@withContext getProgressDataUseCase.executeOnBackground()
            })
        }, onError = {
            progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            postWidgetData.value = Success(withContext(Dispatchers.IO) {
                getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                getPostDataUseCase.executeOnBackground()
            })
        }, onError = {
            postWidgetData.value = Fail(it)
        })
    }
}