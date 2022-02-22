package com.tokopedia.smartbills.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.usecase.RechargeCatalogPrefixSelectUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import java.net.UnknownHostException

class SmartBillsAddTelcoViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var rechargeCatalogPrefixSelectUseCase: RechargeCatalogPrefixSelectUseCase

    lateinit var smartBillsAddTelcoViewModel: SmartBillsAddTelcoViewModel

    val menuId = 1
    val platformId = 5
    val operator = "12"
    val clientNumber = "08327499272"
    val productId = "253"
    val emptyMessage = "Maaf data anda kosong"


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())
        gqlResponseFail = GraphqlResponse(result, errors, false)

        smartBillsAddTelcoViewModel =
                SmartBillsAddTelcoViewModel(graphqlRepository, CoroutineTestDispatchersProvider, rechargeCatalogPrefixSelectUseCase)
    }

    @Test
    fun createMenuDetailAddTelcoParams() {
        val actual = smartBillsAddTelcoViewModel.createMenuDetailAddTelcoParams(menuId)
        Assert.assertEquals(actual, mapOf(SmartBillsAddTelcoViewModel.PARAM_MENU_ID to menuId))
    }

    @Test
    fun createAddBillsParams() {
        val request = RechargeSBMAddBillRequest()

        val actual = smartBillsAddTelcoViewModel.createAddBillsParam(request)
        Assert.assertEquals(actual, mapOf(SmartBillsAddTelcoViewModel.PARAM_ADD_REQUEST to request))
    }

    @Test
    fun createCatalogNominal() {
        val actual = smartBillsAddTelcoViewModel.createCatalogNominal(menuId, platformId, operator)
        Assert.assertEquals(actual, mapOf( SmartBillsAddTelcoViewModel.PARAM_MENU_ID to menuId,
                SmartBillsAddTelcoViewModel.PARAM_PLATFORM_ID to platformId,
                SmartBillsAddTelcoViewModel.PARAM_OPERATOR to operator))
    }

    @Test
    fun createInquiryParam(){
        val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
        enquiryParams.add(TopupBillsEnquiryQuery(SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_SOURCE_TYPE, SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_DEVICE_ID, SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_PRODUCT_ID, productId))
        enquiryParams.add(TopupBillsEnquiryQuery(SmartBillsAddTelcoViewModel.ENQUIRY_PARAM_CLIENT_NUMBER, clientNumber))

        val actual = smartBillsAddTelcoViewModel.createInquiryParam(productId, clientNumber)
        Assert.assertEquals(actual, mapOf(SmartBillsAddTelcoViewModel.PARAM_FIELDS to enquiryParams))
    }


    @Test
    fun getAddBillRecharge_Success() {
        //given
        val addBills = RechargeAddBillsData(RechargeAddBill(message = "Anda berhasil menambahkan data"))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeAddBillsData::class.java
        result[objectType] = addBills
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsAddTelcoViewModel.addBill(mapParams)

        //then
        val actualData = smartBillsAddTelcoViewModel.rechargeAddBills.value
        assert(actualData is Success)
        val actual = (actualData as Success).data
        Assert.assertNotNull(actual)
        Assert.assertEquals(addBills, actual)
    }

    @Test
    fun getAddBillRecharge_Fail() {
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        smartBillsAddTelcoViewModel.addBill(mapParams)

        val actualData = smartBillsAddTelcoViewModel.rechargeAddBills.value
        assert(actualData is Fail)
    }

    @Test
    fun createProductAddBills() {
        val categoryName = "Pulsa"
        val operatorName = "Telkom"
        val listProduct = listOf(RechargeProduct("1", RechargeAttributesProduct(
                desc = "Title", pricePlain = 0
        )))
        val listResult = listOf(RechargeAddBillsProductTrackData(0,
                operatorName,
                categoryName,
                "1",
                "Title",
                "",
                "0"))

        val actual = smartBillsAddTelcoViewModel.getProductTracker(listProduct, categoryName, operatorName)
        Assert.assertEquals(actual, listResult)
    }

    @Test
    fun getProductByCategoryId_Success(){
        val listProducts = listOf(RechargeProduct("1", RechargeAttributesProduct(
                desc = "Title", pricePlain = 0
        )))
        val listProductAll = listOf(RechargeCatalogProductInput("1", "Pulsa", product = RechargeCatalogProduct(dataCollections = listOf(RechargeCatalogDataCollection(
                name = "Pulsa",
                products = listProducts
        )))))

        val actual = smartBillsAddTelcoViewModel.getProductByCategoryId(listProductAll, "Pulsa")

        Assert.assertEquals(actual, listProducts)
    }

    @Test
    fun getProductByCategoryId_Multiple(){
        val listProducts = listOf(RechargeProduct("1", RechargeAttributesProduct(
                desc = "Title", pricePlain = 0
        )))
        val listProductAll = listOf(RechargeCatalogProductInput("1", "Roaming", product = RechargeCatalogProduct(dataCollections = listOf(RechargeCatalogDataCollection(
                name = "Roaming",
                products = listProducts
        )))),RechargeCatalogProductInput("2", "Pulsa", product = RechargeCatalogProduct(dataCollections = listOf(RechargeCatalogDataCollection(
                name = "Pulsa",
                products = listProducts
        ))))
        )

        val actual = smartBillsAddTelcoViewModel.getProductByCategoryId(listProductAll, "Pulsa")

        Assert.assertEquals(actual, listProducts)
    }

    @Test
    fun getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix() {
        //given
        val prefixes = mutableListOf<RechargePrefix>()
        for (i in 0..5) {
            prefixes.add(RechargePrefix(i.toString(), (10044 + i).toString()))
        }
        val response = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect(prefixes = prefixes))
        coEvery { rechargeCatalogPrefixSelectUseCase.execute(any(), any(), any()) } coAnswers {
            secondArg<(TelcoCatalogPrefixSelect) -> Unit>().invoke(response)
        }

        //when
        smartBillsAddTelcoViewModel.getPrefixAddTelco(0)

        //then
        assert(smartBillsAddTelcoViewModel.catalogPrefixSelect.value is Success)
        assert((smartBillsAddTelcoViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes.size == 6)
        assert((smartBillsAddTelcoViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes[3].key == "3")
        assert((smartBillsAddTelcoViewModel.catalogPrefixSelect.value as Success).data
                .rechargeCatalogPrefixSelect.prefixes[3].value == "10047")
    }

    @Test
    fun getPrefixOperator_onFail_shouldShowErrorMessage() {
        //given
        coEvery { rechargeCatalogPrefixSelectUseCase.execute(any(), any(), any()) } coAnswers {
            thirdArg<(Throwable) -> Unit>().invoke(UnknownHostException())
        }

        //when
        smartBillsAddTelcoViewModel.getPrefixAddTelco(0)

        //then
        assert(smartBillsAddTelcoViewModel.catalogPrefixSelect.value is Fail)
        assert((smartBillsAddTelcoViewModel.catalogPrefixSelect.value as Fail).throwable is UnknownHostException)
    }

    @Test
    fun getSelectedOperator_onInputNumberValid_shouldUpdateSelectedOperator() {
        //given
        getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix()

        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("10045", "")

        //then
        assert(smartBillsAddTelcoViewModel.catalogPrefixSelect.value is Success)
        assert(smartBillsAddTelcoViewModel.selectedOperator.value?.key ?: "0" == "1")
        assertNull(smartBillsAddTelcoViewModel.inputNumberNotFound.value)
    }

    @Test
    fun getSelectedOperator_onInputNumberValid_shouldShowThrowableMessage() {
        //given
        getPrefixOperator_onSuccess_shouldUpdateOperatorPrefix()

        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("10040", emptyMessage)

        //then
        assert(smartBillsAddTelcoViewModel.catalogPrefixSelect.value is Success)
        assertEquals(smartBillsAddTelcoViewModel.inputNumberNotFound.value, emptyMessage)
    }

    @Test
    fun getSelectedOperator_onInputNumberEmpty_shouldReturnEmpty() {
        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("", emptyMessage)

        //then
        assertNull(smartBillsAddTelcoViewModel.selectedOperator.value)
    }

    @Test
    fun getSelectedOperator_onInputNumberMinimal_shouldReturnEmpty() {
        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("123", emptyMessage)

        //then
        assertNull(smartBillsAddTelcoViewModel.selectedOperator.value)
    }

    @Test
    fun getSelectedOperator_onInputNumberMaximal_shouldReturnEmpty() {
        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("123456789000000000000000", "")

        //then
        assertNull(smartBillsAddTelcoViewModel.selectedOperator.value)
    }

    @Test
    fun getSelectedOperator_onCatalogPrefixSelectFailed_shouldShowErrorMessage() {
        //given
        getPrefixOperator_onFail_shouldShowErrorMessage()

        //when
        smartBillsAddTelcoViewModel.getSelectedOperator("10045", "")

        //then
        //error
        assert(smartBillsAddTelcoViewModel.catalogPrefixSelect.value is Fail)
        assert(smartBillsAddTelcoViewModel.selectedOperator.value == null)
    }

    @Test
    fun getEnquiry_returnSuccessData() {
        // Given
        val topUpEnquiryData = TopupBillsEnquiryData(
                enquiry = TopupBillsEnquiry(
                        status = "DONE",
                        attributes = TopupBillsEnquiryAttribute(
                                userId = "17211378",
                                productId = "559",
                                price = "Rp.800.000.000",
                                pricePlain = 800000000,
                                mainInfoList = listOf(
                                        TopupBillsEnquiryMainInfo("Nomor Telepon", "081288888888"),
                                        TopupBillsEnquiryMainInfo("Tokopedia User", "Tokopedia User"),
                                        TopupBillsEnquiryMainInfo("Total Bayar", "Rp2.000")
                                )

                        )
                )
        )
        val result = HashMap<Type, Any>()
        result[TopupBillsEnquiryData::class.java] = topUpEnquiryData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        smartBillsAddTelcoViewModel.getInquiryData(mapParams)

        // Then
        val actualData = smartBillsAddTelcoViewModel.inquiryData.value
        assertNotNull(actualData)
        assert(actualData is Success)
    }

    @Test
    fun getEnquiry_returnFailData() {
        // Given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TopupBillsEnquiryData::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        smartBillsAddTelcoViewModel.getInquiryData(mapParams)

        // Then
        val actualData = smartBillsAddTelcoViewModel.inquiryData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)

    }

    @Test
    fun getCatalogData_returnDatafromGQL(){
        //given
        val productInputs = RechargeCatalogProductInputMultiTabData(multitabData = RechargeCatalogProductInputMultiTab(productInputs = listOf(RechargeCatalogProductInput(label = "Input"))))
        val productOutputs = RechargeCatalogProductInputMultiTabData(multitabData = RechargeCatalogProductInputMultiTab(productInputs = listOf(RechargeCatalogProductInput(label = "Output"))))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeCatalogProductInputMultiTabData::class.java
        result[objectType] = productOutputs
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsAddTelcoViewModel.getCatalogNominal(true, productInputs, mapParams)

        //then
        val actualData = smartBillsAddTelcoViewModel.catalogProduct.value
        assert(actualData is Success)
        val actual = (actualData as Success).data
        Assert.assertNotNull(actual)
        Assert.assertEquals(productOutputs, actual)
    }

    @Test
    fun getCatalogData_returnDatafromLocal(){
        //given
        val productInputs = RechargeCatalogProductInputMultiTabData(multitabData = RechargeCatalogProductInputMultiTab(productInputs = listOf(RechargeCatalogProductInput(label = "Input"))))
        val productOutputs = RechargeCatalogProductInputMultiTabData(multitabData = RechargeCatalogProductInputMultiTab(productInputs = listOf(RechargeCatalogProductInput(label = "Output"))))

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeCatalogProductInputMultiTabData::class.java
        result[objectType] = productOutputs
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        //when
        smartBillsAddTelcoViewModel.getCatalogNominal(false, productInputs, mapParams)

        //then
        val actualData = smartBillsAddTelcoViewModel.catalogProduct.value
        assert(actualData is Success)
        val actual = (actualData as Success).data
        Assert.assertNotNull(actual)
        Assert.assertEquals(productInputs, actual)
    }

    @Test
    fun getCatalog_returnFailData() {
        // Given
        val productInputs = RechargeCatalogProductInputMultiTabData(multitabData = RechargeCatalogProductInputMultiTab(productInputs = listOf(RechargeCatalogProductInput(label = "Input"))))

        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCatalogProductInputMultiTabData::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        smartBillsAddTelcoViewModel.getCatalogNominal(true, productInputs, mapParams)

        // Then
        val actualData = smartBillsAddTelcoViewModel.catalogProduct.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)

    }

}