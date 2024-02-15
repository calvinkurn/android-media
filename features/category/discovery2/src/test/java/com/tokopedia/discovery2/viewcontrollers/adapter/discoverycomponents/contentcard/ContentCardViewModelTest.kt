package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.contentCardUseCase.ContentCardUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContentCardViewModelTest {

    companion object {
        private const val PAGE_ENDPOINT = "testing-jeel"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val application = mockk<Application>()
    private val component = mockk<ComponentsItem>(relaxed = true)

    private val useCase: ContentCardUseCase by lazy { mockk(relaxed = true) }

    private lateinit var viewModel: ContentCardViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()

        viewModel = spyk(ContentCardViewModel(application, component, 99))

        viewModel.contentCardUseCase = useCase
    }

    @Test
    fun `given loadContentCard is called, should get content card component data`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns PAGE_ENDPOINT

        viewModel.loadContentCard()

        coVerify(exactly = 1) { useCase.loadFirstPageComponents(componentId, PAGE_ENDPOINT) }
    }

//    @Test
//    fun `given successfully get content card, should post the success result`() {
//        val componentId = (0..Integer.MAX_VALUE).random().toString()
//
//        coEvery { component.id } returns componentId
//
//        coEvery { component.pageEndPoint } returns PAGE_ENDPOINT
//
//        val componentsItems = arrayListOf<ComponentsItem>()
//        componentsItems.add(ComponentsItem(name = ComponentNames.ContentCardItem.componentName))
//
//        coEvery { component.getComponentsItem() } returns componentsItems
//
//        coEvery {
//            useCase.loadFirstPageComponents(
//                componentId,
//                PAGE_ENDPOINT
//            )
//        } returns true
//
//        viewModel.loadContentCard()
//
//        Assert.assertTrue(viewModel.contentCardList.value is Success)
//        Assert.assertEquals(
//            componentsItems[0],
//            (viewModel.contentCardList.value as Success).data[0]
//        )
//    }

    @Test
    fun `given failed to get content card, should post the fail result`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns PAGE_ENDPOINT

        coEvery { useCase.loadFirstPageComponents(componentId, PAGE_ENDPOINT) } throws Throwable()

        viewModel.loadContentCard()

        Assert.assertTrue(viewModel.contentCardList.value is Fail)
    }

//    @Test
//    fun `given failed to get content card due to null result, should post the fail result`() {
//        val componentId = (0..Integer.MAX_VALUE).random().toString()
//
//        coEvery { component.id } returns componentId
//
//        coEvery { component.pageEndPoint } returns PAGE_ENDPOINT
//
//        coEvery {
//            useCase.loadFirstPageComponents(
//                componentId,
//                PAGE_ENDPOINT
//            )
//        } returns false
//
//        viewModel.loadContentCard()
//
//        Assert.assertTrue(viewModel.contentCardList.value is Fail)
//        Assert.assertEquals(
//            "Empty Data",
//            (viewModel.contentCardList.value as Fail).throwable.message.toString()
//        )
//    }

//    @Test
//    fun `given successfully get content card but empty result, should post the fail result`() {
//        val componentId = (0..Integer.MAX_VALUE).random().toString()
//
//        coEvery { component.id } returns componentId
//
//        coEvery { component.pageEndPoint } returns PAGE_ENDPOINT
//
//        val componentsItems = arrayListOf<ComponentsItem>()
//
//        coEvery { component.getComponentsItem() } returns componentsItems
//
//        coEvery {
//            useCase.loadFirstPageComponents(
//                componentId,
//                PAGE_ENDPOINT
//            )
//        } returns true
//
//        viewModel.loadContentCard()
//
//        Assert.assertTrue(viewModel.contentCardList.value is Fail)
//        Assert.assertEquals(
//            "Empty Data",
//            (viewModel.contentCardList.value as Fail).throwable.message.toString()
//        )
//    }
}
