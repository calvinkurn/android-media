package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.topads.common.domain.usecase.GetTopAdsGroupsStatisticsUseCase
import com.tokopedia.topads.common.domain.usecase.GetTopAdsGroupsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.data.AdGroupCompleteData
import com.tokopedia.topads.data.AdGroupSettingData
import com.tokopedia.topads.data.AdGroupStatsData
import com.tokopedia.topads.data.mappers.AdGroupMapper
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupShimmerUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.LoadingMoreUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import kotlin.math.min
import kotlin.text.StringBuilder

class MpAdsGroupsViewModel @Inject constructor(
    private val getTopAdsGroupsUseCase: GetTopAdsGroupsUseCase,
    private val getTopAdsGroupStatsUseCase: GetTopAdsGroupsStatisticsUseCase,
    private val getTopadsDepositsUseCase: TopAdsGetDepositUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object{
        private const val NO_POSITION = -1
        private const val PER_PAGE = 20
    }

    // Data list to hold all ad groups
    private var adGroupDataList:MutableList<AdGroupCompleteData> = mutableListOf()

    /*
      Main list to hold all visitables of recyclerview
      Always update this list and call updateVisitableLiveData()
      to update the main recycler view
    */
    private val visitableList:MutableList<Visitable<*>> = mutableListOf()

    /*
      LiveData that stores visitableList
      Always call updateVisitableLiveData() after updating visitable list
      to update the main recycler view
    */
    private val _mainListLiveData:MutableLiveData<List<Visitable<*>>> = MutableLiveData(visitableList)
    val mainListLiveData:LiveData<List<Visitable<*>>> = _mainListLiveData

    private val _hasNextLiveData:MutableLiveData<Boolean> = MutableLiveData(true)
    val hasNextLiveData:LiveData<Boolean> = _hasNextLiveData

    private val _topadsCreditLiveData:MutableLiveData<com.tokopedia.usecase.coroutines.Result<Deposit>> = MutableLiveData(null)
    val topadsCreditLiveData:LiveData<com.tokopedia.usecase.coroutines.Result<Deposit>> = _topadsCreditLiveData

    private var adGroupListStartIndex = NO_POSITION
    private var selectedAdGroupIndex = NO_POSITION

    var sortParam = ""
    var searchKeyword = ""


    private var currentPage = 1
    private var currentItemCount = 0
    private var totalItemCount = 0
    private var shopId = ""

    fun loadFirstPage(shopId:String){
        this.shopId = shopId
        reset()
        createShimmerList()
        getTopAdsGroupsUseCase.getAdGroups(
            shopId,
            searchKeyword,
            1,
            sortParam,
            ::onFirstPageSuccess,
            ::onFirstPageFailure
        )
    }

    private fun onFirstPageSuccess(data: TopAdsGroupsResponse){
        currentItemCount += data.response?.data?.size.orZero()
        totalItemCount = data.response?.page?.total ?: 1
        val mappedList = AdGroupMapper.getAdGroupsFromDashboardResponse(data)
        createDataList(mappedList.first)
        createAdGroupList(mappedList.second)
        getAdGroupStats()
    }

    private fun onFirstPageFailure(error:Throwable){
        createErrorList(GlobalError.SERVER_ERROR)
    }

    private fun getAdGroupStats(){
        getTopAdsGroupStatsUseCase.getAdGroupsStatistics(
            shopId = shopId,
            keyword = searchKeyword,
            sort = sortParam,
            groupIds = getGroupsIds(),
            page = currentPage,
            success = ::onGetAdGroupStatsSuccess,
            failure = ::onGetAdGroupStatsFailure
        )
    }

    private fun onGetAdGroupStatsSuccess(data: TopAdsGroupsStatisticResponseResponse){
      val statList = AdGroupMapper.getAdGroupStatsFromResponse(data)
//        updateAdStatsVisitableList(statList)
    }

    private fun onGetAdGroupStatsFailure(err:Throwable){
     val b = 10
    }

    private fun updateAdStatsVisitableList(statList:List<AdGroupStatsData>){
        val startIndex = (currentPage - 1) * PER_PAGE + adGroupListStartIndex
        val endIndex = startIndex + min(PER_PAGE,statList.size)
        for(idx in startIndex..endIndex){
            val visitable = visitableList[idx]
            if(visitable is AdGroupUiModel){
                visitableList[idx] = AdGroupUiModel(
                    groupId = visitable.groupId,
                    groupName = visitable.groupName,
                    adGroupSetting = visitable.adGroupSetting.copy(),
                    adGroupStats = statList[idx]
                )
            }
        }
        updateVisitableLiveData()
    }

    private fun reset(){
        currentPage=1
        currentItemCount = 0
        totalItemCount = 0
        _hasNextLiveData.value = true
        setAdGroupStartPosition(NO_POSITION)
        setSelectedAdGroupPosition(NO_POSITION)
    }

    fun loadMorePages(shopId:String){
        createInfiniteLoadingVisitableList()
        getTopAdsGroupsUseCase.getAdGroups(
            shopId,
            searchKeyword,
            currentPage+1,
            sortParam,
            ::onLoadMoreSuccess,
            ::onLoadMoreFailure
        )
    }

    private fun createInfiniteLoadingVisitableList(){
        if(visitableList.last() is ReloadInfiniteUiModel){
            visitableList.removeLast()
        }
        if(visitableList.last() is AdGroupUiModel){
            visitableList.add(LoadingMoreUiModel())
        }
        updateVisitableLiveData()
    }

    private fun removeInfiniteLoadingVisitableList(){
        if(visitableList.last() is LoadingMoreUiModel){
            visitableList.removeLast()
            updateVisitableLiveData()
        }
    }

    private fun onLoadMoreSuccess(data:TopAdsGroupsResponse){
        removeInfiniteLoadingVisitableList()
        if(!isLastPage()){
            currentItemCount += data.response?.data?.size.orZero()
            val mappedList = AdGroupMapper.getAdGroupsFromDashboardResponse(data)
            updateDataList(mappedList.first)
            addMoreAdGroups(mappedList.second)
        }
        currentPage++
        getAdGroupStats()
        _hasNextLiveData.value = !isLastPage()
    }

    private fun onLoadMoreFailure(error:Throwable){
        createInfiniteLoadRetryList()
    }

    private fun createInfiniteLoadRetryList(){
        removeInfiniteLoadingVisitableList()
        visitableList.add(ReloadInfiniteUiModel())
        updateVisitableLiveData()
    }

    private fun createDummyList(){
      visitableList.clear()
        visitableList.addAll(
            listOf(
                CreateAdGroupUiModel(),
                AdGroupUiModel(),
                AdGroupUiModel(adGroupSetting = AdGroupSettingData(loading = true)),
                AdGroupUiModel(adGroupStats = AdGroupStatsData(loading = true)),
                AdGroupUiModel(),
                AdGroupUiModel(),
                AdGroupUiModel(),
                ReloadInfiniteUiModel()
            )
        )
        setAdGroupStartPosition(1)
        updateVisitableLiveData()
    }

    // Call this method to create a new visitable list of Ad groups
    private fun createAdGroupList(groupList:List<AdGroupUiModel>){
        visitableList.clear()
        visitableList.add(CreateAdGroupUiModel())
        visitableList.addAll(groupList)
        setAdGroupStartPosition(1)
        updateVisitableLiveData()
    }

    // Call this method to add Ad groups to the existing visitable list
    private fun addMoreAdGroups(groupList:List<AdGroupUiModel>){
        visitableList.addAll(groupList)
        setAdGroupStartPosition(1)
        updateVisitableLiveData()
    }


    // Call this method to create a new visitable list with shimmer loading
    private fun createShimmerList(){
        visitableList.clear()
        visitableList.add(CreateAdGroupUiModel())
        visitableList.add(AdGroupShimmerUiModel())
        updateVisitableLiveData()
    }

    // Call this method to create a new visitable list with error
    private fun createErrorList(errorType:Int){
        visitableList.clear()
        visitableList.add(CreateAdGroupUiModel())
        visitableList.add(ErrorUiModel(errorType))
        updateVisitableLiveData()
    }

    //Call this method to create a new ad group data list
    private fun createDataList(list:List<AdGroupCompleteData>){
        adGroupDataList.clear()
        adGroupDataList.addAll(list)
    }

    //Call this method to update the existing ad group data list
    private fun updateDataList(list:List<AdGroupCompleteData>){
        adGroupDataList.addAll(list)
    }

    // Call this method after updating visitable list
    private fun updateVisitableLiveData(){
        _mainListLiveData.value = visitableList
    }

    // Call this method to check whether any more pages can be loaded
    private fun isLastPage() = currentItemCount>=totalItemCount

    // Call this method to set Ad Group starting index in visitables list
    private fun setAdGroupStartPosition(position:Int){
        adGroupListStartIndex = position
    }

    // Call this method to set selected Ad Group index in Ad Group data list
    private fun setSelectedAdGroupPosition(position:Int){
        selectedAdGroupIndex = position
    }

    /*
      Call this method to get the combined groupId's of the requested page
      By default it returns for the current page
     */
    private fun getGroupsIds(page:Int = currentPage) : String{
        return if(page<=currentPage){
            val grpIdsString = StringBuilder()
            val dataListSize = adGroupDataList.size
            val startIndex = (page - 1) * PER_PAGE
            val endIndex = startIndex + min(PER_PAGE,(dataListSize - startIndex))
            for(idx in startIndex until endIndex){
                val groupData = adGroupDataList[idx]
                grpIdsString.append(groupData.groupId)
                if(idx!=endIndex-1) grpIdsString.append(",")
            }
            grpIdsString.toString()
        }
        else ""
    }

    fun chooseAdGroup(selectedPos:Int){
        if(selectedAdGroupIndex!= NO_POSITION){
            val alreadySelectedIndex = selectedAdGroupIndex + adGroupListStartIndex
            unselectGroup(alreadySelectedIndex)
        }
        selectGroup(selectedPos)
        setSelectedAdGroupPosition(selectedPos - adGroupListStartIndex)
        updateVisitableLiveData()
    }

    fun unChooseAdGroup(selectedPos:Int){
        unselectGroup(selectedPos)
        setSelectedAdGroupPosition(NO_POSITION)
        updateVisitableLiveData()
    }

    private fun selectGroup(index:Int){
        if(index < visitableList.size && visitableList[index] is AdGroupUiModel){
            visitableList[index] = (visitableList[index] as AdGroupUiModel).copy(selected = true)
        }
    }

    private fun unselectGroup(index:Int){
        if(index < visitableList.size && visitableList[index] is AdGroupUiModel){
            visitableList[index] = (visitableList[index] as AdGroupUiModel).copy(selected = false)
        }
    }

    fun checkTopadsDeposits(){
        getTopadsDepositsUseCase.execute(
            {
                _topadsCreditLiveData.value = Success(it)
            },
            {
                _topadsCreditLiveData.value = Fail(it)
            }
        )
    }
}
