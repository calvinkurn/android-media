package com.tokopedia.centralizedpromo.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPostUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.*
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@ExperimentalCoroutinesApi
class CentralizedPromoViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase

    @RelaxedMockK
    lateinit var getPostUseCase: GetPostUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(PromoCreationStaticData)

        CentralizedPromoViewModel::class.declaredMemberProperties.filter { it.name in arrayOf("startDate", "endDate") }.forEach {
            it.isAccessible = true
            it.get(viewModel)
        }

        CentralizedPromoViewModel::class.declaredMemberProperties.find { it.name == "shopId" }.let {
            it?.isAccessible = true
            it?.get(viewModel)
        }
    }

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel : CentralizedPromoViewModel by lazy {
        CentralizedPromoViewModel(userSession, getOnGoingPromotionUseCase, getPostUseCase, testCoroutineDispatcher)
    }

    @Test
    fun `Success get layout data for on going promotion`() {
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

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        delay(100)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
        assert(result != null && result is Fail)
    }

    @Test
    fun `Success get layout data for post`() {
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

        coVerify {
            getPostUseCase.executeOnBackground()
        }

        delay(100)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.POST)
        assert(result != null && result is Fail)
    }

    @Test
    fun `Success get layout data for promo creation`() = runBlocking {

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        delay(100)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Success)
    }

    @Test
    fun `Failed get layout data for promo creation`() = runBlocking {

        every {
            PromoCreationStaticData.provideStaticData()
        } throws ResponseErrorException()

        viewModel.getLayoutData(LayoutType.PROMO_CREATION)

        delay(100)

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result != null && result is Fail)

    }
}