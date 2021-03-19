package com.tokopedia.topupbills.telco.prepaid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topupbills.telco.JsonToString
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInputMultiTab
import com.tokopedia.topupbills.telco.data.TelcoProduct
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

    lateinit var sharedTelcoPrepaidViewModel: SharedTelcoPrepaidViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        gson = Gson()
        sharedTelcoPrepaidViewModel = SharedTelcoPrepaidViewModel(graphqlRepository, Dispatchers.Unconfined)
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

//    @Test
//    fun setExpandInputNumberView_validData() = runBlockingTest {
//        pauseDispatcher {
//            sharedTelcoPrepaidViewModel.setExpandInputNumberView(true)
//
//            runCurrent()
//            advanceTimeBy(200)
//
//            //then
//            val actualData = sharedTelcoPrepaidViewModel.expandView.value
//            assertEquals(true, actualData)
//        }
//    }

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
    fun setFavNumberSelected_validData() {
        //given
        val favNumber = TopupBillsFavNumberItem(clientNumber = "08123232323")
        //when
        sharedTelcoPrepaidViewModel.setFavNumberSelected(favNumber)
        //then
        val actualData = sharedTelcoPrepaidViewModel.favNumberSelected.value
        assertEquals(favNumber.clientNumber, actualData?.clientNumber)
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
    fun getCatalogProductList_DataValid_SuccessGetData() {
        //given
        val multiTab = gson.fromJson(gson.JsonToString("multitab.json"), TelcoCatalogProductInputMultiTab::class.java)
        val autoSelectProductId = 9

        val result = HashMap<Type, Any>()
        result[TelcoCatalogProductInputMultiTab::class.java] = multiTab
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1", ArrayList(), autoSelectProductId)

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Success)
        val labelPulsa = (actualData as Success).data[0].label
        assertEquals(false, sharedTelcoPrepaidViewModel.loadingProductList.value)
        assertEquals(multiTab.rechargeCatalogProductDataData.productInputList[0].label, labelPulsa)
        assertEquals(autoSelectProductId.toString(), (sharedTelcoPrepaidViewModel.favNumberSelected.value as TopupBillsFavNumberItem).productId)
    }

    @Test
    fun getCatalogProductList_DataValid_FailedGetData() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogProductInputMultiTab::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1", ArrayList(), 9)

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        assertEquals(false, sharedTelcoPrepaidViewModel.loadingProductList.value)
        assertEquals(null, sharedTelcoPrepaidViewModel.favNumberSelected.value)
        assertEquals(errorGql.message, error.message)
    }
}