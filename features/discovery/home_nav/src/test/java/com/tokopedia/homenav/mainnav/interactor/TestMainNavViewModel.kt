package com.tokopedia.homenav.mainnav.interactor

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.sessioncommon.domain.usecase.RefreshShopBasicDataUseCase
import com.tokopedia.sessioncommon.util.AdminUserSessionUtil.refreshUserSessionShopData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
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

    private lateinit var viewModel: MainNavViewModel
    private val mockGetUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
    private val mockGetPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()
    private val mockGetReviewProductUseCase = mockk<GetReviewProductUseCase>()
    private val mockAddToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    private val mockRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed =  true)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
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

    // Back to home icon
    @Test
    fun `given launch global menu when from other pages than homepage then show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = NavSource.PDP
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any(), showCta = any()) }
            .answers { HomeNavMenuDataModel(id = arg(0), notifCount = arg(1), sectionId = arg(2), showCta = arg(3)) }
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
        val pageSource = NavSource.HOME
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
        val separator = visitableList.find {
            it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME ||
                it is SeparatorDataModel && it.sectionId == MainNavConst.Section.PROFILE
        } as? SeparatorDataModel

        Assert.assertNull(backToHomeMenu)
        Assert.assertNull(separator)
    }

    @Test
    fun `given launch global menu when from uoh page then do not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = NavSource.HOME_UOH
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
        val pageSource = NavSource.HOME_WISHLIST
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
    fun `given launch global menu when from home sos page then do not show back to home icon`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val pageSource = NavSource.SOS
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
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any(), showCta = any()) }
            .answers { HomeNavMenuDataModel(id = arg(0), notifCount = arg(1), sectionId = arg(2), showCta = arg(3)) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setInitialState()
        viewModel.setPageSource()
        Assert.assertEquals(NavSource.DEFAULT, viewModel.getPageSource())

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val backToHomeMenu = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as HomeNavMenuDataModel?

        Assert.assertNotNull(backToHomeMenu)
    }

    @Test
    fun `given launch global menu when from other pages first then launch from home should remove back button`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val initialSource = NavSource.PDP
        var visitableList = listOf<Visitable<*>>()
        var backToHomeButton: HomeNavMenuDataModel? = null
        var backToHomeSeparator: SeparatorDataModel? = null
        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any(), showCta = any()) }
            .answers { HomeNavMenuDataModel(id = arg(0), notifCount = arg(1), sectionId = arg(2), showCta = arg(3)) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }

        viewModel = createViewModel(clientMenuGenerator = clientMenuGenerator)
        viewModel.setInitialState()

        viewModel.setPageSource(initialSource)
        Assert.assertEquals(initialSource, viewModel.getPageSource())
        visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        backToHomeButton = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as? HomeNavMenuDataModel
        backToHomeSeparator = visitableList.find { it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME } as? SeparatorDataModel
        Assert.assertNotNull(backToHomeButton)
        Assert.assertNotNull(backToHomeSeparator)

        val homeSource = NavSource.HOME
        viewModel.setPageSource(homeSource)
        Assert.assertEquals(homeSource, viewModel.getPageSource())
        visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        backToHomeButton = visitableList.find { it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME } as? HomeNavMenuDataModel
        backToHomeSeparator = visitableList.find { it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME } as? SeparatorDataModel
        Assert.assertNull(backToHomeButton)
        Assert.assertNull(backToHomeSeparator)
    }

    // User menu section
    @Test
    fun `given success when logged in user get notification then viewmodel update notification counter`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getNavNotification = mockk<GetNavNotification>()

        val mockComplainCount = 10
        val mockInboxCount = 20

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any(), showCta = any()) }
            .answers { HomeNavMenuDataModel(id = arg(0), notifCount = arg(1), sectionId = arg(2), showCta = arg(3)) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
            .answers { HomeNavTickerDataModel() }
        every { clientMenuGenerator.getSectionTitle(identifier = any()) }
            .answers { (HomeNavTitleDataModel(identifier = firstArg())) }
        coEvery { getNavNotification.executeOnBackground() }.answers {
            NavNotificationModel(
                unreadCountComplain = mockComplainCount,
                unreadCountInboxTicket = mockInboxCount
            )
        }

        viewModel = createViewModel(
            clientMenuGenerator = clientMenuGenerator,
            getNavNotification = getNavNotification
        )
        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuDataModel
        val inboxVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_TOKOPEDIA_CARE } as HomeNavMenuDataModel
        val reviewVisitable = visitableList.find { it is HomeNavMenuDataModel && it.id() == ClientMenuGenerator.ID_REVIEW } as HomeNavMenuDataModel

        Assert.assertEquals(mockComplainCount.toString(), complainVisitable.notifCount)
        Assert.assertEquals(mockInboxCount.toString(), inboxVisitable.notifCount)
    }

    @Test
    fun `given thrown exception when get notification then do nothing`() {
        val getNavNotification = mockk<GetNavNotification>()
        coEvery { getNavNotification.executeOnBackground() } throws MessageErrorException()
        viewModel = createViewModel(getNavNotification = getNavNotification)
        viewModel.setInitialState()
        viewModel.getMainNavData(false)

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
    fun `given success when refresh data after login then data not null`() {
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
    fun `given success when refresh shop data with then change shop name and id`() {
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
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository, CoroutineTestDispatchers))

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
            accountAdminInfoUseCase(any())
        } returns accountInfoPair
        viewModel = createViewModel(
            getProfileDataUseCase = getProfileDataUseCase,
            getShopInfoUseCase = shopInfoRefreshData,
            accountAdminInfoUseCase = accountAdminInfoUseCase
        )
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
    fun `given success when refresh data affiliate data then affiliate data should not error in header`() {
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
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(!accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given network error when refresh data affiliate then affiliate data still error in header`() {
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
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `given thrown exception when refresh data affiliate then affiliate data still error in header`() {
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
        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        val visitableList = viewModel.mainNavLiveData.value?.dataList ?: listOf()
        val accountHeaderViewModel = visitableList.find { it is AccountHeaderDataModel } as AccountHeaderDataModel
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)

        viewModel.refreshUserAffiliateData()
        Assert.assertTrue(accountHeaderViewModel.profileAffiliateDataModel.isGetAffiliateError)
    }

    @Test
    fun `test Success getProfileFullData`() {
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
    fun `given success when refresh profile after login then data not null and have exact result`() {
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
    fun `test Success getUserNameAndPictureData`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
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
    fun `test Error getUserNameAndPictureData missing name`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = "Tingkir"))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
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
    fun `test Error getUserNameAndPictureData missing profile picture`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "Joko", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
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
    fun `test Error getUserNameAndPictureData missing all`() {
        val getProfileDataUseCase = mockk<GetProfileDataUseCase>()
        coEvery {
            getProfileDataUseCase.executeOnBackground()
        } returns AccountHeaderDataModel(profileDataModel = ProfileDataModel(userName = "", userImage = ""))
        viewModel = createViewModel(getProfileDataUseCase = getProfileDataUseCase)
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
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository, CoroutineTestDispatchers))
        val userSession = mockk<UserSessionInterface>(relaxed = true)
        coEvery {
            accountAdminInfoUseCase(any())
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
        val accountAdminInfoUseCase = spyk(AccountAdminInfoUseCase(refreshShopBasicDataUseCase, gqlRepository, CoroutineTestDispatchers))

        coEvery {
            accountAdminInfoUseCase(any())
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

    @Test
    fun `given launched from home then still show separator after profile section`() {
        viewModel = createViewModel(userSession = getUserSession(true))
        viewModel.setPageSource(NavSource.HOME)

        Assert.assertNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_HOME
            }
        )
        Assert.assertNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is SeparatorDataModel && it.sectionId == MainNavConst.Section.HOME
            }
        )
        Assert.assertNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is SeparatorDataModel && it.sectionId == MainNavConst.Section.PROFILE
            }
        )
    }

    @Test
    fun `given user not logged in then show no visual cards`() {
        viewModel = createViewModel(userSession = getUserSession(false))

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && !it.showCta
            }
        )
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_REVIEW && !it.showCta
            }
        )
    }

    @Test
    fun `given user logged in then add shimmer placeholder for transaction and review`() {
        viewModel = createViewModel(userSession = getUserSession(true))
        viewModel.setInitialState()

        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && it.showCta
            }
        )
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_REVIEW && it.showCta
            }
        )
    }

    // Transaction section
    @Test
    fun `given logged in user has ongoing payment then show payment cards and menu cta`() {
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val transactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        Assert.assertTrue(transactionModel.orderListModel.paymentList.isNotEmpty())
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && it.showCta
            }
        )
    }

    @Test
    fun `given logged in user has ongoing orders then show order cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns listOf(NavProductOrder())
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val transactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        Assert.assertTrue(transactionModel.orderListModel.orderList.isNotEmpty())
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && it.showCta
            }
        )
    }

    @Test
    fun `given logged in user has more than 5 transactions then show maximum 5 items with payments being prioritized`() {
        val mockOrders = listOf(
            NavProductOrder(),
            NavProductOrder(),
            NavProductOrder(),
            NavProductOrder()
        )
        val mockPayments = listOf(
            NavPaymentOrder(),
            NavPaymentOrder(),
            NavPaymentOrder()
        )

        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns mockOrders
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns mockPayments
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        val resultTransactionModel = viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel } as TransactionListItemDataModel
        val resultOrdersCount = resultTransactionModel.orderListModel.orderList.size
        val resultPaymentsCount = resultTransactionModel.orderListModel.paymentList.size

        Assert.assertTrue(resultOrdersCount + resultPaymentsCount == 5)
        Assert.assertTrue(resultPaymentsCount == 3)
        Assert.assertTrue(resultOrdersCount == 2)
    }

    @Test
    fun `given logged in user has no ongoing orders and payments then remove visual cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { mockGetPaymentOrdersNavUseCase.executeOnBackground() } returns listOf()
        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase,
            getPaymentOrdersNavUseCase = mockGetPaymentOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && !it.showCta
            }
        )
    }

    @Test
    fun `given logged in user gets error when fetching transaction then remove transaction cards and menu cta`() {
        coEvery { mockGetUohOrdersNavUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getUohOrdersNavUseCase = mockGetUohOrdersNavUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is InitialShimmerTransactionRevampDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is TransactionListItemDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_ALL_TRANSACTION && !it.showCta
            }
        )
    }

    // Review section
    @Test
    fun `given logged in user has ongoing review then show review cards and menu cta`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns listOf(NavReviewModel(), NavReviewModel())

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNotNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_REVIEW && it.showCta
            }
        )
    }

    @Test
    fun `given logged in user has no review then remove review cards`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns listOf()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_REVIEW && !it.showCta
            }
        )
    }

    @Test
    fun `given logged in user gets error when fetching review then remove review cards`() {
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } throws Exception()

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ShimmerReviewDataModel })
        Assert.assertNull(viewModel.mainNavLiveData.value?.dataList?.find { it is ReviewListDataModel })
        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is HomeNavMenuDataModel && it.id == ClientMenuGenerator.ID_REVIEW && !it.showCta
            }
        )
    }

    @Test
    fun `given user get back when after giving review then refresh review cards`() {
        val initialData = listOf(NavReviewModel("123"), NavReviewModel("234"))
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns initialData

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getReviewProductUseCase = mockGetReviewProductUseCase
        )

        viewModel.setInitialState()
        viewModel.getMainNavData(true)

        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is ReviewListDataModel && (it as? ReviewListDataModel)?.reviewList == initialData
            }
        )

        val refreshedData = listOf(NavReviewModel("234"))
        coEvery { mockGetReviewProductUseCase.executeOnBackground() } returns refreshedData
        viewModel.refreshReviewData()

        Assert.assertNotNull(
            viewModel.mainNavLiveData.value?.dataList?.find {
                it is ReviewListDataModel && (it as? ReviewListDataModel)?.reviewList == refreshedData
            }
        )
    }

    @Test
    fun `given user add to cart success should update atcProductState pair isError false`(){
        val addToCartDataModel = AddToCartDataModel(status = "OK", data = DataModel(success = 1))

        coEvery { mockAddToCartUseCase.executeOnBackground() } returns addToCartDataModel

        viewModel = createViewModel(
            userSession = getUserSession(true),
            addToCartUseCase = mockAddToCartUseCase
        )

        viewModel.setInitialState()
        viewModel.addToCartProduct("", "")

        val newState = viewModel.onAtcProductState.value
        Assert.assertEquals(false, newState?.first)
    }

    @Test
    fun `given user add to cart throwing exception should update atcProductState pair isError true`(){

        coEvery { mockAddToCartUseCase.executeOnBackground() } throws  Exception("")

        viewModel = createViewModel(
            userSession = getUserSession(true),
            addToCartUseCase = mockAddToCartUseCase
        )

        viewModel.setInitialState()
        viewModel.addToCartProduct("", "")

        val newState = viewModel.onAtcProductState.value
        Assert.assertEquals(true, newState?.first)
    }

    @Test
    fun `given user refreshes buy again data should call recommendation with page name buy again and update data`(){
        val requestParamSlot = slot<GetRecommendationRequestParam>()
        val recommendationItemList = listOf(
            RecommendationItem(
                productId = 1,
            )
        )
        val recommendationWidgetList = listOf(
            RecommendationWidget(
                recommendationItemList = recommendationItemList,
            )
        )

        coEvery { mockRecommendationUseCase.getData(capture(requestParamSlot)) } returns recommendationWidgetList

        viewModel = createViewModel(
            userSession = getUserSession(true),
            getRecommendationUseCase = mockRecommendationUseCase
        )

        viewModel.setInitialState()
        viewModel.refreshBuyAgainData()

        coVerify (exactly = 1){ mockRecommendationUseCase.getData(any()) }
        Assert.assertEquals("buy_it_again_me", requestParamSlot.captured.pageName)

        val resultData = viewModel.mainNavLiveData.value
        assert(resultData?.dataList?.find { it is BuyAgainUiModel } != null)
    }
}
