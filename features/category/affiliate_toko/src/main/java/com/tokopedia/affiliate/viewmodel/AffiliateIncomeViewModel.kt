package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
import com.tokopedia.affiliate.PROJECT_ID
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateBalanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliateKycUseCase
import com.tokopedia.affiliate.usecase.AffiliateTransactionHistoryUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class AffiliateIncomeViewModel : BaseViewModel() {

    private var affiliateBalanceData = MutableLiveData<AffiliateBalance.AffiliateBalance.Data>()
    private var affiliateKycData = MutableLiveData<AffiliateKycDetailsData.KycProjectInfo>()
    private var affiliateKycError = MutableLiveData<String>()
    private var affiliateKycLoader = MutableLiveData<Boolean>()
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateDataList =
        MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var rangeChanged = MutableLiveData<Boolean>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var progressBar = MutableLiveData<Boolean>()
    private var affiliateAnnouncement = MutableLiveData<AffiliateAnnouncementDataV2>()
    private var isUserBlackListed: Boolean = false
    var hasNext = true

    val affiliateBalanceDataUseCase = AffiliateBalanceDataUseCase(AffiliateRepository())
    val affiliateTransactionHistoryUseCase =
        AffiliateTransactionHistoryUseCase(AffiliateRepository())
    val affiliatKycHistoryUseCase = AffiliateKycUseCase(AffiliateRepository())
    val affiliateValidateUseCaseUseCase =
        AffiliateValidateUserStatusUseCase(AffiliateRepository())
    val affiliateAffiliateAnnouncementUseCase =
        AffiliateAnnouncementUseCase(AffiliateRepository())
    val affiliateUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase =
        AffiliateGetUnreadNotificationUseCase(
            AffiliateRepository()
        )
    private val _unreadNotificationCount = MutableLiveData(Int.ZERO)

    fun getUnreadNotificationCount(): LiveData<Int> = _unreadNotificationCount
    fun getAffiliateBalance() {
        launchCatchError(
            block = {
                affiliateBalanceDataUseCase.getAffiliateBalance().affiliateBalance?.data?.let {
                    affiliateBalanceData.value = it
                }
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun getAffiliateValidateUser(email: String) {
        launchCatchError(
            block = {
                validateUserdata.value =
                    affiliateValidateUseCaseUseCase.validateUserStatus(email)
                progressBar.value = false
            },
            onError = {
                progressBar.value = false
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getAnnouncementInformation() {
        launchCatchError(
            block = {
                affiliateAnnouncement.value =
                    affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                        PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
                    )
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun getKycDetails() {
        launchCatchError(
            block = {
                affiliateKycLoader.value = true
                affiliatKycHistoryUseCase.getKycInformation(PROJECT_ID).kycProjectInfo?.let {
                    affiliateKycData.value = it
                }
                affiliateKycLoader.value = false
            },
            onError = {
                affiliateKycError.value = it.message
                affiliateKycLoader.value = false
                it.printStackTrace()
            }
        )
    }

    fun getAffiliateTransactionHistory(page: Int) {
        shimmerVisibility.value = true
        launchCatchError(
            block = {
                affiliateTransactionHistoryUseCase.getAffiliateTransactionHistory(
                    selectedDateValue.toInt(),
                    page
                ).getAffiliateTransactionHistory?.data?.let {
                    hasNext = it.hasNext
                    convertDataToVisitables(it)?.let { visitables ->
                        affiliateDataList.value = visitables
                    }
                }
            },
            onError = {
                shimmerVisibility.value = false
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun convertDataToVisitables(
        data: AffiliateTransactionHistoryData.GetAffiliateTransactionHistory.Data?
    ): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        val tempList: ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        data?.transaction?.let { items ->
            for (transaction in items) {
                transaction.let {
                    tempList.add(AffiliateTransactionHistoryItemModel(transaction))
                }
            }
            return tempList
        }
        return null
    }

    fun fetchUnreadNotificationCount() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            Timber.e(e)
        }
        viewModelScope.launch(coroutineContext + coroutineExceptionHandler) {
            _unreadNotificationCount.value =
                affiliateUnreadNotificationUseCase.getUnreadNotifications()
        }
    }

    fun resetNotificationCount() {
        _unreadNotificationCount.value = Int.ZERO
    }

    private var selectedDateRange = AffiliateBottomDatePicker.THIRTY_DAYS
    private var selectedDateValue = "30"
    fun getSelectedDate(): String {
        return selectedDateRange
    }

    fun onRangeChanged(range: AffiliateDatePickerData) {
        if (selectedDateRange != range.text) {
            selectedDateRange = range.text
            selectedDateValue = range.value
            rangeChanged.value = true
        }
    }

    fun setBlacklisted(isBlackListed: Boolean) {
        isUserBlackListed = isBlackListed
    }

    fun getIsBlackListed(): Boolean {
        return isUserBlackListed
    }

    fun getAffiliateBalanceData(): LiveData<AffiliateBalance.AffiliateBalance.Data> =
        affiliateBalanceData

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getAffiliateKycData(): LiveData<AffiliateKycDetailsData.KycProjectInfo> = affiliateKycData
    fun getAffiliateKycLoader(): LiveData<Boolean> = affiliateKycLoader
    fun getAffiliateDataItems(): LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> =
        affiliateDataList

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getKycErrorMessage(): LiveData<String> = affiliateKycError
    fun getRangeChange(): LiveData<Boolean> = rangeChanged
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement(): LiveData<AffiliateAnnouncementDataV2> = affiliateAnnouncement
}
