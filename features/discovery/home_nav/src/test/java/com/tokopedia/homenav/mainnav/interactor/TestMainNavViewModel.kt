package com.tokopedia.homenav.mainnav.interactor

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.mainnav.domain.interactor.*
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.rule.CoroutinesTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

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

    @Test
    fun `test when viewmodel created and user has no shop then viewmodel create at least 9 user menu`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>(relaxed = true)
        val userMenuMockId = 99
        val defaultUserMenuCount = 9

        coEvery { getMainNavDataUseCase.executeOnBackground() }.answers { MainNavigationDataModel() }
        every { clientMenuGenerator.getMenu(anyInt()) }.answers { HomeNavMenuViewModel(id = userMenuMockId) }
        every { userSession.hasShop() }.answers { false }

        viewModel = createViewModel(
                getUserInfoUseCase = getUserInfoUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getShopInfoUseCase = getShopInfoUseCase,
                getMainNavDataUseCase = getMainNavDataUseCase,
                clientMenuGenerator = clientMenuGenerator,
                userSession = userSession
        )

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        visitableList.filter { it.id() == userMenuMockId }

        Assert.assertEquals(defaultUserMenuCount, visitableList.size)
    }

    @Test
    fun `test when viewmodel created and user has shop then viewmodel create at least 8 user menu`() {
        val clientMenuGenerator = mockk<ClientMenuGenerator>(relaxed = true)
        val userMenuMockId = 99
        val defaultUserMenuCount = 8

        coEvery { getMainNavDataUseCase.executeOnBackground() }.answers { MainNavigationDataModel() }
        every { clientMenuGenerator.getMenu(anyInt()) }.answers { HomeNavMenuViewModel(id = userMenuMockId) }
        every { userSession.hasShop() }.answers { true }

        viewModel = createViewModel(
                getUserInfoUseCase = getUserInfoUseCase,
                getWalletBalanceUseCase = getWalletBalanceUseCase,
                getUserMembershipUseCase = getUserMembershipUseCase,
                getShopInfoUseCase = getShopInfoUseCase,
                getMainNavDataUseCase = getMainNavDataUseCase,
                clientMenuGenerator = clientMenuGenerator,
                userSession = userSession
        )

        val visitableList = viewModel.mainNavLiveData.value?.dataList?: listOf()
        visitableList.filter { it.id() == userMenuMockId }

        Assert.assertEquals(defaultUserMenuCount, visitableList.size)
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