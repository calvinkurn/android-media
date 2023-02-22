package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.GetTopAdsDashboardGroupsV3
import com.tokopedia.topads.common.data.response.GetTopadsDashboardGroupStatisticsV3
import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.topads.common.data.response.TopadsManageGroupAdsResponse
import com.tokopedia.topads.common.domain.usecase.GetTopAdsGroupsStatisticsUseCase
import com.tokopedia.topads.common.domain.usecase.GetTopAdsGroupsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsManageGroupAdsUseCase
import com.tokopedia.topads.util.AdGroupTestUtils.PER_PAGE
import com.tokopedia.topads.util.AdGroupTestUtils.addInfiniteLoadingError
import com.tokopedia.topads.util.AdGroupTestUtils.chooseAdGroup
import com.tokopedia.topads.util.AdGroupTestUtils.createAdGroupList
import com.tokopedia.topads.util.AdGroupTestUtils.createAdGroupListError
import com.tokopedia.topads.util.AdGroupTestUtils.createAdGroupListWithStatsLoading
import com.tokopedia.topads.util.AdGroupTestUtils.getAdGroupDummyData
import com.tokopedia.topads.util.AdGroupTestUtils.getAdGroupStatsData
import com.tokopedia.topads.util.assertLists
import com.tokopedia.topads.view.adapter.adgrouplist.model.CreateAdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MpAdsGroupsViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var viewModel:MpAdsGroupsViewModel? = null
    private val getTopAdsGroupsUseCase: GetTopAdsGroupsUseCase = mockk()
    private val getTopAdsGroupStatsUseCase: GetTopAdsGroupsStatisticsUseCase = mockk()
    private val getTopadsDepositsUseCase: TopAdsGetDepositUseCase = mockk()
    private val topAdsManageGroupAdsUseCase: TopAdsManageGroupAdsUseCase = mockk()

    private val productId = ""
    private val shopId = ""
    private val requestParams = RequestParams()
    private val error = Throwable("error")

    private val userSession = mockk<UserSessionInterface>()

    @Before
    fun setup(){
        every { userSession.shopId } returns ""
        viewModel = MpAdsGroupsViewModel(
            getTopAdsGroupsUseCase,
            getTopAdsGroupStatsUseCase,
            getTopadsDepositsUseCase,
            topAdsManageGroupAdsUseCase,
            userSession,
            rule.dispatchers
        )
    }

    private fun manualViewModelSetup(){
        viewModel = MpAdsGroupsViewModel(
            getTopAdsGroupsUseCase,
            getTopAdsGroupStatsUseCase,
            getTopadsDepositsUseCase,
            topAdsManageGroupAdsUseCase,
            userSession,
            rule.dispatchers
        )
    }

    @Test
    fun `load first page success and stats success`(){
        fetchFirstPage()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page success and stats success with non empty sort param`(){
        clearAllMocks()
        every { userSession.shopId } returns null
        manualViewModelSetup()
        fetchFirstPage(sort = "tampil")
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page null response`(){
        val groupResponse = TopAdsGroupsResponse()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListError(GlobalError.PAGE_NOT_FOUND)
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page null list`(){
        val groupResponse = TopAdsGroupsResponse(GetTopAdsDashboardGroupsV3(data = null, page = null))
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListError(GlobalError.PAGE_NOT_FOUND)
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page empty list`(){
        val groupResponse = TopAdsGroupsResponse(GetTopAdsDashboardGroupsV3())
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListError(GlobalError.PAGE_NOT_FOUND)
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page server error`(){
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListError(GlobalError.SERVER_ERROR)
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(Throwable) -> Unit>(5).invoke(error)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page ad stats failure`(){
        val groupResponse = getAdGroupDummyData()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListWithStatsLoading()
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",1,"",any(),any(),any()) } answers {
            arg<(Throwable) -> Unit>(6).invoke(error)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page success and stats success`(){
        fetchMorePages()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList(2)
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page success and stats success with non empty sort param`(){
       fetchMorePages(sort = "tampil")
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList(2)
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page with last page`(){
        fetchFirstPage()
        val groupResponseNextPage = getAdGroupDummyData(2,2 * PER_PAGE)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponseNextPage)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),"",any(),any(),any()) } answers {
            arg<(Throwable) -> Unit>(6).invoke(error)
        }
        viewModel?.loadMorePages()
        Assert.assertEquals(viewModel?.hasNextLiveData?.value,false)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page total greater than 2 pages`(){
       fetchMorePages(multiplier = 3)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList(2)
        }
        Assert.assertEquals(viewModel?.hasNextLiveData?.value,true)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page with empty first page`(){
        val groupResponse = TopAdsGroupsResponse()
        val groupStatsResponse = TopAdsGroupsStatisticResponseResponse()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            add(CreateAdGroupUiModel())
            add(ErrorUiModel(GlobalError.PAGE_NOT_FOUND))
            add(ReloadInfiniteUiModel())
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            if(arg<Int>(2) == 1) arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
            else arg<(Throwable) -> Unit>(5).invoke(error)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",1,"",any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponse)
        }
        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page with null response`(){
        val groupResponseFirstPage = getAdGroupDummyData(total = 2* PER_PAGE)
        val groupStatsResponseFirstPage = getAdGroupStatsData(total = 2 * PER_PAGE)
        val groupResponseNextPage = TopAdsGroupsResponse()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }

        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(
                if(arg<Int>(2) == 1) groupResponseFirstPage
                else groupResponseNextPage
            )
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),"",any(),any(),any()) } answers {
            if(arg<Int>(2) == 1){
                arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponseFirstPage)
            }
            else{
                arg<(Throwable) -> Unit>(6).invoke(error)
            }
        }
        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page with null list`(){
        val groupResponseFirstPage = getAdGroupDummyData(total = 2* PER_PAGE)
        val groupStatsResponseFirstPage = getAdGroupStatsData(total = 2 * PER_PAGE)
        val groupResponseNextPage = TopAdsGroupsResponse(GetTopAdsDashboardGroupsV3(data = null))
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }

        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(
                if(arg<Int>(2) == 1) groupResponseFirstPage
                else groupResponseNextPage
            )
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),"",any(),any(),any()) } answers {
            if(arg<Int>(2) == 1){
                arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponseFirstPage)
            }
            else{
                arg<(Throwable) -> Unit>(6).invoke(error)
            }
        }
        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page failure`(){
        val groupResponseFirstPage = getAdGroupDummyData(total = 2 * PER_PAGE)
        val groupStatsResponseFirstPage = getAdGroupStatsData(total = 2 * PER_PAGE)

        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            addInfiniteLoadingError()
        }

        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            if(arg<Int>(2) == 1){
                arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponseFirstPage)
            }
            else arg<(Throwable) -> Unit>(5).invoke(error)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),any(),any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponseFirstPage)
        }

        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load next page fail but success next try`(){
        val groupResponseFirstPage = getAdGroupDummyData(total = 2 * PER_PAGE)
        val groupStatsResponseFirstPage = getAdGroupStatsData(total = 2 * PER_PAGE)
        val groupResponseNextPage = getAdGroupDummyData(2,2 * PER_PAGE)
        val groupStatsResponseNextPage = getAdGroupStatsData(2,2 * PER_PAGE)

        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList(2)
        }

        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            if(arg<Int>(2) == 1){
                arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponseFirstPage)
            }
            else arg<(Throwable) -> Unit>(5).invoke(error)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),any(),any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponseFirstPage)
        }

        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
        clearAllMocks()
        every { getTopAdsGroupsUseCase.getAdGroups("","",any(),"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponseNextPage)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",any(),"",any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponseNextPage)
        }
        viewModel?.loadMorePages()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `choose ad group with no existing selected group`(){
        val selectedIndex = 1
        setupChooseAdGroup()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            chooseAdGroup(selectedIndex)
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `choose ad group with existing selected group`(){
        val selectedIndex = 2
        val alreadySelectedIndex = 1
        setupChooseAdGroup(alreadySelectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            chooseAdGroup(alreadySelectedIndex)
            chooseAdGroup(selectedIndex)
        }
        viewModel?.chooseAdGroup(selectedIndex)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `choose out of bounds ad group with no existing selected group`(){
        val selectedIndex = 40
        setupChooseAdGroup(selectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `choose out of bounds ad group with existing selected group`(){
        val selectedIndex = 40
        val alreadySelectedIndex = 1
        setupChooseAdGroup(alreadySelectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.chooseAdGroup(selectedIndex)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `choose invalid ad group`(){
        setupChooseAdGroup(0)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `unselect ad group`(){
        val selectedIndex = 1
        setupChooseAdGroup(selectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.unChooseAdGroup(selectedIndex)
        viewModel?.getSelectedAdGroupPosition()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `unchoose invalid ad group`(){
        fetchFirstPage()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.unChooseAdGroup(0)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `unchoose out of bounds ad group`(){
        fetchFirstPage()
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.unChooseAdGroup(50)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `move selected ad group success and topads deposit success`(){
        val moveAdResponse = TopadsManageGroupAdsResponse()
        val depositResponse = Deposit()
        val selectedIndex = 1
        setupChooseAdGroup(selectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            chooseAdGroup(selectedIndex)
        }
        every { topAdsManageGroupAdsUseCase.executeUseCase(requestParams,any(),any()) } answers {
            secondArg<(TopadsManageGroupAdsResponse) -> Unit>().invoke(moveAdResponse)
        }
        every { topAdsManageGroupAdsUseCase.createRequestParams(any(),shopId,productId) } returns requestParams
        every { getTopadsDepositsUseCase.execute(any(),any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(depositResponse)
        }
        viewModel?.moveTopAdsGroup(productId)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
        Assert.assertEquals((viewModel?.topadsCreditLiveData?.value as Success).data,Pair(moveAdResponse,depositResponse))
    }

    @Test
    fun `move selected ad group success and topads deposit Failure`(){
        val moveAdResponse = TopadsManageGroupAdsResponse()
        val selectedIndex = 1
        setupChooseAdGroup(selectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            chooseAdGroup(selectedIndex)
        }
        every { topAdsManageGroupAdsUseCase.executeUseCase(requestParams,any(),any()) } answers {
            secondArg<(TopadsManageGroupAdsResponse) -> Unit>().invoke(moveAdResponse)
        }
        every { topAdsManageGroupAdsUseCase.createRequestParams(any(),shopId,productId) } returns requestParams
        every { getTopadsDepositsUseCase.execute(any(),any()) } answers {
            lastArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel?.moveTopAdsGroup(productId)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
        Assert.assertEquals((viewModel?.topadsCreditLiveData?.value as Fail).throwable,error)
    }

    @Test
    fun `move selected ad group failure`(){
        val selectedIndex = 1
        setupChooseAdGroup(selectedIndex)
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
            chooseAdGroup(selectedIndex)
        }
        every { topAdsManageGroupAdsUseCase.executeUseCase(requestParams,any(),any()) } answers {
            lastArg<(Throwable) -> Unit>().invoke(error)
        }
        every { topAdsManageGroupAdsUseCase.createRequestParams(any(),shopId,productId) } returns requestParams
        viewModel?.moveTopAdsGroup(productId)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
        Assert.assertEquals((viewModel?.topadsCreditLiveData?.value as Fail).throwable,error)
    }

    @Test
    fun `move selected ad group with no selected group`(){
        fetchFirstPage()
        val productId = ""
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupList()
        }
        viewModel?.loadFirstPage()
        viewModel?.moveTopAdsGroup(productId)
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    @Test
    fun `load first page success empty stats`(){
        val groupResponse = getAdGroupDummyData()
        val groupStatsResponse = TopAdsGroupsStatisticResponseResponse(
            GetTopadsDashboardGroupStatisticsV3()
        )
        val expectedList:MutableList<Visitable<*>> = mutableListOf<Visitable<*>>().apply {
            createAdGroupListWithStatsLoading()
        }
        every { getTopAdsGroupsUseCase.getAdGroups("","",1,"",any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("","",1,"",any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponse)
        }
        viewModel?.loadFirstPage()
        viewModel?.mainListLiveData?.value?.assertLists(expectedList)
    }

    private fun fetchFirstPage(sort:String = "",search:String = ""){
        val groupResponse = getAdGroupDummyData()
        val groupStatsResponse = getAdGroupStatsData()
        viewModel?.sortParam = sort
        viewModel?.searchKeyword = search
        every { getTopAdsGroupsUseCase.getAdGroups("",search,1,getSortParam(sort),any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(groupResponse)
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("",search,1,getSortParam(sort),any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(groupStatsResponse)
        }
        viewModel?.loadFirstPage()
    }

    private fun fetchMorePages(sort:String = "",search:String = "",nextPage:Int = 2,multiplier:Int = 2){
        val groupResponseFirstPage = getAdGroupDummyData(total = multiplier * PER_PAGE)
        val groupStatsResponseFirstPage = getAdGroupStatsData(total = multiplier * PER_PAGE)
        val groupResponseNextPage = getAdGroupDummyData(nextPage,multiplier * PER_PAGE)
        val groupStatsResponseNextPage = getAdGroupStatsData(nextPage,multiplier * PER_PAGE)
        viewModel?.sortParam = sort
        viewModel?.searchKeyword = search
        every { getTopAdsGroupsUseCase.getAdGroups("",search,any(),getSortParam(sort),any(),any()) } answers {
            arg<(TopAdsGroupsResponse) -> Unit>(4).invoke(
                if(arg<Int>(2) == 1) groupResponseFirstPage
                else groupResponseNextPage
            )
        }
        every { getTopAdsGroupStatsUseCase.getAdGroupsStatistics("",search,any(),getSortParam(sort),any(),any(),any()) } answers {
            arg<(TopAdsGroupsStatisticResponseResponse) -> Unit>(5).invoke(
                if(arg<Int>(2) == 1) groupStatsResponseFirstPage
                else groupStatsResponseNextPage
            )
        }

        viewModel?.loadFirstPage()
        viewModel?.loadMorePages()
    }

    private fun setupChooseAdGroup(index:Int = 1){
        fetchFirstPage()
        viewModel?.chooseAdGroup(index)
    }

    private fun getSortParam(sort:String) = if(sort.isEmpty()) "" else "-$sort"
}
