package com.tokopedia.play.repo

import com.tokopedia.play.data.UserReportOptions
import com.tokopedia.play.data.UserReportSubmissionResponse
import com.tokopedia.play.data.repository.PlayViewerUserReportRepositoryImpl
import com.tokopedia.play.domain.GetUserReportListUseCase
import com.tokopedia.play.domain.PostUserReportUseCase
import com.tokopedia.play.domain.repository.PlayViewerUserReportRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
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

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        userReportRepo = PlayViewerUserReportRepositoryImpl(
            getUserReportListUseCase, postUserReportUseCase, mapper, userSession, testDispatcher
        )
    }


    @Test
    fun `when submit report return success`(){
        runBlockingTest {
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
        runBlockingTest {
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
    fun `get reasoning list is success`(){
        runBlockingTest {
            val response = UserReportOptions.Response(
                data = listOf(UserReportOptions(
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
                ))
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
