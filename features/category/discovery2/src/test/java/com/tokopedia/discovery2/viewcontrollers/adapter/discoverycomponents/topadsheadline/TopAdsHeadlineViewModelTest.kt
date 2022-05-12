package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.repository.topads.TopAdsHeadlineRepository
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TopAdsHeadlineViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context:Context = mockk()

    private val viewModel: TopAdsHeadlineViewModel by lazy {
        spyk(TopAdsHeadlineViewModel(application, componentsItem, 99))
    }

    private val topAdsHeadlineRepository: TopAdsHeadlineRepository by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for component`() {
        assert(viewModel.components == componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application == application)
    }

    /**************************** test for getHeadlineAdsParam() *******************************************/
    @Test
    fun `test for getHeadlineAdsParam`(){
        viewModel.topAdsHeadlineRepository = topAdsHeadlineRepository
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)

        every { componentsItem.data } returns list
        coEvery { topAdsHeadlineRepository.getHeadlineAdsParams(componentsItem.pageEndPoint,componentsItem.data?.firstOrNull()?.paramsMobile ?: "") } returns "xyz"

        assert(viewModel.getHeadlineAdsParam() == "xyz")
    }
    /**************************** end of getHeadlineAdsParam() *******************************************/

    /**************************** test for isUser Logged in *******************************************/
    @Test
    fun `isUser Logged in when isLoggedIn is false`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        assert(!viewModel.isUserLoggedIn())
    }

    @Test
    fun `isUser Logged in when isLoggedIn is true`(){
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        assert(viewModel.isUserLoggedIn())
    }

    /**************************** end of isUser Logged in *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
    }
}