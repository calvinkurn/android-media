package com.tokopedia.smartbills.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.topupbills.data.RechargeField
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.smartbills.data.*
import com.tokopedia.smartbills.usecase.SmartBillsMultiCheckoutUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Type

class SmartBillsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private lateinit var gqlResponseFail: GraphqlResponse
    private var checkoutField = RechargeField("client_number", "0123456789")

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
        val statementMonthsResponse = RechargeStatementMonths.Response(listOf(RechargeStatementMonths(0, "April", 4, 2020, true)))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeStatementMonths.Response::class.java
        result[objectType] = statementMonthsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

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
        val statementMonthsResponse = RechargeStatementMonths.Response(listOf())
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeStatementMonths.Response::class.java
        result[objectType] = statementMonthsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        smartBillsViewModel.getStatementMonths(mapParams)
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun getStatementMonths_Fail_ErrorResponse() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        smartBillsViewModel.getStatementMonths(mapParams)
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun getStatementBills_Success() {
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
            month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  1, productName = "test", checkoutFields = listOf(checkoutField))
        )
        ))
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeListSmartBills.Response::class.java
        result[objectType] = statementBillsResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        smartBillsViewModel.getStatementBills(mapParams)
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        val statementBills = (actualData as Success).data
        assertEquals(statementBills.month, "4")
        assertEquals(statementBills.monthText, "April")
        assert(statementBills.isOngoing)
        val bills = statementBills.sections[0].bills
        assert(bills.isNotEmpty())
        assertEquals(bills[0].index, 0)
        assertEquals(bills[0].productID, 1)
        assertEquals(bills[0].productName, "test")
        val checkoutFields = bills[0].checkoutFields
        assert(checkoutFields.isNotEmpty())
        assertEquals(checkoutFields[0], checkoutField)
    }

    @Test
    fun getStatementBills_Success_EmptyResponse() {
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

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        smartBillsViewModel.getStatementBills(mapParams)
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
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        smartBillsViewModel.getStatementMonths(mapParams)
        val actualData = smartBillsViewModel.statementMonths.value
        assert(actualData is Fail)
    }

    @Test
    fun runMultiCheckout_Success() {
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

        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest())

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
        val errorResponseAttributes = RechargeMultiCheckoutResponse.MultiCheckoutResponseAttributes(
                false,
                listOf(RechargeMultiCheckoutResponse.Error(0, 1, "error"))
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

        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest())
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
        assertEquals(error.productID, 1)
        assertEquals(error.errorMessage, "error")
    }

    @Test
    fun runMultiCheckout_Fail() {

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest())

        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_AtributeNotNull() {

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        smartBillsViewModel.runMultiCheckout(MultiCheckoutRequest(attributes = MultiCheckoutRequest.
        MultiCheckoutRequestAttributes(identifier = RequestBodyIdentifier().apply {
            userId = "12345"
        })))

        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_GeneratedAtributeNotNull() {

        val request = MultiCheckoutRequest(attributes = MultiCheckoutRequest.
        MultiCheckoutRequestAttributes(identifier = RequestBodyIdentifier().apply {
            userId = "12345"
        }))

        coEvery { smartBillsMultiCheckoutUseCase.setHeader(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.setParam(any())} returns Unit
        coEvery { smartBillsMultiCheckoutUseCase.executeOnBackground()} throws MessageErrorException("error")

        smartBillsViewModel.runMultiCheckout(request)

        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, "error")
    }

    @Test
    fun runMultiCheckout_ConverterNUll() {
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

        smartBillsViewModel.runMultiCheckout(request)

        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
    }

    @Test
    fun runMultiCheckout_Fail_NullRequest() {
        smartBillsViewModel.runMultiCheckout(null)
        val actualData = smartBillsViewModel.multiCheckout.value
        assert(actualData is Fail)
    }

    @Test
    fun createStatementMonthsParams() {
        val limit = 1
        val actual = smartBillsViewModel.createStatementMonthsParams(limit)
        assertEquals(actual, mapOf(SmartBillsViewModel.PARAM_LIMIT to limit))
    }

    @Test
    fun createStatementBillsParams() {
        val month = 4
        val year = 2020
        val source = 0
        val actual = smartBillsViewModel.createStatementBillsParams(month, year, source)
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year,
                SmartBillsViewModel.PARAM_SOURCE to source
        ))
    }

    @Test
    fun createStatementBillsParams_NullSource() {
        val month = 4
        val year = 2020
        val actual = smartBillsViewModel.createStatementBillsParams(month, year)
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year
        ))
    }

    @Test
    fun createMultiCheckoutParams_Success() {
        val bills = listOf(RechargeBills("123",false, 0, 1, checkoutFields = listOf(checkoutField)))
        val actual = smartBillsViewModel.createMultiCheckoutParams(bills, userSession)
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
        val bills = listOf(RechargeBills(productID = 1, checkoutFields = listOf(checkoutField)))
        val actual = smartBillsViewModel.createMultiCheckoutParams(bills, userSession)
        assertNull(actual)
    }

    @Test
    fun createActionsParam_Success(){
        val uuids = listOf<String>("11","22")
        val month = 4
        val year = 2020
        val source = 0
        val actual = smartBillsViewModel.createRefreshActionParams(uuids, month, year, source)
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_UUIDS to uuids,
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year,
                SmartBillsViewModel.PARAM_SOURCE to source
        ))
    }

    @Test
    fun createActionsParam_NullSource(){
        val uuids = listOf<String>("11","22")
        val month = 4
        val year = 2020
        val actual = smartBillsViewModel.createRefreshActionParams(uuids, month, year)
        assertEquals(actual, mapOf(
                SmartBillsViewModel.PARAM_UUIDS to uuids,
                SmartBillsViewModel.PARAM_MONTH to month,
                SmartBillsViewModel.PARAM_YEAR to year
        ))
    }

    @Test
    fun getSBMWithAction_Success() {
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
                month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  1, productName = "test", checkoutFields = listOf(checkoutField))
        )))))

        val getSBMActionResponse = RechargeMultipleSBMBill.Response(RechargeMultipleSBMBill(
                userID = "1124",
                bills = listOf(
                        RechargeBills(uuid = "122_124", section = Section(title = "Main", type = 2) ,flag = false, index = 1, productID =  2, productName = "testAction", checkoutFields = listOf(checkoutField))
                )
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeMultipleSBMBill.Response::class.java
        result[objectType] = getSBMActionResponse
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        smartBillsViewModel.getSBMWithAction(mapParams, statementBillsResponse.response!!)
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Success)
        val statementBills = (actualData as Success).data
        assertEquals(statementBills.month, "4")
        assertEquals(statementBills.monthText, "April")
        assert(statementBills.isOngoing)

        val bills = statementBills.sections[0].bills
        assert(bills.isNotEmpty())
        assertEquals(bills[0].index, 0)
        assertEquals(bills[0].productID, 1)
        assertEquals(bills[0].productName, "test")
        val checkoutFields = bills[0].checkoutFields
        assert(checkoutFields.isNotEmpty())
        assertEquals(checkoutFields[0], checkoutField)

        assert(bills.isNotEmpty())
        assertEquals(bills[1].index, 1)
        assertEquals(bills[1].productID, 2)
        assertEquals(bills[1].productName, "testAction")
        val checkoutFieldsAction = bills[1].checkoutFields
        assert(checkoutFieldsAction.isNotEmpty())
        assertEquals(checkoutFieldsAction[0], checkoutField)
    }

    @Test
    fun getSBMWithAction_Fail() {
        val statementBillsResponse = RechargeListSmartBills.Response(RechargeListSmartBills(
                month = "4", monthText = "April", isOngoing = true, sections = listOf(Section(
                title = "Section 2", type = 2, bills = listOf(
                RechargeBills(uuid = "122_123",flag = false, index = 0, productID =  1, productName = "test", checkoutFields = listOf(checkoutField))
        )))))
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        smartBillsViewModel.getSBMWithAction(mapParams, statementBillsResponse.response!!)
        val actualData = smartBillsViewModel.statementBills.value
        assert(actualData is Fail)
    }
}