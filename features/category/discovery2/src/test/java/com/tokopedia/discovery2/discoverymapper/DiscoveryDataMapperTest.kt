package com.tokopedia.discovery2.discoverymapper

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoveryDataMapperTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentItemName = "ComponentItem"
    val componentName = "componentName"
    val design = "design_v1"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun shutDown() {
    }

    @Test
    fun `map List To ComponentList test`(){
//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val list : ArrayList<DataItem> = ArrayList()
        var result = DiscoveryDataMapper.mapListToComponentList(list,componentItemName,componentName,9,design)
        assert(result.isEmpty())
        for (i in 0..4){
            val item = DataItem()
            item.name = "item_$i"
            list.add(item)
        }
        result = DiscoveryDataMapper.mapListToComponentList(list,componentItemName,componentName,9,design)
        for (j in 0..4){
            val componentItem = result[j]
            assert(componentItem.position == j)
            assert(componentItem.name == componentItemName)
            assert(componentItem.design == design)
            val dataItem = componentItem.data?.firstOrNull()
            assert(dataItem == list[j])
            assert(dataItem?.parentComponentName == componentName)
            assert(dataItem?.positionForParentItem == 9)
        }
    }
}