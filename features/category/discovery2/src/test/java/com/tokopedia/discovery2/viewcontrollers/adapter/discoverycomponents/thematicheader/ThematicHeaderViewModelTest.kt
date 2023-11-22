package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.TotalProductData
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid.MerchantVoucherGridComponentExtension.addShimmer
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader.ThematicHeaderViewModel.Companion.FIRST_DATA_POSITION
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThematicHeaderViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val component: ComponentsItem = mockk(relaxed = true)

    private val application: Application = mockk()
    private val position: Int = 99
    private val componentId = "12324432"
    private val componentPageEndPoint = "discopagev2-mvc-grid-infinite-test"

    private lateinit var viewModel: ThematicHeaderViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this)
        mockkObject(Utils)
        mockkConstructor(URLParser::class)
        stubUrlParser()

        viewModel = spyk(
            ThematicHeaderViewModel(
                application = application,
                component = component,
                position = position
            )
        )
    }

    private fun stubUrlParser() {
        every {
            anyConstructed<URLParser>().paramKeyValueMapDecoded
        } returns hashMapOf()
    }

    private fun stubComponent(
        dataItems: List<DataItem>?
    ) {
        every {
            component.id
        } returns componentId

        every {
            component.pageEndPoint
        } returns componentPageEndPoint

        every {
            component.data
        } returns dataItems
    }

    @Test
    fun `test for components`(){
        assert(viewModel.component === component)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `When first time load data for thematic header after attaching to view holder but getting empty data items then should get null as result`() {
        // stub necessary data
        stubComponent(
            dataItems = emptyList()
        )

        // create expected result
        val expectedResult: Pair<Int, DataItem?>? = null

        // attach to view holder
        viewModel.onAttachToViewHolder()

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When first time load data for thematic header after attaching to view holder but getting more than one data then should get null as result`() {
        // stub necessary data
        stubComponent(
            dataItems = listOf(
                DataItem(
                    title =  "WAR WIB",
                    subtitle = "Promo aja",
                    color = "#60BB55",
                    image = "https://images.tokopedia.net/img/cache/900/AlPhTU/2023/1/25/ac9e2a21-8322-49f5-b764-894b1e161202.png?width=900&height=235",
                    lottieImage = "https://assets-staging.tokopedia.net/file/bCKpVX/2023/7/6/b72c1b08-72fb-43ea-ac58-9962e61fbc07.json",
                    tabIndex = listOf(1)
                ),
                DataItem(
                    title =  "Diskon>30%",
                    subtitle = "Promo buat incaranmu, nih~",
                    color = "#ff0000",
                    image = "https://img.etimg.com/thumb/msid-102703009,width-300,height-225,imgsize-16886,,resizemode-75/starting-naruto-check-where-to-begin-how-to-watch-heres-your-complete-guide-to-iconic-ninja-series.jpg",
                    lottieImage = "https://assets.tokopedia.net/asts/android/tokofood/tokofood_post_purchase_cook_lottie.json",
                    tabIndex = listOf(2)
                )
            )
        )

        // create expected result
        val expectedResult: Pair<Int, DataItem?>? = null

        // attach to view holder
        viewModel.onAttachToViewHolder()

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When first time load data for thematic header after attaching to view holder and getting one data then should get the same data as result`() {
        val dataItems = listOf(
            DataItem(
                title =  "WAR WIB",
                subtitle = "Promo aja",
                color = "#60BB55",
                image = "https://images.tokopedia.net/img/cache/900/AlPhTU/2023/1/25/ac9e2a21-8322-49f5-b764-894b1e161202.png?width=900&height=235",
                lottieImage = "https://assets-staging.tokopedia.net/file/bCKpVX/2023/7/6/b72c1b08-72fb-43ea-ac58-9962e61fbc07.json",
                tabIndex = listOf(1)
            )
        )

        // stub necessary data
        stubComponent(
            dataItems = dataItems
        )

        // create expected result
        val expectedResult = Pair(
            FIRST_DATA_POSITION,
            dataItems.first()
        )

        // attach to view holder
        viewModel.onAttachToViewHolder()

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When first time load data for thematic header after attaching to view holder but getting null items then should get null as result`() {
        // stub necessary data
        stubComponent(
            dataItems = null
        )

        // create expected result
        val expectedResult: Pair<Int, DataItem?>? = null

        // attach to view holder
        viewModel.onAttachToViewHolder()

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When switch thematic header data with suitable tab position then should get the result`() {
        val tabPosition = 2
        val dataItems = listOf(
            DataItem(
                title =  "WAR WIB",
                subtitle = "Promo aja",
                color = "#60BB55",
                image = "https://images.tokopedia.net/img/cache/900/AlPhTU/2023/1/25/ac9e2a21-8322-49f5-b764-894b1e161202.png?width=900&height=235",
                lottieImage = "https://assets-staging.tokopedia.net/file/bCKpVX/2023/7/6/b72c1b08-72fb-43ea-ac58-9962e61fbc07.json",
                tabIndex = listOf(1)
            ),
            DataItem(
                title =  "Diskon>30%",
                subtitle = "Promo buat incaranmu, nih~",
                color = "#ff0000",
                image = "https://img.etimg.com/thumb/msid-102703009,width-300,height-225,imgsize-16886,,resizemode-75/starting-naruto-check-where-to-begin-how-to-watch-heres-your-complete-guide-to-iconic-ninja-series.jpg",
                lottieImage = "https://assets.tokopedia.net/asts/android/tokofood/tokofood_post_purchase_cook_lottie.json",
                tabIndex = listOf(2)
            )
        )

        // stub necessary data
        stubComponent(
            dataItems = dataItems
        )

        // create expected result
        val dataItem = component.data?.find { it.tabIndex?.contains(tabPosition) == true }
        val expectedResult = Pair(
            tabPosition,
            dataItem
        )

        // switch thematic header data
        viewModel.switchThematicHeaderData(tabPosition)

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When switch thematic header data with not suitable tab position then should get null as result`() {
        val tabPosition = 2
        val dataItems = listOf(
            DataItem(
                title =  "WAR WIB",
                subtitle = "Promo aja",
                color = "#60BB55",
                image = "https://images.tokopedia.net/img/cache/900/AlPhTU/2023/1/25/ac9e2a21-8322-49f5-b764-894b1e161202.png?width=900&height=235",
                lottieImage = "https://assets-staging.tokopedia.net/file/bCKpVX/2023/7/6/b72c1b08-72fb-43ea-ac58-9962e61fbc07.json",
                tabIndex = listOf(3)
            ),
            DataItem(
                title =  "Diskon>30%",
                subtitle = "Promo buat incaranmu, nih~",
                color = "#ff0000",
                image = "https://img.etimg.com/thumb/msid-102703009,width-300,height-225,imgsize-16886,,resizemode-75/starting-naruto-check-where-to-begin-how-to-watch-heres-your-complete-guide-to-iconic-ninja-series.jpg",
                lottieImage = "https://assets.tokopedia.net/asts/android/tokofood/tokofood_post_purchase_cook_lottie.json",
                tabIndex = listOf(4)
            ),
            DataItem(
                title =  "Diskon>30%",
                subtitle = "Promo buat incaranmu, nih~",
                color = "#ff0000",
                image = "https://img.etimg.com/thumb/msid-102703009,width-300,height-225,imgsize-16886,,resizemode-75/starting-naruto-check-where-to-begin-how-to-watch-heres-your-complete-guide-to-iconic-ninja-series.jpg",
                lottieImage = "https://assets.tokopedia.net/asts/android/tokofood/tokofood_post_purchase_cook_lottie.json",
                tabIndex = null
            ),
            DataItem(
                title =  "Diskon>30%",
                subtitle = "Promo buat incaranmu, nih~",
                color = "#ff0000",
                image = "https://img.etimg.com/thumb/msid-102703009,width-300,height-225,imgsize-16886,,resizemode-75/starting-naruto-check-where-to-begin-how-to-watch-heres-your-complete-guide-to-iconic-ninja-series.jpg",
                lottieImage = "https://assets.tokopedia.net/asts/android/tokofood/tokofood_post_purchase_cook_lottie.json",
                tabIndex = emptyList()
            )
        )

        // stub necessary data
        stubComponent(
            dataItems = dataItems
        )

        // create expected result
        val expectedResult: Pair<Int, DataItem?>? = null

        // switch thematic header data
        viewModel.switchThematicHeaderData(tabPosition)

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When switch thematic header data but getting empty data items then should get null as result`() {
        val tabPosition = 2

        // stub necessary data
        stubComponent(
            dataItems = null
        )

        // create expected result
        val expectedResult: Pair<Int, DataItem?>? = null

        // switch thematic header data
        viewModel.switchThematicHeaderData(tabPosition)

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `When switch thematic header data with suitable tab position but before the data already gotten then should get the same result`() {
        val dataItems = listOf(
            DataItem(
                title =  "WAR WIB",
                subtitle = "Promo aja",
                color = "#60BB55",
                image = "https://images.tokopedia.net/img/cache/900/AlPhTU/2023/1/25/ac9e2a21-8322-49f5-b764-894b1e161202.png?width=900&height=235",
                lottieImage = "https://assets-staging.tokopedia.net/file/bCKpVX/2023/7/6/b72c1b08-72fb-43ea-ac58-9962e61fbc07.json",
                tabIndex = listOf(1)
            )
        )

        // stub necessary data
        stubComponent(
            dataItems = dataItems
        )

        // create expected result
        val expectedResult = Pair(
            FIRST_DATA_POSITION,
            dataItems.first()
        )

        // attach to view holder
        viewModel.onAttachToViewHolder()

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)

        /* After getting first data then switch thematic header data with the same tabPosition */

        val tabPosition = 1

        // switch thematic header data
        viewModel.switchThematicHeaderData(tabPosition)

        // compare to the expected result
        viewModel.thematicData
            .verifyValueEquals(expectedResult)
    }
}
