package com.tokopedia.homenav.mainnav.interactor

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.base.datamodel.HomeNavExpandableDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.domain.model.NavNotificationModel
import com.tokopedia.homenav.mainnav.domain.model.MainNavProfileCache
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.*
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ErrorStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.domain.usecase.RefreshShopBasicDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
    private val mockListAllCategory = listOf(HomeNavMenuDataModel())
    private val MOCK_IS_ME_PAGE_ROLLENCE_DISABLE = false
    private val MOCK_IS_ME_PAGE_ROLLENCE_ENABLE = true

    @Before
    fun setup(){
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
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

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
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
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
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNull(backToHomeMenu)
    }

    @Test
    fun `test when nav page launched from wishlist page then do not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

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
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setPageSource()
        Assert.assertEquals(defaultPageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNotNull(backToHomeMenu)
    }

    //user menu section
    @Test
    fun `test when viewmodel created and user has no shop with disable me page rollence then viewmodel create at least 3 user menu`() {
        val defaultUserMenuCount = 3

        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?.filter {
            (it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.USER_MENU)
        }

        Assert.assertEquals(defaultUserMenuCount, visitableList!!.size)
    }

    //user menu section
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
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountComplain = mockUnreadCount) }

        viewModel = createViewModel(
            clientMenuGenerator = clientMenuGenerator,
            getNavNotification = getNavNotification
        )
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    //test user profile cache
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.setProfileCache(mainNavProfileCacheMock)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()

        val accountHeaderDataModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderDataModel)
        Assert.assertTrue(
                 accountHeaderDataModel.profileDataModel.userName == mainNavProfileCacheMock.profileName
                && accountHeaderDataModel.profileDataModel.userImage == mainNavProfileCacheMock.profilePicUrl
                && accountHeaderDataModel.profileMembershipDataModel.badge == mainNavProfileCacheMock.memberStatusIconUrl
                )
    }

    //user menu section
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
            .answers {(HomeNavTitleDataModel(identifier = firstArg()))}
        coEvery { getNavNotification.executeOnBackground() }.answers { NavNotificationModel(unreadCountInboxTicket = mockUnreadCount) }

        viewModel = createViewModel(
            clientMenuGenerator = clientMenuGenerator,
            getNavNotification = getNavNotification
        )
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }


    //transaction section
    @Test
    fun `test when viewmodel created and user does not ongoing order and payment transaction with disable me page rollence then only create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

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
    fun `test when viewmodel created and logged in user only have ongoing order with disable me page rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn() } returns true
        every { userSession.hasShop() } returns true

        coEvery { userSession.isShopOwner } returns true
        coEvery { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers {  }
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
            userSession = userSession
        )
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
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
    fun `test when viewmodel created and loggedin user only have payment transaction with disable me page rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers {  }
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
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
    fun `test when data loaded complete with disabled me page rollence then check account header menu section is available`() {
        viewModel = createViewModel(
        )
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
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
    fun `test when user not login first load init viewmodel with disabled me page rollence then menu not empty`() {
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn() } returns false
        viewModel = createViewModel(userSession = userSession)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

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
        every { getNavOrderUseCase.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE) }.answers {  }
        coEvery { getNavOrderUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        coEvery { getPaymentUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentUseCase,
            getUohOrdersNavUseCase = getNavOrderUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
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
    fun `test when success refresh data after login with disabled me page rollence then check data not null`(){
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.reloadMainNavAfterLogin()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isNotEmpty()
                && accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userName == "Joko"
                && accountHeaderViewModel.profileDataModel.userImage == "Tingkir"
                && accountHeaderViewModel.profileMembershipDataModel.badge == "kucing"
                && accountHeaderViewModel.profileSellerDataModel.shopId == "1234"
                && accountHeaderViewModel.profileSellerDataModel.shopName == "binatang")
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
            ))

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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(visitableList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopId == newShopId
                && accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopName == newShopName)
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
            getAffiliateUserUseCase = getAffiliateUserUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
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
            getAffiliateUserUseCase = getAffiliateUserUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
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
            getAffiliateUserUseCase = getAffiliateUserUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given disabled me page rollence then test Success getProfileFullData`(){
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
            ))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isNotEmpty()
                && accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userName == "Joko"
                && accountHeaderViewModel.profileDataModel.userImage == "Tingkir"
                && accountHeaderViewModel.profileMembershipDataModel.badge == "kucing"
                && accountHeaderViewModel.profileSellerDataModel.shopId == "1234"
                && accountHeaderViewModel.profileSellerDataModel.shopName == "binatang")
    }

    @Test
    fun `test when success refresh profile after login with disabled me page rollence then data not null and have exact result`(){
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
            ))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)
        viewModel.refreshProfileData()

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isNotEmpty()
                && accountHeaderViewModel.profileMembershipDataModel.badge.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopId.isNotEmpty()
                && accountHeaderViewModel.profileSellerDataModel.shopName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userName == "Joko"
                && accountHeaderViewModel.profileDataModel.userImage == "Tingkir"
                && accountHeaderViewModel.profileMembershipDataModel.badge == "kucing"
                && accountHeaderViewModel.profileSellerDataModel.shopId == "1234"
                && accountHeaderViewModel.profileSellerDataModel.shopName == "binatang")
    }

    @Test
    fun `given disabled me page rollence then test Success getUserNameAndPictureData`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isNotEmpty())
    }

    @Test
    fun `given disable me page rollence then Error getUserNameAndPictureData missing name`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isNotEmpty())
    }

    @Test
    fun `given disabled me page rollence then test Error getUserNameAndPictureData missing profile picture`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isNotEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isEmpty())
    }

    @Test
    fun `given disable rollence me page Error getUserNameAndPictureData missing all`(){
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val accountHeaderViewModel = dataList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(dataList.isNotEmpty())
        Assert.assertNotNull(accountHeaderViewModel)
        Assert.assertTrue(accountHeaderViewModel.profileDataModel.userName.isEmpty()
                && accountHeaderViewModel.profileDataModel.userImage.isEmpty())
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)

        viewModel.getMainNavData(true)
        val complainPosition = viewModel.findComplainModelPosition()
        Assert.assertNotEquals(defaultPositionComplaintNotFound, complainPosition)
    }

    @Test
    fun `test when show coachmark all transaction with correct position and disable me page rollence`() {
        val indexDefaultAllTransaction = 1
        val pageSource = "Other page"

        viewModel = createViewModel()
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModel = dataList.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val errorStateBuDataModel = homeNavExpandableDataModel.menus.find { it is ErrorStateBuDataModel } as ErrorStateBuDataModel
        Assert.assertNotNull(errorStateBuDataModel) //error state bu data model existed

        coEvery {
            getBuListUseCase.executeOnBackground()
        }.answers { listOf() }

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModelRefreshed = dataListRefreshed.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val errorStateBuDataModelRefreshed = homeNavExpandableDataModelRefreshed.menus.find { it is ErrorStateBuDataModel }
        Assert.assertNull(errorStateBuDataModelRefreshed)
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_ENABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavExpandableDataModel = dataList.find { it is HomeNavExpandableDataModel } as HomeNavExpandableDataModel
        val homeNavMenuDataModel = homeNavExpandableDataModel.menus.find { it is HomeNavMenuDataModel } as HomeNavMenuDataModel
        Assert.assertNotNull(homeNavMenuDataModel)
    }

    @Test
    fun `given using rollence variant when user not logged in should add empty state non logged in`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns false

        viewModel = createViewModel(
            userSession = userSession
        )
        viewModel.setIsMePageUsingRollenceVariant(true)

        Assert.assertNotNull(viewModel.mainNavLiveData.value)
        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any{ it is EmptyStateNonLoggedInDataModel } == true)
    }

    @Test
    fun `given success and not empty wishlist should add wishlist model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns listOf(NavWishlistModel())

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel } == true)
    }

    @Test
    fun `given success and not empty when favorite shop should add favorite shop model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns listOf(NavFavoriteShopModel())

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel } == true)
    }

    @Test
    fun `given empty data when get wishlist should add empty state`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns emptyList()

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is EmptyStateWishlistDataModel } == true)
    }

    @Test
    fun `given empty data when get favorite shop should add empty state`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns emptyList()

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is EmptyStateFavoriteShopDataModel } == true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
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
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns listOf(favoriteShop1)

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel
                && it.favoriteShops.contains(favoriteShop1) } == true)

        // Refresh data
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns listOf(favoriteShop2)
        viewModel.refreshFavoriteShopData()

        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel
                && it.favoriteShops.contains(favoriteShop2) } == true)
    }

    @Test
    fun `test when refresh wishlist should update existing list`()  {
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
        coEvery { wishlistNavUseCase.executeOnBackground() } returns listOf(wishlist1)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistNavUseCase
        )
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel
                && it.wishlist.contains(wishlist1) } == true)

        // Refresh data
        coEvery { wishlistNavUseCase.executeOnBackground() } returns listOf(wishlist2)
        viewModel.refreshWishlistData()

        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel
                && it.wishlist.contains(wishlist2) } == true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns listOf(wishlist)

        viewModel.refreshWishlistData()

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel
                && it.wishlist.contains(wishlist) } == true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns listOf(wishlist)

        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel
                && it.wishlist.contains(wishlist) } == true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns listOf(favoriteShop)

        viewModel.refreshFavoriteShopData()

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel
                && it.favoriteShops.contains(favoriteShop) } == true)
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
        viewModel.setIsMePageUsingRollenceVariant(true)
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns listOf(favoriteShop)

        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel
                && it.favoriteShops.contains(favoriteShop) } == true)
    }

    @Test
    fun `test success show bu list with disabled me page rollence then error after refresh data should update bu to error state`() {
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
        viewModel.setIsMePageUsingRollenceVariant(MOCK_IS_ME_PAGE_ROLLENCE_DISABLE)
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.contains(successResult))

        coEvery {
            getBuListUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertFalse(dataListRefreshed.contains(successResult))
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateBuDataModel }) //error state bu data model existed
    }
}
