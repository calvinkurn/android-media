package com.tokopedia.entertainment.search.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.data.CategoryModel
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.entertainment.search.data.mapper.SearchMapper
import com.tokopedia.entertainment.search.viewmodel.EventDetailViewModel
import com.tokopedia.entertainment.search.viewmodel.EventLocationViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.lang.Exception
import java.lang.reflect.Type


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class EventDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventDetailViewModel: EventDetailViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository


    val context = mockk<Context>(relaxed = true)
    private val hashSet = HashSet<String>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        eventDetailViewModel = EventDetailViewModel(Dispatchers.Unconfined, graphqlRepository)
    }

    @Test
    fun setCityID_successSetCityID_success(){
        val cityID = "5"
        eventDetailViewModel.setData(cityID)
        assertEquals(cityID, eventDetailViewModel.cityID)
    }

    @Test
    fun setCityID_successSetCityIDDirect_success(){
        val cityID = "5"
        eventDetailViewModel.cityID = cityID
        assertEquals(cityID, eventDetailViewModel.cityID)
    }

    @Test
    fun setCategory_successSetCategoryDirect_success(){
        val category = "Event"
        eventDetailViewModel.category = category
        assertEquals(category, eventDetailViewModel.category)
    }

    @Test
    fun setCategoryModel_successSetCategoryModelDirect_success(){
        val categoryModel = CategoryModel()
        eventDetailViewModel.categoryModel = categoryModel
        assertEquals(categoryModel, eventDetailViewModel.categoryModel)
    }

    @Test
    fun fetchDataCategory_failedFetchDataCategory_failed(){
        val errorGql = GraphqlError()
        errorGql.message = "Error Fetch Data Category"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EventDetailResponse.Data::class.java] = listOf(errorGql)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } coAnswers {
            GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        }

        eventDetailViewModel.getData(query = "")

        Assert.assertNotNull(eventDetailViewModel.errorReport.value)
        Assert.assertEquals(eventDetailViewModel.errorReport.value, errorGql.message)
        assert(!eventDetailViewModel.isItRefreshing.value!!)
        assert(!eventDetailViewModel.isItShimmering.value!!)
        assert(!eventDetailViewModel.showParentView.value!!)
        assert(!eventDetailViewModel.showProgressBar.value!!)
        assert(eventDetailViewModel.showResetFilter.value!!)

    }


    @Test
    fun fetchDataCategory_successFetchDataCategory_page1(){
        eventDetailViewModel.page = "1"

        val dataMock = Gson().fromJson(getJson("category_mock.json"), EventDetailResponse::class.java)
        val categoryModel = CategoryModel()

        val eventGrid = SearchMapper.mapSearchtoGrid(dataMock.data.eventSearch)
        categoryModel.listCategory = SearchMapper.mappingForbiddenID(dataMock.data.eventChildCategory.categories)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventDetailResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)


        eventDetailViewModel.getData(query = "")

        assertEquals(categoryModel, eventDetailViewModel.catLiveData.value)
        assertEquals(eventGrid, eventDetailViewModel.eventLiveData.value)
        assert(!eventDetailViewModel.isItRefreshing.value!!)
        assert(!eventDetailViewModel.isItShimmering.value!!)
        assert(eventDetailViewModel.showParentView.value!!)
        assert(!eventDetailViewModel.showResetFilter.value!!)
    }

    @Test
    fun fetchDataCategory_successFetchDataCategory_putCategoryToQuery(){
        eventDetailViewModel.initCategory = true
        hashSet.add("12")
        val dataMock = Gson().fromJson(getJson("category_mock.json"), EventDetailResponse::class.java)
        val categoryModel = CategoryModel()

        val eventGrid = SearchMapper.mapSearchtoGrid(dataMock.data.eventSearch)
        categoryModel.listCategory = SearchMapper.mappingForbiddenID(dataMock.data.eventChildCategory.categories)
        categoryModel.hashSet = hashSet
        categoryModel.position = 2

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventDetailResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)

        eventDetailViewModel.putCategoryToQuery("12","")

        assertEquals(categoryModel, eventDetailViewModel.catLiveData.value)
        assertEquals(eventGrid, eventDetailViewModel.eventLiveData.value)
        assertEquals(eventDetailViewModel.hashSet, hashSet)
        assert(!eventDetailViewModel.isItRefreshing.value!!)
        assert(!eventDetailViewModel.isItShimmering.value!!)
        assert(eventDetailViewModel.showParentView.value!!)
        assert(!eventDetailViewModel.showResetFilter.value!!)
    }


    @Test
    fun fetchDataCategory_successFetchDataCategory_putCategoryToQueryandHashNotNull(){
        eventDetailViewModel.initCategory = true
        hashSet.add("12")
        val dataMock = Gson().fromJson(getJson("category_mock.json"), EventDetailResponse::class.java)
        val categoryModel = CategoryModel()

        val eventGrid = SearchMapper.mapSearchtoGrid(dataMock.data.eventSearch)
        categoryModel.listCategory = SearchMapper.mappingForbiddenID(dataMock.data.eventChildCategory.categories)
        categoryModel.hashSet = hashSet
        categoryModel.position = 2

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventDetailResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)

        eventDetailViewModel.putCategoryToQuery("12","")

        eventDetailViewModel.getData(query = "")

        assertEquals(categoryModel, eventDetailViewModel.catLiveData.value)
        assertEquals(eventGrid, eventDetailViewModel.eventLiveData.value)
        assertEquals(eventDetailViewModel.hashSet, hashSet)
        assert(!eventDetailViewModel.isItRefreshing.value!!)
        assert(!eventDetailViewModel.isItShimmering.value!!)
        assert(eventDetailViewModel.showParentView.value!!)
        assert(!eventDetailViewModel.showResetFilter.value!!)
    }

    @Test
    fun fetchDataCategory_successFetchDataCategory_clearFilter(){

        val dataMock = Gson().fromJson(getJson("category_mock.json"), EventDetailResponse::class.java)
        val categoryModel = CategoryModel()

        val eventGrid = SearchMapper.mapSearchtoGrid(dataMock.data.eventSearch)
        categoryModel.listCategory = SearchMapper.mappingForbiddenID(dataMock.data.eventChildCategory.categories)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventDetailResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)

        eventDetailViewModel.clearFilter("")

        assertEquals(categoryModel, eventDetailViewModel.catLiveData.value)
        assertEquals(eventGrid, eventDetailViewModel.eventLiveData.value)
        assert(!eventDetailViewModel.isItRefreshing.value!!)
        assert(!eventDetailViewModel.isItShimmering.value!!)
        assert(eventDetailViewModel.showParentView.value!!)
        assert(!eventDetailViewModel.showResetFilter.value!!)
    }


    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }

    @Test
    fun checkHashPutCategory_HashRemovedId(){
        //given
        hashSet.clear()
        eventDetailViewModel.hashSet.add("12")

        //when
        eventDetailViewModel.putCategoryToQuery("12", "")

        //then
        assertEquals(eventDetailViewModel.hashSet, hashSet)
    }

    @Test
    fun checkHashPutCategory_CategoryNotOnlyOne(){
        //given
        eventDetailViewModel.hashSet.add("12")

        //when
        eventDetailViewModel.putCategoryToQuery("13", "")

        //then
        assertEquals(eventDetailViewModel.category, "12,13")
    }
}