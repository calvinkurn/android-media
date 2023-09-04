package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliate.NO_UI_METRICS
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMO_PERFORMA
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDateFilterData
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.model.response.ItemTypesItem
import com.tokopedia.affiliate.sse.AffiliateSSE
import com.tokopedia.affiliate.sse.AffiliateSSEMapper
import com.tokopedia.affiliate.sse.AffiliateSSEPageSource
import com.tokopedia.affiliate.sse.model.AffiliateSSEAction
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClick
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClickItem
import com.tokopedia.affiliate.sse.model.AffiliateSSECloseReason
import com.tokopedia.affiliate.sse.model.AffiliateSSEResponse
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateNoPromoItemFoundModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformanceChipRVModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceListModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceItemTypeUseCase
import com.tokopedia.affiliate.usecase.AffiliateSSEAuthTokenUseCase
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AffiliateAdpViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase,
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase,
    private val affiliatePerformanceItemTypeUseCase: AffiliatePerformanceItemTypeUseCase,
    private val affiliatePerformanceDataUseCase: AffiliatePerformanceDataUseCase,
    private val affiliateSSEAuthTokenUseCase: AffiliateSSEAuthTokenUseCase,
    private val affiliateUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val affiliateSSE: AffiliateSSE
) : BaseViewModel() {
    var staticSize: Int = 0
    private val shimmerVisibility = MutableLiveData<Boolean>()
    private val dataPlatformShimmerVisibility = MutableLiveData<Boolean>()
    private val progressBar = MutableLiveData<Boolean>()
    private val affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementDataV2>()
    private val affiliateDataList =
        MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private val noMoreDataAvailable = MutableLiveData(false)
    private val isSSEConnected = MutableStateFlow(false)
    private val validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private val errorMessage = MutableLiveData<Throwable>()
    private val rangeChanged = MutableLiveData<Boolean>()
    private val affiliateSSEAdpTotalClickItem =
        MutableStateFlow<AffiliateSSEAdpTotalClickItem?>(null)
    private val affiliateSSEAdpTotalClick = MutableStateFlow<AffiliateSSEAdpTotalClick?>(null)
    private var lastID = "0"
    private var firstTime = true
    private var selectedDateRange = AffiliateBottomDatePicker.THIRTY_DAYS
    private var selectedDateMessage = DateUtils().getMessage(selectedDateRange)
    private var dateUpdateDescription = ""
    private var selectedDateValue = "30"
    var lastSelectedChip: ItemTypesItem? = null

    private var itemTypes = emptyList<ItemTypesItem>()
    private var sseJob: Job? = null

    private val _unreadNotificationCount = MutableLiveData(Int.ZERO)

    fun getUnreadNotificationCount(): LiveData<Int> = _unreadNotificationCount

    companion object {
        private const val FILTER_LAST_THIRTY_DAYS = "LastThirtyDays"
        private const val CONVERSION_METRIC = "conversion"
    }

    fun getAffiliateValidateUser() {
        launchCatchError(
            block = {
                validateUserdata.value =
                    affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
                progressBar.value = false
            },
            onError = {
                progressBar.value = false
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getAnnouncementInformation(isHome: Boolean) {
        val page = if (isHome) {
            PAGE_ANNOUNCEMENT_HOME
        } else {
            PAGE_ANNOUNCEMENT_PROMO_PERFORMA
        }
        launchCatchError(
            block = {
                affiliateAnnouncement.value =
                    affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(page)
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun getAffiliatePerformance(page: Int, isFullLoad: Boolean = false) {
        launchCatchError(
            block = {
                if (firstTime) {
                    affiliateUserPerformanceUseCase.getAffiliateFilter().let { filters ->
                        filters.dateFilterData?.getAffiliateDateFilter?.forEach { filter ->
                            if (filter?.filterType == FILTER_LAST_THIRTY_DAYS) {
                                filter.filterDescription?.let { selectedDateMessage = it }
                                filter.filterValue?.let { selectedDateValue = it }
                                filter.filterTitle?.let { selectedDateRange = it }
                                filter.updateDescription?.let { dateUpdateDescription = it }
                            }
                        }
                        firstTime = false
                    }
                }
                var performanceList: AffiliateUserPerformaListItemData? = null
                if (page == PAGE_ZERO) {
                    lastID = "0"
                    if (isFullLoad) {
                        dataPlatformShimmerVisibility.value = true
                        noMoreDataAvailable.value = false
                        lastSelectedChip = null
                        performanceList =
                            affiliateUserPerformanceUseCase.affiliateUserperformance(
                                selectedDateValue
                            )
                    }
                }
                if (!isFullLoad) shimmerVisibility.value = true
                if (firstTime || isFullLoad) {
                    itemTypes =
                        affiliatePerformanceItemTypeUseCase
                            .affiliatePerformanceItemTypeList()
                            .getItemTypeList.data.itemTypes
                }

                affiliatePerformanceDataUseCase.affiliateItemPerformanceList(
                    selectedDateValue,
                    lastID,
                    lastSelectedChip?.pageType?.toIntOrZero() ?: 0
                ).getAffiliatePerformanceList?.performanceList?.performanceListData.let {
                    lastID = it?.lastID ?: "0"
                    if (page == PAGE_ZERO && isFullLoad) dataPlatformShimmerVisibility.value = false
                    convertDataToVisitable(
                        it,
                        performanceList,
                        itemTypes,
                        page,
                        isFullLoad
                    )?.let { visitable ->
                        affiliateDataList.value = visitable
                    }
                }
            },
            onError = {
                if (page == PAGE_ZERO) {
                    dataPlatformShimmerVisibility.value = false
                } else {
                    shimmerVisibility.value = false
                }
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }

    private fun convertDataToVisitable(
        data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data?,
        performanceList: AffiliateUserPerformaListItemData?,
        itemTypesList: List<ItemTypesItem>,
        page: Int,
        isFullLoad: Boolean
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        if (page == PAGE_ZERO && isFullLoad) {
            tempList.add(
                AffiliateDateFilterModel(
                    AffiliateDateFilterData(
                        selectedDateRange,
                        selectedDateMessage,
                        dateUpdateDescription
                    ),
                    isSSEConnected
                )
            )
            tempList.add(
                AffiliateUserPerformanceModel(
                    getListFromData(
                        performanceList
                    )
                )
            )
            getItemChips(tempList, itemTypesList)
            staticSize = tempList.size
        }
        data?.items?.let { items ->
            if (items.isNotEmpty()) {
                for (product in items) {
                    product?.let {
                        tempList.add(
                            AffiliatePerformaSharedProductCardsModel(
                                product,
                                affiliateSSEAdpTotalClickItem
                            )
                        )
                    }
                }
            } else if (page == PAGE_ZERO) {
                tempList.add(AffiliateNoPromoItemFoundModel(lastSelectedChip?.name))
            } else {
                noMoreDataAvailable.value = true
            }
            return tempList
        }
        return null
    }

    private fun getItemChips(
        tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>,
        itemTypesList: List<ItemTypesItem>
    ) {
        itemTypesList.forEachIndexed { index, item ->
            when (index) {
                0 ->
                    item.isSelected =
                        lastSelectedChip == null || lastSelectedChip?.name == item.name

                else -> item.isSelected = lastSelectedChip?.name == item.name
            }
        }
        if (itemTypesList.isNotEmpty()) {
            tempList.add(
                AffiliatePerformanceChipRVModel(
                    itemTypesList.sortedBy { it.order }
                )
            )
        }
    }

    private fun getListFromData(
        affiliatePerformanceResponse: AffiliateUserPerformaListItemData?
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
        val performanceTempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        affiliatePerformanceResponse?.getAffiliatePerformance?.performanceData?.userData?.let { userData ->
            userData.metrics = userData.metrics.sortedBy { metrics -> metrics?.order }
            userData.metrics.forEach { metrics ->
                if (metrics?.order != NO_UI_METRICS && metrics?.metricType != CONVERSION_METRIC) {
                    performanceTempList.add(
                        AffiliateUserPerformanceListModel(
                            metrics,
                            affiliateSSEAdpTotalClick
                        )
                    )
                }
            }
        }
        return performanceTempList
    }

    fun getSelectedDate(): String {
        return selectedDateRange
    }

    fun onRangeChanged(range: AffiliateDatePickerData) {
        if (selectedDateRange != range.text) {
            selectedDateRange = range.text
            selectedDateValue = range.value
            selectedDateMessage = range.message
            dateUpdateDescription = range.updateDescription
            rangeChanged.value = true
        }
    }

    /**
     * SSE
     */
    fun startSSE() {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            val token = affiliateSSEAuthTokenUseCase.getAffiliateToken().data?.token.orEmpty()
            connectSSE(AffiliateSSEPageSource.AffiliateADP.source, token)
            affiliateSSE.listen().collect {
                when (it) {
                    is AffiliateSSEAction.Message -> handleSSEMessage(it.message)
                    is AffiliateSSEAction.Close -> {
                        isSSEConnected.value = false
                        if (it.reason == AffiliateSSECloseReason.ERROR) {
                            connectSSE(AffiliateSSEPageSource.AffiliateADP.source, token)
                        }
                    }
                }
            }
        }
    }

    private fun connectSSE(pageSource: String, authToken: String) {
        affiliateSSE.connect(pageSource, authToken)
    }

    fun stopSSE() {
        sseJob?.cancel()
        affiliateSSE.close()
    }

    private suspend fun handleSSEMessage(message: AffiliateSSEResponse) {
        isSSEConnected.value = true
        val result = withContext(dispatchers.computation) {
            val sseMapper = AffiliateSSEMapper(message)
            sseMapper.mapping()
        }
        when (result) {
            is AffiliateSSEAdpTotalClick -> affiliateSSEAdpTotalClick.value = result
            is AffiliateSSEAdpTotalClickItem -> affiliateSSEAdpTotalClickItem.value = result
        }
    }

    fun fetchUnreadNotificationCount() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e -> Timber.e(e) }
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            _unreadNotificationCount.value =
                affiliateUnreadNotificationUseCase.getUnreadNotifications()
        }
    }

    fun resetNotificationCount() {
        _unreadNotificationCount.value = Int.ZERO
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getDataShimmerVisibility(): LiveData<Boolean> = dataPlatformShimmerVisibility
    fun getRangeChanged(): LiveData<Boolean> = rangeChanged
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementDataV2> = affiliateAnnouncement
    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> =
        affiliateDataList

    fun progressBar(): LiveData<Boolean> = progressBar
    fun noMoreDataAvailable(): LiveData<Boolean> = noMoreDataAvailable
}
