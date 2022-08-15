package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.domain.model.FilterTab
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListData
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListPage
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.model.*
import com.tokopedia.network.exception.MessageErrorException
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
    lateinit var getPromotionUseCase: GetPromotionUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(CentralizedPromoTracking)

        CentralizedPromoViewModel::class.declaredMemberProperties.filter {
            it.name in arrayOf(
                "startDate",
                "endDate"
            )
        }.forEach {
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

    private val viewModel: CentralizedPromoViewModel by lazy {
        CentralizedPromoViewModel(
            userSession,
            getOnGoingPromotionUseCase,
            getPromotionUseCase,
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

        viewModel.getLayoutData(LayoutType.ON_GOING_PROMO, tabId = "0")

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

        viewModel.getLayoutData(LayoutType.ON_GOING_PROMO, tabId = "0")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
        assert(result != null && result is Fail)
    }

    @Test
    fun `Success get layout data for promotion list`() = runBlocking {

        val successResult = MerchantPromotionGetPromoList(
            data = MerchantPromotionGetPromoListData(
                filterTab = listOf(
                    FilterTab(
                        id = "6",
                        name = "Tingkatkan kunjungan pembeli"
                    ),
                    FilterTab(
                        id = "7",
                        name = "Tambah Jumlah Pesanan Baru"
                    )
                ),
                pages = listOf(
                    MerchantPromotionGetPromoListPage(
                        pageId = "63",
                        pageName = "Kupon Produk",
                        pageNameSuffix = "(Baru)",
                        iconImage = "https://images.tokopedia.net/img/promo-page/icon-kupon-product.png",
                        notAvailableText = "",
                        headerText = "Atur diskon khusus ke produk produk tertentu agar semakin cepat laku.",
                        bannerImage = "https://images.tokopedia.net/img/promo-page/preview-kupon-product.png",
                        bottomText = "Mulai buat kupon untuk produk pilihanmu.\"",
                        ctaText = "Buat Kupon",
                        ctaLink = "https://seller.tokopedia.com/v2/vouchertoko/locked-product/create",
                        isEligible = 1,
                        infoText = ""
                    )
                )
            )
        )
        coEvery {
            getPromotionUseCase.execute(any())
        } returns successResult

        viewModel.getLayoutData(LayoutType.PROMO_CREATION, tabId = "0")

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)

        assert(result is Success)
    }

    @Test
    fun `Failed get layout data for promotion list`() = runBlocking {
        coEvery {
            getPromotionUseCase.execute("")
        } throws MessageErrorException("")

        viewModel.getLayoutData(LayoutType.PROMO_CREATION, tabId = "0")

        val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
        assert(result != null && result is Fail)
    }
}