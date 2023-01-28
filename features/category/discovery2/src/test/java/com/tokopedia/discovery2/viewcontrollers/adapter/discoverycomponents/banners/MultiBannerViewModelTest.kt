package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.notifier.NotifierCheckReminder
import com.tokopedia.discovery2.data.notifier.NotifierSetReminder
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.usecase.CheckPushStatusUseCase
import com.tokopedia.discovery2.usecase.SubScribeToUseCase
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.BANNER_ACTION_CODE
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners.MultiBannerViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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
import java.net.SocketTimeoutException

class MultiBannerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)

    private val viewModel: MultiBannerViewModel by lazy {
        spyk(MultiBannerViewModel(application, componentsItem, 99))
    }

    private val bannerUseCase: BannerUseCase by lazy {
        mockk()
    }

    private val subScribeToUseCase: SubScribeToUseCase by lazy {
        mockk()
    }

    private val checkPushStatusUseCase: CheckPushStatusUseCase by lazy {
        mockk()
    }

    val list = arrayListOf<DataItem>()
    var dataItem:DataItem = mockk()
    var userSession: UserSession = mockk()
    var context: Context = mockk(relaxed = true)

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
    fun `test for application`(){
        assert(viewModel.application === application)
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
    fun `test for onAttachToViewHolder when Data properties is null`() {
        coEvery { componentsItem.properties } returns null

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns true`() {
        viewModel.bannerUseCase = bannerUseCase
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents throws exception`() {
        viewModel.bannerUseCase = bannerUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents throws SocketTimeoutException`() {
        viewModel.bannerUseCase = bannerUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } throws SocketTimeoutException("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.showErrorState.value, true)
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
    fun `test for onBannerClicked when bannerData value is null`() {
        every { componentsItem.data } returns null

        viewModel.onBannerClicked(0,context)

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

    @Test
    fun `test for onBannerClicked when position returns null`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.onBannerClicked(1,context)

        TestCase.assertEquals(viewModel.syncData.value, null)
    }

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
    fun `banner action test when action is code`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "CODE")
        list.add(item)
        every { componentsItem.data } returns list
        val clipData : ClipData = mockk(relaxed = true)
        every { (application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clipData)} just Runs

        viewModel.onBannerClicked(0, context)

        assert(viewModel.checkApplink().value != null)
    }

    @Test
    fun `banner action test when action is push notifier and login is true`() {
        viewModel.subScribeToUseCase = subScribeToUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn() } returns true
        val notifierSetReminder = NotifierSetReminder(isSuccess = 1)
        val pushSubscriptionResponse = PushSubscriptionResponse(notifierSetReminder = notifierSetReminder)
        coEvery { subScribeToUseCase.subscribeToPush(any()) } returns pushSubscriptionResponse

        viewModel.onBannerClicked(0, context)

        assert(viewModel.getPushBannerStatusData().value?.first == 0)
    }

    @Test
    fun `banner action test when action is push notifier and NotifierSetReminder returns isSuccess as 2`() {
        viewModel.subScribeToUseCase = subScribeToUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn() } returns true
        val notifierSetReminder = NotifierSetReminder(isSuccess = 2)
        val pushSubscriptionResponse = PushSubscriptionResponse(notifierSetReminder = notifierSetReminder)
        coEvery { subScribeToUseCase.subscribeToPush(any()) } returns pushSubscriptionResponse

        viewModel.onBannerClicked(0, context)

        assert(viewModel.getPushBannerStatusData().value?.first == 0)
    }

    @Test
    fun `banner action test when action is push notifier and subscribeToPush throws exception`() {
        viewModel.subScribeToUseCase = subScribeToUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn() } returns true
        coEvery { subScribeToUseCase.subscribeToPush(any()) } throws Exception("Error")

        viewModel.onBannerClicked(0, context)

        assert(viewModel.syncData.value == null)
    }

    @Test
    fun `banner action test when action is push notifier and login is false`() {
        viewModel.subScribeToUseCase = subScribeToUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn() } returns false

        viewModel.onBannerClicked(0, context)

        assert(viewModel.getShowLoginData().value == true)
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
    fun `banner action test when action is xyz and login is false`() {
        viewModel.subScribeToUseCase = subScribeToUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "XYZ")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.navigate(context, any()) } just Runs

        viewModel.onBannerClicked(0,context)

        verify { viewModel.navigate(context,any()) }
    }

    /****************************************** end of onBannerClicked() ****************************************/

    /****************************************** campaignSubscribedStatus() ****************************************/
    @Test
    fun `Test for campaignSubscribedStatus when action is PUSH_NOTIFIER and checkPushStatus returns success`() {
        viewModel.checkPushStatusUseCase = checkPushStatusUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn()} returns true
        val notifierCheckReminder = NotifierCheckReminder(status = 1)
        val pushSubscriptionResponse = PushStatusResponse(notifierCheckReminder = notifierCheckReminder)
        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } returns pushSubscriptionResponse

        viewModel.campaignSubscribedStatus(0)

        assert(viewModel.getPushBannerSubscriptionData().value == 0)
    }

    @Test
    fun `Test for campaignSubscribedStatus when action is PUSH_NOTIFIER and  checkPushStatus throws Exception`() {
        viewModel.checkPushStatusUseCase = checkPushStatusUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "PUSH_NOTIFIER")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn()} returns true
        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } throws Exception("Error")

        viewModel.campaignSubscribedStatus(0)

        assert(viewModel.syncData.value == null)
    }

    @Test
    fun `Test for campaignSubscribedStatus when action is LOCAL_CALENDAR and checkPushStatus returns success`() {
        viewModel.checkPushStatusUseCase = checkPushStatusUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(applinks = "tokopedia", action = "LOCAL_CALENDAR")
        list.add(item)
        every { componentsItem.data } returns list
        every { viewModel.isUserLoggedIn()} returns false
        val notifierSetReminder = NotifierCheckReminder(status = 1)
        val pushSubscriptionResponse = PushStatusResponse(notifierCheckReminder = notifierSetReminder)
        coEvery { checkPushStatusUseCase.checkPushStatus(any()) } returns pushSubscriptionResponse

        viewModel.campaignSubscribedStatus(0)

        assert(viewModel.getPushBannerSubscriptionData().value == 0)
    }

    /****************************************** end of campaignSubscribedStatus() ****************************************/

    /****************************************** reload() ****************************************/
    @Test
    fun `Test for reload`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        viewModel.bannerUseCase = bannerUseCase
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        coEvery { componentsItem.properties?.dynamic } returns true
        coEvery { bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

        viewModel.reload()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)
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

    @Test
    fun `test for checkForDarkMode`() {
        spyk(context.isDarkMode())

        viewModel.checkForDarkMode(context)

        verify { viewModel.checkForDarkMode(context) }
    }

    @Test
    fun `test for layoutSelector`() {
        every { componentsItem.name } returns ComponentNames.SingleBanner.componentName

        assert( viewModel.layoutSelector() == R.layout.disco_shimmer_single_banner_layout )

        every { componentsItem.name } returns ComponentNames.DoubleBanner.componentName

        assert( viewModel.layoutSelector() == R.layout.disco_shimmer_double_banner_layout )

        every { componentsItem.name } returns ComponentNames.TripleBanner.componentName

        assert( viewModel.layoutSelector() == R.layout.disco_shimmer_triple_banner_layout )

        every { componentsItem.name } returns ComponentNames.QuadrupleBanner.componentName

        assert( viewModel.layoutSelector() == R.layout.disco_shimmer_quadruple_banner_layout )

        every { componentsItem.name } returns ComponentNames.BottomNavigation.componentName

        assert( viewModel.layoutSelector() == R.layout.multi_banner_layout )
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
