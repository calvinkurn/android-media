package com.tokopedia.homenav.mainnav.interactor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.domain.model.NotificationResolutionModel
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.domain.usecases.*
import com.tokopedia.homenav.mainnav.view.viewmodel.*
import com.tokopedia.homenav.rule.CoroutinesTestRule
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

    private val getUserInfoUseCase = mockk<GetUserInfoUseCase>(relaxed = true)
    private val getWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val getUserMembershipUseCase = mockk<GetUserMembershipUseCase>(relaxed = true)
    private val getShopInfoUseCase = mockk<GetShopInfoUseCase>(relaxed = true)
    private val getMainNavDataUseCase = mockk<GetMainNavDataUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    @ApplicationContext
    lateinit var context: Context

    private val defaultLoginState = AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
    private lateinit var viewModel : MainNavViewModel
    private val shopId = 1224

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
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
    fun `test when viewmodel created and user does not have shop then viewmodel create open shop ticker user menu`() {
        val testTickerTitle = "This is test ticker"
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val userSession = mockk<UserSession>()

        every { userSession.hasShop() } returns false
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
    fun `test when get complain notification then viewmodel update complain visitable with notification`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>()
        val getResolutionNotification = mockk<GetResolutionNotification>()

        val mockUnreadCount = 800

        every { clientMenuGenerator.getMenu(menuId = any(), notifCount = any(), sectionId = any()) }
                .answers { HomeNavMenuViewModel(id = firstArg(), notifCount = secondArg(), sectionId = thirdArg()) }
        every { clientMenuGenerator.getTicker(menuId = any()) }
                .answers { HomeNavTickerViewModel() }
        coEvery { getResolutionNotification.executeOnBackground() }.answers { NotificationResolutionModel(mockUnreadCount) }

        viewModel = createViewModel(
                clientMenuGenerator = clientMenuGenerator,
                getResolutionNotification = getResolutionNotification
        )

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        val complainVisitable = visitableList.find { it is HomeNavMenuViewModel && it.id() == ClientMenuGenerator.ID_COMPLAIN } as HomeNavMenuViewModel

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
    fun `test when viewmodel created and user only have ongoing order then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf()
        coEvery { getPaymentOrdersNavUseCase.executeOnBackground() } returns listOf(NavPaymentOrder())

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
        Assert.assertNotNull(transactionDataModel)
    }

    //transaction section
    @Test
    fun `test when viewmodel created and user only have payment transaction then create transaction menu item`() {
        val getUohOrdersNavUseCase = mockk<GetUohOrdersNavUseCase>()
        val getPaymentOrdersNavUseCase = mockk<GetPaymentOrdersNavUseCase>()

        coEvery { getUohOrdersNavUseCase.executeOnBackground() } returns listOf(NavProductOrder())
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
        Assert.assertNotNull(transactionDataModel)
    }
//    @Test
//    fun `test login state non login`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//            val loginState = AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN
//
//            viewModel.getProfileSection(loginState,shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).loginState == (AccountHeaderViewModel.LOGIN_STATE_NON_LOGIN))
//        }
//    }
//
//    @Test
//    fun `test login state login as`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//            val loginState = AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS
//
//            viewModel.getProfileSection(loginState,shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).loginState == (AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS))
//        }
//    }
//
//    @Test
//    fun `test login state login`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//            val loginState = AccountHeaderViewModel.LOGIN_STATE_LOGIN
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(loginState,shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).loginState == (AccountHeaderViewModel.LOGIN_STATE_LOGIN))
//        }
//    }
//
//    @Test
//    fun `Success getUserNameAndPictureData`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserInfoUseCase.executeOnBackground()
//            } returns UserPojo(ProfilePojo(name = "Joko", profilePicture = "Tingkir"))
//
//
//            viewModel.getUserNameAndPictureData(AccountHeaderViewModel.LOGIN_STATE_LOGIN, shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).userName.isNotEmpty()
//                    && (dataList.first() as AccountHeaderViewModel).userImage.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Error getUserNameAndPictureData missing name`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserInfoUseCase.executeOnBackground()
//            } returns UserPojo(ProfilePojo(name = "", profilePicture = "Tingkir"))
//
//
//            viewModel.getUserNameAndPictureData(AccountHeaderViewModel.LOGIN_STATE_LOGIN, shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).userName.isEmpty()
//                    && (dataList.first() as AccountHeaderViewModel).userImage.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Error getUserNameAndPictureData missing profile picture`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserInfoUseCase.executeOnBackground()
//            } returns UserPojo(ProfilePojo(name = "Joko", profilePicture = ""))
//
//
//            viewModel.getUserNameAndPictureData(AccountHeaderViewModel.LOGIN_STATE_LOGIN, shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).userName.isNotEmpty()
//                    && (dataList.first() as AccountHeaderViewModel).userImage.isEmpty())
//        }
//    }
//
//    @Test
//    fun `Error getUserNameAndPictureData missing all`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserInfoUseCase.executeOnBackground()
//            } returns UserPojo(ProfilePojo(name = "", profilePicture = ""))
//
//
//            viewModel.getUserNameAndPictureData(AccountHeaderViewModel.LOGIN_STATE_LOGIN, shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).userName.isEmpty()
//                    && (dataList.first() as AccountHeaderViewModel).userImage.isEmpty())
//        }
//    }
//
//    @Test
//    fun `Success getUserMembershipBadge`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserMembershipUseCase.executeOnBackground()
//            } returns MembershipPojo(TokopointsPojo(TokopointStatusPojo(TierPojo(eggImageURL = "telur"))))
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getUserBadgeImage()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).badge.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Error getUserMembershipBadge empty`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getUserMembershipUseCase.executeOnBackground()
//            } returns MembershipPojo(TokopointsPojo(TokopointStatusPojo(TierPojo(eggImageURL = ""))))
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getUserBadgeImage()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).badge.isEmpty())
//        }
//    }
//
//    @Test
//    fun `Success getShopName`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getShopInfoUseCase.executeOnBackground()
//            } returns ShopInfoPojo(ShopInfoPojo.ShopCore(name = "toko telor"))
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getShopData(shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).shopName.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Error getShopName empty name`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getShopInfoUseCase.executeOnBackground()
//            } returns ShopInfoPojo(ShopInfoPojo.ShopCore(name = ""))
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getShopData(shopId)
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).shopName.isEmpty())
//        }
//    }
//
//    @Test
//    fun `Success getOvoSaldo`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getWalletBalanceUseCase.executeOnBackground()
//            } returns WalletBalanceModel(cashBalance = "Rp 1234", pointBalance = "Rp 2345")
//
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getOvoData()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).ovoSaldo.isNotEmpty() &&
//                    (dataList.first() as AccountHeaderViewModel).ovoPoint.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Success getOvoSaldo empty saldo`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getWalletBalanceUseCase.executeOnBackground()
//            } returns WalletBalanceModel(cashBalance = "Rp 0", pointBalance = "Rp 2345")
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getOvoData()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).ovoSaldo.equals("Rp 0") &&
//                    (dataList.first() as AccountHeaderViewModel).ovoPoint.isNotEmpty())
//        }
//    }
//
//    @Test
//    fun `Success getOvoSaldo empty point`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getWalletBalanceUseCase.executeOnBackground()
//            } returns WalletBalanceModel(cashBalance = "Rp 1234", pointBalance = "Rp 0")
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getOvoData()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).ovoSaldo.isNotEmpty() &&
//                    (dataList.first() as AccountHeaderViewModel).ovoPoint.equals("Rp 0"))
//        }
//    }
//
//    @Test
//    fun `Success getOvoSaldo both empty`(){
//        rule.testDispatcher.runBlockingTest {
//            rule.testDispatcher.pauseDispatcher()
//
//            coEvery {
//                getWalletBalanceUseCase.executeOnBackground()
//            } returns WalletBalanceModel(cashBalance = "Rp 0", pointBalance = "Rp 0")
//            getUserInfoUseCase.getBasicData()
//
//            viewModel.getProfileSection(defaultLoginState, shopId)
//            viewModel.getOvoData()
//
//            rule.testDispatcher.resumeDispatcher()
//            val dataList = viewModel.mainNavLiveData.value?.dataList ?: mutableListOf()
//
//            Assert.assertTrue(dataList.isNotEmpty())
//            Assert.assertTrue((dataList.first() as AccountHeaderViewModel).ovoPoint.equals("Rp 0")&&
//                    (dataList.first() as AccountHeaderViewModel).ovoPoint.equals("Rp 0"))
//        }
//    }
}