package com.tokopedia.salam.umrah.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductEntity
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahSearchViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var mGraphqlRepository: GraphqlRepository
    private val dispatcher = UmrahDispatchersProviderTest()
    private lateinit var umrahSearchViewModel: UmrahSearchViewModel

    private val umrahSearchProducts = listOf(UmrahSearchProduct())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        umrahSearchViewModel = UmrahSearchViewModel(mGraphqlRepository, dispatcher)
    }

    @Test
    fun getFilterParameter_shouldReturnAsSetValue(){
        // given
        val paramFilter = ParamFilter("Mei","Jakarta",5,10,0,10)
        umrahSearchViewModel.setFilter(paramFilter)

        // when
        val actual = umrahSearchViewModel.getFilter()

        // then
        assert(actual == paramFilter)
    }

    @Test
    fun getSortValue_shouldReturnDefaultValue(){
        // given
        val initSortValue = "2"
        umrahSearchViewModel.initSortValue(initSortValue)

        // when
        val actual = umrahSearchViewModel.getSortValue()

        // then
        assert(actual == initSortValue)
    }

    @Test
    fun getSortValue_shouldReturnAsSetValue(){
        // given
        val setSortValue = "3"
        umrahSearchViewModel.setSortValue(setSortValue)

        // when
        val actual = umrahSearchViewModel.getSortValue()

        // then
        assert(actual == setSortValue)
    }

    @Test
    fun getSearchOrCategory_shouldReturnSearch(){
        // given
        umrahSearchViewModel.setSearchParam(UmrahSearchProductDataParam(""))

        // when
        val actual = umrahSearchViewModel.getSearchOrCategory()

        // then
        assert(actual == SearchOrCategory.SEARCH)
    }

    @Test
    fun getSearchOrCategory_shouldReturnCategory(){
        // given
        umrahSearchViewModel.setSearchParam(UmrahSearchProductDataParam("umroh-hemat"))

        // when
        val actual = umrahSearchViewModel.getSearchOrCategory()

        // then
        assert(actual == SearchOrCategory.CATEGORY)
    }


    @Test
    fun searchUmrahProducts_shouldReturn1Data() {
        // given
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(UmrahSearchProductEntity::class.java to UmrahSearchProductEntity(umrahSearchProducts)),
                mapOf(), false)
        coEvery { mGraphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        // when
        umrahSearchViewModel.searchUmrahProducts(1, "")

        // then the result should be success
        val actual = umrahSearchViewModel.searchResult.value
        assertTrue(actual is Success)
        val listActualData = (actual as Success).data
        assertEquals(umrahSearchProducts.size, listActualData.size)
        assertEquals(umrahSearchProducts[0].id, listActualData[0].id)
    }

    @Test
    fun searchUmrahProducts_shouldReturnFail() {
        // given
        val gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(UmrahSearchProductEntity::class.java to listOf(GraphqlError())), false)
        coEvery { mGraphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        // when
        umrahSearchViewModel.searchUmrahProducts(1, "")

        // then
        val actual = umrahSearchViewModel.searchResult.value
        assertTrue(actual is Fail)
    }

    @Test
    fun searchResetParam_shouldResetParam(){
        umrahSearchViewModel.resetSearchParam()

        val actual = UmrahSearchProductDataParam()
        assert(actual==umrahSearchViewModel.getSearchParam())
    }
}