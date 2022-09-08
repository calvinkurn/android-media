package com.tokopedia.smartbills.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.topupbills.data.RechargeField
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.*
import com.tokopedia.smartbills.data.uimodel.HighlightCategoryUiModel
import com.tokopedia.smartbills.usecase.SmartBillsMultiCheckoutUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class SmartBillsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private lateinit var gqlResponseFail: GraphqlResponse
    private var checkoutField = RechargeField("client_number", "0123456789")
    private val userID = "123"

    private val imageUrl = "https://images.tokopedia.net/img/SnKlQx/2022/1/6/6b657144-bbd1-4504-9cdf-7ff63f998142.png"
    private val contentId = "10"
    private val uuID = "9839483-9483904-49384"
    private val title = "Yuk bayar Pajak PBB 2022"
    private val date = "Tagihan PBB 2022"
    private val desc = "Isi Nomor Objek Pajak"
    private val applink = "tokopedia://bayar-sekaligus"

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var smartBillsMultiCheckoutUseCase: SmartBillsMultiCheckoutUseCase

    @MockK
    lateinit var userSession: UserSessionInterface

    lateinit var smartBillsViewModel: SmartBillsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())
        gqlResponseFail = GraphqlResponse(result, errors, false)

        smartBillsViewModel =
                SmartBillsViewModel(graphqlRepository, smartBillsMultiCheckoutUseCase, CoroutineTestDispatchersProvider)

        coEvery { userSession.userId } returns "0123456"
        coEvery { userSession.deviceId } returns "android-3.70"
    }

    @Test
    fun getStatementMonths_Success() {
        //given
        val statementMonthsResponse = RechargeStatementMonths.Response(listOf(RechargeStatementMonths(0, "April", 4, 2020, true)))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeStatementMonths.Response::class.java
        result[objectType] = statementMonthsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        //when
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //then
        smartBillsViewModel.getStatementMonths(mapParams)
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Success)
        val statementMonths = (actualData as Success).data
        assert(statementMonths.isNotEmpty())
        assertEquals(statementMonths[0].index, 0)
        assert(statementMonths[0].isOngoing)
    }

    @Test
    fun getStatementMonths_Fail_EmptyResponse() {
        //given
        val statementMonthsResponse = RechargeStatementMonths.Response(listOf())
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeStatementMonths.Response::class.java
        result[objectType] = statementMonthsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getStatementMonths(mapParams)

        //then
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun getStatementMonths_Fail_ErrorResponse() {
        //given
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        //when
        smartBillsViewModel.getStatementMonths(mapParams)

        //then
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun getStatementBills_Success() {
        //given
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
            month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  "1", productName = "test", checkoutFields = listOf(checkoutField))
        )
        ))
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeListSmartBills.Response::class.java
        result[objectType] = statementBillsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getStatementBills(mapParams)

        //then
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        val statementBills = (actualData as Success).data
        assertEquals(statementBills.month, "4")
        assertEquals(statementBills.monthText, "April")
        assert(statementBills.isOngoing)
        val bills = statementBills.sections[0].bills
        assert(bills.isNotEmpty())
        assertEquals(bills[0].index, 0)
        assertEquals(bills[0].productID, "1")
        assertEquals(bills[0].productName, "test")
        val checkoutFields = bills[0].checkoutFields
        assert(checkoutFields.isNotEmpty())
        assertEquals(checkoutFields[0], checkoutField)
    }

    @Test
    fun getStatementBills_Success_EmptyResponse() {
        //given
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
                month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf()
        ))
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeListSmartBills.Response::class.java
        result[objectType] = statementBillsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getStatementBills(mapParams)

        //then
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        val statementBills = (actualData as Success).data
        assertEquals(statementBills.month, "4")
        assertEquals(statementBills.monthText, "April")
        assert(statementBills.isOngoing)
        val bills = statementBills.sections[0].bills
        assert(bills.isEmpty())
    }

    @Test
    fun getStatementBills_Fail() {
        //given
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        //when
        smartBillsViewModel.getStatementMonths(mapParams)

        //then
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun getStatementBills_Fail_Error_Default() {
        //given
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeListSmartBills.Response::class.java
        val error =
        """
                {
                    "message": "Mohon Maaf telah terjadi kesalahan",
                    "path": [
                    "rechargeSBMList"
                    ],
                    "extensions": {
                    "code": 500,
                    "developerMessage": "error get base data: [favorite] empty favorite data",
                    "timestamp": "2021-07-01 13:52:46.163173013 +0700 WIB m=+3204.614657061"
                }
                }
        """.trimIndent()

        result[objectType] = null
        val graphqlError = Gson().fromJson(error, GraphqlError::class.java)
        errors[objectType] = listOf(graphqlError)
        val gqlResponseFailDefault = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFailDefault

        //when
        smartBillsViewModel.getStatementBills(mapParams)

        //then
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        assertEquals((actualData as Success).data, RechargeListSmartBills())
    }

    @Test
    fun runMultiCheckout_Success() {
        //given
        val successResponseAttributes = RechargeMultiCheckoutResponse.MultiCheckoutResponseAttributes(true,
                redirectUrl = "https://www.tokopedia.com",
                queryString = "test_query_string"
        )
        val mockMultiCheckoutResponse = RechargeMultiCheckoutResponse("test", "0123456789", successResponseAttributes)
        val dataRechargeMultiCheckoutResponse = DataRechargeMultiCheckoutResponse(mockMultiCheckoutResponse)

        val restResponse = RestResponse(dataRechargeMultiCheckoutResponse, 200, false)
        val dataCheckoutMap = mapOf<Type, RestResponse>(
                DataRechargeMultiCheckoutResponse::class.java to restResponse
        )

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} returns dataCheckoutMap

        //when
        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest(), userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Success)
        val multiCheckoutResponse = (actualData as Success).data
        assertEquals(multiCheckoutResponse.type, "test")
        assertEquals(multiCheckoutResponse.id, "0123456789")
        val responseAttributes = multiCheckoutResponse.attributes
        assert(responseAttributes.allSuccess)
        assertEquals(responseAttributes.redirectUrl, "https://www.tokopedia.com")
        assertEquals(responseAttributes.queryString, "test_query_string")
    }

    @Test
    fun runMultiCheckout_Success_Partial() {
        //given
        val errorResponseAttributes = RechargeMultiCheckoutResponse.MultiCheckoutResponseAttributes(
                false,
                listOf(RechargeMultiCheckoutResponse.Error(0, "1", "error"))
        )
        val mockMultiCheckoutResponse = RechargeMultiCheckoutResponse("test", "0123456789", errorResponseAttributes)
        val dataRechargeMultiCheckoutResponse = DataRechargeMultiCheckoutResponse(mockMultiCheckoutResponse)

        val restResponse = RestResponse(dataRechargeMultiCheckoutResponse, 200, false)
        val dataCheckoutMap = mapOf<Type, RestResponse>(
                DataRechargeMultiCheckoutResponse::class.java to restResponse
        )

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} returns dataCheckoutMap

        //when
        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest(), userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Success)
        val multiCheckoutResponse = (actualData as Success).data
        assertEquals(multiCheckoutResponse.type, "test")
        assertEquals(multiCheckoutResponse.id, "0123456789")
        val responseAttributes = multiCheckoutResponse.attributes
        assertFalse(responseAttributes.allSuccess)
        val errors = responseAttributes.errors
        assert(errors.isNotEmpty())
        val error = errors[0]
        assertEquals(error.index, 0)
        assertEquals(error.productID, "1")
        assertEquals(error.errorMessage, "error")
    }

    @Test
    fun runMultiCheckout_Fail() {
        //given
        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        //when
        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest(), userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_AtributeNotNull() {
        //given
        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        //when
        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest(attributes = MultiCheckoutRequest.
        MultiCheckoutRequestAttributes(identifier = RequestBodyIdentifier())),  userId = userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_GeneratedAtributeNotNull() {
        //given
        val request = MultiCheckoutRequest(attributes = MultiCheckoutRequest.
        MultiCheckoutRequestAttributes(identifier = RequestBodyIdentifier().apply {
            userId = "12345"
        }))

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        //when
        smartBillsViewModel.runMultiCheckout(request, userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_ConverterNUll() {
        //given
        val request = MultiCheckoutRequest(attributes = MultiCheckoutRequest.
        MultiCheckoutRequestAttributes(identifier = RequestBodyIdentifier().apply {
            userId = "12345"
        }))

        val dataCheckoutMap = mapOf<Type, RestResponse?>(
                DataRechargeMultiCheckoutResponse::class.java to null
        )

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} returns dataCheckoutMap

        //when
        smartBillsViewModel.runMultiCheckout(request, userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
    }

    @Test
    fun runMultiCheckout_Fail_NullRequest() {
        //when
        smartBillsViewModel.runMultiCheckout(null, userID)

        //then
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
    }

    @Test
    fun createStatementMonthsParams() {
        //given
        val limit = 1
        //when
        val actual = smartBillsViewModel.createStatementMonthsParams(limit)
        //then
        assertEquals(actual, mapOf(SmartBillsViewModel.PARAM_LIMIT to limit))
    }

    @Test
    fun createStatementBillsParams() {
        //given
        val month = 4
        val year = 2020
        val source = 0

        //when
        val actual = smartBillsViewModel.createStatementBillsParams(month, year, source)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year,
                SmartBillsViewModel.PARAM_SOURCE to source
        ))
    }

    @Test
    fun createStatementBillsParams_NullSource() {
        //given
        val month = 4
        val year = 2020

        //when
        val actual = smartBillsViewModel.createStatementBillsParams(month, year)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year
        ))
    }

    @Test
    fun createMultiCheckoutParams_Success() {
        //given
        val bills = listOf(RechargeBills("123",false, 0, "1", checkoutFields = listOf(checkoutField)))
        //when
        val actual = smartBillsViewModel.createMultiCheckoutParams(bills, userSession)
        //then
        assertNotNull(actual)
        actual?.run {
            val items = actual.attributes.items
            assert(items.isNotEmpty())
            val item = actual.attributes.items[0]
            assertEquals(item.index, 0)
            assertEquals(item.productID, 1)
            val fields = item.fields
            assert(fields.isNotEmpty())
            assertEquals(fields[0], checkoutField)
        }
    }

    @Test
    fun createMultiCheckoutParams_Fail() {
        //given
        val bills = listOf(RechargeBills(productID = "1", checkoutFields = listOf(checkoutField)))
        //when
        val actual = smartBillsViewModel.createMultiCheckoutParams(bills, userSession)
        //then
        assertNull(actual)
    }

    @Test
    fun createActionsParam_Success(){
        //given
        val uuids = listOf<String>("11","22")
        val month = 4
        val year = 2020
        val source = 0

        //when
        val actual = smartBillsViewModel.createRefreshActionParams(uuids, month, year, source)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_UUIDS to uuids,
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year,
                SmartBillsViewModel.PARAM_SOURCE to source
        ))
    }

    @Test
    fun createActionsParam_NullSource(){
        //given
        val uuids = listOf<String>("11","22")
        val month = 4
        val year = 2020

        //when
        val actual = smartBillsViewModel.createRefreshActionParams(uuids, month, year)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_UUIDS to uuids,
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year
        ))
    }

    @Test
    fun getSBMWithAction_Success() {
        //given
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
                month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  "1", productName = "test", checkoutFields = listOf(checkoutField))
        )))))

        val getSBMActionResponse = RechargeMultipleSBMBill.Response(RechargeMultipleSBMBill(
                userID = "1124",
                bills = listOf(
                        RechargeBills(uuid = "122_124", section = Section(title = "Main", type = 2) ,flag = false, index = 1, productID = "2", productName = "testAction", checkoutFields = listOf(checkoutField))
                )
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeMultipleSBMBill.Response::class.java
        result[objectType] = getSBMActionResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getSBMWithAction(mapParams, statementBillsResponse.response!!)

        //then
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        val statementBills = (actualData as Success).data
        assertEquals(statementBills.month, "4")
        assertEquals(statementBills.monthText, "April")
        assert(statementBills.isOngoing)

        val bills = statementBills.sections[0].bills
        assert(bills.isNotEmpty())
        assertEquals(bills[0].index, 0)
        assertEquals(bills[0].productID, "1")
        assertEquals(bills[0].productName, "test")
        val checkoutFields = bills[0].checkoutFields
        assert(checkoutFields.isNotEmpty())
        assertEquals(checkoutFields[0], checkoutField)

        assert(bills.isNotEmpty())
        assertEquals(bills[1].index, 1)
        assertEquals(bills[1].productID, "2")
        assertEquals(bills[1].productName, "testAction")
        val checkoutFieldsAction = bills[1].checkoutFields
        assert(checkoutFieldsAction.isNotEmpty())
        assertEquals(checkoutFieldsAction[0], checkoutField)
    }

    @Test
    fun getSBMWithAction_Fail() {
        //given
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
                month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  "1", productName = "test", checkoutFields = listOf(checkoutField))
        )))))
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        //when
        smartBillsViewModel.getSBMWithAction(mapParams, statementBillsResponse.response!!)

        //then
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Fail)
    }

    @Test
    fun createCatalogIDParam(){
        //given
        val platform = 48

        //when
        val actual = smartBillsViewModel.createCatalogIDParam(platform)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_PLATFORM_ID to platform
        ))
    }

    @Test
    fun createParamDeleteSBM(){
        //given
        val request = RechargeSBMDeleteBillRequest()

        //when
        val actual = smartBillsViewModel.createParamDeleteSBM(request)

        //then
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_DELETE_SBM to request
        ))
    }

    @Test
    fun getCatalogAddBills_Success() {
        //given
        val catalogs = RechargeCatalogMenuAddBills(listOf(SmartBillsCatalogMenu(id = "1")))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeCatalogMenuAddBills::class.java
        result[objectType] = catalogs
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getCatalogAddBills(mapParams)

        //then
        val actualData = smartBillsViewModel.catalogList.value
        assert(actualData is Success)
        val actual = (actualData as Success).data
        Assert.assertNotNull(actual)
        Assert.assertEquals(catalogs.response, actual)
    }

    @Test
    fun getCatalogAddBills_Fail() {
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        smartBillsViewModel.getCatalogAddBills(mapParams)

        val actualData = smartBillsViewModel.catalogList.value
        assert(actualData is Fail)
    }

    @Test
    fun deleteProductSBM_Success() {
        //given
        val deleteSBM = RechargeDeleteSBM(RechargeSBMDeleteBill(message = "Berhasil delete"))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeDeleteSBM::class.java
        result[objectType] = deleteSBM
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.deleteProductSBM(mapParams)

        //then
        val actualData = smartBillsViewModel.deleteSBM.value
        assert(actualData is Success)
        val actual = (actualData as Success).data
        Assert.assertNotNull(actual)
        Assert.assertEquals(deleteSBM, actual)
    }

    @Test
    fun deleteProductSBM_Fail() {
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        smartBillsViewModel.deleteProductSBM(mapParams)

        val actualData = smartBillsViewModel.deleteSBM.value
        assert(actualData is Fail)
    }

    @Test
    fun createParamHighlightCategory(){
        //when
        val actual = smartBillsViewModel.createParamHighlightCategory()

        //then
        assertEquals(actual, mapOf(
            SmartBillsViewModel.PARAM_RECHARGE_RECOM to SmartBillsViewModel.RECHARGE_RECOM_TYPE
        ))
    }

    @Test
    fun createParamCloseRecom(){
        //given
        val uuid = "87438-498394-8493849"
        val contentId = "2039"

        //when
        val actual = smartBillsViewModel.createParamCloseRecom(uuid, contentId)

        val request =  RechargeCloseParams(uuid, contentId)
        //then
        assertEquals(actual, mapOf(
            SmartBillsViewModel.PARAM_CLOSE_RECOM to request
        ))
    }

    @Test
    fun checkHighlightIsNotEmpty_and_allDataIsFilled() {
        //given
        val highlightCategory = HighlightCategoryUiModel(
            contentId = contentId,
            uuId = uuID,
            imageUrl = imageUrl,
            title = title,
            date = date,
            desc = desc,
            applink = applink
        )

        //when
        val result = smartBillsViewModel.isHighlightNotEmpty(highlightCategory)

        //then
        assertTrue(result)
    }

    @Test
    fun checkHighlightIsNotEmpty_and_contentIdEmpty() {
        //given
        val highlightCategory = HighlightCategoryUiModel(
            uuId = uuID,
            imageUrl = imageUrl,
            title = title,
            date = date,
            desc = desc,
            applink = applink
        )

        //when
        val result = smartBillsViewModel.isHighlightNotEmpty(highlightCategory)

        //then
        assertFalse(result)
    }

    @Test
    fun checkHighlightIsNotEmpty_and_uuidEmpty() {
        //given
        val highlightCategory = HighlightCategoryUiModel(
            contentId = contentId,
            imageUrl = imageUrl,
            title = title,
            date = date,
            desc = desc,
            applink = applink
        )

        //when
        val result = smartBillsViewModel.isHighlightNotEmpty(highlightCategory)

        //then
        assertFalse(result)
    }

    @Test
    fun checkHighlightIsNotEmpty_and_applinkEmpty() {
        //given
        val highlightCategory = HighlightCategoryUiModel(
            contentId = contentId,
            uuId = uuID,
            imageUrl = imageUrl,
            title = title,
            date = date,
            desc = desc,
        )

        //when
        val result = smartBillsViewModel.isHighlightNotEmpty(highlightCategory)

        //then
        assertFalse(result)
    }

    @Test
    fun getHightlightCategory_dataIsNotEmpty() {
        //given
        val listRecommendation = listOf(
            RechargeRecommendationData(
                contentID = contentId,
                iconURL = imageUrl,
                mainText = title,
                subText = date,
                buttonText = desc,
                applink = applink
            ),
            RechargeRecommendationData(
                contentID = contentId,
                iconURL = imageUrl,
                mainText = title,
                subText = date,
                buttonText = desc,
                applink = applink
            )
        )

        val rechargeRecommendation = RechargeRecommendationResponse(
            RechargeRecommendation(
                uuID,
                listRecommendation
            )
        )

        val actualHighlightCategory = HighlightCategoryUiModel(
            contentId = contentId,
            uuId = uuID,
            imageUrl = imageUrl,
            title = title,
            date = date,
            desc = desc,
            applink = applink
        )

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeRecommendationResponse::class.java
        result[objectType] = rechargeRecommendation
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getHightlightCategory()

        //then
        val actualData = smartBillsViewModel.highlightCategory.value
        assert(actualData is Success)
        assert((actualData as Success).data == actualHighlightCategory)
    }

    @Test
    fun getHightlightCategory_dataIsEmpty() {
        //given

        val rechargeRecommendation = RechargeRecommendationResponse(
            RechargeRecommendation(
                uuID
            )
        )

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeRecommendationResponse::class.java
        result[objectType] = rechargeRecommendation
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.getHightlightCategory()

        //then
        val actualData = smartBillsViewModel.highlightCategory.value
        assert(actualData is Success)
        assert((actualData as Success).data == HighlightCategoryUiModel())
    }

    @Test
    fun getHightlightCategory_failed() {
        //given
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        //when
        smartBillsViewModel.getHightlightCategory()

        //then
        val actualData = smartBillsViewModel.highlightCategory.value
        assert(actualData is Fail)
    }

    @Test
    fun closeHighlight_success() {
        //given
        val closeHighlight = RechargeCloseResponse(RecommendationClose(
            isError = true,
            message =  "Hai"
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeCloseResponse::class.java
        result[objectType] = closeHighlight
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsViewModel.closeHighlight(mapOf())

        //then
        val actualData = smartBillsViewModel.recommendationClose.value
        assert(actualData is Success)
        assert((actualData as Success).data == closeHighlight)
    }

    @Test
    fun closeHighlight_failed() {
        //given
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        //when
        smartBillsViewModel.closeHighlight(mapOf())

        //then
        val actualData = smartBillsViewModel.recommendationClose.value
        assert(actualData is Fail)
    }
}