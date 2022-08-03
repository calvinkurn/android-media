package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.common.util.CentralizedPromoResourceProvider
import com.tokopedia.centralizedpromo.domain.usecase.CheckNonTopAdsUserUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.SellerHomeGetWhiteListedUserUseCase
import com.tokopedia.centralizedpromo.domain.usecase.SlashPriceEligibleUseCase
import com.tokopedia.centralizedpromo.domain.usecase.VoucherCashbackEligibleUseCase
import com.tokopedia.centralizedpromo.view.FirstPromoDataSource
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@ExperimentalCoroutinesApi
class CentralizedPromoViewModelTest {

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var resourcesProvider: CentralizedPromoResourceProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase

    @RelaxedMockK
    lateinit var getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase

    @RelaxedMockK
    lateinit var voucherCashbackEligibleUseCase: VoucherCashbackEligibleUseCase

    @RelaxedMockK
    lateinit var slashPriceEligibleUseCase: SlashPriceEligibleUseCase

    @RelaxedMockK
    lateinit var sharedPref: SharedPreferences

    @RelaxedMockK
    lateinit var checkNonTopAdsUserUseCase: CheckNonTopAdsUserUseCase

    @RelaxedMockK
    lateinit var sellerHomeGetWhiteListedUserUseCase: SellerHomeGetWhiteListedUserUseCase

    @RelaxedMockK
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(PromoCreationStaticData)
        mockkObject(CentralizedPromoTracking)

        CentralizedPromoViewModel::class.declaredMemberProperties.filter { it.name in arrayOf("startDate", "endDate") }.forEach {
            it.isAccessible = true
            it.get(viewModel)
        }

        CentralizedPromoViewModel::class.declaredMemberProperties.find { it.name == "shopId" }.let {
            it?.isAccessible = true
            it?.get(viewModel)
        }

        every {
            context.getString(R.string.centralized_promo_promo_creation_topads_title)
        } returns "TopAds"

        every {
            context.getString(R.string.centralized_promo_promo_creation_topads_description)
        } returns "Iklankan produkmu untuk menjangkau lebih banyak pembeli"

        every {
            context.getString(R.string.centralized_promo_promo_creation_broadcast_chat_title)
        } returns "Broadcast Chat"

        every {
            context.getString(R.string.centralized_promo_promo_creation_broadcast_chat_description)
        } returns "Tingkatkan penjualan dengan kirim pesan promosi ke pembeli"
    }

    private val viewModel : CentralizedPromoViewModel by lazy {
        CentralizedPromoViewModel(
            resourcesProvider,
            userSession,
            getOnGoingPromotionUseCase,
            getChatBlastSellerMetadataUseCase,
            voucherCashbackEligibleUseCase,
            slashPriceEligibleUseCase,
            checkNonTopAdsUserUseCase,
            sellerHomeGetWhiteListedUserUseCase,
            remoteConfig,
            sharedPref,
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `Success get layout data for on going promotion`() = runBlocking {
        val successResult = OnGoingPromoListUiModel(
                title = "Track your promotion",
                items = listOf(
                        OnGoingPromoUiModel(
                                title = "Flash Sale",
                                status = Status(
                                        text = "Terdaftar",
                                        count = 56,
                                        url = "sellerapp://flashsale/management"
                                ),
                                footer = Footer(
                                        text = "Lihat Semua",
                                        url = "sellerapp://flashsale/management"
                                )
                        )
                ),
                errorMessage = ""
        )

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns successResult

        viewModel.getLayoutData(LayoutType.ON_GOING_PROMO)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
        assert(result != null && result == Success(successResult))
    }

    @Test
    fun `Failed get layout data for on going promotion`() = runBlocking {
        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getLayoutData(LayoutType.ON_GOING_PROMO)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
        assert(result != null && result is Fail)
    }

    @Test
    fun `Success get layout data for promo creation with free broadcast chat quota`() = runBlocking {

        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(200, 2)

        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns false

        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns false

        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns true

        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
        } returns true

        every {
            resourcesProvider.composeBroadcastChatFreeQuotaLabel(any())
        } returns String.format("%d kuota gratis", 200)

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[5].extra == "200 kuota gratis")
    }

    @Test
    fun `Success get layout data for promo creation with no broadcast chat quota`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(200, 0)

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false

        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true

        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true

        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns true

        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[2].extra.isEmpty())
    }

    @Test
    fun `Success get layout data for promo creation with no broadcast chat quota and undefined promo type`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false

        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true

        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true

        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns true

        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[2].extra.isEmpty())
    }

    @Test
    fun `When isNonTopAdsUser false, topads applink should go straight to the dashboard`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false

        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true

        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true

        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false

        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
        } returns true

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify(exactly = 0) {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[4].applink == ApplinkConst.CustomerApp.TOPADS_DASHBOARD
        )
    }

    @Test
    fun `When voucher cashback sharedPref returns true but eligible, should not return create voucher applink`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_MVC_FIRST_TIME, true)
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_TOKOPEDIA_PLAY_FIRST_TIME, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        val expectedApplink = "tokopedia-android-internal://sellerapp/create-voucher"
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.get(3)?.applink != expectedApplink)
    }

    @Test
    fun `When voucher cashback sharedPref returns false but eligible, should return create voucher applink`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_MVC_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(
                    RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        val expectedApplink = "tokopedia-android-internal://sellerapp/create-voucher"
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.get(6)?.applink == expectedApplink)
    }

    @Test
    fun `When voucher product sharedPref returns true, should not return create voucher product applink`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        val expectedApplink = "sellerapp://create-voucher-product"
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.get(5)?.applink != expectedApplink)
    }

    @Test
    fun `When voucher product sharedPref returns false, should return create voucher product applink`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        val expectedApplink = "sellerapp://create-voucher-product"
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.get(8)?.applink == expectedApplink)
    }

    @Test
    fun `Success get layout data with remote config flash sale false should hide flash sale`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false

        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true

        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true

        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false

        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_FLASH_SALE_ENTRY_SELLER, true)
        } returns false

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.count {
            it.applink == "sellerapp://shop-flash-sale"
        } == 0)
    }

    @Test
    fun `When voucher product is disabled, should not show the entry point`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.size?.equals(9) == false)
    }

    @Test
    fun `When voucher product isEnabled key throw exception, should not show the entry point`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } throws MessageErrorException()
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns true

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.size?.equals(9) == false)
    }

    @Test
    fun `When slash price is disabled, should not show the entry point`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } returns false

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.size?.equals(9) == false)
    }

    @Test
    fun `When slash price isEnabled key throw exception, should not show the entry point`() = runBlocking {
        // Given
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 1)
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED, true)
        } returns false
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } returns true
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } returns true
        coEvery {
            checkNonTopAdsUserUseCase.execute(any())
        } returns false
        coEvery {
            sellerHomeGetWhiteListedUserUseCase.executeQuery()
        } returns true
        coEvery {
            sharedPref.getBoolean(FirstPromoDataSource.IS_PRODUCT_COUPON_FIRST_TIME, true)
        } returns false
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MVC_PRODUCT, true)
        } returns true
        coEvery {
            remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLASH_PRICE, true)
        } throws MessageErrorException()

        // When
        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        // Then
        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(((result as? Success)?.data as? PromoCreationListUiModel)?.items?.size?.equals(9) == false)
    }


    @Test
    fun `Failed get layout data for promo creation`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Fail)
    }

    @Test
    fun `When get voucher cashback eligiblity fail, get layout data should also fails`() = runBlocking {
        coEvery {
            voucherCashbackEligibleUseCase.execute(any())
        } throws MessageErrorException()

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result is Fail)
    }

    @Test
    fun `When get slash price eligiblity fail, get layout data should also fails`() = runBlocking {
        coEvery {
            slashPriceEligibleUseCase.execute(any())
        } throws MessageErrorException()

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result is Fail)
    }

    @Test
    fun trackFreeShippingImpressionTest() {
        every {
            CentralizedPromoTracking.sendImpressionFreeShipping(userSession, any())
        } just runs

        viewModel.trackFreeShippingImpression()

        verify {
            CentralizedPromoTracking.sendImpressionFreeShipping(userSession, any())
        }
    }

    @Test
    fun trackFreeShippingClickTest() {
        every {
            CentralizedPromoTracking.sendClickFreeShipping(userSession, any())
        } just runs

        viewModel.trackFreeShippingClick()

        verify {
            CentralizedPromoTracking.sendClickFreeShipping(userSession, any())
        }
    }
}