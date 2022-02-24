package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.AnchorTabsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnchorTabsItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: AnchorTabsItemViewModel =
        spyk(AnchorTabsItemViewModel(application, componentsItem, 99))
    private val useCase: AnchorTabsUseCase by lazy {
        mockk()
    }
    private val dataItem: DataItem = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test passed application`() {
        assert(viewModel.application === application)
    }

    @Test
    fun `test for Image Url`() {
        every { componentsItem.data } returns null
        assert(viewModel.getImageUrl().isEmpty())

        val list = arrayListOf<DataItem>()
        every { componentsItem.data } returns list
        assert(viewModel.getImageUrl().isEmpty())

        list.add(dataItem)
        every { dataItem.imageUrlMobile } returns null
        assert(viewModel.getImageUrl().isEmpty())

        every { dataItem.imageUrlMobile } returns ""
        assert(viewModel.getImageUrl().isEmpty())

        every { dataItem.imageUrlMobile } returns "url"
        assert(viewModel.getImageUrl().isNotEmpty())
        assert(viewModel.getImageUrl() == "url")
    }

    @Test
    fun `test for title`() {
        every { componentsItem.data } returns null
        assert(viewModel.getTitle().isEmpty())

        val list = arrayListOf<DataItem>()
        every { componentsItem.data } returns list
        assert(viewModel.getTitle().isEmpty())

        list.add(dataItem)
        every { dataItem.name } returns null
        assert(viewModel.getTitle().isEmpty())

        every { dataItem.name } returns ""
        assert(viewModel.getTitle().isEmpty())

        every { dataItem.name } returns "title"
        assert(viewModel.getTitle().isNotEmpty())
        assert(viewModel.getTitle() == "title")
    }

    @Test
    fun `test for sectionId`() {
        every { componentsItem.data } returns null
        assert(viewModel.getSectionID().isEmpty())

        val list = arrayListOf<DataItem>()
        every { componentsItem.data } returns list
        assert(viewModel.getSectionID().isEmpty())

        list.add(dataItem)
        every { dataItem.targetSectionID } returns null
        assert(viewModel.getSectionID().isEmpty())

        every { dataItem.targetSectionID } returns ""
        assert(viewModel.getSectionID().isEmpty())

        every { dataItem.targetSectionID } returns "9"
        assert(viewModel.getSectionID().isNotEmpty())
        assert(viewModel.getSectionID() == "9")
    }

    @Test
    fun `test for checking if this tab is the selected tab`() {
        viewModel.anchorTabsUseCase = useCase
        every { useCase.selectedId } returns "10"

        every { componentsItem.data } returns null
        assert(!viewModel.isSelected())

        val list = arrayListOf<DataItem>()
        every { componentsItem.data } returns list
        assert(!viewModel.isSelected())

        list.add(dataItem)
        every { dataItem.targetSectionID } returns null
        assert(!viewModel.isSelected())

        every { dataItem.targetSectionID } returns ""
        assert(!viewModel.isSelected())

        every { dataItem.targetSectionID } returns "9"
        assert(!viewModel.isSelected())

        every { dataItem.targetSectionID } returns "10"
        assert(viewModel.isSelected())
    }

    @Test
    fun `test for getting parent position logic`() {
        every { componentsItem.parentComponentPosition } returns 5

        val parentComp: ComponentsItem = mockk()
        mockkStatic(::getComponent)

        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns null
        assert(viewModel.parentPosition() == 5)

        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns parentComp
        every { parentComp.position } returns 10
        assert(viewModel.parentPosition() == 10)
    }

    @Test
    fun `test for resetting refreshComponent key`() {
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val viewModel: AnchorTabsItemViewModel =
            spyk(AnchorTabsItemViewModel(application, componentsItem, 99))
        componentsItem.shouldRefreshComponent = true
        assert(componentsItem.shouldRefreshComponent == true)
        viewModel.onAttachToViewHolder()
        assert(componentsItem.shouldRefreshComponent == null)

    }


}