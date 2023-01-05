package com.tokopedia.homenav.mainnav.interactor

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.*
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.*
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ErrorStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.domain.usecase.RefreshShopBasicDataUseCase
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionShopData
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusResponseDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
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

    private lateinit var viewModel: MainNavViewModel
    private val shopId = 1224
    private val mockListAllCategory = listOf(HomeNavMenuDataModel())
    private val MOCK_IS_ME_PAGE_ROLLENCE_DISABLE = false
    private val MOCK_IS_ME_PAGE_ROLLENCE_ENABLE = true

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test when nav page launched from page others with disabled me page rollence than homepage then show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = "Other page"
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
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
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from uoh page then not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_UOH
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from wishlist page then do not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from wishlist collection page then do not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_COLLECTION
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers {
                HomeNavMenuDataModel(
                    id = firstArg(),
                    notifCount = secondArg(),
                    sectionId = thirdArg()
                )
            }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu =
            visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME }

        Assert.assertNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched and disable me page rollence from homepage then do show back to home icon with default pagesource`() {
        val defaultPageSource = "Default"
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setPageSource()
        Assert.assertEquals(defaultPageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNotNull(backToHomeMenu)
    }

    // user menu section
    @Test
    fun `test when viewmodel created and user has no shop with disable me page rollence then viewmodel create at least 3 user menu`() {
        val defaultUserMenuCount = 3

        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?.filter {
            (it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.USER_MENU)
        }

        Assert.assertEquals(defaultUserMenuCount, visitableList!!.size)
    }

    // user menu section
    @Test
    fun `test when logged in user get complain notification with disable me page rollence then viewmodel update complain visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 800

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountComplain = mockUnreadCount) }

        viewModel = createViewModel(
            clientMenuGenerator = clientMenuGenerator,
            getNavNotification = getNavNotification
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    // test user profile cache
    @Test
    fun `test when set profile from cache with disabled me page rollence`() {
        val mainNavProfileCacheMock = mockk<MainNavProfileCache>()
        val profileName = "Joko"
        val profilePicUrl = "http"
        val memberStatusIconUrl = "kucing"
        every { mainNavProfileCacheMock.profileName } returns profileName
        every { mainNavProfileCacheMock.profilePicUrl } returns profilePicUrl
        every { mainNavProfileCacheMock.memberStatusIconUrl } returns memberStatusIconUrl

        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setProfileCache(mainNavProfileCacheMock)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()

        val accountHeaderDataModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderDataModel)
        Assert.assertTrue(
            accountHeaderDataModel.profileDataModel.userName == mainNavProfileCacheMock.profileName &&
                accountHeaderDataModel.profileDataModel.userImage == mainNavProfileCacheMock.profilePicUrl &&
                accountHeaderDataModel.profileMembershipDataModel.badge == mainNavProfileCacheMock.memberStatusIconUrl
        )
        Assert.assertNull(viewModel.profileDataLiveData.value)
    }

    // user menu section
    @Test
    fun `test when logged in user get inbox ticket notification with disable me page rollence then viewmodel update tokopedia care visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockUnreadCount = 900

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountInboxTicket = mockUnreadCount) }

        viewModel = createViewModel(
            clientMenuGenerator = clientMenuGenerator,
            getNavNotification = getNavNotification
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    // transaction section
    @Test
    fun `test when viewmodel created and user does not ongoing order and payment transaction with disable me page rollence then only create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        } ?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty())
        Assert.assertNull(transactionDataModel)
    }

    // transaction section
    @Test
    fun `test when viewmodel created and logged in user only have ongoing order with disable me page rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn() } returns true
        every { userSession.hasShop() } returns true

        coEvery { userSession.isShopOwner } returns true
        coEvery { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers { }
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        } ?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty())
        Assert.assertNotNull(transactionDataModel)
    }

    // transaction section
    @Test
    fun `test when viewmodel created and loggedin user only have payment transaction with disable me page rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers { }
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
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        } ?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty())
        Assert.assertNotNull(transactionDataModel)
    }

    @Test
    fun `test when first load init viewmodel data then check data not null`() {
        viewModel = createViewModel()
        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        Assert.assertNotNull(visitableList)
    }

    @Test
    fun `test when user not login first load init viewmodel with disabled me page rollence then menu not empty`() {
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns false
        viewModel = createViewModel(userSession = userSession)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        val visitableList = viewModel.mainNavLiveData.value?.dataList
        Assert.assertNotEquals(0, visitableList?.size)
    }

    @Test
    fun `test when success refresh uoh and transaction with disabled me page then check result not null`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavOrderUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn() } returns true
        every { getNavOrderUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers { }
        coEvery { getNavOrderUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        coEvery { getPaymentUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentUseCase,
            getUohOrdersNavUseCase = getNavOrderUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.refreshTransactionListData()

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        } ?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty())
        Assert.assertNotNull(transactionDataModel)
    }

    @Test
    fun `test when success refresh data after login with disabled me page rollence then check data not null`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val accountHeaderDataModel = AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                shopId = "1234"
            )
        )
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns accountHeaderDataModel
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.reloadMainNavAfterLogin()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isNotEmpty() &&
                accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userName == "Joko" &&
                accountHeaderViewModel.profileDataModel.userImage == "Tingkir" &&
                accountHeaderViewModel.profileMembershipDataModel.badge == "kucing" &&
                accountHeaderViewModel.profileSellerDataModel.shopId == "1234" &&
                accountHeaderViewModel.profileSellerDataModel.shopName == "binatang"
        )
        Assert.assertEquals(viewModel.profileDataLiveData.value, accountHeaderDataModel)
    }

    @Test
    fun `test when success refresh shop data with disable me page rollence then check shop name and id changed`() {
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
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            )
        )

        coEvery {
            shopInfoRefreshData.executeOnBackground()
        } returns Success(
            ShopData(
                ShopData.ShopInfoPojo(
                    ShopData.ShopInfoPojo.Info(
                        shopName = newShopName,
                        shopId = newShopId
                    )
                ),
                ShopData.NotificationPojo()
            )
        )
        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } returns accountInfoPair
        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getShopInfoUseCase = shopInfoRefreshData,
            accountAdminInfoUseCase = accountAdminInfoUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopId == newShopId &&
                accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopName == newShopName
        )
    }

    @Test
    fun `given failed affiliate with disable me page rollence data when refresh data affiliate then affiliate data not error in header`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val getAffiliateUserUseCase = mockk<GetAffiliateUserUseCase>()
        val affiliateUserDetailData = AffiliateUserDetailData()
        val mockAffiliateSuccess = Success(affiliateUserDetailData)
        coEvery {
            getAffiliateUserUseCase.executeOnBackground()
        } returns mockAffiliateSuccess

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            ),
            profileAffiliateDataModel = ProfileAffiliateDataModel(isGetAffiliateError = true)
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getAffiliateUserUseCase = getAffiliateUserUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(!accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given failed affiliate data when refresh data affiliate with network error and disable rollence me page then affiliate data still error in header`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val getAffiliateUserUseCase = mockk<GetAffiliateUserUseCase>()
        coEvery {
            getAffiliateUserUseCase.executeOnBackground()
        } throws NetworkErrorException()

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            ),
            profileAffiliateDataModel = ProfileAffiliateDataModel(isGetAffiliateError = true)
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getAffiliateUserUseCase = getAffiliateUserUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given failed affiliate data when refresh data affiliate with failed data and disable me page rollence then affiliate data still error in header`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val getAffiliateUserUseCase = mockk<GetAffiliateUserUseCase>()
        coEvery {
            getAffiliateUserUseCase.executeOnBackground()
        } returns Fail(MessageErrorException())

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            ),
            profileAffiliateDataModel = ProfileAffiliateDataModel(isGetAffiliateError = true)
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getAffiliateUserUseCase = getAffiliateUserUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given disabled me page rollence then test Success getProfileFullData`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                shopId = "1234"
            )
        )
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isNotEmpty() &&
                accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userName == "Joko" &&
                accountHeaderViewModel.profileDataModel.userImage == "Tingkir" &&
                accountHeaderViewModel.profileMembershipDataModel.badge == "kucing" &&
                accountHeaderViewModel.profileSellerDataModel.shopId == "1234" &&
                accountHeaderViewModel.profileSellerDataModel.shopName == "binatang"
        )
    }

    @Test
    fun `test when success refresh profile after login with disabled me page rollence then data not null and have exact result`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val accountHeaderDataModel = AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                shopId = "1234"
            )
        )
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns accountHeaderDataModel
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshProfileData()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isNotEmpty() &&
                accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty() &&
                accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userName == "Joko" &&
                accountHeaderViewModel.profileDataModel.userImage == "Tingkir" &&
                accountHeaderViewModel.profileMembershipDataModel.badge == "kucing" &&
                accountHeaderViewModel.profileSellerDataModel.shopId == "1234" &&
                accountHeaderViewModel.profileSellerDataModel.shopName == "binatang"
        )
        Assert.assertEquals(viewModel.profileDataLiveData.value, accountHeaderDataModel)
    }

    @Test
    fun `given disabled me page rollence then test Success getUserNameAndPictureData`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isNotEmpty()
        )
    }

    @Test
    fun `given disable me page rollence then Error getUserNameAndPictureData missing name`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isNotEmpty()
        )
    }

    @Test
    fun `given disabled me page rollence then test Error getUserNameAndPictureData missing profile picture`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isNotEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isEmpty()
        )
    }

    @Test
    fun `given disable rollence me page Error getUserNameAndPictureData missing all`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(
            accountHeaderViewModel.profileDataModel.userName.isEmpty() &&
                accountHeaderViewModel.profileDataModel.userImage.isEmpty()
        )
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
            userSession = userSession
        )
        viewModel.getMainNavData(false)

        val mainNavDataModel = viewModel.mainNavLiveData.value
        (mainNavDataModel?.dataList?.getOrNull(position) as? AccountHeaderDataModel).let { actualResult ->
            Assert.assertFalse(actualResult?.profileDataModel?.isProfileLoading == true)
        }
    }

    @Test
    fun `test when show coachmark complain with correct position and disabled me page rollence for logged in user first time`() {
        val defaultPositionComplaintNotFound = -1

        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns true
        every { userSession.hasShop() } returns true
        every { userSession.isShopOwner } returns true
        viewModel = createViewModel(userSession = userSession)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        viewModel.getMainNavData(true)
        val complainPosition = viewModel.findComplainModelPosition()
        Assert.assertNotEquals(defaultPositionComplaintNotFound, complainPosition)
    }

    @Test
    fun `test when show coachmark all transaction with correct position and disable me page rollence`() {
        val indexDefaultAllTransaction = 1
        val pageSource = "Other page"

        viewModel = createViewModel()
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setPageSource(pageSource)

        val allTransactionPosition = viewModel.findAllTransactionModelPosition()
        Assert.assertNotEquals(indexDefaultAllTransaction, allTransactionPosition)
    }

    @Test
    fun `test when coachmark all transaction will not show for specific page`() {
        val indexDefaultAllTransaction = 1
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME

        viewModel = createViewModel()
        viewModel.setPageSource(pageSource)

        val allTransactionPosition = viewModel.findAllTransactionModelPosition()
        Assert.assertEquals(indexDefaultAllTransaction, allTransactionPosition)
    }

    @Test
    fun `test show error bu list with enable me page rollence then refresh bu list data will success delete error bu list`() {
        val getBuListUseCase = mockk<GetCategoryGroupUseCase>()
        // failed getBuListUseCase.executeOnBackground() will show ErrorStateBuViewHolder
        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")
        every {
            getBuListUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }
        every {
            getBuListUseCase.setStrategyCache()
        } answers { }
        every {
            getBuListUseCase.setStrategyCloudThenCache()
        } answers { }
        viewModel = createViewModel(getBuListUseCase = getBuListUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModel = dataList.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val errorStateBuDataModel = homeNavExpandableDataModel.menus.find { it is ErrorStateBuDataModel } as ErrorStateBuDataModel
        Assert.assertNotNull(errorStateBuDataModel) // error state bu data model existed
        Assert.assertTrue(viewModel.networkProcessLiveData.value == false)

        coEvery {
            getBuListUseCase.executeOnBackground()
        }.answers { listOf() }

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModelRefreshed = dataListRefreshed.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val errorStateBuDataModelRefreshed = homeNavExpandableDataModelRefreshed.menus.find { it is ErrorStateBuDataModel }
        Assert.assertNull(errorStateBuDataModelRefreshed)
        Assert.assertTrue(viewModel.networkProcessLiveData.value == true)
    }

    @Test
    fun `given default data from cache and enable me page rollence when load all categories with exception then success get data from cache`() {
        val getCategoryGroupUseCase = mockk<GetCategoryGroupUseCase>()
        coEvery {
            getCategoryGroupUseCase.executeOnBackground()
        } returns mockListAllCategory

        coEvery {
            getCategoryGroupUseCase.setStrategyCloudThenCache()
        } throws MessageErrorException("")

        every {
            getCategoryGroupUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }

        every {
            getCategoryGroupUseCase.setStrategyCache()
        } answers { }

        viewModel = createViewModel(getBuListUseCase = getCategoryGroupUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModel = dataList.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val homeNavMenuDataModel = homeNavExpandableDataModel.menus.find { it is HomeNavMenuDataModel } as HomeNavMenuDataModel
        Assert.assertNotNull(homeNavMenuDataModel)
    }

    @Test
    fun `given using rollence variant when user not logged in should add empty data for each section`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns false

        viewModel = createViewModel(
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)

        Assert.assertNotNull(viewModel.mainNavLiveData.value)
        val wishlistModel = viewModel.mainNavLiveData.value?.dataList?.find { it is WishlistDataModel } as? WishlistDataModel
        Assert.assertTrue(wishlistModel != null && wishlistModel.wishlist.isEmpty())
        val favoriteShopModel = viewModel.mainNavLiveData.value?.dataList?.find { it is FavoriteShopListDataModel } as? FavoriteShopListDataModel
        Assert.assertTrue(favoriteShopModel != null && favoriteShopModel.favoriteShops.isEmpty())
        val transactionListModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel?
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.orderList.isEmpty())
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.paymentList.isEmpty())
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.reviewList.isEmpty())
    }

    @Test
    fun `given success and not empty wishlist should add wishlist model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns Pair(listOf(NavWishlistModel(), NavWishlistModel()), false)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel } == true)
        assert(viewModel.allProcessFinished.value?.peekContent() == true)
    }

    @Test
    fun `given success and not empty when favorite shop should add favorite shop model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(
            listOf(
                NavFavoriteShopModel(),
                NavFavoriteShopModel()
            ),
            false
        )

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel } == true)
        assert(viewModel.allProcessFinished.value?.peekContent() == true)
    }

    @Test
    fun `given empty data when get wishlist should add empty wishlist`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns Pair(listOf(), false)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val wishlistModel = viewModel.mainNavLiveData.value?.dataList?.find { it is WishlistDataModel } as WishlistDataModel?
        assert(wishlistModel != null && wishlistModel.wishlist.isEmpty())
    }

    @Test
    fun `given empty data when get favorite shop should add empty favorite shops`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(), false)

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val favoriteShopModel = viewModel.mainNavLiveData.value?.dataList?.find { it is FavoriteShopListDataModel } as FavoriteShopListDataModel?
        assert(favoriteShopModel != null && favoriteShopModel.favoriteShops.isEmpty())
    }

    @Test
    fun `given error when get wishlist should add error state`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)
    }

    @Test
    fun `given error when get favorite shop should add error state`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)
    }

    @Test
    fun `test when refresh favorite shop should update existing list`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        val favoriteShop1 = NavFavoriteShopModel(
            id = "1",
            name = "Toko A",
            location = "Tangerang"
        )
        val favoriteShop2 = NavFavoriteShopModel(
            id = "2",
            name = "Toko B",
            location = "Jakarta"
        )

        // Initial favorite shop
        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(favoriteShop1), true)

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is FavoriteShopListDataModel &&
                    it.favoriteShops.contains(favoriteShop1)
            } == true
        )

        // Refresh data
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(favoriteShop2), true)
        viewModel.refreshFavoriteShopData()

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is FavoriteShopListDataModel &&
                    it.favoriteShops.contains(favoriteShop2)
            } == true
        )
    }

    @Test
    fun `test when refresh wishlist should update existing list`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist1 = NavWishlistModel(
            productId = "123",
            productName = "Item 1",
            wishlistId = "111"
        )
        val wishlist2 = NavWishlistModel(
            productId = "234",
            productName = "Item 2",
            wishlistId = "112"
        )

        // Initial wishlist data
        every { userSession.isLoggedIn } returns true
        coEvery { wishlistNavUseCase.executeOnBackground() } returns Pair(listOf(wishlist1), true)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.wishlist.contains(wishlist1)
            } == true
        )

        // Refresh data
        coEvery { wishlistNavUseCase.executeOnBackground() } returns Pair(listOf(wishlist2), true)
        viewModel.refreshWishlistData()

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.wishlist.contains(wishlist2)
            } == true
        )
    }

    @Test
    fun `test show error wishlist then success after refresh wishlist should update error state to show wishlist`() {
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist = NavWishlistModel(
            productId = "123",
            productName = "Item 1",
            wishlistId = "111"
        )

        coEvery { wishlistNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getWishlistNavUseCase = wishlistNavUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns Pair(listOf(wishlist), true)

        viewModel.refreshWishlistData()

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.wishlist.contains(wishlist)
            } == true
        )
    }

    @Test
    fun `test show error wishlist then success after reload page should update error state to show wishlist`() {
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist = NavWishlistModel(
            productId = "123",
            productName = "Item 1",
            wishlistId = "111"
        )

        coEvery { wishlistNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getWishlistNavUseCase = wishlistNavUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns Pair(listOf(wishlist), true)

        viewModel.getMainNavData(true)

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.wishlist.contains(wishlist)
            } == true
        )
    }

    @Test
    fun `test show error favorite shop then success after refresh favorite shop should update error state to show favorite shop`() {
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        val favoriteShop = NavFavoriteShopModel(
            id = "1",
            name = "Toko A",
            location = "Tangerang"
        )

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getFavoriteShopsNavUseCase = favoriteShopsNavUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(favoriteShop), true)

        viewModel.refreshFavoriteShopData()

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is FavoriteShopListDataModel &&
                    it.favoriteShops.contains(favoriteShop)
            } == true
        )
    }

    @Test
    fun `test show error favorite shop then success after reload page should update error state to show favorite shop`() {
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        val favoriteShop = NavFavoriteShopModel(
            id = "1",
            name = "Toko A",
            location = "Tangerang"
        )

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getFavoriteShopsNavUseCase = favoriteShopsNavUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(favoriteShop), true)

        viewModel.getMainNavData(true)

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is FavoriteShopListDataModel &&
                    it.favoriteShops.contains(favoriteShop)
            } == true
        )
    }

    @Test
    fun `test success show bu list with disabled me page rollence then error after refresh data should update with cache data`() {
        val getBuListUseCase = mockk<GetCategoryGroupUseCase>()
        val successResult = HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON)

        coEvery {
            getBuListUseCase.executeOnBackground()
        }.answers { listOf(successResult) }
        every {
            getBuListUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }
        every {
            getBuListUseCase.setStrategyCache()
        } answers { }
        every {
            getBuListUseCase.setStrategyCloudThenCache()
        } answers { }
        viewModel = createViewModel(getBuListUseCase = getBuListUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.contains(successResult))
        Assert.assertTrue(viewModel.networkProcessLiveData.value == true)

        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataListRefreshed.contains(successResult))
        Assert.assertFalse(dataListRefreshed.any { it is ErrorStateBuDataModel }) // error state bu data model existed
    }

    @Test
    fun `given enable me page rollence when refresh category then show data from cache`() {
        val getBuListUseCase = mockk<GetCategoryGroupUseCase>()
        val successResult = HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON)

        // failed getBuListUseCase.executeOnBackground() will show ErrorStateBuViewHolder
        coEvery {
            getBuListUseCase.executeOnBackground()
        }.answers { listOf(successResult) }
        every {
            getBuListUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }
        every {
            getBuListUseCase.setStrategyCache()
        } answers { }
        every {
            getBuListUseCase.setStrategyCloudThenCache()
        } answers { }
        viewModel = createViewModel(getBuListUseCase = getBuListUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList?.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        Assert.assertTrue(dataList.menus.contains(successResult))

        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList?.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        Assert.assertTrue(dataListRefreshed.menus.contains(successResult))
        Assert.assertFalse(dataListRefreshed.menus.any { it is ErrorStateBuDataModel }) // error state bu data model existed
    }

    @Test
    fun `given enable me page rollence when refresh category with failed data from cache then show retry button`() {
        val getBuListUseCase = mockk<GetCategoryGroupUseCase>()
        val successResult = HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON)

        // failed getBuListUseCase.executeOnBackground() will show ErrorStateBuViewHolder
        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")
        every {
            getBuListUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }
        every {
            getBuListUseCase.setStrategyCache()
        } answers { }
        every {
            getBuListUseCase.setStrategyCloudThenCache()
        } answers { }
        viewModel = createViewModel(getBuListUseCase = getBuListUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList?.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        Assert.assertFalse(dataList.menus.contains(successResult))

        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList?.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        Assert.assertFalse(dataListRefreshed.menus.contains(successResult))
        Assert.assertTrue(dataListRefreshed.menus.any { it is ErrorStateBuDataModel }) // error state bu data model existed
    }

    @Test
    fun `given disable me page rollence when refresh category with failed data from cache then show retry button`() {
        val getBuListUseCase = mockk<GetCategoryGroupUseCase>()
        val successResult = HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON)

        // failed getBuListUseCase.executeOnBackground() will show ErrorStateBuViewHolder
        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")
        every {
            getBuListUseCase.createParams(GetCategoryGroupUseCase.GLOBAL_MENU)
        } answers { }
        every {
            getBuListUseCase.setStrategyCache()
        } answers { }
        every {
            getBuListUseCase.setStrategyCloudThenCache()
        } answers { }
        viewModel = createViewModel(getBuListUseCase = getBuListUseCase)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertFalse(dataList.contains(successResult))

        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertFalse(dataListRefreshed.contains(successResult))
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateBuDataModel }) // error state bu data model existed
    }

    @Test
    fun `given 3 payment list and 3 reviews with enable me page rollence then get order history then show only 3 data payments and 2 data reviews`() {
        val mockList3PaymentOrder = listOf(NavPaymentOrder(), NavPaymentOrder(), NavPaymentOrder())
        val mockList3ReviewProduct = listOf(NavReviewOrder(), NavReviewOrder(), NavReviewOrder())
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val getReviewProductUseCase = mockk<GetReviewProductUseCase>()
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } returns mockList3PaymentOrder
        coEvery {
            getReviewProductUseCase.executeOnBackground()
        } returns mockList3ReviewProduct
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            getReviewProductUseCase = getReviewProductUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as TransactionListItemDataModel

        Assert.assertEquals(3, transactionDataModel.orderListModel.paymentList.size)
        Assert.assertEquals(2, transactionDataModel.orderListModel.reviewList.size)
    }

    @Test
    fun `given me page rollence disable when refresh order transaction then show failed get order transaction`() {
        val userSession = mockk<UserSessionInterface>()
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        every { userSession.isLoggedIn() } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.any { it is ErrorStateOngoingTransactionModel })

        viewModel.refreshTransactionListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateOngoingTransactionModel })
    }

    @Test
    fun `given me page rollence enable when refresh order transaction then show failed get order transaction`() {
        val userSession = mockk<UserSessionInterface>()
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        every { userSession.isLoggedIn() } returns true
        every { userSession.isShopOwner } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.any { it is ErrorStateOngoingTransactionModel })

        viewModel.refreshTransactionListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateOngoingTransactionModel })
    }

    @Test
    fun `given 1 payment list with enable me page rollence then get order history then show only 1 payment full width`() {
        val mockList1PaymentOrder = listOf(NavPaymentOrder())
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } returns mockList1PaymentOrder
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as TransactionListItemDataModel

        Assert.assertEquals(1, transactionDataModel.orderListModel.paymentList.size)
        Assert.assertTrue(transactionDataModel.orderListModel.paymentList[0].fullWidth)
    }

    @Test
    fun `given 1 order product list with enable me page rollence then get order history then show only 1 order product full width`() {
        val mockList1OrderProduct = listOf(NavProductOrder())
        val getUohOrderNavUseCase = mockk<GetUohOrdersNavUseCase>()
        every {
            getUohOrderNavUseCase.setIsMePageUsingRollenceVariant(
                MOCK_IS_ME_PAGE_ROLLENCE_ENABLE
            )
        }.answers { }
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns true
        coEvery {
            getUohOrderNavUseCase.executeOnBackground()
        } returns mockList1OrderProduct
        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrderNavUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as TransactionListItemDataModel

        Assert.assertEquals(1, transactionDataModel.orderListModel.orderList.size)
        Assert.assertTrue(transactionDataModel.orderListModel.orderList[0].fullWidth)
    }

    @Test
    fun `given 1 review product list with enable me page rollence then get order history then show only 1 review product full width`() {
        val mockList1ReviewOrder = listOf(NavReviewOrder())
        val getReviewProductUseCase = mockk<GetReviewProductUseCase>()
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns true
        coEvery {
            getReviewProductUseCase.executeOnBackground()
        } returns mockList1ReviewOrder
        viewModel = createViewModel(
            getReviewProductUseCase = getReviewProductUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as TransactionListItemDataModel

        Assert.assertEquals(1, transactionDataModel.orderListModel.reviewList.size)
        Assert.assertTrue(transactionDataModel.orderListModel.reviewList[0].fullWidth)
    }

    @Test
    fun `given 1 wishlist with enable me page rollence then get wishlist should show only 1 wishlist full width`() {
        val mockList1Wishlist = Pair(listOf(NavWishlistModel()), true)
        val getWishlistNavUseCase = mockk<GetWishlistNavUseCase>()
        coEvery {
            getWishlistNavUseCase.executeOnBackground()
        } returns mockList1Wishlist
        viewModel = createViewModel(
            getWishlistNavUseCase = getWishlistNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val wishlistDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is WishlistDataModel
        } as WishlistDataModel

        Assert.assertEquals(1, wishlistDataModel.wishlist.size)
        Assert.assertTrue(wishlistDataModel.wishlist[0].fullWidth)
    }

    @Test
    fun `given 1 favorite shop with enable me page rollence then get favorite shop should show only 1 favorite shop full width`() {
        val mockList1FavShop = Pair(listOf(NavFavoriteShopModel()), true)
        val getFavoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()
        coEvery {
            getFavoriteShopsNavUseCase.executeOnBackground()
        } returns mockList1FavShop
        viewModel = createViewModel(
            getFavoriteShopsNavUseCase = getFavoriteShopsNavUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)
        val favoriteShopListDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is FavoriteShopListDataModel
        } as FavoriteShopListDataModel

        Assert.assertEquals(1, favoriteShopListDataModel.favoriteShops.size)
        Assert.assertTrue(favoriteShopListDataModel.favoriteShops[0].fullWidth)
    }

    @Test
    fun `given success when refresh tokopedia plus data then tokopedia plus should be in account header`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val getTokopediaPlusUseCase = mockk<TokopediaPlusUseCase>()
        val tokopediaPlusResponseDataModel = TokopediaPlusResponseDataModel()
        coEvery {
            getTokopediaPlusUseCase.invoke(any())
        } returns tokopediaPlusResponseDataModel

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            ),
            tokopediaPlusDataModel = TokopediaPlusDataModel(
                tokopediaPlusError = MessageErrorException()
            )
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getTokopediaPlusUseCase = getTokopediaPlusUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableListBefore = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderBefore = visitableListBefore.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertNotNull(accountHeaderBefore.tokopediaPlusDataModel.tokopediaPlusError)

        viewModel.refreshTokopediaPlusData()
        val visitableListAfter = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderAfter = visitableListAfter.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertNull(accountHeaderAfter.tokopediaPlusDataModel.tokopediaPlusError)
        Assert.assertNotNull(accountHeaderAfter.tokopediaPlusDataModel.tokopediaPlusParam)
    }

    @Test
    fun `given failed when refresh tokopedia plus data then tokopedia plus error should be in account header`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val getTokopediaPlusUseCase = mockk<TokopediaPlusUseCase>()
        val error = MessageErrorException("error")
        coEvery {
            getTokopediaPlusUseCase.invoke(any())
        } throws error

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = ProfileSellerDataModel(
                shopName = "binatang",
                hasShop = true,
                shopId = "1234"
            ),
            tokopediaPlusDataModel = TokopediaPlusDataModel(
                tokopediaPlusError = MessageErrorException()
            )
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getTokopediaPlusUseCase = getTokopediaPlusUseCase
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableListBefore = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderBefore = visitableListBefore.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertNotNull(accountHeaderBefore.tokopediaPlusDataModel.tokopediaPlusError)

        viewModel.refreshTokopediaPlusData()
        val visitableListAfter = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderAfter = visitableListAfter.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertNotNull(accountHeaderAfter.tokopediaPlusDataModel.tokopediaPlusError)
        Assert.assertTrue(accountHeaderAfter.tokopediaPlusDataModel.tokopediaPlusError?.message == error.message)
    }

    @Test
    fun `given thrown exception when refresh shop data then isGetShopError should be true`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()

        val profileSeller = ProfileSellerDataModel(
            shopName = "binatang",
            hasShop = true,
            shopId = "1234"
        )

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = profileSeller
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase
        )

        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        every {
            viewModel invoke "getTotalOrderCount" withArguments listOf(any<ShopData.NotificationPojo>())
        } throws MessageErrorException()

        viewModel.refreshUserShopData()

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.shopId == profileSeller.shopId)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.shopName == profileSeller.shopName)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.isGetShopError)
    }

    @Test
    fun `given failed when refresh shop data then isGetShopError should be true`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val shopInfoRefreshData = mockk<GetShopInfoUseCase>()

        val profileSeller = ProfileSellerDataModel(
            shopName = "binatang",
            hasShop = true,
            shopId = "1234"
        )

        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(
            profileDataModel = ProfileDataModel(
                userName = "Joko",
                userImage = "Tingkir"
            ),
            profileMembershipDataModel = ProfileMembershipDataModel(
                badge = "kucing"
            ),
            profileSellerDataModel = profileSeller
        )

        coEvery {
            shopInfoRefreshData.executeOnBackground()
        } returns Fail(MessageErrorException())
        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getShopInfoUseCase = shopInfoRefreshData
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.shopId == profileSeller.shopId)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.shopName == profileSeller.shopName)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.isGetShopError)
    }

    @Test
    fun `given thrown exception when get notification then do nothing`() {
        val getNavNotification = mockk<GetNavNotification>()
        coEvery { getNavNotification.executeOnBackground() } throws MessageErrorException()
        viewModel = createViewModel(getNavNotification = getNavNotification)
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        val visitableList = viewModel.mainNavLiveData.value?.dataList
        Assert.assertTrue((visitableList?.find { it is HomeNavVisitable && it.id() == ClientMenuGenerator.ID_COMPLAIN } as? HomeNavMenuDataModel)?.notifCount == "")
        Assert.assertTrue((visitableList?.find { it is HomeNavVisitable && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as? HomeNavMenuDataModel)?.notifCount == "")
    }

    @Test
    fun `given location admin role changed when getting admin data then do refreshUserSessionShopData using respective shopid`() {
        val isLocationAdmin: Boolean = true
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        val shopInfoRefreshData = mockk<GetShopInfoUseCase>()
        val userSession = mockk<UserSessionInterface>(relaxed = true)
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
        val shopDataResponse = com.tokopedia.sessioncommon.data.profile.ShopData(shopId = "1")
        val accountInfoPair = Pair(adminDataResponse, shopDataResponse)
        val refreshShopBasicDataUseCase = mockk<RefreshShopBasicDataUseCase>()
        val gqlRepository = mockk<GraphqlRepository>()
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository))

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } returns accountInfoPair

        coEvery {
            shopInfoRefreshData.executeOnBackground()
        } returns Success(
            ShopData(
                ShopData.ShopInfoPojo(
                    ShopData.ShopInfoPojo.Info(
                        shopName = "Shop test",
                        shopId = "123"
                    )
                ),
                ShopData.NotificationPojo()
            )
        )

        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getShopInfoUseCase = shopInfoRefreshData,
            accountAdminInfoUseCase = accountAdminInfoUseCase,
            userSession = userSession
        )
        viewModel.initializeState(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        coVerify { userSession.refreshUserSessionShopData(shopDataResponse) }
    }
}
