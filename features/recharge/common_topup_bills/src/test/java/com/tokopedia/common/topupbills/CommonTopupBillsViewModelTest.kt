package com.tokopedia.common.topupbills

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckout
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.RechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.response.CommonTopupbillsDummyData
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_DEVICE_ID
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_PRODUCT_ID
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_SOURCE_TYPE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_DEVICE_ID
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_DEVICE_ID_DEFAULT_VALUE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_INSTANT_CHECKOUT
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_VOUCHER_CODE
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PARAM_CART
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PARAM_FIELDS
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PARAM_FILTERS
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PARAM_MENU_ID
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PLUGIN_PARAM_CATEGORY
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PLUGIN_PARAM_ID
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PLUGIN_PARAM_KEY
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.PLUGIN_PARAM_OPERATOR
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class CommonTopupBillsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var topupBillsViewModel: TopupBillsViewModel

    @RelaxedMockK
    lateinit var digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase

    @RelaxedMockK
    lateinit var rechargeFavoriteNumberUseCase: RechargeFavoriteNumberUseCase

    @RelaxedMockK
    lateinit var contactDataSource: ContactDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        topupBillsViewModel = TopupBillsViewModel(graphqlRepository, digitalCheckVoucherUseCase, rechargeFavoriteNumberUseCase, contactDataSource, testCoroutineRule.dispatchers)
    }

    @Test
    fun getEnquiry_returnSuccessData() {
        // Given
        val topUpEnquiryData = CommonTopupbillsDummyData.getEnquiryDataSuccess()
        val result = HashMap<Type, Any>()
        result[TopupBillsEnquiryData::class.java] = topUpEnquiryData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getEnquiry("", listOf())

        // Then
        val actualData = topupBillsViewModel.enquiryData.value
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
        topupBillsViewModel.getEnquiry("", listOf())

        // Then
        val actualData = topupBillsViewModel.enquiryData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }

    @Test
    fun getMenuDetail_returnSuccessData() {
        // Given
        val telcoCatalogMenuDetailData = CommonTopupbillsDummyData.getMenuDetailSuccess()
        val result = HashMap<Type, Any>()
        result[TelcoCatalogMenuDetailData::class.java] = telcoCatalogMenuDetailData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getMenuDetail("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.menuDetailData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)
    }

    @Test
    fun getMenuDetail_returnFailData() {
        // Given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogMenuDetailData::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getMenuDetail("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.menuDetailData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }

    @Test
    fun getCatalogPluginData_returnSuccessData() {
        // Given
        val rechargeCatalogPluginData = CommonTopupbillsDummyData.getRechargeCatalogPluginSuccess()
        val result = HashMap<Type, Any>()
        result[RechargeCatalogPlugin.Response::class.java] = rechargeCatalogPluginData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getCatalogPluginData("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.catalogPluginData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)
    }

    @Test
    fun getCatalogPluginData_returnNullData() {
        // Given
        val rechargeCatalogPluginData = CommonTopupbillsDummyData.getRechargeCatalogPluginSuccess(isNull = true)
        val result = HashMap<Type, Any>()
        result[RechargeCatalogPlugin.Response::class.java] = rechargeCatalogPluginData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getCatalogPluginData("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.catalogPluginData.value
        assert(actualData is Fail)

        val resultData = (actualData as Fail).throwable
        Assert.assertEquals(resultData.message, "null response")
    }

    @Test
    fun getCatalogPluginData_returnNonNullFailData() {
        // Given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCatalogPlugin.Response::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getCatalogPluginData("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.catalogPluginData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }

    @Test
    fun getFavoriteNumber_returnSuccessData() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.getFavoriteNumberSuccess()
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } returns favoriteNumber

        // When
        topupBillsViewModel.getFavoriteNumbers(listOf())

        // Then
        val actualData = topupBillsViewModel.favNumberData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData[0].clientNumber == "081288888888")
    }

    @Test
    fun getFavoriteNumber_returnFailData() {
        // Given
        val throwable = MessageErrorException("error")
        coEvery { rechargeFavoriteNumberUseCase.executeOnBackground() } throws throwable

        // When
        topupBillsViewModel.getFavoriteNumbers(listOf())

        // Then
        val actualData = topupBillsViewModel.favNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(throwable.message, error.message)
    }

    @Test
    fun getSeamlessFavoriteNumbers_returnSuccessData() {
        // Given
        val favoriteNumber = CommonTopupbillsDummyData.getSeamlessFavoriteNumberSuccess()
        val result = HashMap<Type, Any>()
        result[TopupBillsSeamlessFavNumberData::class.java] = favoriteNumber
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getSeamlessFavoriteNumbers("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.seamlessFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)

        assertThat(resultData.first.favoriteNumbers[0].clientNumber == "081288888888")
    }

    @Test
    fun getSeamlessFavoriteNumber_returnFailData() {
        // Given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TopupBillsSeamlessFavNumberData::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.getSeamlessFavoriteNumbers("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.seamlessFavNumberData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }

    @Test
    fun createEnquiryParams_returnsCorrectListContent() {
        val params = topupBillsViewModel.createEnquiryParams(
            operatorId = "578",
            productId = "123",
            inputData = mapOf("testing_id" to "1111")
        )
        assertThat(params[0]).isInstanceOf(TopupBillsEnquiryQuery::class.java)
        assertTrue(
            params.any { obj ->
                obj.key == ENQUIRY_PARAM_SOURCE_TYPE && obj.value == ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE
            }
        )
        assertTrue(
            params.any { obj ->
                obj.key == ENQUIRY_PARAM_DEVICE_ID && obj.value == ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE
            }
        )
        assertTrue(
            params.any { obj ->
                obj.key == ENQUIRY_PARAM_PRODUCT_ID && obj.value == "123"
            }
        )
        assertTrue(
            params.any { obj ->
                obj.key == "testing_id" && obj.value == "1111"
            }
        )
    }

    @Test
    fun createMenuDetailParams_returnsCorrectMapContent() {
        val params = topupBillsViewModel.createMenuDetailParams(5)
        assertTrue(params.containsKey(PARAM_MENU_ID))
        assertTrue(params[PARAM_MENU_ID] == 5)
    }

    @Test
    fun createCatalogPluginParams_returnsCorrectMapContent() {
        val params = topupBillsViewModel.createCatalogPluginParams(578, 34)
        assertTrue(params.containsKey(PARAM_FILTERS))

        val filters = params[PARAM_FILTERS] as MutableList<Map<String, Any>>
        assertTrue(filters.any { obj -> obj.containsKey(PLUGIN_PARAM_KEY) })

        assertTrue(
            filters.any { obj ->
                obj.containsKey(PLUGIN_PARAM_KEY) && obj[PLUGIN_PARAM_KEY] == PLUGIN_PARAM_OPERATOR
            }
        )
        assertTrue(
            filters.any { obj ->
                obj.containsKey(PLUGIN_PARAM_KEY) && obj[PLUGIN_PARAM_KEY] == PLUGIN_PARAM_CATEGORY
            }
        )

        assertTrue(
            filters.any { obj ->
                obj.containsKey(PLUGIN_PARAM_ID) && obj[PLUGIN_PARAM_ID] == 578
            }
        )
        assertTrue(
            filters.any { obj ->
                obj.containsKey(PLUGIN_PARAM_ID) && obj[PLUGIN_PARAM_ID] == 34
            }
        )
    }

    @Test
    fun createExpressCheckoutParams_defaultParam_returnsCorrectMapContent() {
        val params = topupBillsViewModel.createExpressCheckoutParams(5, mapOf())
        assertTrue(params.containsKey(PARAM_CART))

        val innerParams = params[PARAM_CART] as MutableMap<String, Any>
        assertTrue(
            innerParams.any { obj ->
                obj.key == PARAM_FIELDS && obj.value == mutableListOf<Map<String, String>>()
            }
        )
        assertTrue(
            innerParams.any { obj ->
                obj.key == EXPRESS_PARAM_INSTANT_CHECKOUT && obj.value == false
            }
        )
        assertTrue(
            innerParams.any { obj ->
                obj.key == EXPRESS_PARAM_VOUCHER_CODE && obj.value == ""
            }
        )
        assertTrue(
            innerParams.any { obj ->
                obj.key == EXPRESS_PARAM_DEVICE_ID && obj.value == EXPRESS_PARAM_DEVICE_ID_DEFAULT_VALUE
            }
        )
        assertTrue(
            innerParams.any { obj ->
                obj.key == TopupBillsViewModel.EXPRESS_PARAM_ADD_TO_BILLS && obj.value == false
            }
        )
        assertTrue(
            innerParams.any { obj ->
                obj.key == TopupBillsViewModel.EXPRESS_PARAM_CHECK_OTP && obj.value == false
            }
        )
    }

    @Test
    fun createExpressCheckoutFieldParam_expressCheckoutInputNotEmpty_isCalled() {
        val topupBillsViewModelSpyk = spyk(TopupBillsViewModel(graphqlRepository, digitalCheckVoucherUseCase, rechargeFavoriteNumberUseCase, contactDataSource, testCoroutineRule.dispatchers), recordPrivateCalls = true)

        every { topupBillsViewModelSpyk["createExpressCheckoutFieldParam"](allAny<String>(), allAny<String>()) } returns mapOf<String, String>()

        topupBillsViewModelSpyk.createExpressCheckoutParams(5, mapOf("test1" to "test2"))

        verify { topupBillsViewModelSpyk["createExpressCheckoutFieldParam"](allAny<String>(), allAny<String>()) }
    }

    @Test
    fun processExpressCheckout_returnsSuccessData() {
        // Given
        val expressCheckout = CommonTopupbillsDummyData.getRechargeExpressCheckoutSuccess()
        val result = HashMap<Type, Any>()
        result[RechargeExpressCheckout.Response::class.java] = expressCheckout
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.processExpressCheckout("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.expressCheckoutData.value
        assertNotNull(actualData)
        assert(actualData is Success)

        val resultData = (actualData as Success).data
        assertNotNull(resultData)
    }

    @Test
    fun processExpressCheckout_returnsFailData() {
        // Given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeExpressCheckout.Response::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.processExpressCheckout("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.expressCheckoutData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }

    @Test
    fun processExpressCheckout_returnsNonNullFailData() {
        // Given
        val expressCheckout = CommonTopupbillsDummyData.getRechargeExpressCheckoutNonNullError()
        val result = HashMap<Type, Any>()
        result[RechargeExpressCheckout.Response::class.java] = expressCheckout
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // When
        topupBillsViewModel.processExpressCheckout("", hashMapOf())

        // Then
        val actualData = topupBillsViewModel.expressCheckoutData.value
        assertNotNull(actualData)
        assert(actualData is Fail)

        val error = (actualData as Fail).throwable
        Assert.assertEquals("Testing Error", error.message)
    }

    @Test
    fun checkVoucher_callUseCaseExecute() = testCoroutineRule.runTest {
        every { digitalCheckVoucherUseCase.execute(any(), any()) } returns Unit

        topupBillsViewModel.checkVoucher("", PromoDigitalModel())
        testCoroutineRule.coroutineDispatcher.advanceTimeBy(1_000L)

        verify { digitalCheckVoucherUseCase.execute(any(), any()) }
    }

    @Test
    fun getContactList_returnsCorrectContactData() {
        val fakeContacts = mutableListOf(
            TopupBillsContact("Tokopedia", "081234567890"),
            TopupBillsContact("GoTo", "085600001111")
        )

        every { contactDataSource.getContactList() } returns fakeContacts

        val actualContacts = topupBillsViewModel.getContactList()

        verify { contactDataSource.getContactList() }

        Assert.assertEquals(fakeContacts, actualContacts)
    }
}
