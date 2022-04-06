package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner.DiscoveryTDNBannerViewModel
import com.tokopedia.quest_widget.constants.QuestUrls
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TopQuestViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TopQuestViewModel by lazy {
        spyk(TopQuestViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for components passed`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for onResume`(){
        viewModel.shouldUpdate = true
        viewModel.loggedInUpdate = true
        viewModel.onResume()
        assert(viewModel.updateQuestData.value == true)

        viewModel.loggedInUpdate = true
        viewModel.onResume()
        assert(!viewModel.loggedInUpdate)

    }


    @Test
    fun `test for loggedInCallback`(){
        viewModel.loggedInCallback()
        assert(viewModel.navigateData.value == QuestUrls.QUEST_URL)

    }
}