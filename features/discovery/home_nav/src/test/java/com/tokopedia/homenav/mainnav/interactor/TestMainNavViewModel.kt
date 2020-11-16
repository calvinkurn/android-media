package com.tokopedia.homenav.mainnav.interactor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.membership.TierPojo
import com.tokopedia.homenav.mainnav.data.pojo.membership.TokopointStatusPojo
import com.tokopedia.homenav.mainnav.data.pojo.membership.TokopointsPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopInfoPojo
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.*
import com.tokopedia.homenav.rule.CoroutinesTestRule
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TestMainNavViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutinesTestRule()

    @ApplicationContext
    lateinit var context: Context

    private val defaultLoginState = AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
    private lateinit var viewModel : MainNavViewModel
    private val shopId = 1224

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
    @Test
    fun `test when nav page launched from page others than homepage then show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = "Other page"
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuViewModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuViewModel

        Assert.assertNotNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from homepage then do notshow back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuViewModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuViewModel?

        Assert.assertNull(backToHomeMenu)
    }

    //user menu section
    @Test
    fun `test when viewmodel created and user has no shop then viewmodel create at least 7 user menu`() {
        val defaultUserMenuCount = 7

        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?.filter {
            (it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.USER_MENU)
        }

        Assert.assertEquals(defaultUserMenuCount, visitableList!!.size)
    }

    //user menu section
    @Test
    fun `test when viewmodel created and logged in user does not have shop then viewmodel create open shop ticker user menu`() {
        val testTickerTitle = "This is test ticker"
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val userSession = mockk<UserSession>()

        every { userSession.hasShop() } returns false
        every { userSession.isLoggedIn() } returns true
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel(title = testTickerTitle) }

        viewModel = createViewModel(userSession = userSession, clientMenuGenerator = clientMenuGenerator)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val shopTicker = visitableList.find { it is HomeNavTickerViewModel } as HomeNavTickerViewModel

        Assert.assertNotNull(shopTicker)
        Assert.assertEquals(shopTicker.title, testTickerTitle)
    }

    //user menu section
    @Test
    fun `test when logged in user get complain notification then viewmodel update complain visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 800

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountComplain = mockUnreadCount) }

        viewModel = createViewModel(
                clientMenuGenerator = clientMenuGenerator,
                getNavNotification = getNavNotification
        )

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuViewModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuViewModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    //user menu section
    @Test
    fun `test when logged in user get inbox ticket notification then viewmodel update tokopedia care visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 900

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountInboxTicket = mockUnreadCount) }

        viewModel = createViewModel(
                clientMenuGenerator = clientMenuGenerator,
                getNavNotification = getNavNotification
        )

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuViewModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuViewModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and user does not ongoing order and payment transaction then only create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
                getUohOrdersNavUseCase = getUohOrdersNavUseCase,
                getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemViewModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNull(transactionDataModel)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and logged in user only have ongoing order then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSession>()

        every { userSession.isLoggedIn() } returns true
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())

        viewModel = createViewModel(
                getUohOrdersNavUseCase = getUohOrdersNavUseCase,
                getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
                userSession = userSession
        )

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemViewModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNotNull(transactionDataModel)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and loggedin user only have payment transaction then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSession>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()
        every { userSession.isLoggedIn() } returns true

        viewModel = createViewModel(
                getUohOrdersNavUseCase = getUohOrdersNavUseCase,
                getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
                userSession = userSession
        )

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuViewModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemViewModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNotNull(transactionDataModel)
    }

    @Test
    fun `Success getUserNameAndPictureData`(){
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        coEvery {
            getMainNavDataUseCase.executeOnBackground()
        } returns MainNavigationDataModel(listOf(AccountHeaderViewModel(userName = "Joko", userImage = "Tingkir")))
        viewModel = createViewModel(getMainNavDataUseCase = getMainNavDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty())
    }

    @Test
    fun `Error getUserNameAndPictureData missing name`(){
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        coEvery {
            getMainNavDataUseCase.executeOnBackground()
        } returns MainNavigationDataModel(listOf(AccountHeaderViewModel(userName = "", userImage = "Tingkir")))
        viewModel = createViewModel(getMainNavDataUseCase = getMainNavDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty())
    }

    @Test
    fun `Error getUserNameAndPictureData missing profile picture`(){
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        coEvery {
            getMainNavDataUseCase.executeOnBackground()
        } returns MainNavigationDataModel(listOf(AccountHeaderViewModel(userName = "Joko", userImage = "")))
        viewModel = createViewModel(getMainNavDataUseCase = getMainNavDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isEmpty())
    }

    @Test
    fun `Error getUserNameAndPictureData missing all`(){
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        coEvery {
            getMainNavDataUseCase.executeOnBackground()
        } returns MainNavigationDataModel(listOf(AccountHeaderViewModel(userName = "", userImage = "")))
        viewModel = createViewModel(getMainNavDataUseCase = getMainNavDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isEmpty()
                && accountHeaderViewModel.userImage.isEmpty())
    }

    @Test
    fun `Success getUserMembershipBadge`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getUserMembershipUseCase = mockk<GetUserMembershipUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getUserMembershipUseCase.executeOnBackground()
        } returns MembershipPojo(TokopointsPojo(TokopointStatusPojo(TierPojo(eggImageURL = "telur"))))
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.badge.isNotEmpty())
    }

    @Test
    fun `Error getUserMembershipBadge empty`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getUserMembershipUseCase = mockk<GetUserMembershipUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getUserMembershipUseCase.executeOnBackground()
        } returns MembershipPojo(TokopointsPojo(TokopointStatusPojo(TierPojo(eggImageURL = ""))))
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.badge.isEmpty())
    }

//    @Test
//    fun `Success getShopName`(){
//        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
//        val getShopInfoUseCase = mockk<GetShopInfoUseCase>()
//        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
//        getUserInfoUseCase.getBasicData()
//        getMainNavDataUseCase.getBasicData()
//        coEvery {
//            getShopInfoUseCase.executeOnBackground()
//        } returns ShopInfoPojo(ShopInfoPojo.ShopCore(name = "toko telor"))
//        viewModel = createViewModel(
//                getMainNavDataUseCase = getMainNavDataUseCase,
//                getShopInfoUseCase = getShopInfoUseCase)
//
//        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
//        Assert.assertTrue(dataList.isNotEmpty())
//        Assert.assertNotNull(accountHeaderViewModel)
//        Assert.assertTrue(accountHeaderViewModel.shopName.isNotEmpty())
//    }

    @Test
    fun `Error getShopName empty name`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getShopInfoUseCase = mockk<GetShopInfoUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } returns ShopInfoPojo(ShopInfoPojo.ShopCore(name = ""))
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getShopInfoUseCase = getShopInfoUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.shopName.isEmpty())
    }

    @Test
    fun `Success getOvoSaldo`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns WalletBalanceModel(cashBalance = "Rp 1234", pointBalance = "Rp 2345")
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty())
    }

    @Test
    fun `Success getOvoSaldo empty saldo`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns WalletBalanceModel(cashBalance = "", pointBalance = "Rp 2345")
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isEmpty())
    }

    @Test
    fun `Success getOvoSaldo empty point`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns WalletBalanceModel(cashBalance = "Rp 1234", pointBalance = "")
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.ovoPoint.isEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty())
    }

    @Test
    fun `Success getOvoSaldo both empty`(){
        val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
        val getWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>()
        val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>()
        getUserInfoUseCase.getBasicData()
        getMainNavDataUseCase.getBasicData()
        coEvery {
            getWalletBalanceUseCase.executeOnBackground()
        } returns WalletBalanceModel(cashBalance = "", pointBalance = "")
        viewModel = createViewModel(
                getMainNavDataUseCase = getMainNavDataUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.ovoPoint.isEmpty()
                && accountHeaderViewModel.ovoSaldo.isEmpty())
    }
}