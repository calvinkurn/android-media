package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.NO_UI_METRICS
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.TOTAL_ITEMS_METRIC_TYPE
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDateFilterData
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateFilterModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateNoPromoItemFoundModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceListModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceModel
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateHomeViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase,
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase,
    private val affiliatePerformanceDataUseCase: AffiliatePerformanceDataUseCase
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var dataPlatformShimmerVisibility = MutableLiveData<Boolean>()
    private var progressBar = MutableLiveData<Boolean>()
    private var affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementDataV2>()
    private var affiliateDataList =
        MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var rangeChanged = MutableLiveData<Boolean>()
    private var lastID = "0"
    private var firstTime = true
    private var selectedDateRange = AffiliateBottomDatePicker.THIRTY_DAYS
    private var selectedDateMessage = DateUtils().getMessage(selectedDateRange)

    private var selectedDateValue = "30"

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            validateUserdata.value =
                affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
            progressBar.value = false
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    fun getAnnouncementInformation() {
        launchCatchError(block = {
            affiliateAnnouncement.value =
                affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(PAGE_ANNOUNCEMENT_HOME)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getAffiliatePerformance(page: Int) {
        launchCatchError(block = {
            if (firstTime) affiliateUserPerformanceUseCase.getAffiliateFilter().let { filters ->
                filters.data?.getAffiliateDateFilter?.forEach { filter ->
                    if (filter?.filterType == "LastThirtyDays") {
                        filter.filterDescription?.let { selectedDateMessage = it }
                        filter.filterValue?.let { selectedDateValue = it }
                        filter.filterTitle?.let { selectedDateRange = it }
                    }
                }
                firstTime = false
            }
            var performanceList: AffiliateUserPerformaListItemData? = null
            if (page == PAGE_ZERO) {
                dataPlatformShimmerVisibility.value = true
                lastID = "0"
                totalItemsCount.value = 0
                performanceList =
                    affiliateUserPerformanceUseCase.affiliateUserperformance(selectedDateValue)
            } else {
                shimmerVisibility.value = true
            }
            affiliatePerformanceDataUseCase.affiliateItemPerformanceList(
                selectedDateValue,
                lastID
            ).getAffiliatePerformanceList?.data?.data.let {
                lastID = it?.lastID ?: "0"
                if (page == PAGE_ZERO) dataPlatformShimmerVisibility.value = false
                convertDataToVisitable(it, performanceList, page)?.let { visitable ->
                    affiliateDataList.value = visitable
                }

            }
        }, onError = {
            if (page == PAGE_ZERO) {
                dataPlatformShimmerVisibility.value = false
            } else {
                shimmerVisibility.value = false
            }
            it.printStackTrace()
            errorMessage.value = it
        })
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
        page: Int
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        if (page == PAGE_ZERO) {
            tempList.add(
                AffiliateDateFilterModel(
                    AffiliateDateFilterData(
                        selectedDateRange,
                        selectedDateMessage
                    )
                )
            )
            tempList.add(
                AffiliateUserPerformanceModel(
                    getListFromData(
                        performanceList
                    )
                )
            )
        }
        data?.items?.let { items ->
            if (items.isNotEmpty()) {
                for (product in items) {
                    product?.let {
                        tempList.add(AffiliatePerformaSharedProductCardsModel(product))
                    }
                }
            } else if (totalItemsCount.value == 0) {
                tempList.add(AffiliateNoPromoItemFoundModel())
            }
            return tempList
        }
        return null
    }

    private fun getListFromData(affiliatePerformanceResponse: AffiliateUserPerformaListItemData?): ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
        val performanceTempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        affiliatePerformanceResponse?.getAffiliatePerformance?.data?.userData?.let { userData ->
            userData.metrics = userData.metrics.sortedBy { metrics -> metrics?.order }
            userData.metrics.forEach { metrics ->
                if (metrics?.metricType == TOTAL_ITEMS_METRIC_TYPE) {
                    totalItemsCount.value = metrics.metricValue?.toInt()
                } else if (metrics?.order != NO_UI_METRICS) {
                    performanceTempList.add(AffiliateUserPerformanceListModel(metrics))
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
            rangeChanged.value = true
        }
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getDataShimmerVisibility(): LiveData<Boolean> = dataPlatformShimmerVisibility
    fun getRangeChanged(): LiveData<Boolean> = rangeChanged
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementDataV2> = affiliateAnnouncement
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> =
        affiliateDataList

    fun progressBar(): LiveData<Boolean> = progressBar

}
