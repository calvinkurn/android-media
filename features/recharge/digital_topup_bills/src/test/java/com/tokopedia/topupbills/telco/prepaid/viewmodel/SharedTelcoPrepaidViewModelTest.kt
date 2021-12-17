package com.tokopedia.topupbills.telco.prepaid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckout
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topupbills.telco.JsonToString
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInputMultiTab
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class SharedTelcoPrepaidViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    lateinit var sharedTelcoPrepaidViewModel: SharedTelcoPrepaidViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        gson = Gson()
        sharedTelcoPrepaidViewModel = SharedTelcoPrepaidViewModel(graphqlRepository, coroutineTestRule.dispatchers)
    }

    @Test
    fun setProductCatalogSelected_validData() {
        //given
        val productCatalogItem = TelcoProduct(id = "2")
        //when
        sharedTelcoPrepaidViewModel.setProductCatalogSelected(productCatalogItem)
        //then
        val actualData = sharedTelcoPrepaidViewModel.productCatalogItem.value
        assertEquals(productCatalogItem.id, actualData?.id)
    }

    @Test
    fun setProductAutoCheckout_validData() {
        //given
        val telcoProduct = TelcoProduct(id = "3")
        //when
        sharedTelcoPrepaidViewModel.setProductAutoCheckout(telcoProduct)
        //then
        val actualData = sharedTelcoPrepaidViewModel.productAutoCheckout.value
        assertEquals(telcoProduct.id, actualData?.id)
    }

    @Test
    fun setPositionScrollToItem_validData() {
        //given
        val position = 123
        //when
        sharedTelcoPrepaidViewModel.setPositionScrollToItem(position)
        //then
        val actualData = sharedTelcoPrepaidViewModel.positionScrollItem.value
        assertEquals(position, actualData)
    }

    @Test
    fun setVisibilityTotalPrice_validData() {
        //when
        sharedTelcoPrepaidViewModel.setVisibilityTotalPrice(true)
        //then
        val actualData = sharedTelcoPrepaidViewModel.showTotalPrice.value
        assertEquals(true, actualData)
    }

    @Test
    fun setSelectedCategoryViewPager_validData() {
        //given
        val categoryName = "Paket Data"
        //when
        sharedTelcoPrepaidViewModel.setSelectedCategoryViewPager(categoryName)
        //then
        val actualData = sharedTelcoPrepaidViewModel.selectedCategoryViewPager.value
        assertEquals(categoryName, actualData)
    }

    @Test
    fun setSelectedFilter_validData() {
        //given
        val selectedFilter = ArrayList<HashMap<String, Any>>()
        val map = HashMap<String, Any>()
        val valueTag = ArrayList<String>()
        valueTag.add("2")
        valueTag.add("3")
        map["param_name"] = "tag_feature"
        map["values"] = valueTag
        selectedFilter.add(map)

        //when
        sharedTelcoPrepaidViewModel.setSelectedFilter(selectedFilter)

        //then
        val actualData = sharedTelcoPrepaidViewModel.selectedFilter.value
        assertEquals(selectedFilter[0].getValue("values"), actualData?.get(0)?.getValue("values"))
    }

    @Test
    fun clearCatalogProductList() {
        //given
        val clientNumber = "08152832"
        val multiTab = gson.fromJson(gson.JsonToString("multitab.json"), TelcoCatalogProductInputMultiTab::class.java)
        val autoSelectProductId = 9

        val result = HashMap<Type, Any>()
        result[TelcoCatalogProductInputMultiTab::class.java] = multiTab
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1", ArrayList(), autoSelectProductId, clientNumber)

        // when
        sharedTelcoPrepaidViewModel.clearCatalogProductList()

        // then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assert(actualData is Success)
        assert((actualData as Success).data.isEmpty())

    }

    @Test
    fun getCatalogProductList_DataValid_SuccessGetData() {
        //given
        val clientNumber = "08152832"
        val multiTab = gson.fromJson(gson.JsonToString("multitab.json"), TelcoCatalogProductInputMultiTab::class.java)
        val autoSelectProductId = 9

        val result = HashMap<Type, Any>()
        result[TelcoCatalogProductInputMultiTab::class.java] = multiTab
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1", ArrayList(), autoSelectProductId, clientNumber)

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Success)
        val labelPulsa = (actualData as Success).data[0].label
        assertEquals(false, sharedTelcoPrepaidViewModel.loadingProductList.value)
        assertEquals(multiTab.rechargeCatalogProductDataData.productInputList[0].label, labelPulsa)
        assertEquals(autoSelectProductId.toString(), sharedTelcoPrepaidViewModel.selectedProductById.value)
    }

    @Test
    fun getCatalogProductList_DataValid_FailedGetData() {
        //given
        val clientNumber = "08152832"
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogProductInputMultiTab::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1", ArrayList(),clientNumber = clientNumber)

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        assertEquals(false, sharedTelcoPrepaidViewModel.loadingProductList.value)
        assertEquals(null, sharedTelcoPrepaidViewModel.selectedProductById.value)
        assertEquals(errorGql.message, error.message)
    }

    @Test
    fun setExpandInputNumberView_shouldShowCorrectData() = coroutineTestRule.runBlockingTest {
        //given
        sharedTelcoPrepaidViewModel.setExpandInputNumberView(true)
        advanceTimeBy(500L)

        //then
        assert(sharedTelcoPrepaidViewModel.expandView.value ?: false)
    }

    @Test
    fun setInputWidgetFocus_shouldShowCorrectData() {
        //given
        sharedTelcoPrepaidViewModel.setInputWidgetFocus(true)

        //then
        assert(sharedTelcoPrepaidViewModel.inputWidgetFocus.value ?: false)
    }

    @Test
    fun setProductListShimmer_shouldShowCorrectData() {
        //given
        sharedTelcoPrepaidViewModel.setProductListShimmer(true)

        //then
        assert(sharedTelcoPrepaidViewModel.loadingProductList.value ?: false)
    }

    @Test
    fun setSelectedProductById_shouldShowCorrectData() {
        //given
        sharedTelcoPrepaidViewModel.setSelectedProductById("123")

        //then
        assert(sharedTelcoPrepaidViewModel.selectedProductById.value == "123")
    }

    @Test
    fun setResetSelectedProduct_shouldShowCorrectData() {
        //given
        sharedTelcoPrepaidViewModel.resetSelectedProduct()

        //then
        assert(sharedTelcoPrepaidViewModel.resetSelectedProduct.value ?: false)
    }
}
