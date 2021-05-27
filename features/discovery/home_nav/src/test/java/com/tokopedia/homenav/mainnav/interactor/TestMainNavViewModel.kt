package com.tokopedia.homenav.mainnav.interactor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
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
    val rule = CoroutineTestRule()

    @ApplicationContext
    lateinit var context: Context

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
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
                .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel

        Assert.assertNotNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from homepage then do notshow back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
                .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNull(backToHomeMenu)
    }

    //user menu section
    @Test
    fun `test when viewmodel created and user has no shop then viewmodel create at least 3 user menu`() {
        val defaultUserMenuCount = 3

        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
                .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?.filter {
            (it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.USER_MENU)
        }

        Assert.assertEquals(defaultUserMenuCount, visitableList!!.size)
    }

    //user menu section
    @Test
    fun `test when logged in user get complain notification then viewmodel update complain visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 800

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
                .answers {(HomeNavTitleDataModel(identifier = firstArg()))}
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountComplain = mockUnreadCount) }

        viewModel = createViewModel(
                clientMenuGenerator = clientMenuGenerator,
                getNavNotification = getNavNotification
        )
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    //user menu section
    @Test
    fun `test when logged in user get inbox ticket notification then viewmodel update tokopedia care visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 900

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
                .answers {(HomeNavTitleDataModel(identifier = firstArg()))}
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountInboxTicket = mockUnreadCount) }

        viewModel = createViewModel(
                clientMenuGenerator = clientMenuGenerator,
                getNavNotification = getNavNotification
        )
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuDataModel

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
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNull(transactionDataModel)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and logged in user only have ongoing order then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn() } returns true
        every { userSession.hasShop() } returns true

        coEvery { userSession.isShopOwner } returns true
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())

        viewModel = createViewModel(
                getUohOrdersNavUseCase = getUohOrdersNavUseCase,
                getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
                userSession = userSession
        )
        viewModel.getMainNavData(true)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNotNull(transactionDataModel)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and loggedin user only have payment transaction then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { userSession.isShopOwner } returns true
        every { userSession.isLoggedIn() } returns true
        every { userSession.hasShop() } returns true

        viewModel = createViewModel(
                getUohOrdersNavUseCase = getUohOrdersNavUseCase,
                getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
                userSession = userSession
        )
        viewModel.getMainNavData(true)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNotNull(transactionDataModel)
    }

    //user menu section
    @Test
    fun `test when data loaded complete then check account header menu section is available`() {
        viewModel = createViewModel(
        )
        viewModel.getMainNavData(true)

        val headerModelPosition = viewModel.findHeaderModelPosition()

        Assert.assertNotNull(headerModelPosition)
    }

    @Test
    fun `test when first load init viewmodel data then check data not null`() {
        viewModel = createViewModel()
        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        Assert.assertNotNull(visitableList)
    }

    @Test
    fun `test when success refresh uoh and transaction then check result not null`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavOrderUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn() } returns true
        coEvery { getNavOrderUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        coEvery { getPaymentUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())
        viewModel = createViewModel(
                getPaymentOrdersNavUseCase = getPaymentUseCase,
                getUohOrdersNavUseCase = getNavOrderUseCase)
        viewModel.refreshTransactionListData()


        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        }?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty());
        Assert.assertNotNull(transactionDataModel)
    }

    @Test
    fun `test when success refresh data after login then check data not null`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
                userName = "Joko",
                userImage = "Tingkir",
                ovoSaldo = "Rp 100",
                ovoPoint = "Rp 100",
                badge = "kucing",
                shopName = "binatang",
                shopId = "1234")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)
        viewModel.reloadMainNavAfterLogin()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty()
                && accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.badge.isNotEmpty()
                && accountHeaderViewModel.shopId.isNotEmpty()
                && accountHeaderViewModel.shopName.isNotEmpty()
                && accountHeaderViewModel.userName == "Joko"
                && accountHeaderViewModel.userImage == "Tingkir"
                && accountHeaderViewModel.ovoSaldo == "Rp 100"
                && accountHeaderViewModel.ovoPoint == "Rp 100"
                && accountHeaderViewModel.badge == "kucing"
                && accountHeaderViewModel.shopId == "1234"
                && accountHeaderViewModel.shopName == "binatang")
    }

    @Test
    fun `test when success refresh shop data then check shop name and id changed`() {
        val newShopName = "binatang kucing"
        val newShopId = "123123"
        val isLocationAdmin: Boolean = true
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val shopInfoRefreshData = mockk<GetShopInfoUseCase>()
        val expectedAdminRoleText = "Joko Tingkir"
        val adminDataResponse =
                AdminDataResponse(
                        data = AdminData(
                                adminTypeText = expectedAdminRoleText,
                                detail = AdminDetailInformation(
                                        roleType = AdminRoleType(
                                                isLocationAdmin = isLocationAdmin
                                        )
                                ),
                                status = "1"
                        )
                )
        val accountInfoPair = Pair(adminDataResponse, null)
        val refreshShopBasicDataUseCase = mockk<RefreshShopBasicDataUseCase>()
        val gqlRepository = mockk<GraphqlRepository>()
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository))

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
                userName = "Joko",
                userImage = "Tingkir",
                ovoSaldo = "Rp 100",
                ovoPoint = "Rp 100",
                badge = "kucing",
                shopName = "binatang",
                hasShop = true,
                shopId = "1234")

        coEvery {
            shopInfoRefreshData.executeOnBackground()
        } returns Success(ShopData(
                ShopData.ShopInfoPojo(
                        ShopData.ShopInfoPojo.Info(
                                shopName = newShopName,
                                shopId = newShopId
                        )
                ),
                ShopData.NotificationPojo()))
        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } returns accountInfoPair
        viewModel = createViewModel(
                getProfileDataUseCase = getProfileDataUseCase,
                getShopInfoUseCase = shopInfoRefreshData,
                accountAdminInfoUseCase = accountAdminInfoUseCase)
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.shopId.isNotEmpty()
                && accountHeaderViewModel.shopId == newShopId
                && accountHeaderViewModel.shopName.isNotEmpty()
                && accountHeaderViewModel.shopName == newShopName)
    }

    @Test
    fun `Success getProfileFullData`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
                userName = "Joko",
                userImage = "Tingkir",
                ovoSaldo = "Rp 100",
                ovoPoint = "Rp 100",
                badge = "kucing",
                shopName = "binatang",
                shopId = "1234")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty()
                && accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.badge.isNotEmpty()
                && accountHeaderViewModel.shopId.isNotEmpty()
                && accountHeaderViewModel.shopName.isNotEmpty()
                && accountHeaderViewModel.userName == "Joko"
                && accountHeaderViewModel.userImage == "Tingkir"
                && accountHeaderViewModel.ovoSaldo == "Rp 100"
                && accountHeaderViewModel.ovoPoint == "Rp 100"
                && accountHeaderViewModel.badge == "kucing"
                && accountHeaderViewModel.shopId == "1234"
                && accountHeaderViewModel.shopName == "binatang")
    }

    @Test
    fun `test when success refresh profile after login then data not null and have exact result`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
                userName = "Joko",
                userImage = "Tingkir",
                ovoSaldo = "Rp 100",
                ovoPoint = "Rp 100",
                badge = "kucing",
                shopName = "binatang",
                shopId = "1234")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)
        viewModel.refreshProfileData()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.userName.isNotEmpty()
                && accountHeaderViewModel.userImage.isNotEmpty()
                && accountHeaderViewModel.ovoSaldo.isNotEmpty()
                && accountHeaderViewModel.ovoPoint.isNotEmpty()
                && accountHeaderViewModel.badge.isNotEmpty()
                && accountHeaderViewModel.shopId.isNotEmpty()
                && accountHeaderViewModel.shopName.isNotEmpty()
                && accountHeaderViewModel.userName == "Joko"
                && accountHeaderViewModel.userImage == "Tingkir"
                && accountHeaderViewModel.ovoSaldo == "Rp 100"
                && accountHeaderViewModel.ovoPoint == "Rp 100"
                && accountHeaderViewModel.badge == "kucing"
                && accountHeaderViewModel.shopId == "1234"
                && accountHeaderViewModel.shopName == "binatang")
    }

    @Test
    fun `Success getUserNameAndPictureData`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(userName = "Joko", userImage = "Tingkir")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
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
                                ),
                                status = "1"
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
        (mainNavDataModel?.dataList?.getOrNull(position) as? AccountHeaderDataModel).let { actualResult ->
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
        } returns AccountHeaderDataModel(userName = "", userImage = "Tingkir")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
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
        } returns AccountHeaderDataModel(userName = "Joko", userImage = "")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
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
        } returns AccountHeaderDataModel(userName = "", userImage = "")
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel} as AccountHeaderDataModel
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
        (mainNavDataModel?.dataList?.getOrNull(position) as? AccountHeaderDataModel).let { actualResult ->
            Assert.assertFalse(actualResult?.isProfileLoading == true)
        }
    }

}
