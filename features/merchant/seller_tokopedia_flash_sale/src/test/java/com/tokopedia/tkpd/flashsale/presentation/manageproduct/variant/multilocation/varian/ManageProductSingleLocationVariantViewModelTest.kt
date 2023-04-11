package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.ManageProductVariantViewModel
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.MULTI_LOCATION
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.SINGLE_LOCATION
import com.tokopedia.tkpd.flashsale.util.tracker.ManageProductVariantTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ManageProductSingleLocationVariantViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase

    @RelaxedMockK
    lateinit var tracker: ManageProductVariantTracker

    private lateinit var viewModel: ManageProductVariantViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ManageProductVariantViewModel(
            CoroutineTestDispatchersProvider,
            ErrorMessageHelper(context),
            getFlashSaleProductCriteriaCheckingUseCase,
            tracker
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When calculation of percent to nominal 50 percent`() {
        val price = 5000L
        val percent = 50L
        val expected = "2500"
        val actual = viewModel.calculatePrice(percentInput = percent, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When calculation of percent to nominal 29 percent`() {
        val price = 5000L
        val percent = 29L
        val expected = "3550"
        val actual = viewModel.calculatePrice(percentInput = percent, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When calculation of nominal to percent 29 percent`() {
        val price = 5000L
        val discount = 3550L
        val expected = "29"
        val actual = viewModel.calculatePercent(priceInput = discount, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When validateInput return all good`() {
        val price = 5000L
        val discount = 3750L
        val expected = "25"
        val actual = viewModel.calculatePercent(priceInput = discount, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When sendManageAllClickEvent, Expect tracker sendClickManageAllEvent triggered`() {
        // given
        val campaignId = ""
        // when
        viewModel.sendManageAllClickEvent(campaignId)
        // then
        verify {
            tracker.sendClickManageAllEvent("$campaignId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendAdjustToggleVariantEvent, Expect tracker sendAdjustToggleVariantEvent triggered`() {
        // given
        val campaignId = ""
        // when
        viewModel.sendAdjustToggleVariantEvent(campaignId)
        // then
        verify {
            tracker.sendAdjustToggleVariantEvent("$campaignId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendFillInColumnPriceEvent, Expect tracker sendClickFillInCampaignPriceEvent triggered`() {
        // given
        val campaignId = ""
        // when
        viewModel.sendFillInColumnPriceEvent(campaignId)
        // then
        verify {
            tracker.sendClickFillInCampaignPriceEvent("$campaignId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendFillInDiscountPercentageEvent, Expect tracker sendClickFillInDiscountPercentageEvent triggered`() {
        // given
        val campaignId = ""
        // when
        viewModel.sendFillInDiscountPercentageEvent(campaignId)
        // then
        verify {
            tracker.sendClickFillInDiscountPercentageEvent("$campaignId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendSaveClickEvent, Expect tracker sendClickSaveEvent triggered`() {
        // given
        val campaignId = ""
        // when
        viewModel.sendSaveClickEvent(campaignId)
        // then
        verify {
            tracker.sendClickSaveEvent("$campaignId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendCheckDetailClickEvent, Expect tracker sendClickCheckDetailEvent triggered`() {
        // given
        val campaignId = ""
        val productId = 123123L
        // when
        viewModel.sendCheckDetailClickEvent(campaignId, productId)
        // then
        verify {
            tracker.sendClickCheckDetailEvent("$campaignId - $productId - $SINGLE_LOCATION")
        }
    }

    @Test
    fun `When sendManageAllLocationClickEvent, Expect tracker sendClickManageAllLocationEvent triggered`() {
        // given
        val campaignId = ""
        val productId = 123123L
        // when
        viewModel.sendManageAllLocationClickEvent(campaignId, productId)
        // then
        verify {
            tracker.sendClickManageAllLocationEvent("$campaignId - $productId - $MULTI_LOCATION")
        }
    }

}
