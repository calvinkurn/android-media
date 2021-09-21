package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.CategoryNavigationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryNavigationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val testString = "testData"
    private val viewModel: CategoryNavigationViewModel by lazy {
        spyk(CategoryNavigationViewModel(application, componentsItem, 0))
    }
    private val useCase: CategoryNavigationUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test if title is available or not`() {
        every { componentsItem.title } returns null
        assert((viewModel.getTitle().value as Success).data == "")
        every { componentsItem.title } returns testString
        assert((viewModel.getTitle().value as Success).data == testString)
    }

    @Test
    fun `test if ImageUrl is available or not`() {
        every { componentsItem.data } returns null
        assert((viewModel.getImageUrl().value as Success).data == "")
        val list = ArrayList<DataItem>()
        val dataItem: DataItem = mockk()
        list.add(dataItem)
        every { componentsItem.data } returns list
        every { dataItem.backgroundImageApps } returns null
        assert((viewModel.getImageUrl().value as Success).data == "")
        every { dataItem.backgroundImageApps } returns testString
        assert((viewModel.getImageUrl().value as Success).data == testString)
    }

    @Test
    fun `test getCategoryNavigationData and cases for success and failure`() {
        mockkStatic(Dispatchers::class)
        coEvery { Dispatchers.IO } returns TestCoroutineDispatcher()

        viewModel.categoryNavigationUseCase = useCase
        coEvery { useCase.getCategoryNavigationData(componentsItem.id, componentsItem.pageEndPoint) } throws Exception("Error")
        viewModel.getCategoryNavigationData()
        coVerify { useCase.getCategoryNavigationData(componentsItem.id, componentsItem.pageEndPoint) }
        assert(viewModel.getListData().value is Fail)

        coEvery { useCase.getCategoryNavigationData(componentsItem.id, componentsItem.pageEndPoint) } returns true
        val list = ArrayList<ComponentsItem>()
        every { componentsItem.getComponentsItem() } returns list
        viewModel.getCategoryNavigationData()
        assert(viewModel.getListData().value is Success)
        assert((viewModel.getListData().value as Success).data === list)
    }

}