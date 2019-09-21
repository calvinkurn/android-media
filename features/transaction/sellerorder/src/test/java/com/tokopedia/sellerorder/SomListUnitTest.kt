package com.tokopedia.sellerorder

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2019-09-02.
 */

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class SomListUnitTest {

    private lateinit var viewModel: SomListViewModel
    private val dispatcher = Dispatchers.Unconfined
    private val queryFilterList = "query_filter_list"
    private val filterSuccessJson = "response_filter_list_success.json"
    private val gson = Gson()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var mockLiveDataFilterList: MutableLiveData<Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>>

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        viewModel = SomListViewModel(dispatcher, graphqlRepository)
    }

    @Test
    fun confirmedLoadFilterFunc() {
        //given
        val spy = spyk(viewModel)
        val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(filterSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(jsonResponse, SomListFilter::class.java)
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(SomListFilter::class.java to response),
                mapOf(SomListFilter::class.java to listOf()), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        //when
        runBlocking {
            spy.getFilterList(queryFilterList)
        }

        //then
        coVerify { spy.getFilterList(queryFilterList) }
    }

    @Test
    fun getFilterListNotNull() {
        val spy = spyk(viewModel)
        //given
        coEvery { graphqlRepository.getReseponse(any()) }
        every { mockLiveDataFilterList.value } returns mockk()
        every { spy.filterListResult } returns mockLiveDataFilterList

        coEvery { spy.getFilterList(queryFilterList) } answers {}

        //when
        runBlocking {
            spy.getFilterList(queryFilterList)
        }

        //then
        coVerify { spy.getFilterList(queryFilterList) }
        Assert.assertNotNull(spy.filterListResult.value)
    }

    @Test
    fun confirmedLoadFilterFuncSuccess() {
        //given
        val jsonResponse = this.javaClass.classLoader?.getResourceAsStream(filterSuccessJson)?.readBytes()?.toString(Charsets.UTF_8)
        val response = gson.fromJson(jsonResponse, SomListFilter::class.java)
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(SomListFilter::class.java to response),
                mapOf(SomListFilter::class.java to listOf()), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        //when
        runBlocking {
            viewModel.getFilterList(queryFilterList)
        }

        //then
        Assert.assertTrue(viewModel.filterListResult.value is Success<*>)
        // assert(viewModel.filterListResult.value == response.data.orderFilterSom.statusList)
    }

    @Test
    fun getFilterReturnNull() {
        val spy = spyk(viewModel)

        //given
        coEvery { graphqlRepository.getReseponse(any()) }
        every { mockLiveDataFilterList.value } returns null
        every { spy.filterListResult } returns mockLiveDataFilterList


        coEvery { spy.getFilterList(queryFilterList)} answers {  }

        //when
        runBlocking {
            spy.getFilterList(queryFilterList)
        }

        //then
        coVerify { spy.getFilterList(queryFilterList) }
        Assert.assertEquals(null, spy.filterListResult.value)
    }

    /*@Test
    fun getFilterReturnError() {
        val spy = spyk(viewModel)

        //given
        coEvery { graphqlRepository.getReseponse(any()) }
        every { mockLiveDataFilterList } returns kotlin.Throwable("ERROR")
        every { spy.filterListResult } returns mockLiveDataFilterList
    }*/
}