package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BrandRecommendationViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BrandRecommendationViewModel by lazy {
        spyk(BrandRecommendationViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `position test`() {
        assert(viewModel.getComponentPosition() == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentDataLiveData().value == componentsItem)
    }

    @Test
    fun `component item test`() {
        every { componentsItem.data } returns null
        viewModel.mapBrandRecomItems()
        assert(viewModel.getListDataLiveData().value == null)
        assert(viewModel.getListData() == null)
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        viewModel.mapBrandRecomItems()
        assert(viewModel.getListDataLiveData().value?.isEmpty() == true)
        assert(viewModel.getListData()?.isEmpty() == true)
//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val viewModel1 = spyk(BrandRecommendationViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }
        for (i in 0..4) {
            val item = DataItem()
            item.name = "item_$i"
            if (i != 3)
                item.imageUrlMobile = "URL"
            list.add(item)
        }
        viewModel1.mapBrandRecomItems()
//        This returned list must not contain item with empty imageUrlMobile
        assert(viewModel1.getListDataLiveData().value?.size == 4)
        assert(viewModel1.getListData()?.size == 4)
        viewModel1.getListData()?.forEach {
            assert(it.data?.firstOrNull() != null)
            assert(it.data?.firstOrNull()?.name != "Item_3")
        }

    }


}