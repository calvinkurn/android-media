package com.tokopedia.home.account.revamp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.home.account.revamp.domain.usecase.GetBuyerAccountDataUseCase
import com.tokopedia.home.account.revamp.domain.usecase.GetShortcutDataUseCase
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminDetailInformation
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.usecase.AccountAdminInfoUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class BuyerAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase = mockk(relaxed = true)
    private val checkAffiliateUseCase: CheckAffiliateUseCase = mockk(relaxed = true)
    private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    private val getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true)
    private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk(relaxed = true)
    private val getShortcutDataUseCase: GetShortcutDataUseCase = mockk(relaxed = true)
    private val accountAdminInfoUseCase: AccountAdminInfoUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val walletPref: WalletPref = mockk(relaxed = true)

    private lateinit var viewModel: BuyerAccountViewModel
    private val dispatcherProvider = CoroutineTestDispatchersProvider

    @Before
    fun setUp() {
        viewModel = BuyerAccountViewModel(
                getBuyerAccountDataUseCase,
                checkAffiliateUseCase,
                getBuyerWalletBalanceUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                getRecommendationUseCase,
                topAdsWishlishedUseCase,
                getShortcutDataUseCase,
                accountAdminInfoUseCase,
                userSession,
                walletPref,
                dispatcherProvider
        )
    }

    @Test
    fun `it successfully get buyer account data`() = runBlockingTest {
        val expectedReturn = Success(AccountDataModel())
        val expectedWallet = WalletModel()
        val shortcutResponse = ShortcutResponse()
        val accountAdminInfoResponse: Pair<AdminDataResponse?, ShopData?> = Pair(null, null)

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            expectedReturn.data
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedReturn.data.wallet = expectedWallet
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedReturn.data.isAffiliate = true
            true
        }

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } answers {
            expectedReturn.data.shortcutResponse = shortcutResponse
            shortcutResponse
        }

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            expectedReturn.data.adminTypeText = accountAdminInfoResponse.first?.data?.adminTypeText
            accountAdminInfoResponse
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to get buyer account data` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val expectedWallet = WalletModel()
        val shortcutResponse = ShortcutResponse()
        val accountAdminInfoResponse = Pair(null, null)

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } throws expectedReturn.throwable

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } answers {
            shortcutResponse
        }

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            accountAdminInfoResponse
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to get buyer wallet` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val shortcutResponse = ShortcutResponse()
        val accountAdminInfoResponse = Pair(null, null)

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountDataModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } throws expectedReturn.throwable

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } answers {
            shortcutResponse
        }

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            accountAdminInfoResponse
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to check is affiliate` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val expectedWallet = WalletModel()
        val shortcutResponse = ShortcutResponse()
        val accountAdminInfoResponse = Pair(null, null)

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountDataModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } throws expectedReturn.throwable

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } answers {
            shortcutResponse
        }

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            accountAdminInfoResponse
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `it failed to get shortcut` () = runBlockingTest {
        val expectedReturn = Fail(Throwable("Oops"))
        val expectedWallet = WalletModel()
        val accountAdminInfoResponse = Pair(null, null)

        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountDataModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            expectedWallet
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } throws expectedReturn.throwable

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            accountAdminInfoResponse
        }

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.buyerAccountDataData.value).isEqualTo(expectedReturn)
    }

    @Test
    fun `success get account admin info will update account data to success`() = runBlockingTest {
        val adminTypeText = "Shop Admin"
        val isLocationAdmin = true
        val adminDataResponse = AdminDataResponse(
                data = AdminData(
                        adminTypeText = adminTypeText,
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(
                                        isLocationAdmin = isLocationAdmin
                                )
                        ),
                        status = "1"
                )
        )

        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(adminDataResponse, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        Assertions.assertThat(viewModel.canGoToSellerAccount.value).isEqualTo(!isLocationAdmin)
        assert((viewModel.buyerAccountDataData.value as? Success)?.data?.adminTypeText?.equals(adminTypeText) == true)
    }

    @Test
    fun `fail get account admin info will update account data to fail but wont update can go to seller account data`() = runBlockingTest {
        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } throws MessageErrorException("")

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        assert(viewModel.buyerAccountDataData.value is Fail)
        assert(viewModel.canGoToSellerAccount.value == null)
    }

    @Test
    fun `success get not null account admin data will refresh user session admin related data`() = runBlockingTest {
        val isShopOwner = false
        val isShopAdmin = true
        val isLocationAdmin = true
        val isMultiLocationShop = true
        val adminDataResponse = AdminDataResponse(
                isMultiLocationShop = isMultiLocationShop,
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(isShopAdmin, isLocationAdmin, isShopOwner)
                        ),
                        status = "1"
                )
        )

        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(adminDataResponse, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify {
            userSession.setIsShopOwner(isShopOwner)
            userSession.setIsLocationAdmin(isLocationAdmin)
            userSession.setIsShopAdmin(isShopAdmin)
            userSession.setIsMultiLocationShop(isMultiLocationShop)
        }
    }

    @Test
    fun `success get not null account admin data will remove unnecessary shop data if is location admin`() = runBlockingTest {
        val isLocationAdmin = true
        val adminDataResponse = AdminDataResponse(
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(
                                        isLocationAdmin = isLocationAdmin
                                )
                        ),
                        status = "1"
                )
        )

        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(adminDataResponse, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify {
            userSession.shopAvatar = ""
            userSession.shopId = "0"
            userSession.shopName = ""
            userSession.setIsGoldMerchant(false)
            userSession.setIsShopOfficialStore(false)
        }
    }

    @Test
    fun `success get not null account admin data with inactive shop will set shopId to empty`() = runBlockingTest {
        val isShopOwner = false
        val isShopAdmin = true
        val isLocationAdmin = true
        val isMultiLocationShop = true
        val adminDataResponse = AdminDataResponse(
                isMultiLocationShop = isMultiLocationShop,
                data = AdminData(
                        detail = AdminDetailInformation(
                                roleType = AdminRoleType(isShopAdmin, isLocationAdmin, isShopOwner)
                        ),
                        status = "0"
                )
        )

        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(adminDataResponse, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify {
            userSession.shopName = ""
        }
    }

    @Test
    fun `success get null account admin data will not update any related user session value`() = runBlockingTest {
        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(null, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify(exactly = 0) {
            userSession.setIsShopOwner(anyBoolean())
            userSession.setIsLocationAdmin(anyBoolean())
            userSession.setIsShopAdmin(anyBoolean())
            userSession.setIsMultiLocationShop(anyBoolean())
            userSession.shopAvatar = anyString()
            userSession.shopId = anyString()
            userSession.shopName = anyString()
            userSession.setIsGoldMerchant(anyBoolean())
            userSession.setIsShopOfficialStore(anyBoolean())
        }
    }

    @Test
    fun `success get non null shop data will refresh user session values`() = runBlockingTest {
        val shopID = "0"
        val domain = "terisugi"
        val name = "Tumbler Starbucks 123"
        val logo = "www.weeew.com"
        val level = 0
        val shopData = ShopData(shopID, domain, name, logo, level)

        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(null, shopData)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify {
            userSession.shopId = ""
            userSession.shopName = name
            userSession.shopAvatar = logo
            userSession.setIsGoldMerchant(anyBoolean())
            userSession.setIsShopOfficialStore(anyBoolean())
        }
    }

    @Test
    fun `success get null shop data will not refresh user session values`() = runBlockingTest {
        setDefaultEveryReturnForNonAdminRelatedData()

        coEvery {
            accountAdminInfoUseCase.executeOnBackground()
        } answers {
            Pair(null, null)
        }

        getUserSessionIsShopOwner_thenReturn(false)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify(exactly = 0) {
            userSession.shopId = anyString()
            userSession.shopName = anyString()
            userSession.shopAvatar = anyString()
            userSession.setIsGoldMerchant(anyBoolean())
            userSession.setIsShopOfficialStore(anyBoolean())
        }
    }

    @Test
    fun `shop owner will not need to get admin info`() = runBlockingTest {
        setDefaultEveryReturnForNonAdminRelatedData()
        getUserSessionIsShopOwner_thenReturn(true)
        getUserSessionIsLocationAdmin_thenReturn(anyBoolean())

        viewModel.getBuyerData()

        coVerify(exactly = 0) {
            accountAdminInfoUseCase.executeOnBackground()
            userSession.setIsShopOwner(anyBoolean())
            userSession.setIsLocationAdmin(anyBoolean())
            userSession.setIsShopAdmin(anyBoolean())
            userSession.setIsMultiLocationShop(anyBoolean())
            userSession.shopAvatar = anyString()
            userSession.shopId = anyString()
            userSession.shopName = anyString()
            userSession.setIsGoldMerchant(anyBoolean())
            userSession.setIsShopOfficialStore(anyBoolean())
        }
    }

    private fun setDefaultEveryReturnForNonAdminRelatedData() {
        coEvery {
            getBuyerAccountDataUseCase.executeOnBackground()
        } answers {
            AccountDataModel()
        }

        coEvery {
            getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            WalletModel()
        }

        coEvery {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        } answers {
            true
        }

        coEvery {
            getShortcutDataUseCase.executeOnBackground()
        } answers {
            ShortcutResponse()
        }
    }

    private fun getUserSessionIsShopOwner_thenReturn(isShopOwner: Boolean) {
        coEvery {
            userSession.isShopOwner
        } returns isShopOwner
    }

    private infix fun getUserSessionIsLocationAdmin_thenReturn(isLocationAdmin: Boolean) {
        coEvery {
            userSession.isLocationAdmin
        } returns isLocationAdmin
    }
}