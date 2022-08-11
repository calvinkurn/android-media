package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.AnchorTabsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnchorTabsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: AnchorTabsViewModel =
        spyk(AnchorTabsViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test passed application`() {
        assert(viewModel.application === application)
    }

    @Test
    fun ` test for list size`(){
        every { componentsItem.getComponentsItem() } returns null
        assert(viewModel.getListSize() == 0)
        val list: MutableList<ComponentsItem> = mutableListOf()
        every { componentsItem.getComponentsItem() } returns list
        assert(viewModel.getListSize() == 0)
        list.add(mockk())
        list.add(mockk())
        every { viewModel.getListSize() == 2 }
    }

    @Test
    fun `test for mapComponents when data items are not present`(){
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application,spykComp,99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        every { spykComp.data } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.selectedSectionPos == Integer.MAX_VALUE)
        assert(viewModel.selectedSectionId == "")
        assert(componentsItem.getComponentsItem().isNullOrEmpty())
        assert(viewModel.getCarouselItemsListData().value == null)
        assert(useCase.selectedId == "")
        assert(spykComp.noOfPagesLoaded == 1)
    }

    @Test
    fun `test for mapComponents when data items present`() {
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        every { spykComp.data } returns dataItems
        viewModel.onAttachToViewHolder()
        assert(viewModel.selectedSectionPos == Integer.MAX_VALUE)
        assert(viewModel.selectedSectionId == "")
        assert(viewModel.getListSize() == 4)
        assert(viewModel.getCarouselItemsListData().value != null)
        assert(useCase.selectedId == "")
        assert(spykComp.noOfPagesLoaded == 1)
    }

    @Test
    fun `update selection test with non click notified`(){
//        setting up data before test
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        every { spykComp.data } returns dataItems
        viewModel.onAttachToViewHolder()
//        setup done

        viewModel.updateSelectedSection("1", false)

        assert(viewModel.selectedSectionPos == 0)
        assert(viewModel.selectedSectionId == "1")
        assert(useCase.selectedId == "1")
        assert(viewModel.getUpdatePositionsLD().value == true)
        assert(!viewModel.pauseDispatchChanges)

    }

    @Test
    fun `update selection test with click notified after user scrolls`(){
//        setting up data before test
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        every { spykComp.data } returns dataItems
        viewModel.onAttachToViewHolder()
        viewModel.updateSelectedSection("1", false)
//        setup done

        viewModel.updateSelectedSection("3",true)
        assert(viewModel.selectedSectionPos == 2)
        assert(viewModel.selectedSectionId == "3")
        assert(useCase.selectedId == "3")
        assert(viewModel.getUpdatePositionsLD().value == true)
        assert(viewModel.pauseDispatchChanges)

    }

    @Test
    fun `test for delete section when we are deleting non selected section tab`(){
        //        setting up data before test
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        spykComp.data = dataItems
        viewModel.onAttachToViewHolder()
        viewModel.updateSelectedSection("1", false)
//        setup done
//        before deleting
        assert(viewModel.getListSize() == 4)
        assert(viewModel.selectedSectionPos == 0)
        assert(viewModel.selectedSectionId == "1")
        assert(useCase.selectedId == "1")

        viewModel.deleteSectionTab("2")

//        after deleting the tab with id 2
        assert(viewModel.getListSize() == 3)
        assert(viewModel.selectedSectionPos == 0)
        assert(viewModel.selectedSectionId == "1")
        assert(useCase.selectedId == "1")
        assert(viewModel.getCarouselItemsListData().value != null)
        assert(viewModel.shouldShowMissingSectionToaster().value != true)

        val list = viewModel.getCarouselItemsListData().value!!
        for(item in list){
            assert(item.data?.firstOrNull()?.targetSectionID != null && item.data?.firstOrNull()?.targetSectionID != "2")
        }

    }

    @Test
    fun `test for delete section when we are deleting the selected section tab`(){
        //        setting up data before test
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        spykComp.data = dataItems
        viewModel.onAttachToViewHolder()
        viewModel.updateSelectedSection("1", false)
//        setup done
//        before deleting
        assert(viewModel.getListSize() == 4)
        assert(viewModel.selectedSectionPos == 0)
        assert(viewModel.selectedSectionId == "1")
        assert(useCase.selectedId == "1")

        viewModel.deleteSectionTab("1")

//        after deleting the tab with id 1
        assert(viewModel.getListSize() == 3)
        assert(viewModel.selectedSectionPos == Integer.MAX_VALUE)
        assert(viewModel.selectedSectionId == "")
        assert(useCase.selectedId == "")
        assert(viewModel.shouldShowMissingSectionToaster().value == true)
        assert(viewModel.getCarouselItemsListData().value != null)

        val list = viewModel.getCarouselItemsListData().value!!
        for(item in list){
            assert(item.data?.firstOrNull()?.targetSectionID != null && item.data?.firstOrNull()?.targetSectionID != "1")
        }

    }

    @Test
    fun `test for finding if a section was deleted`(){
        //        setting up data before test
        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val spykComp = spyk<ComponentsItem>()
        val useCase = spyk<AnchorTabsUseCase>()
        viewModel = AnchorTabsViewModel(application, spykComp, 99)
        viewModel.anchorTabsUseCase = useCase
        viewModel.coroutineDispatchers = rule.dispatchers
        assert(spykComp.noOfPagesLoaded == 0)
        val dataItems = getAnchorTabsDataList()
        spykComp.data = dataItems
        viewModel.onAttachToViewHolder()
        viewModel.updateSelectedSection("1", false)
        viewModel.deleteSectionTab("2")
//        setup done

//      finding out section was deleted
        assert(viewModel.wasSectionDeleted())
//        making sure value is reset after its read
        assert(!viewModel.wasSectionDeleted())

    }

    private fun getAnchorTabsDataList(): List<DataItem> {
        val dataItems = mutableListOf<DataItem>()
        dataItems.add(DataItem(targetSectionID = "1"))
        dataItems.add(DataItem(targetSectionID = "2"))
        dataItems.add(DataItem(targetSectionID = "3"))
        dataItems.add(DataItem(targetSectionID = "4"))
        return dataItems
    }

}