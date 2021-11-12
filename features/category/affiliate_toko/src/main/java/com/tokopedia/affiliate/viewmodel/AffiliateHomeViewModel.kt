package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.*
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceUseCase
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
        private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var progressBar = MutableLiveData<Boolean>()
    private var validateUserdata = MutableLiveData<AffiliateValidateUserData>()
    private var affiliateAnnouncement=MutableLiveData<AffiliateAnnouncementData>()
    private var affiliateDataList = MutableLiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>>()
    private var totalItemsCount = MutableLiveData<Int>()
    private var errorMessage = MutableLiveData<Throwable>()
    private var affiliateErrorMessage = MutableLiveData<Throwable>()
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
            affiliatePerformanceUseCase.affiliatePerformance(page,pageLimit).getAffiliateItemsPerformanceList?.data?.sectionData?.let {
                totalItemsCount.value = it.itemTotalCount
                convertDataToVisitables(it)?.let { visitables ->
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

    fun convertDataToVisitables(data : AffiliatePerformanceData.GetAffiliateItemsPerformanceList.Data.SectionData) : ArrayList<Visitable<AffiliateAdapterTypeFactory>>?{
        val tempList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        var itemCount :Int = data.itemTotalCount ?: 0
        val affiliatePerfomanceResponse=createmockResponce()
        tempList.add(AffiliateDateFilterModel(AffiliateDateFilterData("7 Hari Terakhir")))
        tempList.add(AffiliateHomeHeaderModel(AffiliateHomeHeaderData("Performa Affiliate")))
        tempList.add(AffiliateUserPerformanceModel(AffiliateUserPerformaData(getDataFromMock(affiliatePerfomanceResponse))))
        tempList.add(AffiliateHomeHeaderModel(AffiliateHomeHeaderData("Produk yang dipromosikan","$itemCount Produk",true)))
        data.items?.let { items ->
            for (product in items) {
                product?.let {
                    tempList.add(AffiliateSharedProductCardsModel(product))
                }
            }
            return tempList
        }
        return null
    }

    private fun getDataFromMock(affiliatePerfomanceResponse: AffiliateUserPerformaListItemData): ArrayList<Visitable<AffiliateAdapterTypeFactory>>? {
        var performaTempList:ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        affiliatePerfomanceResponse?.getAffiliatePerformance.data?.userData?.metrics?.forEach {
            performaTempList.add(AffiliateUserPerformanceListModel(it))
        }
        return performaTempList
    }

    private fun createmockResponce(): AffiliateUserPerformaListItemData {
        val list= ArrayList<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics>()
        list.add(AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics("commission","Pendapatan","1500000","Rp1.500.000","550000","-Rp550.000",10))
        list.add(AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics("soldQty","Terjual","1500","1.500","18","18",20))
        list.add(AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics("commission","Pendapatan","1500000","Rp1.500.000","550000","-Rp550.000",10))
        list.add(AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics("soldQty","Terjual","1500","1.500","18","18",20))
        return  AffiliateUserPerformaListItemData(AffiliateUserPerformaListItemData.GetAffiliatePerformance(AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data(null,
            AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData("123456789","7","2021-08-07T23:59:59.000","2021-08-01T00:00:00.000Z",list),1)))
    }

    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun getAffiliateErrorMessage(): LiveData<Throwable> = affiliateErrorMessage
    fun getValidateUserdata(): LiveData<AffiliateValidateUserData> = validateUserdata
    fun getAffiliateAnnouncement() : LiveData<AffiliateAnnouncementData> = affiliateAnnouncement
    fun getAffiliateItemCount(): LiveData<Int> = totalItemsCount
    fun getAffiliateDataItems() : LiveData<ArrayList<Visitable<AffiliateAdapterTypeFactory>>> = affiliateDataList
    fun progressBar(): LiveData<Boolean> = progressBar

}
