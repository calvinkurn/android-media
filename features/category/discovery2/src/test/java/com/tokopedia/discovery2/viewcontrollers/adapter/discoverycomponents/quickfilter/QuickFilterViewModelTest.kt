package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.quickFilter.FilterRepository
import com.tokopedia.discovery2.repository.quickFilter.IQuickFilterGqlRepository
import com.tokopedia.discovery2.repository.quickFilter.QuickFilterRepository
import com.tokopedia.discovery2.usecase.QuickFilterUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuickFilterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val context: Context = mockk()
    private val application: Application = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
        unmockkConstructor(UserSession::class)
    }

    @Test
    fun `test for components`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        assert(viewModel.application === application)
    }

    /**************************** test for getTargetComponent() *******************************************/
    @Test
    fun `test for getTargetComponent when properties dynamic is true`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val parentComponentsItem: ComponentsItem = mockk(relaxed = true)
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { getComponent(any(), any()) } returns parentComponentsItem
        every { componentsItem.properties } returns properties
        every { componentsItem.parentComponentId } returns "2"
        val componentItemList = ArrayList<ComponentsItem>()
        val item = ComponentsItem(id = "2", dynamicOriginalId = "3")
        componentItemList.add(item)
        every { parentComponentsItem.getComponentsItem() } returns componentItemList

        TestCase.assertEquals(viewModel.getTargetComponent(), parentComponentsItem)

    }

    @Test
    fun `test for getTargetComponent when properties dynamic is false`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val parentComponentsItem: ComponentsItem = mockk(relaxed = true)
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns false
        every { getComponent(any(), any()) } returns parentComponentsItem
        every { componentsItem.properties } returns properties
        val componentItemList = ArrayList<ComponentsItem>()
        val item = ComponentsItem(id = "2")
        componentItemList.add(item)
        every { parentComponentsItem.getComponentsItem() } returns componentItemList
        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(), parentComponentsItem)
    }

    @Test
    fun `test for getTargetComponent when properties dynamic is true and dynamicOriginalId is empty`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val parentComponentsItem: ComponentsItem = mockk(relaxed = true)
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { getComponent(any(), any()) } returns parentComponentsItem
        every { componentsItem.properties } returns properties
        every { componentsItem.parentComponentId } returns "3"

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(), parentComponentsItem)

    }

    @Test
    fun `test for getTargetComponent when getComponent returns null`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { componentsItem.properties } returns properties
        every { componentsItem.parentComponentId } returns "3"
        every { getComponent(any(), any()) } returns null

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(), null)

    }

    @Test
    fun `test for on Quick Filter Selected return null`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val componentsItem: ComponentsItem = mockk(relaxed = true)

        val option: Option = mockk(relaxed = true)
        val quickFilterUseCase: QuickFilterUseCase = mockk(relaxed = true)
        every { viewModel.isQuickFilterSelected(option) } returns false
        every {
            quickFilterUseCase.onFilterApplied(
                componentsItem,
                selectedFilter = null,
                selectedSort = null
            )
        } returns true
        every { getComponent(any(), any()) } returns null

        viewModel.onQuickFilterSelected(option)

        assert(viewModel.syncData.value == null)

    }

    @Test
    fun `test for on Quick Filter Selected when quick Filter is of type Radio return null`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val componentsItem: ComponentsItem = mockk(relaxed = true)

        val option: Option = mockk(relaxed = true)
        every { option.inputType } returns Option.INPUT_TYPE_RADIO
        val quickFilterUseCase: QuickFilterUseCase = mockk(relaxed = true)
        every { viewModel.isQuickFilterSelected(option) } returns false
        every {
            quickFilterUseCase.onFilterApplied(
                componentsItem,
                selectedFilter = null,
                selectedSort = null
            )
        } returns true
        every { getComponent(any(), any()) } returns null

        viewModel.onQuickFilterSelected(option)

        assert(viewModel.syncData.value == null)

    }

    @Test
    fun `test for on Quick Filter Selected when quick Filter is selected return null`() {
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val componentsItem: ComponentsItem = mockk(relaxed = true)

        val option: Option = mockk(relaxed = true)
        val quickFilterUseCase: QuickFilterUseCase = mockk(relaxed = true)
        every { viewModel.isQuickFilterSelected(option) } returns true
        every {
            quickFilterUseCase.onFilterApplied(
                componentsItem,
                selectedFilter = null,
                selectedSort = null
            )
        } returns true
        every { getComponent(any(), any()) } returns null

        viewModel.onQuickFilterSelected(option)

        assert(viewModel.syncData.value == null)

    }

    //    TEST Init Methods
    @Test
    fun `test for on fetchQuickFilters with filters not present anywhere already present in data`() {
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val quickFilterRepository: QuickFilterRepository = mockk(relaxed = true)

        viewModel.quickFilterRepository = quickFilterRepository
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns null

        every { componentsItem.data } returns null
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        every { componentsItem.data } returns listOf()
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        val dateItem = mockk<DataItem>()
        every { componentsItem.data } returns listOf(dateItem)
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        every { dateItem.filter } returns arrayListOf()
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        val filter = mockk<Filter>()
        every { dateItem.filter } returns arrayListOf(filter)
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())
    }

    @Test
    fun `test for on fetchQuickFilters with filters already present in data`() {
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val quickFilterRepository: QuickFilterRepository = mockk(relaxed = true)

        viewModel.quickFilterRepository = quickFilterRepository
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns null

        val dateItem = mockk<DataItem>()
        every { componentsItem.data } returns listOf(dateItem)
        val filter = mockk<Filter>()
        every { dateItem.filter } returns arrayListOf(filter)
        every { filter.options } returns listOf(mockk())
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isNotEmpty())
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).first() === filter)
    }

    @Test
    fun `test for on fetchQuickFilters with filters not present in data but fetched from repo`() {
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val quickFilterRepository: QuickFilterRepository = mockk(relaxed = true)

        viewModel.quickFilterRepository = quickFilterRepository
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns arrayListOf()

        every { componentsItem.data } returns null
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        val filter = mockk<Filter>()
        every { filter.options } returns listOf(mockk())
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns arrayListOf(filter)
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isNotEmpty())
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).first() === filter)
    }

    @Test
    fun `test for on fetchQuickFilters with filters not present in data but fetched from repo but options is empty`() {
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        val quickFilterRepository: QuickFilterRepository = mockk(relaxed = true)

        viewModel.quickFilterRepository = quickFilterRepository
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns arrayListOf()

        every { componentsItem.data } returns null
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())

        val filter = mockk<Filter>()
        every { filter.options } returns listOf()
        coEvery {
            quickFilterRepository.getQuickFilterData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns arrayListOf(filter)
        viewModel.fetchQuickFilters()
        assert((viewModel.getQuickFilterLiveData().value as ArrayList<Filter>).isEmpty())
    }

    @Test
    fun `test that every time viewmodel is attached shouldRefreshComponent key is reset`() {
        val componentsItem: ComponentsItem = spyk()
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        componentsItem.shouldRefreshComponent = true
        assert(componentsItem.shouldRefreshComponent == true)
        viewModel.onAttachToViewHolder()
        assert(componentsItem.shouldRefreshComponent == null)

    }

    @Test
    fun `test for fetchDynamicFilterModel for non dynamic component`() {
        val componentsItem: ComponentsItem = spyk()
        val filterRepository: FilterRepository = mockk()
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        viewModel.filterRepository = filterRepository
        val prop: Properties = mockk()
        every { componentsItem.properties } returns prop
        every { prop.dynamic } returns false
        every { componentsItem.id } returns "1001"
        every { componentsItem.userAddressData } returns null
        val filterModel: DynamicFilterModel = spyk()
        coEvery {
            filterRepository.getFilterData(
                "1001",
                any(),
                componentsItem.pageEndPoint
            )
        } returns filterModel

        viewModel.fetchDynamicFilterModel()
        assert(viewModel.getDynamicFilterModelLiveData().value === filterModel)
    }

    @Test
    fun `test for fetchDynamicFilterModel for dynamic component`() {
        val componentsItem: ComponentsItem = spyk()
        val filterRepository: FilterRepository = mockk()
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        viewModel.filterRepository = filterRepository
        val prop: Properties = mockk()
        every { componentsItem.properties } returns prop
        every { prop.dynamic } returns true
        every { componentsItem.id } returns "1_1001"
        every { componentsItem.dynamicOriginalId } returns "1001"
        every { componentsItem.userAddressData } returns null
        val filterModel: DynamicFilterModel = spyk()
        coEvery {
            filterRepository.getFilterData(
                "1001",
                any(),
                componentsItem.pageEndPoint
            )
        } returns filterModel

        viewModel.fetchDynamicFilterModel()
        assert(viewModel.getDynamicFilterModelLiveData().value === filterModel)
    }

    @Test
    fun `test for renderDynamicFilter for non dynamic component`() {
        val componentsItem: ComponentsItem = spyk()
        val filterRepository: FilterRepository = mockk()
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        viewModel.filterRepository = filterRepository
        val prop: Properties = mockk()
        every { componentsItem.properties } returns prop
        every { prop.dynamic } returns false
        every { componentsItem.id } returns "1001"
        every { componentsItem.userAddressData } returns null
        val filterModel: DynamicFilterModel = spyk()
        coEvery {
            filterRepository.getFilterData(
                "1001",
                any(),
                componentsItem.pageEndPoint
            )
        } returns filterModel

        viewModel.fetchDynamicFilterModel()
    }

    @Test
    fun `test for filterProductsCount`() {
        val componentsItem: ComponentsItem = spyk()
        val quickFilterGQLRepository: IQuickFilterGqlRepository = mockk()
        val viewModel: QuickFilterViewModel =
            spyk(QuickFilterViewModel(application, componentsItem, 99))
        viewModel.quickFilterGQLRepository = quickFilterGQLRepository
        val mapOfSelectedFilters = mutableMapOf<String, String>()
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == null)

        val prop: Properties = mockk()
        every { componentsItem.properties } returns prop
        every { prop.targetId } returns null
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == null)

        every { prop.targetId } returns "1,2"

        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "10234"
        coEvery { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "10234"

        val discoResponse: DiscoveryResponse = mockk()
        coEvery {
            quickFilterGQLRepository.getQuickFilterProductCountData(
                "1",
                any(),
                allAny(),
                "10234"
            )
        } returns discoResponse

        every { discoResponse.component } returns null
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == "")

        val mockTargetResponse: ComponentsItem = mockk()
        every { discoResponse.component } returns mockTargetResponse
        every { mockTargetResponse.compAdditionalInfo } returns null
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == "")

        val compAdditionInfo: ComponentAdditionalInfo = mockk()
        every { mockTargetResponse.compAdditionalInfo } returns compAdditionInfo
        every { compAdditionInfo.totalProductData } returns null
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == "")

        val totalProductData: TotalProductData = mockk()
        every { compAdditionInfo.totalProductData } returns totalProductData
        every { totalProductData.productCountWording } returns null
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == "")

        every { totalProductData.productCountWording } returns "1000 products"
        viewModel.filterProductsCount(mapOfSelectedFilters)
        assert(viewModel.productCountLiveData.value == "1000 products")


    }

}
