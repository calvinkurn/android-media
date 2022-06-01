package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.BANNER_ACTION_CODE
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
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

class MultiBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: MultiBannerViewModel by lazy {
        spyk(MultiBannerViewModel(application, componentsItem, 99))
    }

    private val bannerUseCase: BannerUseCase by lazy {
        mockk()
    }

    val list = arrayListOf<DataItem>()
    var dataItem:DataItem = mockk()
    var userSession: UserSession = mockk()
    var context: Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

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
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

    }

    /****************************************** onAttachToViewHolder() ****************************************/
    @Test
    fun `test for onAttachToViewHolder`() {
        viewModel.bannerUseCase = bannerUseCase
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint, application.applicationContext) } returns true

        val list = ArrayList<ComponentsItem>()
        coEvery { componentsItem.getComponentsItem() } returns list
        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getComponentData().value != null, true)
    }


    /****************************************** banner width ****************************************/
    @Test
    fun `test for banner width`(){
        every { Utils.extractDimension(any(),any()) } returns 100
        val list = mutableListOf(DataItem(imageUrlDynamicMobile = "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp"))
        every { componentsItem.data } returns list

        assert(viewModel.getBannerUrlWidth() == 100)
    }


    /****************************************** onAttachToViewHolder() ****************************************/
    @Test
    fun `test for banner height`(){
        every { Utils.extractDimension(any()) } returns 100
        val list = mutableListOf(DataItem(imageUrlDynamicMobile = "https://images.tokopedia.net/img/cache/900/QBrNqa/2021/11/30/4a521cc9-560d-4763-9ff2-85a1c22abcf8.png.webp"))
        every { componentsItem.data } returns list

        assert(viewModel.getBannerUrlHeight() == 100)
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }

    /****************************************** user LogIn() ****************************************/
    @Test
    fun `isUser Logged in`(){
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
    fun `banner action is Login when isUserLoggedIn is true`() {
        list.clear()
        coEvery { componentsItem.data } returns list
        list.add(dataItem)
        every { dataItem.action } returns "LOGIN"
        every { dataItem.applinks } returns "tokopedia://xyz"
        every { viewModel.navigate(context, any()) } just Runs
        every { viewModel.isUserLoggedIn() } returns true

        viewModel.onBannerClicked(0, context)

        assert(viewModel.isPageRefresh().value != true)
    }

    @Test
    fun `banner action is Login when isUserLoggedIn is false`() {
        list.clear()
        coEvery { componentsItem.data } returns list
        list.add(dataItem)
        every { dataItem.action } returns "LOGIN"
        every { dataItem.applinks } returns "tokopedia://xyz"
        every { viewModel.navigate(context, any()) } just Runs
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

    @Test
    fun `test for setComponentPromoNameForCoupons when component is SingleBanner`() {
        val list = mutableListOf(DataItem(action = BANNER_ACTION_CODE))
        every { componentsItem.data } returns list

        viewModel.setComponentPromoNameForCoupons(ComponentNames.SingleBanner.componentName,list)
        assert(viewModel.getComponentData().value?.data?.firstOrNull()?.componentPromoName == "single_promo_code")
    }

    @Test
    fun `test for setComponentPromoNameForCoupons when component is DoubleBanner`() {
        val list = mutableListOf(DataItem(action = BANNER_ACTION_CODE))
        every { componentsItem.data } returns list

        viewModel.setComponentPromoNameForCoupons(ComponentNames.DoubleBanner.componentName,list)
        assert(viewModel.getComponentData().value?.data?.firstOrNull()?.componentPromoName == "double_promo_code")
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}