package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.AffiliateAnnouncementData
import com.tokopedia.affiliate.model.AffiliateValidateUserData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSharedProductCardsModel
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val affiliateValidateUseCaseUseCase: AffiliateValidateUserStatusUseCase,
        private val affiliatePerformanceUseCase: AffiliatePerformanceUseCase,
        private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var progressBar = MutableLiveData<Boolean>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var affiliateAnnouncement=MutableLiveData<AffiliateAnnouncementData>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<String>()
    private val pageLimit = 6

    fun getAffiliateValidateUser() {
        launchCatchError(block = {
            progressBar.value = false
            validateUserdata.value = affiliateValidateUseCaseUseCase.validateUserStatus(userSessionInterface.email)
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }
    fun getAnnouncementInformation() {
        launchCatchError(block = {
            affiliateAnnouncement.value=affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement()
        },onError = {
            it.printStackTrace()
            errorMessage.value=it.localizedMessage
        })
    }
    fun getAffiliatePerformance(page : Int) {
        if(page == PAGE_ZERO)
            shimmerVisibility.value = true
        launchCatchError(block = {
            if(page == PAGE_ZERO)
                shimmerVisibility.value = false
            affiliatePerformanceUseCase.affiliatePerformance(page,pageLimit).getAffiliateItemsPerformanceList?.data?.sectionData?.let {
                totalItemsCount.value = it.itemTotalCount
                convertDataToVisitables(it)?.let { visitables ->
                    affiliateDataList.value = visitables
                }
            }
        }, onError = {
            shimmerVisibility.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
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

    fun convertDataToVisitables(data : AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData) : ArrayList<Visitable<AffiliateAdapterTypeFactory>>?{
        val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        data.items?.let { items ->
            for (product in items) {
                product?.let {
                    tempList.add(AffiliateSharedProductCardsModel(product))
                }
            }
            return@let tempList
        }
        return null
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement() : LiveData<AffiliateAnnouncementData> = affiliateAnnouncement
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun progressBar(): LiveData<Boolean> = progressBar

}
