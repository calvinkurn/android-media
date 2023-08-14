package com.tokopedia.play.repo

import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.report_content.model.UserReportSubmissionResponse
import com.tokopedia.play.data.repository.PlayViewerUserReportRepositoryImpl
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.play.domain.repository.PlayViewerUserReportRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author by astidhiyaa on 04/01/22
 */
@ExperimentalCoroutinesApi
class PlayViewerUserReportTest {
    lateinit var userReportRepo : PlayViewerUserReportRepository

    private val getUserReportListUseCase: GetUserReportListUseCase = mockk(relaxed = true)
    private val postUserReportUseCase: PostUserReportUseCase = mockk(relaxed = true)

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val testDispatcher = CoroutineTestDispatchers

    private val classBuilder = ClassBuilder()
    private val mapper = classBuilder.getPlayUiModelMapper()

    /**
     * Partner Id
     */
    private val userId = 10L
    private val shopId = 12L

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        userReportRepo = PlayViewerUserReportRepositoryImpl(
            getUserReportListUseCase, postUserReportUseCase, mapper, userSession, testDispatcher
        )
    }


    @Test
    fun `when submit report return success`(){
        runTest {
            val response = UserReportSubmissionResponse(
                submissionReport = UserReportSubmissionResponse.Result(
                    "success"
                )
            )

            coEvery { postUserReportUseCase.executeOnBackground() } returns response

            val result = userReportRepo.submitReport(
                channelId = 1L,
                mediaUrl = "htpp://tokopedia",
                partnerId = 111L,
                timestamp = 9000L,
                reasonId = 1,
                reportDesc = "OK",
                partnerType = PartnerType.Shop)

            result.assertTrue()
        }
    }

    @Test
    fun `when submit report return failed`(){
        runTest {
            val response = UserReportSubmissionResponse(
                submissionReport = UserReportSubmissionResponse.Result(
                    "failed"
                )
            )

            coEvery { postUserReportUseCase.executeOnBackground() } returns response

            val result = userReportRepo.submitReport(
                channelId = 1L,
                mediaUrl = "htpp://tokopedia",
                partnerId = 111L,
                timestamp = 9000L,
                reasonId = 1,
                reportDesc = "OK",
                partnerType = PartnerType.Shop
            )

            result.assertFalse()
        }
    }

    @Test
    fun `when submit report return success - kol`(){
        runTest {
            val response = UserReportSubmissionResponse(
                submissionReport = UserReportSubmissionResponse.Result(
                    "success"
                )
            )

            coEvery { postUserReportUseCase.executeOnBackground() } returns response

            val result = userReportRepo.submitReport(
                channelId = 1L,
                mediaUrl = "htpp://tokopedia",
                partnerId = 111L,
                timestamp = 9000L,
                reasonId = 1,
                reportDesc = "OK",
                partnerType = PartnerType.Buyer)

            result.assertTrue()
        }
    }

    @Test
    fun `when submit report return failed - kol`(){
        runTest {
            val response = UserReportSubmissionResponse(
                submissionReport = UserReportSubmissionResponse.Result(
                    "failed"
                )
            )

            coEvery { postUserReportUseCase.executeOnBackground() } returns response

            val result = userReportRepo.submitReport(
                channelId = 1L,
                mediaUrl = "htpp://tokopedia",
                partnerId = 111L,
                timestamp = 9000L,
                reasonId = 1,
                reportDesc = "OK",
                partnerType = PartnerType.Buyer
            )

            result.assertFalse()
        }
    }

    @Test
    fun `get reasoning list is success`(){
        runTest {
            val response = UserReportOptions.Response(
                data = listOf(
                    UserReportOptions(
                    id = 1,
                    value = "Harga melanggar etika",
                    detail = ""
                ), UserReportOptions(
                    id = 11,
                    value = "Melanggar HAM",
                    detail = ""
                ), UserReportOptions(
                    id = 12,
                    value = "SARA",
                    detail = ""
                ), UserReportOptions(
                    id = 9,
                    value = "Melanggar etik",
                    detail = ""
                )
                )
            )

            coEvery { getUserReportListUseCase.executeOnBackground() } returns response
            val result = userReportRepo.getReasoningList()

            result.isNotEmpty().assertTrue()
        }
    }


    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}
