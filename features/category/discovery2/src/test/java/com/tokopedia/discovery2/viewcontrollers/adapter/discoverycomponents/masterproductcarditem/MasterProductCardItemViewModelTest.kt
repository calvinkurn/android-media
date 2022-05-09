package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MasterProductCardItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: MasterProductCardItemViewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
    private var context:Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
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
    fun `test for application`(){
        assert(viewModel.application === application)
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

    /**************************** test for userId *******************************************/
    @Test
    fun `get user id of logged in user when userId is empty`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns ""

        assert(viewModel.getUserID() == "")
    }

    @Test
    fun `get user id of logged in user when userId is not empty`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "1012"

        assert(viewModel.getUserID() == "1012")
    }
    /**************************** end of userId *******************************************/

    /**************************** test for getProductDataItem() *******************************************/
    @Test
    fun `get product data item when data is null`() {
        every { componentsItem.data } returns null

        assert(viewModel.getProductDataItem() == null)

    }
    @Test
    fun `get product data item when list is empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getProductDataItem() == null)
    }
    @Test
    fun `get product data item when list is not empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item:DataItem = mockk()
        list.add(item)

        assert(viewModel.getProductDataItem() === item)

    }
    /**************************** end of getProductDataItem() *******************************************/

    /**************************** test for updateProductQuantity() *******************************************/
    @Test
    fun `update quantity confirmation test`(){
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item = DataItem()
        list.add(item)

        viewModel.updateProductQuantity(10)

        assert(item.quantity == 10)
    }

    /**************************** test for handleATCFailed() *******************************************/
    @Test
    fun `handle ATCFailed quantity recheck and reload`(){
        val viewmodel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        every { viewmodel.onAttachToViewHolder() } just Runs
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item = DataItem()
        list.add(item)

        viewmodel.handleATCFailed()

        assert(item.quantity == 0)
        verify { viewmodel.onAttachToViewHolder() }
    }

    /**************************** end of handleATCFailed() *******************************************/

    /**************************** test for getComponentName() *******************************************/
    @Test
    fun `product card type name when name is null`() {
        every { componentsItem.name } returns null

        assert(viewModel.getComponentName().isEmpty())
    }

    @Test
    fun `product card type name when name is not null`() {
        every { componentsItem.name } returns "XYZ"

        assert(viewModel.getComponentName() == "XYZ")
    }
    /**************************** end of getComponentName() *******************************************/
}