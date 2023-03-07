package com.tokopedia.home.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.GetHomeBalanceItem
import com.tokopedia.home.beranda.data.model.GetHomeBalanceList
import com.tokopedia.home.beranda.data.model.GetHomeBalanceWidgetData
import com.tokopedia.home.beranda.data.model.SectionContentItem
import com.tokopedia.home.beranda.data.model.TextAttributes
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerList
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.interactor.repository.GetHomeBalanceWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTokopointsListRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeWalletAppRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_SUBSCRIPTION
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.ReserveBalance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletappGetBalance
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 25/01/22.
 */

class HomeBalanceWidgetUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val mockValueErrorHomeBalanceWidget = GetHomeBalanceWidgetData(
        getHomeBalanceList = GetHomeBalanceList(error = "Data Error From Backend",
        balancesList = listOf(GetHomeBalanceItem("Gopay", "gopay")))
    )
    private val mockValueSuccessHomeBalanceWidget = GetHomeBalanceWidgetData(
        getHomeBalanceList = GetHomeBalanceList(
            balancesList = listOf(
                GetHomeBalanceItem("Gopay", "gopay"),
                GetHomeBalanceItem("Rewards", "rewards"), GetHomeBalanceItem
                    (
                    "Langganan",
                    "subscription",
                    "{\"ResultStatus\":{\"Code\":\"200\",\"Message\":[\"Success\"],\"Reason\":\"OK\"},\"DrawerList\":[{\"IconImageURL\":\"https://images.tokopedia.net/img/tokopoints/benefit/BebasOngkir.png\",\"RedirectURL\":\"https://www.tokopedia.com/rewards\",\"RedirectAppLink\":\"tokopedia://rewards\",\"SectionContent\":[{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"GoToPlus\",\"Color\":\"#31353B\",\"IsBold\":true},\"TagAttributes\":{}},{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"Subscribe Now\",\"Color\":\"#03ac0e\"},\"TagAttributes\":{}}]}],\"CoachMarkList\":[{\"Type\":\"not_subscriber\",\"CoachMark\":[{\"IsShown\":true,\"Title\":\"Your loyalty levels have been maximized!\",\"Content\":\"Awesome! You leveled up to Juragan on GoClub because you're Platinum\",\"CTA\":{}}]}],\"IsShown\":true,\"IsSubscriber\": false}"
                )
            )
        )
    )
    private val mockValueSuccessHomeBalanceWidgetOnlyGopayAndRewards = GetHomeBalanceWidgetData(
        getHomeBalanceList = GetHomeBalanceList(
            balancesList = listOf(
                GetHomeBalanceItem("Gopay", "gopay"),
                GetHomeBalanceItem("Rewards", "rewards")
            )
        )
    )
    private val mockValueHomeBalanceWidgetErrorDataSubscription = GetHomeBalanceWidgetData(
        getHomeBalanceList = GetHomeBalanceList(
            balancesList = listOf(
                GetHomeBalanceItem("Gopay", "gopay"),
                GetHomeBalanceItem("Rewards", "rewards"), GetHomeBalanceItem
                    (
                    "Langganan",
                    "subscription",
                    "{\"ResultStatus\":{\"Code\":\"200\",\"Message\":[\"Success\"],\"Reason\":\"OK\"},\"DrawerList\":{\"IconImageURL\":\"https://images.tokopedia.net/img/tokopoints/benefit/BebasOngkir.png\",\"RedirectURL\":\"https://www.tokopedia.com/rewards\",\"RedirectAppLink\":\"tokopedia://rewards\",\"SectionContent\":[{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"GoToPlus\",\"Color\":\"#31353B\",\"IsBold\":true},\"TagAttributes\":{}},{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"Subscribe Now\",\"Color\":\"#03ac0e\"},\"TagAttributes\":{}}]}],\"CoachMarkList\":[{\"Type\":\"not_subscriber\",\"CoachMark\":[{\"IsShown\":true,\"Title\":\"Your loyalty levels have been maximized!\",\"Content\":\"Awesome! You leveled up to Juragan on GoClub because you're Platinum\",\"CTA\":{}}]}],\"IsShown\":true,\"IsSubscriber\": false}"
                )
            )
        )
    )
    private val mockValueSuccessHomeBalanceWidgetIsShownFalse = GetHomeBalanceWidgetData(
        getHomeBalanceList = GetHomeBalanceList(
            balancesList = listOf(
                GetHomeBalanceItem("Gopay", "gopay"),
                GetHomeBalanceItem("Rewards", "rewards"), GetHomeBalanceItem
                    (
                    "Langganan",
                    "subscription",
                    "{\"ResultStatus\":{\"Code\":\"200\",\"Message\":[\"Success\"],\"Reason\":\"OK\"},\"DrawerList\":[{\"IconImageURL\":\"https://images.tokopedia.net/img/tokopoints/benefit/BebasOngkir.png\",\"RedirectURL\":\"https://www.tokopedia.com/rewards\",\"RedirectAppLink\":\"tokopedia://rewards\",\"SectionContent\":[{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"GoToPlus\",\"Color\":\"#31353B\",\"IsBold\":true},\"TagAttributes\":{}},{\"Type\":\"text\",\"TextAttributes\":{\"Text\":\"Subscribe Now\",\"Color\":\"#03ac0e\"},\"TagAttributes\":{}}]}],\"CoachMarkList\":[{\"Type\":\"not_subscriber\",\"CoachMark\":[{\"IsShown\":true,\"Title\":\"Your loyalty levels have been maximized!\",\"Content\":\"Awesome! You leveled up to Juragan on GoClub because you're Platinum\",\"CTA\":{}}]}],\"IsShown\":false,\"IsSubscriber\": false}"
                )
            )
        )
    )
    private val mockTitleGopay = "Rp10.000"
    private val mockSubtitleGopay = "10.000 Coins"
    private val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(
                            Balance(
                                amountFmt = mockTitleGopay,
                                walletCode = "PEMUDA"
                            ),
                            Balance(
                                amountFmt = mockSubtitleGopay,
                                walletCode = "PEMUDAPOINTS"
                            )
                        )
                    )
                )
            )
        )
    private val mockTierRewards = "Platinum"
    private val mockNewCoupon = "10 Kupon Baru"
    private val mockTokopointsDrawerData = TokopointsDrawerListHomeData(TokopointsDrawerList(
        drawerList = listOf(
            TokopointsDrawer(
                sectionContent = listOf(
                    SectionContentItem(
                        TextAttributes(text = mockTierRewards)
                    ),
                    SectionContentItem(
                        TextAttributes(text = mockNewCoupon)
                    )
                )
            )
        )
    ))

    @Test
    fun `given value error balance widget when get header data then show error`() {
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockValueErrorHomeBalanceWidget
        runBlocking {
            val headerDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData()
            Assert.assertEquals(HomeBalanceModel.STATUS_ERROR, headerDataModel.headerDataModel?.homeBalanceModel?.status)
        }
    }

    @Test
    fun `given balance widget contains subscription when get balance widget then success get data subscription`() {
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeTokopointsListRepository = mockk<HomeTokopointsListRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeTokopointsListRepository = homeTokopointsListRepository,
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given walletapprepository tokopointsrepository and balance widget use case`(
            homeWalletAppRepository,
            getHomeBalanceWidgetRepository,
            homeTokopointsListRepository
        )
        runBlocking {
            var headerDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData()
            headerDataModel = homeBalanceWidgetUseCase.onGetWalletAppData(headerDataModel, 0, mockTitleGopay)
            headerDataModel = homeBalanceWidgetUseCase.onGetTokopointData(headerDataModel, 1, mockTierRewards)
            Assert.assertEquals(HomeBalanceModel.STATUS_SUCCESS, headerDataModel.headerDataModel?.homeBalanceModel?.status)
            Assert.assertEquals(
                mockValueSuccessHomeBalanceWidget.getHomeBalanceList.balancesList.size,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.size
            )
            Assert.assertEquals(
                TYPE_SUBSCRIPTION,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(2)?.drawerItemType
            )
            Assert.assertEquals(
                mockTierRewards,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(1)?.balanceTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockNewCoupon,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(1)?.balanceSubTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockTitleGopay,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(0)?.balanceTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockSubtitleGopay,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(0)?.balanceSubTitleTextAttribute?.text
            )
        }
    }

    @Test
    fun `given balance widget contains gopay and rewards when get balance widget then success get data gopay and rewards`() {
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeTokopointsListRepository = mockk<HomeTokopointsListRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeTokopointsListRepository = homeTokopointsListRepository,
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given walletapprepository tokopointsrepository and balance widget use case only gopay and rewards`(
            homeWalletAppRepository,
            getHomeBalanceWidgetRepository,
            homeTokopointsListRepository
        )
        runBlocking {
            var headerDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData()
            headerDataModel = homeBalanceWidgetUseCase.onGetWalletAppData(headerDataModel, 0, mockTitleGopay)
            headerDataModel = homeBalanceWidgetUseCase.onGetTokopointData(headerDataModel, 1, mockTierRewards)
            Assert.assertEquals(HomeBalanceModel.STATUS_SUCCESS, headerDataModel.headerDataModel?.homeBalanceModel?.status)
            Assert.assertEquals(
                mockValueSuccessHomeBalanceWidgetOnlyGopayAndRewards.getHomeBalanceList.balancesList.size,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.size
            )
            Assert.assertEquals(
                mockTierRewards,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(1)?.balanceTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockNewCoupon,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(1)?.balanceSubTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockTitleGopay,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(0)?.balanceTitleTextAttribute?.text
            )
            Assert.assertEquals(
                mockSubtitleGopay,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.get(0)?.balanceSubTitleTextAttribute?.text
            )
        }
    }

    @Test
    fun `given balance widget contains subscription shown false when get balance widget then success get data subscription without data subscription`() {
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeTokopointsListRepository = mockk<HomeTokopointsListRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeTokopointsListRepository = homeTokopointsListRepository,
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given walletapprepository tokopointsrepository and balance widget use case subscription shown false`(
            homeWalletAppRepository,
            getHomeBalanceWidgetRepository,
            homeTokopointsListRepository
        )
        runBlocking {
            val headerDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData()
            Assert.assertEquals(HomeBalanceModel.STATUS_SUCCESS, headerDataModel.headerDataModel?.homeBalanceModel?.status)
            Assert.assertEquals(
                mockValueSuccessHomeBalanceWidget.getHomeBalanceList.balancesList.size - 1,
                headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.size
            )
            headerDataModel.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.filter { it.drawerItemType == TYPE_SUBSCRIPTION }
                ?.isEmpty()
                ?.let {
                    Assert.assertTrue(
                        it
                    )
                }
        }
    }

    @Test
    fun `given balance widget contains subscription error data when get balance widget with failed parse data subscription then show error`() {
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val homeTokopointsListRepository = mockk<HomeTokopointsListRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeTokopointsListRepository = homeTokopointsListRepository,
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given walletapprepository tokopointsrepository and balance widget error data subscription use case`(
            homeWalletAppRepository,
            getHomeBalanceWidgetRepository,
            homeTokopointsListRepository
        )
        runBlocking {
            val headerDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData()
            Assert.assertEquals(HomeBalanceModel.STATUS_ERROR, headerDataModel.headerDataModel?.homeBalanceModel?.status)
        }
    }

    private fun `given walletapprepository tokopointsrepository and balance widget use case`(
        homeWalletAppRepository: HomeWalletAppRepository,
        getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository,
        homeTokopointsListRepository: HomeTokopointsListRepository
    ) {
        coEvery { homeWalletAppRepository.getRemoteData(any()) } returns mockWalletAppData
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockValueSuccessHomeBalanceWidget
        coEvery { homeTokopointsListRepository.getRemoteData(any()) } returns mockTokopointsDrawerData
    }

    private fun `given walletapprepository tokopointsrepository and balance widget use case only gopay and rewards`(
        homeWalletAppRepository: HomeWalletAppRepository,
        getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository,
        homeTokopointsListRepository: HomeTokopointsListRepository
    ) {
        coEvery { homeWalletAppRepository.getRemoteData(any()) } returns mockWalletAppData
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockValueSuccessHomeBalanceWidgetOnlyGopayAndRewards
        coEvery { homeTokopointsListRepository.getRemoteData(any()) } returns mockTokopointsDrawerData
    }

    private fun `given walletapprepository tokopointsrepository and balance widget use case subscription shown false`(
        homeWalletAppRepository: HomeWalletAppRepository,
        getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository,
        homeTokopointsListRepository: HomeTokopointsListRepository
    ) {
        coEvery { homeWalletAppRepository.getRemoteData(any()) } returns mockWalletAppData
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockValueSuccessHomeBalanceWidgetIsShownFalse
        coEvery { homeTokopointsListRepository.getRemoteData(any()) } returns mockTokopointsDrawerData
    }

    private fun `given walletapprepository tokopointsrepository and balance widget error data subscription use case`(
        homeWalletAppRepository: HomeWalletAppRepository,
        getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository,
        homeTokopointsListRepository: HomeTokopointsListRepository
    ) {
        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockValueHomeBalanceWidgetErrorDataSubscription
        coEvery { homeTokopointsListRepository.getRemoteData() } returns mockTokopointsDrawerData
    }

    @Test
    fun `given WalletAppRepository returns reserve balance above 10000 and not linked when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given test WalletAppRepository returns reserve balance above 10000 and not linked`(homeWalletAppRepository, getHomeBalanceWidgetRepository)
        runBlocking {
            val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
            `then reserve_balance in homeHeaderDataModel should not be empty`(result)
        }
    }

    @Test
    fun `given WalletAppRepository returns reserve balance below 10000 and not linked when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )
        `given test WalletAppRepository returns reserve balance below 10000 and not linked`(homeWalletAppRepository, getHomeBalanceWidgetRepository)
        runBlocking {
            val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
            `then reserve_balance in homeHeaderDataModel should be empty`(result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given WalletAppRepository returns linked wallet when onGetBalanceWidgetData then reserve_balance in homeHeaderDataModel should not be empty`() = runBlocking {
        val homeWalletAppRepository = mockk<HomeWalletAppRepository>(relaxed = true)
        val getHomeBalanceWidgetRepository = mockk<GetHomeBalanceWidgetRepository>(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        `given test WalletAppRepository returns linked wallet`(homeWalletAppRepository, getHomeBalanceWidgetRepository)
        val homeBalanceWidgetUseCase = createBalanceWidgetUseCase(
            userSessionInterface = userSessionInterface,
            homeWalletAppRepository = homeWalletAppRepository,
            getHomeBalanceWidgetRepository = getHomeBalanceWidgetRepository
        )

        val result = `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase)
        `then reserve_balance in homeHeaderDataModel should be empty`(result)
    }

    private fun `then reserve_balance in homeHeaderDataModel should not be empty`(
        homeHeaderDataModelResult: HomeHeaderDataModel
    ) {
        Assert.assertTrue(
            checkHeaderContainsPemudaPointsReserveBalance(homeHeaderDataModelResult)
        )
    }

    private fun `then reserve_balance in homeHeaderDataModel should be empty`(
        homeHeaderDataModelResult: HomeHeaderDataModel
    ) {
        Assert.assertTrue(
            checkHeaderNotContainsPemudaPointsReserveBalance(homeHeaderDataModelResult)
        )
    }

    fun `given test WalletAppRepository returns reserve balance above 10000 and not linked`(homeWalletAppRepository: HomeWalletAppRepository, getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = false,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 11000,
                                amountFmt = "Rp11.000"
                            )
                        )
                    )
                )
            )
        )
        val mockBalanceWidgetData = GetHomeBalanceWidgetData(
            getHomeBalanceList = GetHomeBalanceList(
                balancesList = mutableListOf(GetHomeBalanceItem(title = "Gopay", type = "gopay"))
            )
        )
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockBalanceWidgetData
        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    fun `given test WalletAppRepository returns reserve balance below 10000 and not linked`(homeWalletAppRepository: HomeWalletAppRepository, getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = false,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 9000,
                                amountFmt = "Rp9.000"
                            )
                        )
                    )
                )
            )
        )
        val mockBalanceWidgetData = GetHomeBalanceWidgetData(
            getHomeBalanceList = GetHomeBalanceList(
                balancesList = mutableListOf(GetHomeBalanceItem(title = "Gopay", type = "gopay"))
            )
        )
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockBalanceWidgetData
        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    fun `given test WalletAppRepository returns linked wallet`(homeWalletAppRepository: HomeWalletAppRepository, getHomeBalanceWidgetRepository: GetHomeBalanceWidgetRepository) {
        val mockWalletAppData = WalletAppData(
            walletappGetBalance = WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        reserveBalance = listOf(
                            ReserveBalance(
                                walletCode = "PEMUDAPOINTS",
                                walletCodeFmt = "PEMUDAPOINTS",
                                amount = 9000,
                                amountFmt = "Rp9.000"
                            )
                        )
                    )
                )
            )
        )
        val mockBalanceWidgetData = GetHomeBalanceWidgetData(
            getHomeBalanceList = GetHomeBalanceList(
                balancesList = mutableListOf(GetHomeBalanceItem(title = "Gopay", type = "gopay"))
            )
        )
        coEvery { getHomeBalanceWidgetRepository.getRemoteData(any()) } returns mockBalanceWidgetData
        coEvery { homeWalletAppRepository.getRemoteData() } returns mockWalletAppData
    }

    suspend fun `when onGetBalanceWidgetData`(homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase): HomeHeaderDataModel {
        return homeBalanceWidgetUseCase.onGetBalanceWidgetData()
    }

    private fun checkHeaderContainsPemudaPointsReserveBalance(data: HomeHeaderDataModel): Boolean {
        if (data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) return false
        return data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.filter {
            it.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED &&
                    it.reserveBalance.isNotEmpty()
        } != null
    }

    private fun checkHeaderNotContainsPemudaPointsReserveBalance(data: HomeHeaderDataModel): Boolean {
        if (data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.isEmpty() == true) return false
        return data.headerDataModel?.homeBalanceModel?.balanceDrawerItemModels?.filter {
            it.drawerItemType == BalanceDrawerItemModel.TYPE_WALLET_APP_NOT_LINKED &&
                    it.reserveBalance.isNotEmpty()
        }?.isEmpty()?:false
    }
}
