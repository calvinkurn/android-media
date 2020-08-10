package com.tokopedia.entertainment.search.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
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

    @MockK
    lateinit var resources: Resources

    val context = mockk<Context>(relaxed = true)
    private val hashSet = HashSet<String>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        eventDetailViewModel = EventDetailViewModel(Dispatchers.Unconfined, graphqlRepository)
        eventDetailViewModel.resources = context.resources
        setFirstHash()
    }

    private fun setFirstHash(){
        hashSet.add("30")
        hashSet.add("40")
        hashSet.add("20")
        hashSet.remove("40")
        hashSet.add("31")
        hashSet.add("44")
    }

    @Test
    fun putCategory(){
        eventDetailViewModel.putCategoryToQuery("30")
        eventDetailViewModel.putCategoryToQuery("40")
        eventDetailViewModel.putCategoryToQuery("20")
        eventDetailViewModel.putCategoryToQuery("40")
        eventDetailViewModel.putCategoryToQuery("31")
        eventDetailViewModel.putCategoryToQuery("44")

        Assert.assertEquals(eventDetailViewModel.hashSet, hashSet)
    }

    @Test
    fun getDataGQL(){
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventDetailViewModel.resources)

        val dataMock = Gson().fromJson(getJson("category_mock.json"), EventDetailResponse::class.java)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(mapOf(
                EventDetailResponse.Data::class.java to dataMock.data
        )as MutableMap<Type, Any>,  HashMap<Type, List<GraphqlError>>(), false)
        Assert.assertNotNull(dataMock)

        runBlocking(Dispatchers.Unconfined){
            try {
                val data = eventDetailViewModel.getQueryData(CacheType.CACHE_FIRST)
                Assert.assertNotNull(data)
                Assert.assertEquals(dataMock.data, data)
                Assert.assertEquals(data.eventChildCategory.categories.size, 7)
                Assert.assertEquals(data.eventSearch.products.size.toString(), data.eventSearch.count)
            }catch (e: Exception){
                println(e.message)
            }
        }
    }

    @Test
    fun categoryIsDiffTest(){
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(eventDetailViewModel.resources)

        var event = EventDetailResponse.Data.EventChildCategory()
        var eventData: MutableList<EventDetailResponse.Data.EventChildCategory.CategoriesItem> = mutableListOf()

        for (i in 1..5){
            eventData.add(EventDetailResponse.Data.EventChildCategory.CategoriesItem())
        }
        event.categories = eventData

        Assert.assertEquals(eventDetailViewModel.categoryIsDifferentOrEmpty(event), true) // First case

        for(i in 1..5){
            eventDetailViewModel.categoryData.add(CategoryTextBubbleAdapter.CategoryTextBubble("1","TESTONLY"))
        }

        Assert.assertEquals(eventDetailViewModel.categoryIsDifferentOrEmpty(event), true) // Second case

        eventDetailViewModel.hashSet.add("aaa")

        Assert.assertEquals(eventDetailViewModel.categoryIsDifferentOrEmpty(event), true) // third case

        eventDetailViewModel.hashSet.clear()
        event = EventDetailResponse.Data.EventChildCategory()
        eventDetailViewModel.categoryData.clear()
        eventData.clear()

        eventDetailViewModel.hashSet.add("14")
        eventDetailViewModel.hashSet.add("3")

        eventDetailViewModel.categoryData.add(CategoryTextBubbleAdapter.CategoryTextBubble("14", "Test"))
        eventDetailViewModel.categoryData.add(CategoryTextBubbleAdapter.CategoryTextBubble("3", "Test1"))

        eventData.add(EventDetailResponse.Data.EventChildCategory.CategoriesItem(id = "14",title = "Test"))
        eventData.add(EventDetailResponse.Data.EventChildCategory.CategoriesItem(id = "3",title = "Test1"))
        event.categories = eventData

        Assert.assertEquals(eventDetailViewModel.categoryIsDifferentOrEmpty(event), true) // fourth casew
    }

    private fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}