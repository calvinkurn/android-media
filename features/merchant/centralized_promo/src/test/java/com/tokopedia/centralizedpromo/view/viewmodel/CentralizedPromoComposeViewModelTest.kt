@file:OptIn(ExperimentalCoroutinesApi::class)

package com.tokopedia.centralizedpromo.view.viewmodel

import android.content.SharedPreferences
import com.tokopedia.centralizedpromo.MainCoroutineRule
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.PromoPlayAuthorConfigUseCase
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Empty
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Fail
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Loading
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoResult.Success
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoUiState
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.model.FilterTab
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoListData
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoListPage
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CentralizedPromoComposeViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase

    @RelaxedMockK
    lateinit var getPromotionUseCase: GetPromotionUseCase

    @RelaxedMockK
    lateinit var promoPlayAuthorConfigUseCase: PromoPlayAuthorConfigUseCase

    @RelaxedMockK
    lateinit var pref: SharedPreferences

    private val viewModel: CentralizedPromoComposeViewModel by lazy {
        CentralizedPromoComposeViewModel(
            userSession,
            getOnGoingPromotionUseCase,
            getPromotionUseCase,
            promoPlayAuthorConfigUseCase,
            pref,
            CoroutineTestDispatchersProvider
        )
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val mockPromoCreation = MerchantPromotionGetPromoList(
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

    private val mockPromoCreationAfterFilter = MerchantPromotionGetPromoList(
        data = MerchantPromotionGetPromoListData(
            filterTab = listOf(
                FilterTab(
                    id = "6",
                    name = "After Filter"
                )
            ),
            pages = listOf(
                MerchantPromotionGetPromoListPage(
                    pageId = "After Filter",
                    pageName = "After Filter",
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
                ),
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

    private val mockOnGoingData = OnGoingPromoListUiModel(
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

    private val mockOnGoingDataAfterFilter = OnGoingPromoListUiModel(
        title = "After Filter",
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(true, viewModel.layoutList.value.promoCreationData is Loading)
        assertEquals(true, viewModel.layoutList.value.onGoingData is Loading)
    }

    @Test
    fun `success get layout data for initial state`() = runTest {
        every {
            userSession.shopId
        } returns "123"

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingData

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns mockPromoCreation

        coEvery {
            promoPlayAuthorConfigUseCase.execute(any())
        } returns true

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.collect()
        }

        `ensure toaster value not emit`()

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        coVerify {
            promoPlayAuthorConfigUseCase.execute(any())
        }

        val data = viewModel.layoutList.value

        val promoCreationData =
            ((data.promoCreationData as Success).data as PromoCreationListUiModel)
        assertTrue(promoCreationData.filterItems.size == 3)
        assertTrue(promoCreationData.items.size == 1)
        assertTrue(promoCreationData.errorMessage == "")

        val onGoingData =
            ((data.onGoingData as Success).data as OnGoingPromoListUiModel)
        assertTrue(onGoingData.title == "Track your promotion")
        assertTrue(onGoingData.items.size == 1)
        assertTrue(onGoingData.errorMessage == "")

        job.cancel()
    }

    @Test
    fun `success get layout data for initial state but returning empty`() = runTest {
        every {
            userSession.shopId
        } returns "123"

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns OnGoingPromoListUiModel(
            title = "",
            items = listOf(),
            errorMessage = ""
        )

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns MerchantPromotionGetPromoList()

        coEvery {
            promoPlayAuthorConfigUseCase.execute(any())
        } returns true

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.collect()
        }

        `ensure toaster value not emit`()

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        coVerify {
            promoPlayAuthorConfigUseCase.execute(any())
        }

        val data = viewModel.layoutList.value

        assertTrue(data.promoCreationData is Empty)
        assertTrue(data.onGoingData is Empty)

        job.cancel()
    }

    @Test
    fun `fail get layout data for initial state`() = runTest {
        every {
            userSession.shopId
        } returns "123"

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } throws MessageErrorException("asd")

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } throws MessageErrorException("asd")

        coEvery {
            promoPlayAuthorConfigUseCase.execute(any())
        } returns true

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.collect()
        }

        `ensure toaster value emit`()

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        coVerify {
            promoPlayAuthorConfigUseCase.execute(any())
        }

        val data = viewModel.layoutList.value
        assertTrue(data.promoCreationData is Fail)
        assertTrue(data.onGoingData is Fail)

        job.cancel()
    }

    @Test
    fun `success get promo creation and on going promo one by one after all error`() = runTest {
        `fail get layout data for initial state`()
        clearMocks(getPromotionUseCase)
        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns mockPromoCreationAfterFilter

        clearMocks(getOnGoingPromotionUseCase)

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingDataAfterFilter

        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }

        viewModel.sendEvent(
            CentralizedPromoEvent.LoadPromoCreation
        )

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify(inverse = true) {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        /**
         * First attempt the promocreation is fail but loading true, then
         * ongoing data is only fail, because we only click promo creation local load
         */
        val firstFlow = testResults[1]
        assertEquals((firstFlow.promoCreationData as Fail).isLoading, true)
        assertEquals((firstFlow.onGoingData as Fail).isLoading, false)

        val lastFlow = testResults.last()
        assertEquals(lastFlow.promoCreationData is Success, true)
        assertEquals((lastFlow.onGoingData as Fail).isLoading, false)

        testResults.clear()
        /**
         * Load ongoing promo while promo creation already success
         */
        viewModel.sendEvent(
            CentralizedPromoEvent.LoadOnGoingPromo
        )

        advanceUntilIdle()

        val firstFlowOngoing = testResults[0]
        assertEquals(firstFlowOngoing.promoCreationData is Success, true)
        assertEquals((firstFlowOngoing.onGoingData as Fail).isLoading, true)

        val lastFlowOngoing = testResults.last()
        assertEquals(lastFlowOngoing.promoCreationData is Success, true)
        assertEquals(lastFlowOngoing.onGoingData is Success, true)

        job.cancel()
    }

    @Test
    fun `Failed casting Fail when all error and trying to refresh each one`() = runTest {
        `success get layout data for initial state`()
        clearMocks(getPromotionUseCase)
        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns mockPromoCreationAfterFilter

        clearMocks(getOnGoingPromotionUseCase)

        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingDataAfterFilter

        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }

        viewModel.sendEvent(
            CentralizedPromoEvent.LoadPromoCreation
        )

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify(inverse = true) {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        /**
         * It simulate when data success but somehow user trying to click load more
         */
        val firstFlow = testResults[1]
        assertEquals(firstFlow.promoCreationData is Success, true)
        assertEquals(firstFlow.onGoingData is Success, true)

        val lastFlow = testResults.last()
        assertEquals(lastFlow.promoCreationData is Success, true)
        assertEquals(lastFlow.onGoingData is Success, true)

        testResults.clear()

        /**
         * It simulate when data success but somehow user trying to click load more
         */
        viewModel.sendEvent(
            CentralizedPromoEvent.LoadOnGoingPromo
        )

        advanceUntilIdle()

        val lastFlowOngoing = testResults.last()
        assertEquals(lastFlowOngoing.promoCreationData is Success, true)
        assertEquals(lastFlowOngoing.onGoingData is Success, true)

        job.cancel()
    }

    @Test
    fun `success get promo creation after filter clicked`() = runTest {
        `success get layout data for initial state`()

        clearMocks(getPromotionUseCase)

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns mockPromoCreationAfterFilter

        // we re-mock this in purpose of ensure this ongoing not change when filter change, we only want to change promo creation
        clearMocks(getOnGoingPromotionUseCase)
        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingDataAfterFilter

        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }
        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi" to "1"
            )
        )

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify(inverse = true) {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        coVerify {
            promoPlayAuthorConfigUseCase.execute(any())
        }

        // size 3
        val secondFlow = testResults[1]
        assertEquals(secondFlow.promoCreationData is Loading, true)
        assertEquals(secondFlow.onGoingData !is Loading, true)

        // after filter and get data success

        val thirdFlow = testResults[2]
        val promoCreationData =
            ((thirdFlow.promoCreationData as Success).data as PromoCreationListUiModel)
        assertEquals(secondFlow.onGoingData !is Loading, true)
        assertEquals(promoCreationData.filterItems.size, 2)

        // must change to the new one
        assertEquals(promoCreationData.filterItems[1].name, "After Filter")
        assertEquals(promoCreationData.items.size, 2)
        assertEquals(promoCreationData.items.first().pageId, "After Filter")

        val onGoingData =
            ((thirdFlow.onGoingData as Success).data as OnGoingPromoListUiModel)
        // ensure its not change
        assertTrue(onGoingData.title == "Track your promotion")
        assertTrue(onGoingData.items.size == 1)
        assertTrue(onGoingData.errorMessage == "")

        assertEquals(thirdFlow.selectedTabFilterData.first, "Harga Tertinggi")
        assertEquals(thirdFlow.selectedTabFilterData.second, "1")
        job.cancel()
    }

    @Test
    fun `fail get promo creation after filter clicked`() = runTest {
        `success get layout data for initial state`()

        clearMocks(getPromotionUseCase)

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } throws MessageErrorException("asd")

        // we re-mock this in purpose of ensure this ongoing not change when filter change, we only want to change promo creation
        clearMocks(getOnGoingPromotionUseCase)
        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingDataAfterFilter

        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }
        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi" to "1"
            )
        )

        advanceUntilIdle()

        coVerify {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        }

        coVerify(inverse = true) {
            getOnGoingPromotionUseCase.executeOnBackground()
        }

        coVerify {
            promoPlayAuthorConfigUseCase.execute(any())
        }

        // size 3
        val secondFlow = testResults[1]
        assertEquals(secondFlow.promoCreationData is Loading, true)
        assertEquals(secondFlow.onGoingData !is Loading, true)

        // after filter and get data success

        val thirdFlow = testResults[2]
        assertEquals(secondFlow.onGoingData !is Loading, true)
        assertTrue(thirdFlow.promoCreationData is Fail)

        val onGoingData =
            ((thirdFlow.onGoingData as Success).data as OnGoingPromoListUiModel)
        // ensure its not change
        assertTrue(onGoingData.title == "Track your promotion")
        assertTrue(onGoingData.items.size == 1)
        assertTrue(onGoingData.errorMessage == "")

        assertEquals(thirdFlow.selectedTabFilterData.first, "Harga Tertinggi")
        assertEquals(thirdFlow.selectedTabFilterData.second, "1")
        job.cancel()
    }

    @Test
    fun `assert when filter has the same value with current selected filter`() = runTest {
        `success get layout data for initial state`()
        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }

        // mimic when user select the same filter
        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi" to "1"
            )
        )

        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi" to "1"
            )
        )

        advanceUntilIdle()

        val lastData = testResults.last()
        // ensure data only 3, so last call will not be called
        assertEquals(testResults.size, 3)
        assertEquals(lastData.onGoingData is Success, true)
        assertEquals(lastData.promoCreationData is Success, true)

        // ensure selected filter not change
        assertEquals(lastData.selectedTabFilterData.first, "Harga Tertinggi")
        assertEquals(lastData.selectedTabFilterData.second, "1")

        job.cancel()
    }

    @Test
    fun `assert when filter has different value with current selected filter`() = runTest {
        `success get layout data for initial state`()
        val testResults = mutableListOf<CentralizedPromoUiState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }

        // mimic when user select the same filter
        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi" to "1"
            )
        )

        viewModel.sendEvent(
            CentralizedPromoEvent.FilterUpdate(
                "Harga Tertinggi last" to "5"
            )
        )

        advanceUntilIdle()

        val lastData = testResults.last()
        // ensure data is updating one more time
        assertEquals(testResults.size, 5)
        assertEquals(lastData.onGoingData is Success, true)
        assertEquals(lastData.promoCreationData is Success, true)

        // ensure selected filter change to the last one
        assertEquals(lastData.selectedTabFilterData.first, "Harga Tertinggi last")
        assertEquals(lastData.selectedTabFilterData.second, "5")

        job.cancel()
    }

    @Test
    fun `success swipe refresh and refresh all data`() = runTest {
        `success get layout data for initial state`()

        val testResults = mutableListOf<CentralizedPromoUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.layoutList.toList(testResults)
        }

        clearMocks(getPromotionUseCase)

        coEvery {
            getPromotionUseCase.execute(shopId = any(), tabId = any())
        } returns mockPromoCreation

        // we re-mock this in purpose of ensure this ongoing not change when filter change, we only want to change promo creation
        clearMocks(getOnGoingPromotionUseCase)
        coEvery {
            getOnGoingPromotionUseCase.executeOnBackground()
        } returns mockOnGoingDataAfterFilter

        viewModel.sendEvent(CentralizedPromoEvent.SwipeRefresh)

        advanceUntilIdle()

        assertEquals(testResults.first().isSwipeRefresh, false)
        assertEquals(testResults[1].isSwipeRefresh, true)
        assertEquals(testResults.last().isSwipeRefresh, false)

        job.cancel()
    }

    @Test
    fun `success get shared pref RBAC`() = runTest {
        every {
            pref.getBoolean(any(), any())
        } returns true

        val dataValue = viewModel.getKeyRBAC("asd")
        assertEquals(dataValue, true)
    }

    @Test
    fun `success get shared pref coachmark`() = runTest {
        val keyCoachMark = slot<String>()

        every {
            pref.getBoolean(capture(keyCoachMark), any())
        } returns true

        val dataValue = viewModel.getCoachmarkSharedPref("1")
        assertEquals(dataValue, true)

        assertEquals(keyCoachMark.captured, "1coachmark")
    }

    @Test
    fun `success change shared pref RBAC`() = runTest {
        viewModel.sendEvent(CentralizedPromoEvent.UpdateRbacBottomSheet("key"))
    }

    @Test
    fun `success change shared pref coachmark`() = runTest {
        viewModel.sendEvent(CentralizedPromoEvent.CoachMarkShown("1"))
    }

    private fun `ensure toaster value not emit`() = runTest {
        var toasterInitialValue = false

        val job2 = launch(UnconfinedTestDispatcher()) {
            viewModel.toasterState.collect {
                toasterInitialValue = it
            }
        }
        advanceUntilIdle()
        assertTrue(!toasterInitialValue)
        job2.cancel()
    }

    private fun `ensure toaster value emit`() = runTest {
        var toasterInitialValue = false

        val job2 = launch(UnconfinedTestDispatcher()) {
            viewModel.toasterState.collect {
                toasterInitialValue = it
            }
        }
        advanceUntilIdle()
        assertTrue(toasterInitialValue)
        job2.cancel()
    }
}
