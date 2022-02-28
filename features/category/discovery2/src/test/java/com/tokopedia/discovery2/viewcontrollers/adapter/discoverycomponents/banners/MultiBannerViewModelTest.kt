package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val pushSubscriptionResponse: PushSubscriptionResponse = mockk(relaxed = true)
    private val multiBannerDataUseCase: BannerUseCase = mockk(relaxed = true)

    private val viewModel: MultiBannerViewModel by lazy {
        spyk(MultiBannerViewModel(application, componentsItem, 99))
    }
    val list = arrayListOf<DataItem>()
    var dataItem:DataItem = mockk()
    var userSession:UserSession = mockk()
    var context:Context = mockk()

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
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

    }

    @Test
    fun `test for onAttachToViewHolder`(){
        runBlocking {
            every { componentsItem.properties?.dynamic } returns true
            coEvery { viewModel.bannerUseCase.loadFirstPageComponents(any(), any()) } returns true

            viewModel.onAttachToViewHolder()

            TestCase.assertEquals(viewModel.getComponentData().value != null, true)
        }
    }


    @Test
    fun `test for banner width`(){
        every { componentsItem.data } returns null
        assert(viewModel.getBannerUrlWidth() == null)
        list.clear()
        every { componentsItem.data } returns list
        assert(viewModel.getBannerUrlWidth() == null)
        list.add(dataItem)
        every { dataItem.imageUrlDynamicMobile } returns null
        assert(viewModel.getBannerUrlWidth() == null)
        every { dataItem.imageUrlDynamicMobile } returns ""
        assert(viewModel.getBannerUrlWidth() == null)
        every { dataItem.imageUrlDynamicMobile } returns "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp"
        assert(viewModel.getBannerUrlWidth() == null)
//        Todo:: Need to find a way to call Uri.parse in mockk.
//        every { dataItem.imageUrlDynamicMobile } returns "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp?width=900&height=180"
//        assert(viewModel.getBannerUrlWidth() == 900)
    }


    @Test
    fun `test for banner height`(){
        every { componentsItem.data } returns null
        assert(viewModel.getBannerUrlHeight()==null)
        list.clear()
        every { componentsItem.data } returns list
        assert(viewModel.getBannerUrlHeight() == null)
        list.add(dataItem)
        every { dataItem.imageUrlDynamicMobile } returns null
        assert(viewModel.getBannerUrlHeight() == null)
        every { dataItem.imageUrlDynamicMobile } returns ""
        assert(viewModel.getBannerUrlHeight() == null)
        every { dataItem.imageUrlDynamicMobile } returns "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp"
        assert(viewModel.getBannerUrlHeight() == null)
        //        Todo:: Need to find a way to call Uri.parse in mockk.
//        every { dataItem.imageUrlDynamicMobile } returns "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp?width=900&height=180"
//        assert(viewModel.getBannerUrlHeight() == 180)
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }

    @Test
    fun `isUser Logged in`(){
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        assert(!viewModel.isUserLoggedIn())
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        assert(viewModel.isUserLoggedIn())
    }
    /****************************************** onBannerClicked() ****************************************/

    @Test
    fun `banner action is APPLINK`() {
        list.clear()
        coEvery { componentsItem.data} returns list
        list.add(dataItem)
        every { dataItem.action } returns "APPLINK"
        every { dataItem.applinks } returns "tokopedia://xyz"
        every { viewModel.navigate(context,any()) } just Runs
        viewModel.onBannerClicked(0, context)
        coVerify { viewModel.navigate(context,"tokopedia://xyz") }
    }
    @Test
    fun `banner action is Empty`() {
        list.clear()
        coEvery { componentsItem.data} returns list
        list.add(dataItem)
        every { dataItem.action } returns ""
        every { dataItem.applinks } returns "tokopedia://xyz"
        every { viewModel.navigate(context,any()) } just Runs
        viewModel.onBannerClicked(0, context)
        coVerify { viewModel.navigate(context,"tokopedia://xyz") }
    }

    @Test
    fun `banner action is Login`(){
        list.clear()
        coEvery { componentsItem.data} returns list
        list.add(dataItem)
        every { dataItem.action } returns "LOGIN"
        every { dataItem.applinks } returns "tokopedia://xyz"
        every { viewModel.navigate(context,any()) } just Runs

        every { viewModel.isUserLoggedIn()} returns true
        viewModel.onBannerClicked(0,context)
        assert(viewModel.isPageRefresh().value != true)
        verify { viewModel.navigate(context,"tokopedia://xyz") }

        every { viewModel.isUserLoggedIn()} returns false
        viewModel.onBannerClicked(0,context)
        assert(viewModel.isPageRefresh().value == true)
    }

    @Test
    fun `user not loggedin for pushNotification`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
//        coEvery { viewModel.isUserLoggedIn() } returns false
//        assertTrue(viewModel.getShowLoginData().value ?: false)

    }

    @Test
    fun `user loggedin for pushNotification`() {
//        coEvery { componentsItem.data?.get(0)?.action } returns "PUSH_NOTIFIER"
//        coEvery { viewModel.isUserLoggedIn() } returns true
//        coVerify {
//        }

    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}