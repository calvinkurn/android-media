package com.tokopedia.tradein.viewmodel

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.tradein.Constants
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein.usecase.CheckMoneyInUseCase
import com.tokopedia.tradein.usecase.ProcessMessageUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class TradeInHomeViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    val checkMoneyInUseCase: CheckMoneyInUseCase = mockk()
    val processMessageUseCase: ProcessMessageUseCase = mockk()
    val userSession: UserSessionInterface = mockk()

    var tradeInHomeViewModel = spyk(TradeInHomeViewModel(processMessageUseCase, checkMoneyInUseCase, userSession))

    @RelaxedMockK
    lateinit var deviceDiagnostics: DeviceDiagnostics

    @RelaxedMockK
    lateinit var response: DeviceDiagInputResponse

    @RelaxedMockK
    lateinit var moneyInResponse: ValidateTradePDP

    @MockK
    lateinit var androidIntent: Intent

    @RelaxedMockK
    lateinit var laku6TradeIn: Laku6TradeIn

    private val tradeInType = 1

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** doOnCreate() *******************************************/

    @Test
    fun doOnCreate(){
        every { tradeInHomeViewModel.checkLogin() } returns Unit

        tradeInHomeViewModel.doOnCreate()

        verify { tradeInHomeViewModel.checkLogin() }
    }
    /**************************** doOnCreate() *******************************************/

    /**************************** checkLogin() *******************************************/

    @Test
    fun `check askUserLogin is LOGIN_REQUIRED`() {
        coEvery { userSession.isLoggedIn } returns false

        tradeInHomeViewModel.checkLogin()

        assertEquals(tradeInHomeViewModel.askUserLogin.value, Constants.LOGIN_REQUIRED)
    }

    @Test
    fun `check askUserLogin is LOGEED_IN`() {
        coEvery { userSession.isLoggedIn } returns true

        tradeInHomeViewModel.checkLogin()

        assertEquals(tradeInHomeViewModel.askUserLogin.value, Constants.LOGEED_IN)
    }
    /**************************** checkLogin() *******************************************/

    /**************************** processMessage() *******************************************/

    @Test
    fun processMessageException() {
        coEvery { tradeInHomeViewModel.getDiagnosticData(androidIntent) } returns deviceDiagnostics
        coEvery { processMessageUseCase.processMessage(any(), any()) } throws Exception("check warningmessage value is called when exception thrown in Process Message")
        every { deviceDiagnostics.imei } returns "3"

        tradeInHomeViewModel.processMessage(androidIntent)

        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceDiagnostics.imei)
        assertEquals(tradeInHomeViewModel.getWarningmessage().value, "check warningmessage value is called when exception thrown in Process Message")
    }

    @Test
    fun processMessage() {
        coEvery { tradeInHomeViewModel.getDiagnosticData(androidIntent) } returns deviceDiagnostics
        coEvery { processMessageUseCase.processMessage(any(), any()) } returns response
        every { deviceDiagnostics.imei } returns "3"

        tradeInHomeViewModel.processMessage(androidIntent)

        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceDiagnostics.imei)
        coVerify { tradeInHomeViewModel.setDiagnoseResult(any(), any()) }
    }

    /**************************** processMessage() *******************************************/

    /**************************** setDiagnoseResult() *******************************************/

    @Test
    fun `setDiagnoseResult is Success and Eligible`() {
        every { response?.deviceDiagInputRepsponse?.isEligible } returns true

        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.DIAGNOSED_VALID)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
    }

    @Test
    fun `setDiagnoseResult is Success and not Eligible`() {
        every { response?.deviceDiagInputRepsponse?.isEligible } returns false

        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.DIAGNOSED_INVALID)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDiagnostics.tradeInPrice!!, true))
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
    }

    @Test
    fun `setDiagnoseResult Data is Failure`() {
        every { response?.deviceDiagInputRepsponse } returns null

        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value, null)
    }
    /**************************** setDiagnoseResult() *******************************************/

    /**************************** checkDiagnosticsData() *******************************************/
    @Test
    fun getDiagnosticData() {
        coEvery { androidIntent.getStringExtra("test-result") } returns Gson().toJson(deviceDiagnostics)
        assertEquals(tradeInHomeViewModel.getDiagnosticData(androidIntent)::class.java, deviceDiagnostics::class.java)
    }

    @Test(expected = IllegalStateException::class)
    fun getDiagnosticDataException() {
        coEvery { androidIntent.getStringExtra("test-result") } returns null
        tradeInHomeViewModel.getDiagnosticData(androidIntent)
    }

    /**************************** checkDiagnosticsData() *******************************************/

    /**************************** checkMoneyIn() *******************************************/
    @Test
    fun checkMoneyInException() {
        val jsonObject = JSONObject()
        val modelId = 2
        val userId = "3"
        coEvery { checkMoneyInUseCase.checkMoneyIn(any(), any(), any()) } throws Exception("check warningmessage value is called when exception thrown in checkMoneyIn")
        coEvery { userSession.userId } returns userId

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.getWarningmessage().value, "check warningmessage value is called when exception thrown in checkMoneyIn")
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, true)
    }

    @Test
    fun checkMoneyIn() {
        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")
        val modelId = 2
        val userId = "3"
        val usedPrice = 4
        tradeInHomeViewModel.tradeInType = 2
        coEvery { checkMoneyInUseCase.checkMoneyIn(any(), any(), any()) } returns moneyInResponse

        /**Not Eligible Case**/
        coEvery { moneyInResponse?.response?.isEligible } returns false
        coEvery { userSession.userId } returns userId

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.getDisplayMessage(), moneyInResponse?.response?.message)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.MONEYIN_ERROR)
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, true)

        /**Eligible case**/
        coEvery { moneyInResponse?.response?.isEligible } returns true
        coEvery { moneyInResponse?.response?.usedPrice } returns usedPrice
        coEvery { moneyInResponse?.response?.isUseKyc } returns false
        coEvery { userSession.userId } returns userId

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
        assertEquals(tradeInHomeViewModel.tradeInParams.isEligible, 1)
        assertEquals(tradeInHomeViewModel.tradeInParams.usedPrice, usedPrice)
        assertEquals(tradeInHomeViewModel.tradeInParams.isUseKyc, 0)
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
    }
//
//    @Test
//    fun checkMoneyIn(){
//        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")
//        val userID = "1"
//        val modelId = 3
//        val deviceId = "4"
//
//        /**Not Eligible Case**/
//
//        mockkStatic(TradeInUtils::class)
//        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
//        coEvery { application.resources } returns resources
//        mockkStatic(GraphqlHelper::class)
//        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
//        coEvery { tradeInHomeViewModel.getRepo() } returns repository
//        coEvery { moneyInResponse?.response?.message } returns "123"
//        coEvery { moneyInResponse?.response?.isEligible } returns false
//        coEvery { repository.getUserLoginState().userId } returns userID
//        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } returns moneyInResponse
//
//        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)
//
//        assertEquals(tradeInHomeViewModel.tradeInParams.userId, userID.toInt())
//        assertEquals(tradeInHomeViewModel.tradeInParams.tradeInType, 2)
//        assertEquals(tradeInHomeViewModel.tradeInParams.modelID, modelId)
//        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
//        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, moneyInResponse?.response?.message)
//        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
//        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.MONEYIN_ERROR)
//
//
//
//        /**Eligible case**/
//
//        mockkStatic(TradeInUtils::class)
//        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
//        coEvery { application.resources } returns resources
//        mockkStatic(GraphqlHelper::class)
//        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
//        coEvery { tradeInHomeViewModel.getRepo() } returns repository
//        coEvery { moneyInResponse?.response?.message } returns "123"
//        coEvery { moneyInResponse?.response?.isEligible } returns true
//        coEvery { moneyInResponse?.response?.usedPrice } returns 10
//        coEvery { moneyInResponse?.response?.isUseKyc} returns false
//        coEvery { repository.getUserLoginState().userId } returns userID
//        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } returns moneyInResponse
//        tradeInHomeViewModel.tradeInParams.usedPrice = 10
//        tradeInHomeViewModel.tradeInType = 2
//
//        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)
//
//        assertEquals(tradeInHomeViewModel.tradeInParams.userId, userID.toInt())
//        assertEquals(tradeInHomeViewModel.tradeInParams.tradeInType, 2)
//        assertEquals(tradeInHomeViewModel.tradeInParams.modelID, modelId)
//        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
//        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
//        assertEquals(tradeInHomeViewModel.tradeInParams.isEligible, 1)
//        assertEquals(tradeInHomeViewModel.tradeInParams.usedPrice, 10)
//        assertEquals(tradeInHomeViewModel.tradeInParams.isUseKyc, 0)
//        assertEquals(HomeResult.PriceState.DIAGNOSED_VALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
//    }
//
//    @Test
//    fun checkMoneyInException(){
//        val jsonObject = JSONObject()
//        val userID = "1"
//        val modelId = 3
//        val deviceId = "4"
//        mockkStatic(TradeInUtils::class)
//        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
//        coEvery { application.resources } returns resources
//        mockkStatic(GraphqlHelper::class)
//        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
//        coEvery { tradeInHomeViewModel.getRepo() } returns repository
//
//        coEvery { repository.getUserLoginState().userId } returns userID
//        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } throws Exception("check warningmessage value is called when exception thrown in checkMoneyIn")
//
//        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)
//
//        assertEquals(tradeInHomeViewModel.warningMessage.value, "check warningmessage value is called when exception thrown in checkMoneyIn")
//    }
//
//    /**************************** checkMoneyIn() *******************************************/

    /**************************** onFinished() *******************************************/

    @Test
    fun onFinishedMoneyIn() {
        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")

        /** Money in **/
        tradeInHomeViewModel.tradeInType = 2
        tradeInHomeViewModel.tradeInParams.usedPrice = 0
        coEvery { userSession.userId } returns "3"
        coEvery { checkMoneyInUseCase.checkMoneyIn(any(), any(), any()) } returns moneyInResponse

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 1) { tradeInHomeViewModel.checkMoneyIn(any(), jsonObject) }
    }

    @Test
    fun onFinishedException() {
        val jsonObject = JSONObject()

        tradeInHomeViewModel.onFinished(jsonObject)

        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
    }

    @Test
    fun onFinishedExceptionHomeResult() {
        val jsonObject = JSONObject("{\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")

        tradeInHomeViewModel.onFinished(jsonObject)

        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
    }

    @Test
    fun onFinished() {
        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")

        /** Diagnosed invalid **/
        tradeInHomeViewModel.tradeInType = 1
        tradeInHomeViewModel.tradeInParams.usedPrice = 10
        tradeInHomeViewModel.tradeInParams.setPrice(0)

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.DIAGNOSED_INVALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.deviceDisplayName, "LG Nexus 5")
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)


        /** Not Diagnosed **/
        tradeInHomeViewModel.tradeInParams.usedPrice = 0

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.NOT_DIAGNOSED, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.deviceDisplayName, "LG Nexus 5")
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)


        /** Diagnosed valid **/
        tradeInHomeViewModel.tradeInParams.usedPrice = 10
        tradeInHomeViewModel.tradeInParams.setPrice(20)

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.DIAGNOSED_VALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.getDeviceDisplayName(), "LG Nexus 5")

    }

    /**************************** onFinished() *******************************************/

    /**************************** onError() *******************************************/

    @Test
    fun onError() {
        val jsonObject = JSONObject("{\"message\":\"Error tradein\"}")

        tradeInHomeViewModel.onError(jsonObject)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), false)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, "Error tradein")
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
    }

    @Test
    fun onErrorException() {
        val jsonObject = JSONObject("{\"message\":1}")

        tradeInHomeViewModel.onError(jsonObject)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), false)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, "")
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, false)
    }

    /**************************** onError() *******************************************/

    /**************************** getMaxPrice() *******************************************/

    @Test
    fun getMaxPrice() {
        tradeInHomeViewModel.getMaxPrice(laku6TradeIn, tradeInType)

        assertEquals(tradeInHomeViewModel.tradeInType, tradeInType)
        coVerify { laku6TradeIn.getMinMaxPrice(any()) }
        assertEquals(tradeInHomeViewModel.getProgressBarVisibility().value, true)
    }

    /**************************** getMaxPrice() *******************************************/

}

