package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.common.utils.DateTimeUtil
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
        private val userSession: UserSessionInterface,
        private val getTickerUseCase: GetTickerUseCase,
        private val getLayoutUseCase: GetLayoutUseCase,
        private val getCardDataUseCase: GetCardDataUseCase,
        private val getLineGraphDataUseCase: GetLineGraphDataUseCase,
        private val getProgressDataUseCase: GetProgressDataUseCase,
        private val getPostDataUseCase: GetPostDataUseCase,
        private val getCarouselDataUseCase: GetCarouselDataUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val shopId: String by lazy { userSession.shopId }

    private val locale = Locale.getDefault()
    private val startDate: String by lazy {
        val cal = Calendar.getInstance(locale)
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH).minus(7))
        return@lazy DateTimeUtil.format(cal.timeInMillis, "dd-MM-yyyy", locale)
    }
    private val endDate: String by lazy {
        val cal = Calendar.getInstance(locale)
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH).minus(1))
        return@lazy DateTimeUtil.format(cal.timeInMillis, "dd-MM-yyyy", locale)
    }

    val homeTicker = MutableLiveData<Result<List<TickerUiModel>>>()
    val widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    val cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    val lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    val progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    val postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    val carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()

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
                getLayoutUseCase.params = GetLayoutUseCase.getRequestParams(shopId)
                return@withContext getLayoutUseCase.executeOnBackground()
            })
        }, onError = {
            widgetLayout.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            cardWidgetData.value = Success(withContext(Dispatchers.IO) {
                getCardDataUseCase.params = GetCardDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                return@withContext getCardDataUseCase.executeOnBackground()
            })
        }, onError = {
            cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            lineGraphWidgetData.value = Success(withContext(Dispatchers.IO) {
                getLineGraphDataUseCase.params = GetLineGraphDataUseCase.getRequestParams(shopId, dataKeys, startDate, endDate)
                return@withContext getLineGraphDataUseCase.executeOnBackground()
            })
        }, onError = {
            lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            progressWidgetData.value = Success(withContext(Dispatchers.IO) {
                val today = DateTimeUtil.format(Date().time, "yyyy-MM-dd", locale)
                getProgressDataUseCase.params = GetProgressDataUseCase.getRequestParams(userSession.shopId, today, dataKeys)
                return@withContext getProgressDataUseCase.executeOnBackground()
            })
        }, onError = {
            progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            postListWidgetData.value = Success(withContext(Dispatchers.IO) {
                getPostDataUseCase.params = GetPostDataUseCase.getRequestParams(shopId.toIntOrZero(), dataKeys, startDate, endDate)
                return@withContext getPostDataUseCase.executeOnBackground()
            })
        }, onError = {
            postListWidgetData.value = Fail(it)
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            carouselWidgetData.value = Success(withContext(Dispatchers.IO) {
                getCarouselDataUseCase.params = GetCarouselDataUseCase.getRequestParams(dataKeys, 5)
                return@withContext getCarouselDataUseCase.executeOnBackground()
            })
        }, onError = {
            carouselWidgetData.value = Fail(it)
        })
    }
}