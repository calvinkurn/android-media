package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.domain.usecase.GetChatBlastSellerMetadataUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPostUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhome.R
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase

    @RelaxedMockK
    lateinit var getPostUseCase: GetPostUseCase

    @RelaxedMockK
    lateinit var getChatBlastSellerMetadataUseCase: GetChatBlastSellerMetadataUseCase

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
            context,
            userSession,
            getOnGoingPromotionUseCase,
            getPostUseCase,
            getChatBlastSellerMetadataUseCase,
            remoteConfig,
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
    fun `Success get layout data for post`() = runBlocking {
        val successResult = PostListUiModel(
                items = listOf(
                        PostUiModel(
                                title = "Test Post",
                                applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                url = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                subtitle = "<p>Info &#183; 20 SEP 19</p>"
                        ),
                        PostUiModel(
                                title = "Test ke 2",
                                applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                url = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                subtitle = "<p>Info &#183; 6 SEP 19</p>"
                        ),
                        PostUiModel(
                                title = "Kumpul Keluarga Tokopedia Bersama Toko Cabang",
                                applink = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                url = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                featuredMediaUrl = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/tokocabang-event-seller-center_1024x439/",
                                subtitle = "<p>Seller Event &#183; 5 MAR 20</p>"
                        )
                ),
                errorMessage = ""
        )

        coEvery {
            getPostUseCase.executeOnBackground()
        } returns successResult

        viewModel.getLayoutData(LayoutType.POST)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPostUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.POST)
        assert(result != null && result == Success(successResult))
    }

    @Test
    fun `Failed get layout data for posts`() = runBlocking {
        coEvery {
            getPostUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getLayoutData(LayoutType.POST)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getPostUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.POST)
        assert(result != null && result is Fail)
    }

    @Test
    fun `Success get layout data for promo creation with free broadcast chat quota`() = runBlocking {

        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(200, 2)

        every {
            remoteConfig.getBoolean(RemoteConfigKey.FREE_SHIPPING_FEATURE_DISABLED)
        } returns true

        every {
            context.getString(R.string.centralized_promo_broadcast_chat_extra_free_quota, any<Integer>())
        } returns String.format("%d kuota gratis", 200)

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[1].extra == "200 kuota gratis")
    }

    @Test
    fun `Success get layout data for promo creation with no broadcast chat quota`() = runBlocking {
        coEvery {
            getChatBlastSellerMetadataUseCase.executeOnBackground()
        } returns ChatBlastSellerMetadataUiModel(0, 0)

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success &&
                result.data is PromoCreationListUiModel &&
                (result.data as PromoCreationListUiModel).items[1].extra.isEmpty())
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