package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.producthighlight.DiscoveryOCSDataModel
import com.tokopedia.discovery2.usecase.producthighlightusecase.ProductHighlightUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class ProductHighlightViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)

    private val viewModel: ProductHighlightViewModel by lazy {
        spyk(ProductHighlightViewModel(application, componentsItem, 99))
    }

    private val productHighlightUseCase: ProductHighlightUseCase by lazy {
        mockk()
    }

    private val atcUseCase: AddToCartOcsUseCase by lazy {
        mockk()
    }

    private val testDispatchers = CoroutineTestDispatchersProvider

    val list = arrayListOf<DataItem>()
    var dataItem: DataItem = mockk()
    var context: Context = mockk(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkObject(Utils)
        mockkConstructor(UserSession::class)
        coEvery { application.applicationContext } returns context
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkObject(Utils)
        unmockkConstructor(UserSession::class)
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
    fun `test for application`() {
        assert(viewModel.application === application)
    }

    @Test
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())
    }

    /****************************************** onAttachToViewHolder() ****************************************/
    @Test
    fun `test for onAttachToViewHolder when Data properties is null`() {
        coEvery { componentsItem.properties } returns null

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns true`() {
        viewModel.productHighlightUseCase = productHighlightUseCase
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        coEvery { productHighlightUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents throws exception`() {
        viewModel.productHighlightUseCase = productHighlightUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { productHighlightUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents throws SocketTimeoutException`() {
        viewModel.productHighlightUseCase = productHighlightUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { productHighlightUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } throws SocketTimeoutException("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.showErrorState.value, true)
    }

    /****************************************** onBannerClicked() ****************************************/

    @Test
    fun `test for onCardClicked when data value is null`() {
        every { componentsItem.data } returns null

        viewModel.onCardClicked(0, context)

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `test for onCardClicked when position returns null`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.onCardClicked(1, context)

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `test for onCardClicked when data is not null`() {
        list.clear()
        viewModel.productHighlightUseCase = productHighlightUseCase
        coEvery { componentsItem.data } returns list
        list.add(dataItem)
        every { dataItem.applinks } returns "tokopedia://xyz"
        coEvery { productHighlightUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true
        every { viewModel.navigate(context, any()) } just Runs
        viewModel.onAttachToViewHolder()
        viewModel.onCardClicked(0, context)

        coVerify { viewModel.navigate(context, "tokopedia://xyz") }
    }

    /****************************************** end of onBannerClicked() ****************************************/

    /****************************************** reload() ****************************************/
    @Test
    fun `Test for reload`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        viewModel.productHighlightUseCase = productHighlightUseCase
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        coEvery { productHighlightUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

        viewModel.reload()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
    }

    @Test
    fun `test for layoutSelector`() {
        every { componentsItem.properties?.type } returns "single"

        assert(viewModel.layoutSelector() == R.layout.disco_item_shimmer_single_product_highlight_layout)

        every { componentsItem.properties?.type } returns "double"

        assert(viewModel.layoutSelector() == R.layout.disco_item_shimmer_double_pl_layout)

        every { componentsItem.properties?.type } returns "triple"

        assert(viewModel.layoutSelector() == R.layout.disco_item_shimmer_triple_pl_layout)

        every { componentsItem.properties?.type } returns ComponentNames.AnchorTabs.componentName

        assert(viewModel.layoutSelector() == R.layout.multi_banner_layout)
    }

    /**************************** test for Login *******************************************/
    @Test
    fun `isUser Logged in when isLoggedIn is false`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        assert(!viewModel.isUserLoggedIn())
    }

    @Test
    fun `isUser Logged in when isLoggedIn is true`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        assert(viewModel.isUserLoggedIn())
    }

    /**************************** end of Login *******************************************/

    /**************************** test for OCS *******************************************/
    @Test
    fun `test for click on OCS button when success add to cart`() {
        viewModel.atcUseCase = atcUseCase
        viewModel.dispatcher = testDispatchers

        val slot = slot<RequestParams>()

        val atcResponseSuccess = AddToCartDataModel(
            data = DataModel(success = 1, productId = "2147818593"),
            status = "OK"
        )

        every { dataItem.productId } returns "2147818593"
        every { dataItem.shopId } returns "shopId#1"
        every { dataItem.minQuantity } returns 1
        every { dataItem.productName } returns "ProductName#1"
        every { dataItem.category } returns ""
        every { dataItem.price } returns "Rp15.000"
        every { dataItem.shopName } returns ""

        coEvery {
            atcUseCase.createObservable(capture(slot)).toBlocking().single()
        } returns atcResponseSuccess

        viewModel.onOCSClicked(context, dataItem)

        val expectedValue = DiscoveryOCSDataModel(dataItem, atcResponseSuccess)

        TestCase.assertEquals(expectedValue, viewModel.redirectToOCS.value)
        TestCase.assertEquals(null, viewModel.ocsErrorMessage.value)
    }

    @Test
    fun `test for click on OCS button when failed to add to cart`() {
        viewModel.atcUseCase = atcUseCase
        viewModel.dispatcher = testDispatchers

        val errorMessage = "gagal ya"
        val atcResponseError = AddToCartDataModel(
            data = DataModel(success = 0),
            status = "",
            errorMessage = arrayListOf(errorMessage)
        )

        every { dataItem.productId } returns "2147818593"
        every { dataItem.shopId } returns "shopId#1"
        every { dataItem.minQuantity } returns 1
        every { dataItem.productName } returns "ProductName#1"
        every { dataItem.category } returns ""
        every { dataItem.price } returns "Rp15.000"
        every { dataItem.shopName } returns ""

        coEvery {
            atcUseCase.createObservable(any()).toBlocking().single()
        } returns atcResponseError

        viewModel.onOCSClicked(context, dataItem)

        TestCase.assertEquals(null, viewModel.redirectToOCS.value)
        TestCase.assertEquals(errorMessage, viewModel.ocsErrorMessage.value)
    }

    @Test
    fun `test for click on OCS button when result is null`() {
        viewModel.atcUseCase = atcUseCase
        viewModel.dispatcher = testDispatchers

        every { dataItem.productId } returns "2147818593"
        every { dataItem.shopId } returns "shopId#1"
        every { dataItem.minQuantity } returns 1
        every { dataItem.productName } returns "ProductName#1"
        every { dataItem.category } returns ""
        every { dataItem.price } returns "Rp15.000"
        every { dataItem.shopName } returns ""

        coEvery {
            atcUseCase.createObservable(any()).toBlocking().single()
        } returns null

        viewModel.onOCSClicked(context, dataItem)

        TestCase.assertEquals(null, viewModel.redirectToOCS.value)
        TestCase.assertEquals("Failed to request ATC", viewModel.ocsErrorMessage.value)
    }

    /**************************** end of OCS *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
        unmockkObject(Utils)
    }
}
