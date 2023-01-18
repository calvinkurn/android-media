package com.tokopedia.homenav.mainnav.interactor

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.MePageRollenceController
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
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ShimmerFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ErrorStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ShimmerWishlistDataModel
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

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkObject(MePageRollenceController, recordPrivateCalls = true)
    }

    // Global
    @Test
    fun `test when first load init viewmodel data then data not null`() {
        viewModel = createViewModel()
        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        Assert.assertNotNull(visitableList)
    }

    @Test
    fun `given not logged in user first load viewmodel then menu not empty`() {
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn } returns false
        viewModel = createViewModel(userSession = userSession)

        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList
        Assert.assertNotEquals(0, visitableList?.size)
    }

    @Test
    fun `given user not logged in when using me page variant 1 should add empty data for transaction and favorite shop`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns false

        viewModel = createViewModel(
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant1() } returns true
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value)

        val favoriteShopModel = viewModel.mainNavLiveData.value?.dataList?.find { it is FavoriteShopListDataModel } as? FavoriteShopListDataModel
        Assert.assertTrue(favoriteShopModel != null && favoriteShopModel.favoriteShops.isEmpty())

        val transactionListModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as? TransactionListItemDataModel?
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.orderList.isEmpty())
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.paymentList.isEmpty())

        val menuDataModel = viewModel.mainNavLiveData.value?.dataList?.filterIsInstance<HomeNavMenuDataModel>().orEmpty()

        Assert.assertNotNull(menuDataModel.find { it.id == ClientMenuGenerator.ID_REVIEW && it.sectionId == MainNavConst.Section.ACTIVITY })
        Assert.assertNotNull(menuDataModel.find { it.id == ClientMenuGenerator.ID_WISHLIST_MENU && it.sectionId == MainNavConst.Section.ACTIVITY })
    }

    @Test
    fun `given user not logged in when using me page variant 2 should add empty data for all sections`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns false

        viewModel = createViewModel(
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value)

        val wishlistModel = viewModel.mainNavLiveData.value?.dataList?.find { it is WishlistDataModel } as? WishlistDataModel
        Assert.assertTrue(wishlistModel != null && (wishlistModel.collections.isEmpty() || wishlistModel.isEmptyState))

        val favoriteShopModel = viewModel.mainNavLiveData.value?.dataList?.find { it is FavoriteShopListDataModel } as? FavoriteShopListDataModel
        Assert.assertTrue(favoriteShopModel != null && favoriteShopModel.favoriteShops.isEmpty())

        val transactionListModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as? TransactionListItemDataModel?
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.orderList.isEmpty())
        Assert.assertTrue(transactionListModel != null && transactionListModel.orderListModel.paymentList.isEmpty())

        val reviewModel = viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel } as? ReviewListDataModel
        Assert.assertTrue(reviewModel != null && reviewModel.reviewList.isEmpty())
    }

    @Test
    fun `given user logged in when using me page variant 1 should add shimmer for transaction and favorite shop`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns true

        viewModel = createViewModel(
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant1() } returns true
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value)

        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerFavoriteShopDataModel })

        val menuDataModel = viewModel.mainNavLiveData.value?.dataList?.filterIsInstance<HomeNavMenuDataModel>().orEmpty()

        Assert.assertNotNull(menuDataModel.find { it.id == ClientMenuGenerator.ID_REVIEW && it.sectionId == MainNavConst.Section.ACTIVITY })
        Assert.assertNotNull(menuDataModel.find { it.id == ClientMenuGenerator.ID_WISHLIST_MENU && it.sectionId == MainNavConst.Section.ACTIVITY })
    }

    @Test
    fun `given user logged in when using me page variant 2 should add shimmer for all sections`() {
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns true

        viewModel = createViewModel(
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value)

        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerFavoriteShopDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerWishlistDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
    }

    // Back to home icon
    @Test
    fun `given launch global menu when from other pages than homepage then show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = "Other page"
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setInitialState()
        viewModel.setPageSource(pageSource)
        Assert.assertEquals(pageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel

        Assert.assertNotNull(backToHomeMenu)
    }

    @Test
    fun `given launch global menu when from homepage then do not show back to home icon`() {
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
    fun `given launch global menu when from uoh page then do not show back to home icon`() {
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
    fun `given launch global menu when from wishlist page then do not show back to home icon`() {
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
    fun `given launch global menu when from wishlist collection page then do not show back to home icon`() {
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
    fun `given launch global menu when using default page source then do show back to home icon`() {
        val defaultPageSource = "Default"
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.setPageSource()
        Assert.assertEquals(defaultPageSource, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNotNull(backToHomeMenu)
    }

    // User menu section
    @Test
    fun `given user has no shop then create at least 3 user menu`() {
        val defaultUserMenuCount = 3

        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
            .answers { HomeNavMenuDataModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList?.filter {
            (it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.USER_MENU)
        }

        Assert.assertEquals(defaultUserMenuCount, visitableList!!.size)
    }

    @Test
    fun `given success when logged in user get complain notification then viewmodel update complain visitable with notification`() {
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
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    @Test
    fun `given success when logged in user get inbox ticket notification then viewmodel update tokopedia care visitable with notification`() {
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
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuDataModel

        Assert.assertEquals(mockUnreadCount.toString(), complainVisitable.notifCount)
    }

    @Test
    fun `given thrown exception when get notification then do nothing`() {
        val getNavNotification = mockk<GetNavNotification>()
        coEvery { getNavNotification.executeOnBackground() } throws MessageErrorException()
        viewModel = createViewModel(getNavNotification = getNavNotification)
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()

        val visitableList = viewModel.mainNavLiveData.value?.dataList
        Assert.assertTrue((visitableList?.find { it is HomeNavVisitable && it.id() == ClientMenuGenerator.ID_COMPLAIN } as? HomeNavMenuDataModel)?.notifCount == "")
        Assert.assertTrue((visitableList?.find { it is HomeNavVisitable && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as? HomeNavMenuDataModel)?.notifCount == "")
    }

    // Account Header section
    @Test
    fun `test when set profile from cache with`() {
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
        viewModel.setInitialState()
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

    @Test
    fun `given success when refresh data after login with control rollence then data not null`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given success when refresh shop data with control rollence then change shop name and id`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given success when refresh data affiliate with control rollence data then affiliate data should not error in header`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(!accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given network error when refresh data affiliate with control rollence then affiliate data still error in header`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given thrown exception when refresh data affiliate with control rollence then affiliate data still error in header`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given control rollence then test Success getProfileFullData`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given success when refresh profile after login with control rollence then data not null and have exact result`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given control rollence then test Success getUserNameAndPictureData`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given control rollence then test Error getUserNameAndPictureData missing profile picture`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        viewModel.refreshUserShopData()

        coVerify { userSession.refreshUserSessionShopData(shopDataResponse) }
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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

    // Category section
    @Test
    fun `given success when refresh category after got error with control rollence then should be success and delete error bu list`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val errorStateBuDataModel = dataList.find { it is ErrorStateBuDataModel } as? ErrorStateBuDataModel
        Assert.assertNotNull(errorStateBuDataModel) // error state bu data model existed
        Assert.assertTrue(viewModel.networkProcessLiveData.value == false)

        coEvery {
            getBuListUseCase.executeOnBackground()
        }.answers { listOf() }

        viewModel.refreshBuListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val errorStateBuDataModelRefreshed = dataListRefreshed.find { it is ErrorStateBuDataModel }
        Assert.assertNull(errorStateBuDataModelRefreshed)
        Assert.assertTrue(viewModel.networkProcessLiveData.value == true)
    }

    @Test
    fun `given default data from cache when load all categories on control rollence with exception then success get data from cache`() {
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
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        val homeNavMenuDataModel = dataList.find { it is HomeNavMenuDataModel } as? HomeNavMenuDataModel
        Assert.assertNotNull(homeNavMenuDataModel)
    }

    @Test
    fun `given error when refresh category after got success with control rollence then update with cache data`() {
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

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given failed data when refresh category with control rollence then show retry button`() {
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

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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

    // Transaction section
    @Test
    fun `given user does not have ongoing order and payment transaction with control rollence then only create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns Success(listOf())
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns Success(listOf())

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase
        )
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()

        val menuList = viewModel.mainNavLiveData.value?.dataList?.filter {
            it is HomeNavMenuDataModel && it.sectionId == MainNavConst.Section.ORDER
        } ?: listOf()

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        }

        Assert.assertFalse(menuList.isEmpty())
        Assert.assertNull(transactionDataModel)
    }

    @Test
    fun `given logged in user only has ongoing order with control rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns true
        every { userSession.hasShop() } returns true

        coEvery { userSession.isShopOwner } returns true
        coEvery { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(any()) }.answers { }
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns Success(listOf())
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns Success(listOf(NavPaymentOrder()))

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
            userSession = userSession
        )
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given logged in user only has payment transaction with control page rollence then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(any()) }.answers { }
        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns Success(listOf(NavProductOrder()))
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns Success(listOf())
        coEvery { userSession.isShopOwner } returns true
        every { userSession.isLoggedIn } returns true
        every { userSession.hasShop() } returns true

        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = getPaymentOrdersNavUseCase,
            userSession = userSession
        )
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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
    fun `given success when refresh uoh and transaction with control rollence then result not null`() {
        val getNavOrderUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns true
        every { getNavOrderUseCase.setIsMePageUsingRollenceVariant(any()) }.answers { }
        coEvery { getNavOrderUseCase.executeOnBackground() } returns Success(listOf(NavProductOrder()))
        coEvery { getPaymentUseCase.executeOnBackground() } returns Success(listOf(NavPaymentOrder()))
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentUseCase,
            getUohOrdersNavUseCase = getNavOrderUseCase
        )
        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
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

    // Wishlist section
    @Test
    fun `given success and not empty data when get wishlist with me page variant 2 should add wishlist model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns Triple(listOf(NavWishlistModel(), NavWishlistModel()), false, false)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is WishlistDataModel } == true)
        assert(viewModel.allProcessFinished.value?.peekContent() == true)
    }

    @Test
    fun `given empty data when get wishlist with me page variant 2 should add empty wishlist`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } returns Triple(listOf(), false, true)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val wishlistModel = viewModel.mainNavLiveData.value?.dataList?.find { it is WishlistDataModel } as? WishlistDataModel
        assert(wishlistModel != null && wishlistModel.collections.isEmpty())
    }

    @Test
    fun `given error when get wishlist with me page variant 2 should add error state`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistUseCase = mockk<GetWishlistNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { wishlistUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)
    }

    @Test
    fun `given success when refresh wishlist after got error with me page variant 2 then update error state to show wishlist`() {
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist = NavWishlistModel(
            id = "123",
            name = "Item 1"
        )

        coEvery { wishlistNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getWishlistNavUseCase = wishlistNavUseCase)

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns Triple(listOf(wishlist), true, false)

        viewModel.refreshWishlistData()

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.collections.contains(wishlist)
            } == true
        )
    }

    @Test
    fun `given success when refresh wishlist with me page variant 2 should update existing list`() {
        val userSession = mockk<UserSessionInterface>()
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist1 = NavWishlistModel(
            id = "123",
            name = "Item 1"
        )
        val wishlist2 = NavWishlistModel(
            id = "234",
            name = "Item 2"
        )

        // Initial wishlist data
        every { userSession.isLoggedIn } returns true
        coEvery { wishlistNavUseCase.executeOnBackground() } returns Triple(listOf(wishlist1), true, false)

        viewModel = createViewModel(
            userSession = userSession,
            getWishlistNavUseCase = wishlistNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.collections.contains(wishlist1)
            } == true
        )

        // Refresh data
        coEvery { wishlistNavUseCase.executeOnBackground() } returns Triple(listOf(wishlist2), true, false)
        viewModel.refreshWishlistData()

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.collections.contains(wishlist2)
            } == true
        )
    }

    @Test
    fun `given error when get wishlist with me page variant 2 then success after reload page should update error state to show wishlist`() {
        val wishlistNavUseCase = mockk<GetWishlistNavUseCase>()

        val wishlist = NavWishlistModel(
            id = "123",
            name = "Item 1"
        )

        coEvery { wishlistNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getWishlistNavUseCase = wishlistNavUseCase)

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateWishlistDataModel } == true)

        coEvery { wishlistNavUseCase.executeOnBackground() } returns Triple(listOf(wishlist), true, false)

        viewModel.getMainNavData(true)

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is WishlistDataModel &&
                    it.collections.contains(wishlist)
            } == true
        )
    }

    @Test
    fun `given only 1 wishlist data when get wishlist with me page variant 2 should show only 1 full width wishlist data`() {
        val mockList1Wishlist = Triple(listOf(NavWishlistModel()), true, false)
        val getWishlistNavUseCase = mockk<GetWishlistNavUseCase>()
        coEvery {
            getWishlistNavUseCase.executeOnBackground()
        } returns mockList1Wishlist
        viewModel = createViewModel(
            getWishlistNavUseCase = getWishlistNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val wishlistDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is WishlistDataModel
        } as? WishlistDataModel

        Assert.assertEquals(1, wishlistDataModel?.collections?.size)
        Assert.assertTrue(wishlistDataModel?.collections?.get(0)?.fullWidth == true)
    }

    // Favorite shop section
    @Test
    fun `given success and not empty data when get favorite shop with me page variant should add favorite shop model to visitable list`() {
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

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is FavoriteShopListDataModel } == true)
        assert(viewModel.allProcessFinished.value?.peekContent() == true)
    }

    @Test
    fun `given empty data when get favorite shop with me page variant should add empty favorite shops`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(), false)

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val favoriteShopModel = viewModel.mainNavLiveData.value?.dataList?.find { it is FavoriteShopListDataModel } as? FavoriteShopListDataModel
        assert(favoriteShopModel != null && favoriteShopModel.favoriteShops.isEmpty())
    }

    @Test
    fun `given error when get favorite shop with me page variant should add error state`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)
    }

    @Test
    fun `given success when refresh favorite shop after got error with me page variant should update existing list`() {
        val userSession = mockk<UserSessionInterface>()
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        val favoriteShop = NavFavoriteShopModel(
            id = "1",
            name = "Toko A",
            location = "Tangerang"
        )

        // Initial favorite shop
        every { userSession.isLoggedIn } returns true
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getFavoriteShopsNavUseCase = favoriteShopsNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateFavoriteShopDataModel } == true)

        // Refresh data
        coEvery { favoriteShopsNavUseCase.executeOnBackground() } returns Pair(listOf(favoriteShop), true)
        viewModel.refreshFavoriteShopData()

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is FavoriteShopListDataModel &&
                    it.favoriteShops.contains(favoriteShop)
            } == true
        )
    }

    @Test
    fun `given success when refresh favorite shop with me page variant should update existing list`() {
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

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
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
    fun `given error when get favorite shop using me page variant then success after reload page should update error state to show favorite shop`() {
        val favoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()

        val favoriteShop = NavFavoriteShopModel(
            id = "1",
            name = "Toko A",
            location = "Tangerang"
        )

        coEvery { favoriteShopsNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getFavoriteShopsNavUseCase = favoriteShopsNavUseCase)

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
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
    fun `given only 1 favorite shop when get favorite shop with me page variant should show only 1 full width favorite shop`() {
        val mockList1FavShop = Pair(listOf(NavFavoriteShopModel()), true)
        val getFavoriteShopsNavUseCase = mockk<GetFavoriteShopsNavUseCase>()
        coEvery {
            getFavoriteShopsNavUseCase.executeOnBackground()
        } returns mockList1FavShop
        viewModel = createViewModel(
            getFavoriteShopsNavUseCase = getFavoriteShopsNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val favoriteShopListDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is FavoriteShopListDataModel
        } as? FavoriteShopListDataModel

        Assert.assertEquals(1, favoriteShopListDataModel?.favoriteShops?.size)
        Assert.assertTrue(favoriteShopListDataModel?.favoriteShops?.get(0)?.fullWidth == true)
    }

    // Transaction section revamp (me page)
    @Test
    fun `given 3 payment list and 3 ongoing transaction when get order history with me page variant then show only 3 payments and 2 ongoing transaction data`() {
        val mockList3PaymentOrder = listOf(NavPaymentOrder(), NavPaymentOrder(), NavPaymentOrder())
        val mockList3ProductOrder = listOf(NavProductOrder(), NavProductOrder(), NavProductOrder())
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()

        every { userSession.isLoggedIn } returns true
        every {
            getUohOrdersNavUseCase.setIsMePageUsingRollenceVariant(any())
        } answers { }
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } returns Success(mockList3PaymentOrder)
        coEvery {
            getUohOrdersNavUseCase.executeOnBackground()
        } returns Success(mockList3ProductOrder)

        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            getUohOrdersNavUseCase = getUohOrdersNavUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as? TransactionListItemDataModel

        Assert.assertEquals(3, transactionDataModel?.orderListModel?.paymentList?.size)
        Assert.assertEquals(2, transactionDataModel?.orderListModel?.orderList?.size)
    }

    @Test
    fun `given error when refresh order transaction with control rollence then show failed get order transaction`() {
        val userSession = mockk<UserSessionInterface>()
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        every { userSession.isLoggedIn } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns false
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.any { it is ErrorStateOngoingTransactionModel })

        viewModel.refreshTransactionListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateOngoingTransactionModel })
    }

    @Test
    fun `given error when refresh order transaction with me page variant then show failed get order transaction`() {
        val userSession = mockk<UserSessionInterface>()
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        every { userSession.isLoggedIn } returns true
        every { userSession.isShopOwner } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataList.any { it is ErrorStateOngoingTransactionModel })

        viewModel.refreshTransactionListData()
        val dataListRefreshed = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
        Assert.assertTrue(dataListRefreshed.any { it is ErrorStateOngoingTransactionModel })
    }

    @Test
    fun `given only 1 payment data when get order history with me page variant then show only 1 full width payment data`() {
        val mockList1PaymentOrder = listOf(NavPaymentOrder())
        val getPaymentOrderNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn } returns true
        coEvery {
            getPaymentOrderNavUseCase.executeOnBackground()
        } returns Success(mockList1PaymentOrder)
        viewModel = createViewModel(
            getPaymentOrdersNavUseCase = getPaymentOrderNavUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as? TransactionListItemDataModel

        Assert.assertEquals(1, transactionDataModel?.orderListModel?.paymentList?.size)
        Assert.assertTrue(transactionDataModel?.orderListModel?.paymentList?.get(0)?.fullWidth == true)
    }

    @Test
    fun `given only 1 ongoing order when get order history with me page variant then show only 1 full width order data`() {
        val mockList1OrderProduct = listOf(NavProductOrder())
        val getUohOrderNavUseCase = mockk<GetUohOrdersNavUseCase>()
        every { getUohOrderNavUseCase.setIsMePageUsingRollenceVariant(any()) }.answers { }
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn } returns true
        coEvery {
            getUohOrderNavUseCase.executeOnBackground()
        } returns Success(mockList1OrderProduct)
        viewModel = createViewModel(
            getUohOrdersNavUseCase = getUohOrderNavUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val transactionDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is TransactionListItemDataModel
        } as? TransactionListItemDataModel

        Assert.assertEquals(1, transactionDataModel?.orderListModel?.orderList?.size)
        Assert.assertTrue(transactionDataModel?.orderListModel?.orderList?.get(0)?.fullWidth == true)
    }

    // Review section
    @Test
    fun `given success and not empty data when get review with me page variant 2 should add review model to visitable list`() {
        val userSession = mockk<UserSessionInterface>()
        val reviewProductUseCase = mockk<GetReviewProductUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { reviewProductUseCase.executeOnBackground() } returns listOf(NavReviewModel(), NavReviewModel())

        viewModel = createViewModel(
            userSession = userSession,
            getReviewProductUseCase = reviewProductUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ReviewListDataModel } == true)
        assert(viewModel.allProcessFinished.value?.peekContent() == true)
    }

    @Test
    fun `given empty data when get review with me page variant 2 should add empty review`() {
        val userSession = mockk<UserSessionInterface>()
        val reviewProductUseCase = mockk<GetReviewProductUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { reviewProductUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
            userSession = userSession,
            getReviewProductUseCase = reviewProductUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val reviewModel = viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel } as? ReviewListDataModel
        assert(reviewModel != null && reviewModel.reviewList.isEmpty())
    }

    @Test
    fun `given error when get review with me page variant 2 should add error state`() {
        val userSession = mockk<UserSessionInterface>()
        val reviewProductUseCase = mockk<GetReviewProductUseCase>()

        every { userSession.isLoggedIn } returns true
        coEvery { reviewProductUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = userSession,
            getReviewProductUseCase = reviewProductUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        assert(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateReviewDataModel } == true)
    }

    @Test
    fun `given success when refresh review after got error with me page variant 2 then update error state to show review`() {
        val reviewProductUseCase = mockk<GetReviewProductUseCase>()

        val review = NavReviewModel(
            productId = "123",
            productName = "Asd"
        )

        coEvery { reviewProductUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getReviewProductUseCase = reviewProductUseCase)

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateReviewDataModel } == true)

        coEvery { reviewProductUseCase.executeOnBackground() } returns listOf(review)

        viewModel.refreshReviewData()

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is ReviewListDataModel &&
                    it.reviewList.contains(review)
            } == true
        )
    }

    @Test
    fun `given success when refresh review with me page variant 2 should update existing list`() {
        val userSession = mockk<UserSessionInterface>()
        val reviewNavUseCase = mockk<GetReviewProductUseCase>()

        val review1 = NavReviewModel(
            productId = "123",
            productName = "Item 1"
        )
        val review2 = NavReviewModel(
            productId = "234",
            productName = "Item 2"
        )

        // Initial wishlist data
        every { userSession.isLoggedIn } returns true
        coEvery { reviewNavUseCase.executeOnBackground() } returns listOf(review1)

        viewModel = createViewModel(
            userSession = userSession,
            getReviewProductUseCase = reviewNavUseCase
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is ReviewListDataModel &&
                    it.reviewList.contains(review1)
            } == true
        )

        // Refresh data
        coEvery { reviewNavUseCase.executeOnBackground() } returns listOf(review2)
        viewModel.refreshReviewData()

        assert(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is ReviewListDataModel &&
                    it.reviewList.contains(review2)
            } == true
        )
    }

    @Test
    fun `given error when get review with me page variant 2 then success after reload page should update error state to show review`() {
        val reviewNavUseCase = mockk<GetReviewProductUseCase>()

        val review = NavReviewModel(
            productId = "123",
            productName = "Item 1"
        )

        coEvery { reviewNavUseCase.executeOnBackground() } throws MessageErrorException("")

        viewModel = createViewModel(getReviewProductUseCase = reviewNavUseCase)

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertTrue(viewModel.mainNavLiveData.value?.dataList?.any { it is ErrorStateReviewDataModel } == true)

        coEvery { reviewNavUseCase.executeOnBackground() } returns listOf(review)

        viewModel.getMainNavData(true)

        Assert.assertTrue(
            viewModel.mainNavLiveData.value?.dataList?.any {
                it is ReviewListDataModel &&
                    it.reviewList.contains(review)
            } == true
        )
    }

    @Test
    fun `given only 1 review product data when get order history with me page variant 2 then show only 1 full width review product`() {
        val mockList1ReviewOrder = listOf(NavReviewModel())
        val getReviewProductUseCase = mockk<GetReviewProductUseCase>()
        val userSession = mockk<UserSessionInterface>()
        every { userSession.isLoggedIn } returns true
        coEvery {
            getReviewProductUseCase.executeOnBackground()
        } returns mockList1ReviewOrder
        viewModel = createViewModel(
            getReviewProductUseCase = getReviewProductUseCase,
            userSession = userSession
        )

        every { MePageRollenceController.isUsingMePageRollenceVariant() } returns true
        every { MePageRollenceController.isUsingMePageRollenceVariant2() } returns true
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val reviewListDataModel = viewModel.mainNavLiveData.value?.dataList?.find {
            it is ReviewListDataModel
        } as? ReviewListDataModel

        Assert.assertEquals(1, reviewListDataModel?.reviewList?.size)
        Assert.assertTrue(reviewListDataModel?.reviewList?.get(0)?.fullWidth == true)
    }
}
