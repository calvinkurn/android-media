package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.*
import com.tokopedia.affiliate.PERFORMA_MAP
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
        private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase,
        private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase,
        private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var progressBar = MutableLiveData<Boolean>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var affiliateAnnouncement=MutableLiveData<AffiliateAnnouncementData>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var rangeItemCount :String? = "0"
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateErrorMessage = MutableLiveData<Throwable>()
    private var rangeChanged = MutableLiveData<Boolean>()
    private val pageLimit = 6

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = true
            validateUserdata.value = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it
        })
    }
    fun getAnnouncementInformation() {
        launchCatchError(block = {
            progressBar.value = true
            affiliateAnnouncement.value = affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement()
        },onError = {
            progressBar.value = false
            it.printStackTrace()
            affiliateErrorMessage.value = it
        })
    }
    fun getAffiliatePerformance(page : Int) {
        shimmerVisibility.value = true
        launchCatchError(block = {
            var performanceList :AffiliateUserPerformaListItemData? = null
            if(page == PAGE_ZERO)
                performanceList = affiliateUserPerformanceUseCase.affiliateUserperformance(selectedDateRange)
            affiliatePerformanceUseCase.affiliateItemPerformanceList(selectedDateValue).getAffiliatePerformanceList?.data?.data.let {
                convertDataToVisitables(it,performanceList,page)?.let { visitables ->
                    affiliateDataList.value = visitables
                }

            }
        }, onError = {
            shimmerVisibility.value = false
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
    ) : ArrayList<Visitable<AffiliateAdapterTypeFactory>>?{
        val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        if(page == PAGE_ZERO) {
            tempList.add(AffiliateDateFilterModel(AffiliateDateFilterData(selectedDateRange)))
            tempList.add(
                AffiliateUserPerformanceModel(
                    AffiliateUserPerformaData(
                        getListFromData(
                            performanceList
                        ), rangeItemCount
                    )
                )
            )
        }
        data?.items?.let { items ->
            for (product in items) {
                product?.let {
                    tempList.add(AffiliatePerformaSharedProductCardsModel(product))
                }
            }
            return tempList
        }
        return null
    }

    private fun getListFromData(affiliatePerfomanceResponse: AffiliateUserPerformaListItemData?): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val performaTempList:ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        affiliatePerfomanceResponse?.getAffiliatePerformance?.data?.userData?.metrics?.forEach {
            if(it?.metricType != "totalItems") {
                it?.description = PERFORMA_MAP[it?.metricTitle]
                performaTempList.add(AffiliateUserPerformanceListModel(it))
            }
            else if(it.metricType == "totalItems")
                rangeItemCount = it.metricValue
        }
        return performaTempList
    }
    private var selectedDateRange = AffiliateBottomDatePicker.TODAY
    private var selectedDateValue = "0"
    fun getSelectedDate(): String {
        return selectedDateRange
    }
    fun onRangeChanged(range: AffiliateDatePickerData) {
        if(selectedDateRange != range.text) {
            selectedDateRange = range.text
            selectedDateValue = range.value
            rangeChanged.value = true
        }
    }
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getRangeChanged(): LiveData<Boolean> = rangeChanged
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getAffiliateErrorMessage(): LiveData<Throwable> = affiliateErrorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement() : LiveData<AffiliateAnnouncementData> = affiliateAnnouncement
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun progressBar(): LiveData<Boolean> = progressBar

}
