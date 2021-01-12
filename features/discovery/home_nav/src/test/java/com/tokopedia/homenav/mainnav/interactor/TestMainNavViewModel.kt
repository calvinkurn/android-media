package com.tokopedia.homenav.mainnav.interactor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.*
import com.tokopedia.homenav.rule.CoroutinesTestRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.domain.usecase.RefreshShopBasicDataUseCase
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
        every { userSession.hasShop() } returns true

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
        every { userSession.hasShop() } returns true

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
    fun `Success getProfileFullData`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderViewModel(
                userName = "Joko",
                userImage = "Tingkir",
                ovoSaldo = "Rp 100",
                ovoPoint = "Rp 100",
                badge = "kucing",
                shopName = "binatang",
                shopId = "1234")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty()
                && accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.badge.isNotEmpty()
                && accountHeaderViewModel.shopId.isNotEmpty()
                && accountHeaderViewModel.shopName.isNotEmpty())
    }


    @Test
    fun `Success getUserNameAndPictureData`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderViewModel(userName = "Joko", userImage = "Tingkir")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty())
    }

    @Test
    fun `Success get account admin info`() {
        val position = 0
        val isLocationAdmin: Boolean = true
        val expectedAdminRoleText = "Joko Tingkir"
        val adminDataResponse =
                AdminDataResponse(
                        data = AdminData(
                                adminTypeText = expectedAdminRoleText,
                                detail = AdminDetailInformation(
                                        roleType = AdminRoleType(
                                                isLocationAdmin = isLocationAdmin
                                        )
                                )
                        )
                )
        val expectedCanGoToSellerAccount = !isLocationAdmin
        val accountInfoPair = Pair(adminDataResponse, null)
        val refreshShopBasicDataUseCase = mockk<RefreshShopBasicDataUseCase>()
        val gqlRepository = mockk<GraphqlRepository>()
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository))
        val userSession = mockk<UserSessionInterface>(relaxed = true)
        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } returns accountInfoPair
        coEvery {
            userSession.hasShop()
        } returns true
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isLocationAdmin
        } returns false
        coEvery {
            userSession.isLoggedIn
        } returns true

        viewModel = createViewModel(
                accountAdminInfoUseCase = accountAdminInfoUseCase,
                userSession = userSession)
        viewModel.getMainNavData(false)

        val mainNavDataModel = viewModel.mainNavLiveData.value
        (mainNavDataModel?.dataList?.getOrNull(position) as? AccountHeaderViewModel).let { actualResult ->
            val actualCanGoToSellerAccount = actualResult?.canGoToSellerAccount
            val actualAdminRoleText = actualResult?.adminRoleText
            Assert.assertEquals(expectedAdminRoleText, actualAdminRoleText)
            Assert.assertEquals(expectedCanGoToSellerAccount, actualCanGoToSellerAccount)
        }
    }

    @Test
    fun `Error getUserNameAndPictureData missing name`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderViewModel(userName = "", userImage = "Tingkir")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty())
    }

    @Test
    fun `Error getUserNameAndPictureData missing profile picture`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderViewModel(userName = "Joko", userImage = "")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isEmpty())
    }

    @Test
    fun `Error getUserNameAndPictureData missing all`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderViewModel(userName = "", userImage = "")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderViewModel} as AccountHeaderViewModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isEmpty()
                && accountHeaderViewModel.userImage.isEmpty())
    }

    @Test
    fun `Error get account admin info`() {
        val position = 0
        val refreshShopBasicDataUseCase = mockk<RefreshShopBasicDataUseCase>()
        val gqlRepository = mockk<GraphqlRepository>()
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository))
        val userSession = mockk<UserSessionInterface>(relaxed = true)
        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } throws MessageErrorException("")
        coEvery {
            userSession.hasShop()
        } returns true
        coEvery {
            userSession.isShopOwner
        } returns false
        coEvery {
            userSession.isLocationAdmin
        } returns false
        coEvery {
            userSession.isLoggedIn
        } returns true

        viewModel = createViewModel(
                accountAdminInfoUseCase = accountAdminInfoUseCase,
                userSession = userSession)
        viewModel.getMainNavData(false)

        val mainNavDataModel = viewModel.mainNavLiveData.value
        (mainNavDataModel?.dataList?.getOrNull(position) as? AccountHeaderViewModel).let { actualResult ->
            Assert.assertFalse(actualResult?.isProfileLoading == true)
        }
    }

}