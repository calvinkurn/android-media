package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDateFilterData
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.pojo.AffiliateUserPerformaData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementData
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*
import com.tokopedia.affiliate.usecase.*
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
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
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementData>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateErrorMessage = MutableLiveData<Throwable>()
    private var rangeChanged = MutableLiveData<Boolean>()
    private var showProductCount = true
    private var lastID = "0"

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = true
            validateUserdata.value =
                affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }

    fun getAnnouncementInformation() {
        launchCatchError(block = {
            progressBar.value = true
            affiliateAnnouncement.value =
                affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement()
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            affiliateErrorMessage.value = it
        })
    }

    fun getAffiliatePerformance(page: Int) {
        launchCatchError(block = {
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
                if(page == PAGE_ZERO) dataPlatformShimmerVisibility.value = false
                convertDataToVisitables(it, performanceList, page)?.let { visitables ->
                    affiliateDataList.value = visitables
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

    fun convertDataToVisitables(
            data: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data?,
            performanceList: AffiliateUserPerformaListItemData?,
            page: Int
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        if (page == PAGE_ZERO) {
            tempList.add(AffiliateDateFilterModel(AffiliateDateFilterData(selectedDateRange,selectedDateMessage)))
            tempList.add(
                AffiliateUserPerformanceModel(
                    AffiliateUserPerformaData(
                        getListFromData(
                            performanceList
                        ), totalItemsCount.value,showProductCount
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
            } else if(totalItemsCount.value == 0) {
                tempList.add(AffiliateNoPromoItemFoundModel())
            }
            return tempList
        }
        return null
    }

    private fun getListFromData(affiliatePerfomanceResponse: AffiliateUserPerformaListItemData?): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val performaTempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        affiliatePerfomanceResponse?.getAffiliatePerformance?.data?.userData?.let { userData ->
            userData.metrics = userData.metrics.sortedBy { metrics -> metrics?.order }
            userData.metrics.forEach { metrics ->
                if (metrics?.order == 0) {
                    totalItemsCount.value = metrics.metricValue?.toInt()
                } else {
                    performaTempList.add(AffiliateUserPerformanceListModel(metrics))
                }
            }
        }
        return performaTempList
    }

    private var selectedDateRange = AffiliateBottomDatePicker.THIRTY_DAYS
    private var selectedDateMessage = DateUtils().getMessage(selectedDateRange)

    private var selectedDateValue = "30"
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
    fun getAffiliateErrorMessage(): LiveData<Throwable> = affiliateErrorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementData> = affiliateAnnouncement
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> =
        affiliateDataList

    fun progressBar(): LiveData<Boolean> = progressBar

}
