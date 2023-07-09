package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMO_PERFORMA
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.TOTAL_ITEMS_METRIC_TYPE
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.model.response.AffiliateDateFilterResponse
import com.tokopedia.affiliate.model.response.AffiliatePerformanceItemTypeListData
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.sse.AffiliateSSE
import com.tokopedia.affiliate.sse.AffiliateSSEPageSource
import com.tokopedia.affiliate.sse.model.AffiliateSSEAction
import com.tokopedia.affiliate.sse.model.AffiliateSSECloseReason
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.usecase.AffiliateAnnouncementUseCase
import com.tokopedia.affiliate.usecase.AffiliateGetUnreadNotificationUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceDataUseCase
import com.tokopedia.affiliate.usecase.AffiliatePerformanceItemTypeUseCase
import com.tokopedia.affiliate.usecase.AffiliateSSEAuthTokenUseCase
import com.tokopedia.affiliate.usecase.AffiliateUserPerformanceUseCase
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateAdpViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private val affiliateAffiliateAnnouncementUseCase: AffiliateAnnouncementUseCase = mockk()
    private val affiliateUserPerformanceUseCase: AffiliateUserPerformanceUseCase = mockk()
    private val affiliatePerformanceDataUseCase: AffiliatePerformanceDataUseCase = mockk()
    private val affiliatePerformanceItemTypeUseCase: AffiliatePerformanceItemTypeUseCase = mockk()
    private val affiliateSSEAuthTokenUseCase: AffiliateSSEAuthTokenUseCase = mockk()
    private val getUnreadNotificationUseCase: AffiliateGetUnreadNotificationUseCase = mockk()
    private val dispatchers: CoroutineDispatchers = mockk()
    private val affiliateSSE: AffiliateSSE = mockk(relaxed = true)
    private var affiliateAdpViewModel = spyk(
        AffiliateAdpViewModel(
            userSessionInterface,
            affiliateValidateUserStatus,
            affiliateAffiliateAnnouncementUseCase,
            affiliateUserPerformanceUseCase,
            affiliatePerformanceItemTypeUseCase,
            affiliatePerformanceDataUseCase,
            affiliateSSEAuthTokenUseCase,
            getUnreadNotificationUseCase,
            dispatchers,
            affiliateSSE
        )
    )

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getAnnouncementInformation() *******************************************/
    @Test
    fun `announcement information should be there for home`() {
        val affiliateAnnouncementData: AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_HOME
            )
        } returns affiliateAnnouncementData

        affiliateAdpViewModel.getAnnouncementInformation(true)

        assertEquals(
            affiliateAdpViewModel.getAffiliateAnnouncement().value,
            affiliateAnnouncementData
        )
    }

    @Test
    fun `announcement information should be there for performa`() {
        val affiliateAnnouncementData: AffiliateAnnouncementDataV2 = mockk(relaxed = true)
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_PROMO_PERFORMA
            )
        } returns affiliateAnnouncementData

        affiliateAdpViewModel.getAnnouncementInformation(false)

        assertEquals(
            affiliateAdpViewModel.getAffiliateAnnouncement().value,
            affiliateAnnouncementData
        )
    }

    @Test
    fun getAnnouncementValidateException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery {
            affiliateAffiliateAnnouncementUseCase.getAffiliateAnnouncement(
                PAGE_ANNOUNCEMENT_HOME
            )
        } throws throwable

        affiliateAdpViewModel.getAnnouncementInformation(true)
    }

    /**************************** getAffiliateValidateUser() *******************************************/
    @Test
    fun getAffiliateValidateUser() {
        val affiliateValidateUserData: AffiliateValidateUserData = mockk(relaxed = true)
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateAdpViewModel.getAffiliateValidateUser()

        assertEquals(affiliateAdpViewModel.getValidateUserdata().value, affiliateValidateUserData)
    }

    @Test
    fun getAffiliateValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } throws throwable

        affiliateAdpViewModel.getAffiliateValidateUser()

        assertEquals(affiliateAdpViewModel.getErrorMessage().value, throwable)
        assertEquals(affiliateAdpViewModel.progressBar().value, false)
    }

    /**************************** getAffiliatePerformance() *******************************************/
    @Test
    fun getAffiliatePerformance() {
        val affiliateUserPerformaListData: AffiliateUserPerformaListItemData = mockk(relaxed = true)
        val defaultMetricData =
            AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics(
                TOTAL_ITEMS_METRIC_TYPE,
                "",
                "1",
                null,
                null,
                null,
                0,
                null
            )
        val performData = AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData(
            null,
            null,
            null,
            null,
            arrayListOf(defaultMetricData)
        )
        affiliateUserPerformaListData.getAffiliatePerformance.data?.userData = performData
        coEvery {
            affiliateUserPerformanceUseCase.affiliateUserperformance(any())
        } returns affiliateUserPerformaListData
        val ticker = AffiliateDateFilterResponse.Data.Ticker("info", "")
        val list = arrayListOf(
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter(
                "",
                "",
                "30 Hari Terakhir",
                "LastThirtyDays",
                "30"
            ),
            AffiliateDateFilterResponse.Data.GetAffiliateDateFilter(
                "",
                "",
                "7 Hari Terakhir",
                "LastSevenDays",
                "7"
            )
        )
        val filterResponse = AffiliateDateFilterResponse(
            AffiliateDateFilterResponse.Data(
                list,
                arrayListOf(ticker)
            )
        )
        coEvery { affiliateUserPerformanceUseCase.getAffiliateFilter() } returns filterResponse
        val affiliatePerformanceListData: AffiliatePerformanceListData = mockk(relaxed = true)
        val item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item =
            mockk(relaxed = true)
        val data = AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data(
            null,
            null,
            null,
            null,
            null,
            null,
            arrayListOf(item),
            null
        )
        affiliatePerformanceListData.getAffiliatePerformanceList?.data?.data = data
        coEvery {
            affiliatePerformanceDataUseCase.affiliateItemPerformanceList(
                any(),
                any(),
                any()
            )
        } returns affiliatePerformanceListData

        affiliateAdpViewModel.getAffiliatePerformance(PAGE_ZERO)
        assertEquals(affiliateAdpViewModel.noMoreDataAvailable().value, false)

        val affiliatePerformanceListItemData: AffiliateUserPerformaListItemData =
            mockk(relaxed = true)
        val itemTypes: AffiliatePerformanceItemTypeListData = mockk(relaxed = true)
        coEvery {
            affiliatePerformanceDataUseCase.affiliateItemPerformanceList(
                any(),
                any(),
                any()
            )
        } returns affiliatePerformanceListData
        coEvery {
            affiliateUserPerformanceUseCase.affiliateUserperformance(
                affiliateAdpViewModel.getSelectedDate()
            )
        } returns affiliatePerformanceListItemData
        coEvery {
            affiliatePerformanceItemTypeUseCase.affiliatePerformanceItemTypeList()
        } returns itemTypes
        affiliateAdpViewModel.getAffiliatePerformance(PAGE_ZERO, true)
        assertEquals(affiliateAdpViewModel.noMoreDataAvailable().value, false)
        assertEquals(affiliateAdpViewModel.getDataShimmerVisibility().value, false)
    }

    @Test
    fun getAffiliatePerformanceException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateUserPerformanceUseCase.affiliateUserperformance(any()) } throws throwable
        coEvery { affiliateUserPerformanceUseCase.getAffiliateFilter() } throws throwable

        affiliateAdpViewModel.getAffiliatePerformance(PAGE_ZERO)

        assertEquals(affiliateAdpViewModel.getDataShimmerVisibility().value, false)
        assertEquals(affiliateAdpViewModel.getErrorMessage().value, throwable)

        affiliateAdpViewModel.getAffiliatePerformance(1)

        assertEquals(affiliateAdpViewModel.getShimmerVisibility().value, false)
    }

    /**************************** userSession() *******************************************/

    @Test
    fun userSessionTest() {
        val name = "Testing"
        val profile = "Profile Testing"
        val isLoggedIn = false
        coEvery { userSessionInterface.name } returns name
        coEvery { userSessionInterface.profilePicture } returns profile
        coEvery { userSessionInterface.isLoggedIn } returns isLoggedIn

        assertEquals(affiliateAdpViewModel.getUserName(), name)
        assertEquals(affiliateAdpViewModel.getUserProfilePicture(), profile)
        assertEquals(affiliateAdpViewModel.isUserLoggedIn(), isLoggedIn)
    }

    /**************************** getSelectedDate() *******************************************/
    @Test
    fun getSelectedDataTest() {
        val selectedDate = AffiliateBottomDatePicker.THIRTY_DAYS
        assertEquals(affiliateAdpViewModel.getSelectedDate(), selectedDate)
    }

    /**************************** onRangeChanged() *******************************************/
    @Test
    fun onRangeChangeTest() {
        val range: AffiliateDatePickerData = mockk(relaxed = true)
        affiliateAdpViewModel.onRangeChanged(range)

        assertEquals(true, affiliateAdpViewModel.getRangeChanged().value)
    }

    @Test
    fun onRangeChangeTestFailure() {
        val range: AffiliateDatePickerData = mockk()
        every { range.text } returns AffiliateBottomDatePicker.THIRTY_DAYS
        affiliateAdpViewModel.onRangeChanged(range)

        assertEquals(null, affiliateAdpViewModel.getRangeChanged().value)
    }

    @Test
    fun `successfully getting unread notification count`() {
        coEvery {
            getUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliateAdpViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliateAdpViewModel.getUnreadNotificationCount().value)
    }

    @Test
    fun `should reset notification count to zero`() {
        coEvery {
            getUnreadNotificationUseCase.getUnreadNotifications()
        } returns 5

        affiliateAdpViewModel.fetchUnreadNotificationCount()
        assertEquals(5, affiliateAdpViewModel.getUnreadNotificationCount().value)

        affiliateAdpViewModel.resetNotificationCount()
        assertEquals(0, affiliateAdpViewModel.getUnreadNotificationCount().value)
    }

    @Test
    fun `when start sse connect and listen should run`() {
        val dummyToken = "token"
        val source = AffiliateSSEPageSource.AffiliateADP.source
        val action = mockk<AffiliateSSEAction>()

        coEvery { affiliateSSEAuthTokenUseCase.getAffiliateToken().data?.token.orEmpty() } returns dummyToken
        coEvery { affiliateSSE.listen() } returns flowOf(action)

        affiliateAdpViewModel.startSSE()

        coVerify {
            affiliateSSE.connect(source, dummyToken)
            affiliateSSE.listen()
        }
    }

    @Test
    fun `when sse closed because of any error it should retry connection`() {
        val dummyToken = "token"
        val source = AffiliateSSEPageSource.AffiliateADP.source
        val action = AffiliateSSEAction.Close(AffiliateSSECloseReason.ERROR)

        coEvery { affiliateSSEAuthTokenUseCase.getAffiliateToken().data?.token.orEmpty() } returns dummyToken
        coEvery { affiliateSSE.listen() } returns flowOf(action)

        affiliateAdpViewModel.startSSE()

        coVerify {
            affiliateSSE.connect(source, dummyToken)
            affiliateSSE.listen()
        }
    }

    @Test
    fun `when sse closed intentionally it should not retry`() {
        val dummyToken = "token"
        val source = AffiliateSSEPageSource.AffiliateADP.source
        val action = AffiliateSSEAction.Close(AffiliateSSECloseReason.INTENDED)

        coEvery { affiliateSSEAuthTokenUseCase.getAffiliateToken().data?.token.orEmpty() } returns dummyToken
        coEvery { affiliateSSE.listen() } returns flowOf(action)

        affiliateAdpViewModel.startSSE()

        coVerify(exactly = 1) {
            affiliateSSE.connect(source, dummyToken)
        }
    }

    @Test
    fun `when stop sse it should stop listening`() {
        affiliateAdpViewModel.stopSSE()

        coVerify {
            affiliateSSE.close()
        }
    }
}
