package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.onVisorInitListener
import com.tokopedia.devicefingerprint.datavisor.response.DVData
import com.tokopedia.devicefingerprint.datavisor.response.GetDVInitStat
import com.tokopedia.devicefingerprint.datavisor.response.GetDVInitStatData
import com.tokopedia.devicefingerprint.datavisor.response.GetDVInitStatResponse
import com.tokopedia.devicefingerprint.datavisor.response.SubDvcIntlEvent
import com.tokopedia.devicefingerprint.datavisor.response.SubRDVInit
import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.devicefingerprint.datavisor.response.SubmitRDVResponse
import com.tokopedia.devicefingerprint.datavisor.usecase.GetDVInitStatUseCase
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitRDVTokenUseCase
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.ERROR_TYPE_BACKEND_RESPONSE
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.ERROR_TYPE_INIT_SDK
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.ERROR_TYPE_SEND_BACKEND
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.LOG_ERROR
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.LOG_ERROR_TYPE
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.LOG_TAG
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.LOG_TOKEN
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisor.Companion.MAX_RUN_ATTEMPT
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class DataVisorWorkerTest {

    private val context = mockk<Context>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val submitDVTokenUseCase = mockk<SubmitDVTokenUseCase>(relaxed = true)
    private val submitRDVTokenUseCase = mockk<SubmitRDVTokenUseCase>(relaxed = true)
    private val dataVisorRepository = DataVisorMockRepository()
    private val getDVInitStatUseCase = mockk<GetDVInitStatUseCase>(relaxed = true)
    private val dataVisor = DataVisor(
        context,
        CoroutineTestDispatchers,
        userSession,
        dataVisorRepository,
        submitDVTokenUseCase,
        submitRDVTokenUseCase,
        getDVInitStatUseCase,
    )

    @Before
    fun setUp() {
        mockkObject(FingerprintModelGenerator)
        mockkObject(VisorFingerprintInstance.Companion)
        mockkStatic(ServerLogger::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //region Pre-Auth Flow
    @Test
    fun `user non login do work will do preprocessing logic`() = runBlocking {
        `given user is non login`()
        `given init data visor fingerprint success`()
        `given preprocessing API success`()

        `when do work`()

        `then verify call preprocessing API`()
        `then verify save dv token`()
        `then verify no post processing`()
    }

    private fun `given preprocessing API success`() {
        coEvery {
            submitRDVTokenUseCase.execute(any(), any(), any(), any())
        } returns SUCCESS_SUBMIT_RDV_RESPONSE
    }

    private fun `given user is non login`() {
        every { userSession.isLoggedIn } returns false
        every { userSession.userId } returns NON_LOGIN_USER_ID
    }

    private fun `given init data visor fingerprint success`() {
        every {
            VisorFingerprintInstance.initToken(any(), any(), any())
        } answers {
            thirdArg<onVisorInitListener>().onSuccessInitToken(DUMMY_TOKEN)
        }
    }

    private suspend fun `when do work`(isFromLogin: Boolean = false) {
        dataVisor.doWork(DUMMY_ACTIVITY_NAME, isFromLogin)
    }

    private fun `then verify call preprocessing API`() {
        coVerify {
            submitRDVTokenUseCase.execute(
                DUMMY_TOKEN,
                any(),
                "",
                DUMMY_ACTIVITY_NAME,
            )
        }
    }

    private fun `then verify save dv token`() {
        dataVisorRepository.verifyToken(DUMMY_TOKEN)

        coVerify {
            FingerprintModelGenerator.expireFingerprint()
        }
    }

    private fun `then verify no post processing`() {
        coVerify(exactly = 0) { submitDVTokenUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `user non login and init fingerprint fail, preprocessing will send error message to API`() =
        runBlocking {
            `given user is non login`()
            `given init data visor fingerprint failed`()
            `given preprocessing API success`()

            `when do work`()

            `then verify call preprocessing API with error`()
            `then verify not save dv token`()
            `then verify send log error`(
                mapOf(
                    LOG_TOKEN to "",
                    LOG_ERROR_TYPE to ERROR_TYPE_INIT_SDK,
                    LOG_ERROR to DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE,
                )
            )
        }

    private fun `given init data visor fingerprint failed`() {
        every {
            VisorFingerprintInstance.initToken(any(), any(), any())
        } answers {
            thirdArg<onVisorInitListener>().onFailedInitToken(DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE)
        }
    }

    private fun `then verify call preprocessing API with error`() {
        coVerify {
            submitRDVTokenUseCase.execute(
                DEFAULT_VALUE_DATAVISOR,
                any(),
                DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE,
                DUMMY_ACTIVITY_NAME,
            )
        }
    }

    private fun `then verify not save dv token`() {
        dataVisorRepository.verifyToken(DEFAULT_VALUE_DATAVISOR)

        coVerify(exactly = 0) {
            FingerprintModelGenerator.expireFingerprint()
        }
    }

    private fun `then verify send log error`(expectedErrorMap: Map<String, String>) {
        verify {
            ServerLogger.log(
                Priority.P1,
                LOG_TAG,
                expectedErrorMap
            )
        }
    }

    @Test
    fun `user non login and preprocessing API fail when called will send log`() = runBlocking {
        `given user is non login`()
        `given init data visor fingerprint success`()
        `given preprocessing API throws exception`()

        `when do work`()

        `then verify call preprocessing API`()
        `then verify save dv token`()
        `then verify send log error`(
            mapOf(
                LOG_TOKEN to DUMMY_TOKEN,
                LOG_ERROR_TYPE to ERROR_TYPE_SEND_BACKEND,
                LOG_ERROR to DUMMY_SUBMIT_DEVICE_EXCEPTION.toString(),
            )
        )
    }

    private fun `given preprocessing API throws exception`() {
        coEvery {
            submitRDVTokenUseCase.execute(any(), any(), any(), any())
        } throws DUMMY_SUBMIT_DEVICE_EXCEPTION
    }

    @Test
    fun `user non login and preprocessing API returns error response will send log`() = runBlocking {
        `given user is non login`()
        `given init data visor fingerprint success`()
        `given preprocessing API with error response`()

        `when do work`()

        `then verify save dv token`()
        `then verify call preprocessing API`()
        `then verify send log error`(
            mapOf(
                LOG_TOKEN to DUMMY_TOKEN,
                LOG_ERROR_TYPE to ERROR_TYPE_BACKEND_RESPONSE,
                LOG_ERROR to "",
            )
        )
    }

    private fun `given preprocessing API with error response`() {
        coEvery {
            submitRDVTokenUseCase.execute(any(), any(), any(), any())
        } returns FAILED_SUBMIT_RDV_RESPONSE
    }
    //endregion

    //region Post Auth Flow
    @Test
    fun `user login do work will do post processing logic`() = runBlocking {
        `given user is already logged in`()
        `given init data visor fingerprint success`()
        `given post processing API success`()

        `when do work`()

        `then verify call post processing API`()
        `then verify no pre processing`()
        `then verify save dv token`()
    }

    private fun `given user is already logged in`() {
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns DUMMY_LOGIN_USER_ID
    }

    private fun `given post processing API success`() {
        coEvery {
            submitDVTokenUseCase.execute(any(), any(), any(), any())
        } returns SUCCESS_SUBMIT_DV_RESPONSE
    }

    private fun `then verify call post processing API`() {
        coVerify {
            submitDVTokenUseCase.execute(
                DUMMY_TOKEN,
                any(),
                "",
                DUMMY_ACTIVITY_NAME,
            )
        }
    }

    private fun `then verify no pre processing`() {
        coVerify(exactly = 0) { submitRDVTokenUseCase.execute(any(), any(), any(), any()) }
    }

    @Test
    fun `user login and init fingerprint fail, post processing will send error message to API`() =
        runBlocking {
            `given user is already logged in`()
            `given init data visor fingerprint failed`()
            `given preprocessing API success`()

            `when do work`()

            `then verify call post processing API with error`()
            `then verify not save dv token`()
            `then verify send log error`(
                mapOf(
                    LOG_TOKEN to "",
                    LOG_ERROR_TYPE to ERROR_TYPE_INIT_SDK,
                    LOG_ERROR to DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE,
                )
            )
        }

    private fun `then verify call post processing API with error`() {
        coVerify {
            submitDVTokenUseCase.execute(
                DEFAULT_VALUE_DATAVISOR,
                any(),
                DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE,
                DUMMY_ACTIVITY_NAME,
            )
        }
    }

    @Test
    fun `user login and post processing API fail when called will send log`() = runBlocking {
        `given user is already logged in`()
        `given init data visor fingerprint success`()
        `given post processing API throws exception`()

        `when do work`()

        `then verify save dv token`()
        `then verify call post processing API`()
        `then verify send log error`(
            mapOf(
                LOG_TOKEN to DUMMY_TOKEN,
                LOG_ERROR_TYPE to ERROR_TYPE_SEND_BACKEND,
                LOG_ERROR to DUMMY_SUBMIT_DEVICE_EXCEPTION.toString(),
            )
        )
    }

    private fun `given post processing API throws exception`() {
        coEvery {
            submitDVTokenUseCase.execute(any(), any(), any(), any())
        } throws DUMMY_SUBMIT_DEVICE_EXCEPTION
    }

    @Test
    fun `user login and post processing API returns error response will send log`() = runBlocking {
        `given user is already logged in`()
        `given init data visor fingerprint success`()
        `given post processing API with error response`()

        `when do work`()

        `then verify save dv token`()
        `then verify call post processing API`()
        `then verify send log error`(
            mapOf(
                LOG_TOKEN to DUMMY_TOKEN,
                LOG_ERROR_TYPE to ERROR_TYPE_BACKEND_RESPONSE,
                LOG_ERROR to "",
            )
        )
    }

    private fun `given post processing API with error response`() {
        coEvery {
            submitDVTokenUseCase.execute(any(), any(), any(), any())
        } returns FAILED_SUBMIT_DV_RESPONSE
    }
    // endregion

    @Test
    fun `do not init fingerprint if user is logging out`() = runBlocking {
        dataVisorRepository.saveWorkerTimeStamp(System.currentTimeMillis())
        dataVisorRepository.saveToken(DUMMY_TOKEN)

        every { userSession.isLoggedIn } returns false
        every { userSession.userId } returns NON_LOGIN_USER_ID

        every {
            VisorFingerprintInstance.initToken(any(), any(), any())
        } answers {
            thirdArg<onVisorInitListener>().onSuccessInitToken("1234_dummy_token_shouldnotsave")
        }

        `when do work`()

        dataVisorRepository.verifyRunAttemptCount(0)
        dataVisorRepository.verifyToken(DUMMY_TOKEN)
        coVerify(exactly = 0) {
            FingerprintModelGenerator.expireFingerprint()
        }
    }

    @Test
    fun `user non login - do work - user login - do work will do pre processing and post processing`() =
        runBlocking {
            `given init data visor fingerprint success`()
            `given preprocessing API success`()
            `given post processing API success`()

            `given user is non login`()
            `when do work`()
            `then verify call preprocessing API`()

            `given user is already logged in`()
            `when do work`(true)
            `then verify call post processing API`()
        }

    //region Expiry check
    @Test
    fun `check expiry if there is a non default token and time stamp more than 24 hrs`() =
        runBlocking {
            `given saved token is valid`()
            `given saved timestamp is yesterday`()

            `when do work`()

            `then verify check expiry from API one time`()
        }

    private fun `given saved token is valid`() {
        dataVisorRepository.saveToken(DUMMY_TOKEN)
        dataVisorRepository.saveWorkerTimeStamp(System.currentTimeMillis())
    }

    private fun `given saved timestamp is yesterday`() {
        val oneDay = TimeUnit.HOURS.toMillis(26L)

        dataVisorRepository.reduceTimeStampBy(oneDay)
    }

    private fun `then verify check expiry from API one time`() {
        coVerify (exactly = 1) {
            getDVInitStatUseCase.execute(DUMMY_TOKEN, any())
        }
    }

    @Test
    fun `do not check expiry if the token is default`() = runBlocking {
        `given saved token is default token`()

        `when do work`()

        `then do not check expiry from API`()
    }

    private fun `given saved token is default token`() {
        dataVisorRepository.saveToken(DEFAULT_VALUE_DATAVISOR)
    }

    @Test
    fun `do not check expiry if the token is empty`() = runBlocking {
        `given saved token is empty`()

        `when do work`()

        `then do not check expiry from API`()
    }

    private fun `given saved token is empty`() {
        dataVisorRepository.saveToken("")
    }

    private fun `then do not check expiry from API`() {
        coVerify(exactly = 0) {
            getDVInitStatUseCase.execute(any(), any())
        }
    }

    @Test
    fun `do not check expiry if the token is valid but not more than 24 hours`() = runBlocking {
        `given saved token is valid`()

        `when do work`()

        `then do not check expiry from API`()
    }

    @Test
    fun `only check expiry once per day if token is not expired`() = runBlocking {
        `given saved token is valid`()
        `given saved timestamp is yesterday`()
        `given token is not expired`()

        `when do work`()
        `when do work`()

        `then verify check expiry from API one time`()
    }

    private fun `given token is not expired`() {
        coEvery {
            getDVInitStatUseCase.execute(any(), any())
        } returns NOT_EXPIRED_SUBMIT_DEVICE_RESPONSE
    }
    //endregion

    //region Recovery flow
    @Test
    fun `do not do recovery flow if token is not expired`() =
        runBlocking {
            `given saved token is valid`()
            `given saved timestamp is yesterday`()
            `given token is not expired`()

            `when do work`()

            `then verify no pre processing`()
            `then verify no post processing`()
        }

    @Test
    fun `user non login and token expired, do recovery flow pre processing`() = runBlocking {
        `given user is non login`()
        `given init data visor fingerprint success`()
        `given preprocessing API success`()
        `given token is expired`()

        `when do work`()

        `then verify call preprocessing API`()
    }

    private fun `given token is expired`() {
        coEvery {
            getDVInitStatUseCase.execute(any(), any())
        } returns EXPIRED_SUBMIT_DEVICE_RESPONSE
    }

    @Test
    fun `user login and token expired, do recovery flow post processing`() = runBlocking {
        `given user is already logged in`()
        `given init data visor fingerprint success`()
        `given post processing API success`()
        `given token is expired`()

        `when do work`()

        `then verify call post processing API`()
    }
    //endregion

    @Test
    fun `init data visor will retry three times daily`() = runBlocking {
        val retryCountList = mutableListOf<Int>()

        coEvery {
            submitRDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_RDV_RESPONSE

        `given init data visor fingerprint failed`()

        `when do work more than max attempt`() // 4x init

        `given saved timestamp is yesterday`()

        `when do work more than max attempt`() // 4x init

        `then verify init token three times twice (once per day)`()
        assertThat(retryCountList, `is`(listOf(0, 1, 2, 3, 0, 1, 2, 3)))
    }

    private suspend fun `when do work more than max attempt`() {
        for (i in 1..(MAX_RUN_ATTEMPT + 99))
            dataVisor.doWork(DUMMY_ACTIVITY_NAME)
    }

    private fun `then verify init token three times twice (once per day)`() {
        val firstDayInstallCount = 1
        verify(exactly = (MAX_RUN_ATTEMPT + firstDayInstallCount) * 2) {
            VisorFingerprintInstance.initToken(any(), any(), any())
        }
    }

    @Test
    fun `data visor will run once per day if successful`() = runBlocking {
        `given init data visor fingerprint success`()

        `when do work more than max attempt`()

        `given saved timestamp is yesterday`()

        `given token is expired`()

        `when do work more than max attempt`()

        `then verify init token twice`()
    }

    private fun `then verify init token twice`() {
        verify(exactly = 2) {
            VisorFingerprintInstance.initToken(any(), any(), any())
        }
    }

    @Test
    fun `do not update worker timestamp if token is valid and not expired`() = runBlocking {
        val currentTime = System.currentTimeMillis()
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns DUMMY_LOGIN_USER_ID
        dataVisorRepository.saveToken(DUMMY_TOKEN)
        dataVisorRepository.saveWorkerTimeStamp(currentTime)

        `when do work`()

        dataVisorRepository.verifyWorkerTimeStamp(currentTime)
    }

    @Test
    fun `increment retry count for every init when previous token is not valid - pre auth`() = runBlocking {
        val retryCountList = mutableListOf<Int>()

        coEvery {
            submitRDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_RDV_RESPONSE

        `given init data visor fingerprint failed`()

        `when do work`()

        `given init data visor fingerprint success`()

        `when do work`()

        assertThat(retryCountList, `is`(listOf(0, 1)))
    }

    @Test
    fun `increment retry count for every init when previous token is not valid - post auth`() = runBlocking {
        val retryCountList = mutableListOf<Int>()

        coEvery {
            submitRDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_RDV_RESPONSE

        coEvery {
            submitDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_DV_RESPONSE

        `given init data visor fingerprint failed`()

        `given user is non login`()

        `when do work`()

        `given user is already logged in`()

        `when do work`(isFromLogin = true)

        assertThat(retryCountList, `is`(listOf(0, 1)))
    }

    @Test
    fun `post auth will not reset retry count`() = runBlocking {
        val retryCountList = mutableListOf<Int>()

        coEvery {
            submitRDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_RDV_RESPONSE

        coEvery {
            submitDVTokenUseCase.execute(any(), capture(retryCountList), any(), any())
        } returns SUCCESS_SUBMIT_DV_RESPONSE

        `given init data visor fingerprint failed`()
        `given user is non login`()
        `when do work`()

        `given init data visor fingerprint success`()
        `when do work`()

        `given user is already logged in`()
        `when do work`(true)

        assertThat(retryCountList, `is`(listOf(0, 1, 1)))
    }

    companion object {
        private const val NON_LOGIN_USER_ID = "0"
        private const val DUMMY_LOGIN_USER_ID = "01255"
        private const val DUMMY_TOKEN = "1234"
        private const val DUMMY_INIT_DV_TOKEN_ERROR_MESSAGE = "error message init token"
        private const val DUMMY_ACTIVITY_NAME = "DummyActivity"
        private val DEFAULT_VALUE_DATAVISOR = VisorFingerprintInstance.DEFAULT_VALUE_DATAVISOR
        private val SUCCESS_SUBMIT_DV_RESPONSE = SubmitDeviceInitResponse(
            SubDvcIntlEvent(isError = false, dvData = DVData(isExpire = false))
        )
        private val FAILED_SUBMIT_DV_RESPONSE = SubmitDeviceInitResponse(
            SubDvcIntlEvent(isError = true, dvData = DVData(isExpire = false))
        )
        private val SUCCESS_SUBMIT_RDV_RESPONSE = SubmitRDVResponse(
            SubRDVInit(isError = false)
        )
        private val FAILED_SUBMIT_RDV_RESPONSE = SubmitRDVResponse(
            SubRDVInit(isError = true)
        )
        private val NOT_EXPIRED_SUBMIT_DEVICE_RESPONSE = GetDVInitStatResponse(
            GetDVInitStat(isError = false, data = GetDVInitStatData(isExpire = false))
        )
        private val EXPIRED_SUBMIT_DEVICE_RESPONSE = GetDVInitStatResponse(
            GetDVInitStat(isError = false, data = GetDVInitStatData(isExpire = true))
        )
        private val DUMMY_SUBMIT_DEVICE_EXCEPTION = Exception("dummy_exception")
    }
}