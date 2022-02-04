package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoByIdUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhome.view.helper.SellerHomeLayoutHelper
import com.tokopedia.sellerhome.view.model.ShopShareDataUiModel
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.shop.common.domain.interactor.ShopQuestGeneralTrackerUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.*
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
    private val getRecommendationUseCase: Lazy<GetRecommendationDataUseCase>,
    private val getMilestoneDataUseCase: Lazy<GetMilestoneDataUseCase>,
    private val getShopInfoByIdUseCase: Lazy<GetShopInfoByIdUseCase>,
    private val shopQuestTrackerUseCase: Lazy<ShopQuestGeneralTrackerUseCase>,
    private val sellerHomeLayoutHelper: Lazy<SellerHomeLayoutHelper>,
    private val remoteConfig: SellerHomeRemoteConfig,
    private val dispatcher: CoroutineDispatchers
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val SELLER_HOME_PAGE_NAME = "seller-home"
        private const val TICKER_PAGE_NAME = "seller"
        const val DATE_FORMAT = "dd-MM-yyyy"
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
    private val _multiLineGraphWidgetData =
        MutableLiveData<Result<List<MultiLineGraphDataUiModel>>>()
    private val _announcementWidgetData = MutableLiveData<Result<List<AnnouncementDataUiModel>>>()
    private val _startWidgetCustomMetricTag = MutableLiveData<String>()
    private val _stopWidgetType = MutableLiveData<String>()
    private val _recommendationWidgetData =
        MutableLiveData<Result<List<RecommendationDataUiModel>>>()
    private val _milestoneWidgetData = MutableLiveData<Result<List<MilestoneDataUiModel>>>()
    private val _shopShareData = MutableLiveData<Result<ShopShareDataUiModel>>()
    private val _shopShareTracker = MutableLiveData<Result<ShopQuestGeneralTracker>>()

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
    val startWidgetCustomMetricTag: LiveData<String>
        get() = _startWidgetCustomMetricTag
    val stopWidgetType: LiveData<String>
        get() = _stopWidgetType
    val recommendationWidgetData: LiveData<Result<List<RecommendationDataUiModel>>>
        get() = _recommendationWidgetData
    val milestoneWidgetData: LiveData<Result<List<MilestoneDataUiModel>>>
        get() = _milestoneWidgetData
    val shopShareData: LiveData<Result<ShopShareDataUiModel>>
        get() = _shopShareData
    val shopShareTracker: LiveData<Result<ShopQuestGeneralTracker>>
        get() = _shopShareTracker

    init {
        sellerHomeLayoutHelper.get().init(
            startWidgetLiveData = _startWidgetCustomMetricTag,
            stopWidgetLiveData = _stopWidgetType,
            dynamicParameter = dynamicParameter
        )
    }

    fun getTicker() {
        launchCatchError(block = {
            val params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                val useCase = getTickerUseCase.get()
                useCase.startCollectingResult(_homeTicker)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                getTickerUseCase.get().params = params
                getDataFromUseCase(getTickerUseCase.get(), _homeTicker)
            }
        }, onError = {
            _homeTicker.value = Fail(it)
        })
    }

    /**
     * Get widget layout for seller home. If screen height value is provided,
     * that means we will also fetch widgets' data that expected to be shown first on the screen
     * and combine those before showing the widgets to user.
     *
     * @param   heightDp    height of device screen in dp
     */
    fun getWidgetLayout(heightDp: Float? = null) {
        launchCatchError(block = {
            val params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
            val useCase = getLayoutUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    if (heightDp == null) {
                        startCollectingResult(_widgetLayout)
                    } else {
                        startCollectingResult(_widgetLayout) {
                            sellerHomeLayoutHelper.get()
                                .getInitialWidget(it, heightDp, includeCache)
                                .flowOn(dispatcher.io)
                        }
                    }
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                if (heightDp == null) {
                    getDataFromUseCase(useCase, _widgetLayout)
                } else {
                    getDataFromUseCase(useCase, _widgetLayout) { result, isFromCache ->
                        sellerHomeLayoutHelper.get().getInitialWidget(result, heightDp, isFromCache)
                            .flowOn(dispatcher.io)
                    }
                }
            }
        }, onError = {
            _widgetLayout.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getCardDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.startCollectingResult(_cardWidgetData)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _cardWidgetData)
            }
        }, onError = {
            _cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getLineGraphDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_lineGraphWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _lineGraphWidgetData)
            }
        }, onError = {
            _lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
            val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
            val useCase = getProgressDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_progressWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _progressWidgetData)
            }
        }, onError = {
            _progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<TableAndPostDataKey>) {
        launchCatchError(block = {
            val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val isCachingEnabled = remoteConfig.isSellerHomeDashboardNewCachingEnabled()
            val useCase = getPostDataUseCase.get()
            if (isCachingEnabled) {
                useCase.startCollectingResult(_postListWidgetData)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _postListWidgetData)
            }
        }, onError = {
            _postListWidgetData.value = Fail(it)
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
            val useCase = getCarouselDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_carouselWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _carouselWidgetData)
            }
        }, onError = {
            _carouselWidgetData.value = Fail(it)
        })
    }

    fun getTableWidgetData(dataKeys: List<TableAndPostDataKey>) {
        launchCatchError(block = {
            val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getTableDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.startCollectingResult(_tableWidgetData)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _tableWidgetData)
            }
        }, onError = {
            _tableWidgetData.value = Fail(it)
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getPieChartDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_pieChartWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _pieChartWidgetData)
            }
        }, onError = {
            _pieChartWidgetData.value = Fail(it)
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getBarChartDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_barChartWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _barChartWidgetData)
            }
        }, onError = {
            _barChartWidgetData.value = Fail(it)
        })
    }

    fun getMultiLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
            val useCase = getMultiLineGraphUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_multiLineGraphWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _multiLineGraphWidgetData)
            }
        }, onError = {
            _multiLineGraphWidgetData.value = Fail(it)
        })
    }

    fun getAnnouncementWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
            val useCase = getAnnouncementUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.run {
                    startCollectingResult(_announcementWidgetData)
                    val includeCache = isFirstLoad
                            && remoteConfig.isSellerHomeDashboardCachingEnabled()
                    executeOnBackground(params, includeCache)
                }
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _announcementWidgetData)
            }
        }, onError = {
            _announcementWidgetData.value = Fail(it)
        })
    }

    fun getRecommendationWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetRecommendationDataUseCase.createParams(dataKeys)
            val useCase = getRecommendationUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.startCollectingResult(_recommendationWidgetData)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _recommendationWidgetData)
            }
        }, onError = {
            _recommendationWidgetData.value = Fail(it)
        })
    }

    fun getMilestoneWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetMilestoneDataUseCase.createParams(dataKeys)
            val useCase = getMilestoneDataUseCase.get()
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                useCase.startCollectingResult(_milestoneWidgetData)
                val includeCache = useCase.isFirstLoad
                        && remoteConfig.isSellerHomeDashboardCachingEnabled()
                useCase.executeOnBackground(params, includeCache)
            } else {
                useCase.params = params
                getDataFromUseCase(useCase, _milestoneWidgetData)
            }
        }, onError = {
            _milestoneWidgetData.value = Fail(it)
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            val result: Success<ShippingLoc> = Success(withContext(dispatcher.io) {
                getShopLocationUseCase.get().params =
                    GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.get().executeOnBackground()
            })
            _shopLocation.value = result
        }, onError = {
            _shopLocation.value = Fail(it)
        })
    }

    fun getShopInfoById() {
        launchCatchError(context = dispatcher.io, block = {
            val shopId = userSession.get().shopId.toLongOrZero()
            val result = withContext(dispatcher.io) {
                getShopInfoByIdUseCase.get().execute(shopId)
            }
            val shopShareData = ShopShareDataUiModel(
                shopUrl = result.coreInfo?.url.orEmpty(),
                shopSnippetURL = result.shopSnippetURL.orEmpty()
            )
            _shopShareData.postValue(Success(shopShareData))
        }, onError = {
            _shopShareData.postValue(Fail(it))
        })
    }

    fun sendShopShareQuestTracker(socialMediaName: String) {
        launchCatchError(context = dispatcher.io, block = {
            val useCase = shopQuestTrackerUseCase.get()
            useCase.params = ShopQuestGeneralTrackerUseCase.createRequestParams(
                actionName = SellerHomeConst.SHOP_SHARE_GQL_TRACKER_ACTION,
                source = SellerHomeConst.SHOP_SHARE_GQL_TRACKER_SOURCE,
                channel = socialMediaName,
                input = ShopQuestGeneralTrackerInput(shopId)
            )
            val result = withContext(dispatcher.io) {
                useCase.executeOnBackground()
            }
            _shopShareTracker.postValue(Success(result))
        }, onError = {
            _shopShareTracker.postValue(Fail(it))
        })
    }

    private suspend fun <T : Any> BaseGqlUseCase<T>.executeUseCase() = withContext(dispatcher.io) {
        executeOnBackground()
    }

    private suspend fun <T : Any> getDataFromUseCase(
        useCase: BaseGqlUseCase<T>,
        liveData: MutableLiveData<Result<T>>
    ) {
        try {
            useCase.setUseCache(false)
            liveData.value = Success(useCase.executeUseCase())
        } catch (networkException: Exception) {
            if (remoteConfig.isSellerHomeDashboardCachingEnabled()) {
                try {
                    useCase.setUseCache(true)
                    liveData.value = Success(useCase.executeUseCase())
                } catch (_: Exception) {
                    // ignore exception from cache
                    throw networkException
                }
            }
        }
    }

    private suspend fun <T : Any> getDataFromUseCase(
        useCase: BaseGqlUseCase<T>,
        liveData: MutableLiveData<Result<T>>,
        getTransformerFlow: suspend (result: T, isFromCache: Boolean) -> Flow<T>
    ) {
        if (remoteConfig.isSellerHomeDashboardCachingEnabled() && useCase.isFirstLoad) {
            useCase.isFirstLoad = false
            try {
                useCase.setUseCache(true)
                val useCaseResult = useCase.executeOnBackground()
                getTransformerFlow(useCaseResult, true).collect {
                    liveData.value = Success(it)
                }
            } catch (_: Exception) {
                // ignore exception from cache
            }
        }
        useCase.setUseCache(false)
        val useCaseResult = useCase.executeOnBackground()
        getTransformerFlow(useCaseResult, false).collect {
            liveData.value = Success(it)
        }
    }

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                getResultFlow().collect {
                    withContext(dispatcher.main) {
                        liveData.value = Success(it)
                    }
                }
            }, onError = {
                liveData.value = Fail(it)
            })
        }
    }

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(
        liveData: MutableLiveData<Result<T>>,
        getTransformedFlow: suspend (result: T) -> Flow<T>
    ) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                getResultFlow().collect { initialResult ->
                    getTransformedFlow(initialResult).collect {
                        withContext(dispatcher.main) {
                            liveData.value = Success(it)
                        }
                    }
                }
            }, onError = {
                liveData.value = Fail(it)
            })
        }
    }
}
