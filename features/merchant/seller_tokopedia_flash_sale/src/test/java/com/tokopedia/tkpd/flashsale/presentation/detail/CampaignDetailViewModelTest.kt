package com.tokopedia.tkpd.flashsale.presentation.detail

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.*
import com.tokopedia.tkpd.flashsale.util.tracker.CampaignDetailPageTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Rule
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
class CampaignDetailViewModelTest {

    @RelaxedMockK
    lateinit var getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase

    @RelaxedMockK
    lateinit var getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase

    @RelaxedMockK
    lateinit var getFlashSaleSellerStatusUseCase: GetFlashSaleSellerStatusUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase

    @RelaxedMockK
    lateinit var doFlashSaleSellerRegistrationUseCase: DoFlashSaleSellerRegistrationUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var sharedPreference: SharedPreferences

    @RelaxedMockK
    lateinit var tracker: CampaignDetailPageTracker

    @RelaxedMockK
    lateinit var campaignObserver: Observer<in Result<FlashSale>>

    private lateinit var viewModel: CampaignDetailViewModel

    private val dummyCampaignId = 10L

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignDetailViewModel(
            CoroutineTestDispatchersProvider,
            getFlashSaleDetailForSellerUseCase,
            getFlashSaleSubmittedProductListUseCase,
            getFlashSaleSellerStatusUseCase,
            doFlashSaleProductReserveUseCase,
            doFlashSaleProductDeleteUseCase,
            doFlashSaleSellerRegistrationUseCase,
            userSessionInterface,
            sharedPreference,
            tracker
        )
        with(viewModel) {
            campaign.observeForever(campaignObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            campaign.removeObserver(campaignObserver)
        }
    }

    @Test
    fun `when fetch campaign detail data success, rbac rule is active and user is eligible, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                val dummyCampaignDetailData = generateDummyCampaignDetailData()
                val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                val expectedResult = Success(generateDummyCampaignDetailData())

                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData

                getCampaignDetail(dummyCampaignId)

                val actualResult = campaign.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    private fun generateDummyCampaignDetailData(): FlashSale {
        val flashSaleStartDate = GregorianCalendar(2022, 10, 10, 0, 0, 0).time
        val flashSaleEndDate = GregorianCalendar(2022, 10, 20, 0, 0, 0).time
        val flashSaleReview = GregorianCalendar(2022, 10, 5, 0, 0, 0).time
        val flashSaleSubmission = GregorianCalendar(2022, 10, 1, 7, 0, 0).time

        return FlashSale(
            1,
            "",
            "",
            "",
            flashSaleEndDate,
            1,
            "Flash Sale 1",
            true,
            FlashSale.ProductMeta(5, 5, 10, 5, 5, 0, 0.0),
            1,
            flashSaleReview,
            flashSaleReview,
            "",
            flashSaleStartDate,
            1,
            "Pendaftaran berakhir",
            flashSaleSubmission,
            flashSaleSubmission,
            false,
            FlashSale.FormattedDate("", ""),
            FlashSaleStatus.UPCOMING,
            emptyList(),
            FlashSaleListPageTab.UPCOMING
        )
    }
}
