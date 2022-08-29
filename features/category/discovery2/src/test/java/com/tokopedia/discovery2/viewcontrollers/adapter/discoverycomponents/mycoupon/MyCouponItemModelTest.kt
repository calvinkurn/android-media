package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.mycoupon.MyCoupon
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

class MyCouponItemModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context:Context = mockk()
    private val viewModel: MyCouponItemViewModel by lazy {
        spyk(MyCouponItemViewModel(application, componentsItem, 99))
    }

    private val couponItem: MyCoupon = mockk()

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for getApplink`() {
        val list = arrayListOf<MyCoupon>()
        every { componentsItem.myCouponList } returns list
        list.add(couponItem)
        every { couponItem.redirectAppLink } returns "appLink"

        val viewModel = spyk(MyCouponItemViewModel(application, componentsItem, 99))
        assert(viewModel.getCouponAppLink() == "appLink")

        every { couponItem.redirectAppLink } returns ""
        assert(viewModel.getCouponAppLink().isEmpty())
    }

    @Test
    fun `get userId`(){
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns ""
        assert(viewModel.getUserId() == "")
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "1012"
        assert(viewModel.getUserId() == "1012")
    }


    @Test
    fun `test for components LD`() {
        assert(viewModel.getComponentData().value === componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun ` test for getCouponItem`(){
        every { componentsItem.myCouponList } returns null
        assert(viewModel.getCouponItem() == null)

        val list = arrayListOf<MyCoupon>()
        every { componentsItem.myCouponList } returns list
        list.add(couponItem)
        assert(viewModel.getCouponItem() === couponItem)
    }

}