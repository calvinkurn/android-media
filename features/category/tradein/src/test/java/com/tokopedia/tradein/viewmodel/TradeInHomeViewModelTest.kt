package com.tokopedia.tradein.viewmodel

import android.app.Application
import android.content.Intent
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tradein.model.DeviceDiagInput
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein_common.Constants
import com.tokopedia.tradein_common.repository.BaseRepository
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
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import tradein_common.TradeInUtils
import java.io.File
import java.lang.reflect.Type


@ExperimentalCoroutinesApi
class TradeInHomeViewModelTest {

    val application : Application = mockk()
    val resources : Resources = mockk()
    var tradeInHomeViewModel = spyk(TradeInHomeViewModel(application = application, intent = Intent()))

    var repository = mockk<BaseRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var deviceDiagnostics: DeviceDiagnostics

    @RelaxedMockK
    var response : DeviceDiagInputResponse? = null

    @RelaxedMockK
    var moneyInResponse : ValidateTradePDP? = null

    @MockK
    lateinit var androidIntent: Intent

    @RelaxedMockK
    lateinit var laku6TradeIn : Laku6TradeIn

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

    /**************************** checkLogin() *******************************************/

    @Test
    fun `check askUserLogin is LOGIN_REQUIRED`() {
        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { repository.getUserLoginState().isLoggedIn } returns false

        tradeInHomeViewModel.checkLogin()

        assertEquals(tradeInHomeViewModel.askUserLogin.value, Constants.LOGIN_REQUIRED)
    }

    @Test
    fun `check askUserLogin is LOGEED_IN`() {
        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { repository.getUserLoginState().isLoggedIn } returns true

        tradeInHomeViewModel.checkLogin()

        assertEquals(tradeInHomeViewModel.askUserLogin.value, Constants.LOGEED_IN)
    }
    /**************************** checkLogin() *******************************************/

    /**************************** getRepo() *******************************************/

    @Test
    fun getRepo() {
        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        assertEquals(tradeInHomeViewModel.getRepo(), repository)
    }
    /**************************** getRepo() *******************************************/

    /**************************** checkDiagnosticsData() *******************************************/
    @Test
    fun getDiagnosticData(){
        coEvery { androidIntent.getStringExtra("test-result") } returns Gson().toJson(deviceDiagnostics)
        assertEquals(tradeInHomeViewModel.getDiagnosticData(androidIntent)::class.java, deviceDiagnostics::class.java)
    }

    @Test(expected = IllegalStateException::class)
    fun getDiagnosticDataException(){
        coEvery { androidIntent.getStringExtra("test-result") } returns null
        tradeInHomeViewModel.getDiagnosticData(androidIntent)
    }

    /**************************** checkDiagnosticsData() *******************************************/

    /**************************** RequestParamsTest() *******************************************/

    @Test
    fun getProcessMessageRequestParamTest(){
        every { deviceDiagnostics.brand  } returns "1"
        every { deviceDiagnostics.grade  } returns "2"
        every { deviceDiagnostics.imei  } returns "3"
        every { deviceDiagnostics.model  } returns "4"
        every { deviceDiagnostics.modelId  } returns 5
        every { deviceDiagnostics.ram  } returns "6"
        every { deviceDiagnostics.storage  } returns "7"
        every { deviceDiagnostics.tradeInUniqueCode  } returns "8"
        every { deviceDiagnostics.reviewDetails  } returns listOf()
        every { deviceDiagnostics.tradeInPrice } returns 10

        val variable = tradeInHomeViewModel.getProcessMessageRequestParam(deviceDiagnostics)

        assertNotNull(variable["params"])
        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceDiagnostics.imei)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.brand, deviceDiagnostics.brand)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.grade, deviceDiagnostics.grade)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.imei[0], deviceDiagnostics.imei)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.model, deviceDiagnostics.model)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.modelId, deviceDiagnostics.modelId)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.ram, deviceDiagnostics.ram)
        assertEquals((variable["params"] as DeviceDiagInput).deviceAttr.storage, deviceDiagnostics.storage)
        assertEquals((variable["params"] as DeviceDiagInput).uniqueCode, deviceDiagnostics.tradeInUniqueCode)
        assertEquals((variable["params"] as DeviceDiagInput).deviceId, deviceDiagnostics.imei)
        assertEquals((variable["params"] as DeviceDiagInput).deviceReview, deviceDiagnostics.reviewDetails)
        assertEquals((variable["params"] as DeviceDiagInput).newPrice, tradeInHomeViewModel.tradeInParams.newPrice)
        assertEquals((variable["params"] as DeviceDiagInput).oldPrice, deviceDiagnostics.tradeInPrice)
    }

    /**************************** RequestParamsTest() *******************************************/

    /**************************** processMessage() *******************************************/

    @Test
    fun processMessageException(){
        coEvery { tradeInHomeViewModel.getDiagnosticData(androidIntent) } returns deviceDiagnostics
        coEvery { tradeInHomeViewModel.getProcessMessageRequestParam(deviceDiagnostics) } returns HashMap()

        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { repository.getGQLData(any(), DeviceDiagInputResponse::class.java, any()) } throws Exception("check warningmessage value is called when exception thrown in Process Message")

        tradeInHomeViewModel.processMessage(androidIntent, anyString())

        assertEquals(tradeInHomeViewModel.warningMessage.value, "check warningmessage value is called when exception thrown in Process Message")
    }

    @Test
    fun processMessage(){
        coEvery { tradeInHomeViewModel.getDiagnosticData(androidIntent) } returns deviceDiagnostics
        coEvery { tradeInHomeViewModel.getProcessMessageRequestParam(deviceDiagnostics) } returns HashMap()

        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { repository.getGQLData(any(), DeviceDiagInputResponse::class.java, any()) } returns response

        tradeInHomeViewModel.processMessage(androidIntent, anyString())

        coVerify { tradeInHomeViewModel.setDiagnoseResult(any(), any()) }
    }

    /**************************** processMessage() *******************************************/

   /**************************** setDiagnoseResult() *******************************************/

    @Test
    fun `setDiagnoseResult is Success and Eligible`(){
        every { response?.deviceDiagInputRepsponse?.isEligible } returns true
        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.DIAGNOSED_VALID)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
    }

    @Test
    fun `setDiagnoseResult is Success and not Eligible`(){
        every { response?.deviceDiagInputRepsponse?.isEligible } returns false
        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.DIAGNOSED_INVALID)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDiagnostics.tradeInPrice!!, true))
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
    }

    @Test
    fun `setDiagnoseResult Data is Failure`(){
        every { response?.deviceDiagInputRepsponse } returns null

        tradeInHomeViewModel.setDiagnoseResult(response, deviceDiagnostics)

        assertEquals(tradeInHomeViewModel.homeResultData.value, null)
    }
    /**************************** setDiagnoseResult() *******************************************/

    /**************************** checkMoneyIn() *******************************************/

    @Test
    fun checkMoneyIn(){
        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")
        val userID = "1"
        val modelId = 3
        val deviceId = "4"

        /**Not Eligible Case**/

        mockkStatic(TradeInUtils::class)
        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
        coEvery { application.resources } returns resources
        mockkStatic(GraphqlHelper::class)
        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { moneyInResponse?.response?.message } returns "123"
        coEvery { moneyInResponse?.response?.isEligible } returns false
        coEvery { repository.getUserLoginState().userId } returns userID
        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } returns moneyInResponse

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.tradeInParams.userId, userID.toInt())
        assertEquals(tradeInHomeViewModel.tradeInParams.tradeInType, 2)
        assertEquals(tradeInHomeViewModel.tradeInParams.modelID, modelId)
        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, moneyInResponse?.response?.message)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess, true)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.priceStatus, HomeResult.PriceState.MONEYIN_ERROR)



        /**Eligible case**/

        mockkStatic(TradeInUtils::class)
        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
        coEvery { application.resources } returns resources
        mockkStatic(GraphqlHelper::class)
        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { tradeInHomeViewModel.getRepo() } returns repository
        coEvery { moneyInResponse?.response?.message } returns "123"
        coEvery { moneyInResponse?.response?.isEligible } returns true
        coEvery { moneyInResponse?.response?.usedPrice } returns 10
        coEvery { moneyInResponse?.response?.isUseKyc} returns false
        coEvery { repository.getUserLoginState().userId } returns userID
        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } returns moneyInResponse
        tradeInHomeViewModel.tradeInParams.usedPrice = 10
        tradeInHomeViewModel.tradeInType = 2

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.tradeInParams.userId, userID.toInt())
        assertEquals(tradeInHomeViewModel.tradeInParams.tradeInType, 2)
        assertEquals(tradeInHomeViewModel.tradeInParams.modelID, modelId)
        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
        assertEquals(tradeInHomeViewModel.tradeInParams.deviceId, deviceId)
        assertEquals(tradeInHomeViewModel.tradeInParams.isEligible, 1)
        assertEquals(tradeInHomeViewModel.tradeInParams.usedPrice, 10)
        assertEquals(tradeInHomeViewModel.tradeInParams.isUseKyc, 0)
        assertEquals(HomeResult.PriceState.DIAGNOSED_VALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
    }

    @Test
    fun checkMoneyInException(){
        val jsonObject = JSONObject()
        val userID = "1"
        val modelId = 3
        val deviceId = "4"
        mockkStatic(TradeInUtils::class)
        coEvery { TradeInUtils.getDeviceId(any()) } returns deviceId
        coEvery { application.resources } returns resources
        mockkStatic(GraphqlHelper::class)
        coEvery { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { tradeInHomeViewModel.getRepo() } returns repository

        coEvery { repository.getUserLoginState().userId } returns userID
        coEvery { repository.getGQLData(any(), ValidateTradePDP::class.java, any()) } throws Exception("check warningmessage value is called when exception thrown in checkMoneyIn")

        tradeInHomeViewModel.checkMoneyIn(modelId, jsonObject)

        assertEquals(tradeInHomeViewModel.warningMessage.value, "check warningmessage value is called when exception thrown in checkMoneyIn")
    }

    /**************************** checkMoneyIn() *******************************************/

    /**************************** onError() *******************************************/

    @Test
    fun onError(){
        val jsonObject = JSONObject("{\"message\":\"Error tradein\"}")
        tradeInHomeViewModel.onError(jsonObject)

        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), false)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.displayMessage, "Error tradein")
    }

    /**************************** onError() *******************************************/

    /**************************** onFinished() *******************************************/

    @Test
    fun onFinished(){
        val jsonObject = JSONObject("{\"min_price\":100000,\"max_price\":350000,\"model_id\":49,\"brand\":\"LG\",\"model\":\"Nexus 5\",\"model_display_name\":\"LG Nexus 5\"}")

        /**Diagnosed invalid**/
        tradeInHomeViewModel.tradeInParams.usedPrice = 10
        tradeInHomeViewModel.tradeInParams.setPrice(0)

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.DIAGNOSED_INVALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.deviceDisplayName, "LG Nexus 5")


        /**Not Diagnosed**/
        tradeInHomeViewModel.tradeInParams.usedPrice = 0

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.NOT_DIAGNOSED, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.deviceDisplayName, "LG Nexus 5")


        /**Diagnosed valid**/
        tradeInHomeViewModel.tradeInParams.usedPrice = 10
        tradeInHomeViewModel.tradeInParams.setPrice(20)

        tradeInHomeViewModel.onFinished(jsonObject)

        verify(exactly = 0) { tradeInHomeViewModel.checkMoneyIn(any(), any()) }
        assertEquals(tradeInHomeViewModel.homeResultData.value?.isSuccess(), true)
        assertEquals(HomeResult.PriceState.DIAGNOSED_VALID, tradeInHomeViewModel.homeResultData.value?.priceStatus)
        assertEquals(tradeInHomeViewModel.homeResultData.value?.deviceDisplayName, "LG Nexus 5")

    }

    /**************************** onFinished() *******************************************/

    /**************************** getMaxPrice() *******************************************/

    @Test
    fun getMaxPrice(){
        tradeInHomeViewModel.getMaxPrice(laku6TradeIn, tradeInType)

        assertEquals(tradeInHomeViewModel.tradeInType, tradeInType)
        coVerify { laku6TradeIn.getMinMaxPrice(any()) }
    }

    /**************************** getMaxPrice() *******************************************/

}

