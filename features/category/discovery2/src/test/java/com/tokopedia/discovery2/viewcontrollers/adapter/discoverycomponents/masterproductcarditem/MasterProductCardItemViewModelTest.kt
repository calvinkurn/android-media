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
import kotlinx.coroutines.test.setMain
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

    @Test
    fun `isUser Logged in`(){
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        assert(!viewModel.isUserLoggedIn())
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        assert(viewModel.isUserLoggedIn())
        unmockkConstructor(UserSession::class)
    }

    @Test
    fun `get user id of logged in user`(){
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns ""
        assert(viewModel.getUserID() == "")
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "1012"
        assert(viewModel.getUserID() == "1012")
    }

    @Test
    fun `get product data item`(){
        every { componentsItem.data } returns null
        assert(viewModel.getProductDataItem() == null)
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        assert(viewModel.getProductDataItem() == null)
        val item:DataItem = mockk()
        list.add(item)
        assert(viewModel.getProductDataItem() === item)

    }

    @Test
    fun `update quantity confirmation test`(){
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item = DataItem()
        list.add(item)
        viewModel.updateProductQuantity(10)
        assert(item.quantity == 10)
    }
}