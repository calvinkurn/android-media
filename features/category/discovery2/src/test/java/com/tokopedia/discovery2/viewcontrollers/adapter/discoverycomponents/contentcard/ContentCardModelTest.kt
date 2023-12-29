package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentcard

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.contentCardUseCase.ContentCardUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard.ContentCardViewModel
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

class ContentCardModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    var context: Context = mockk(relaxed = true)
    private var viewModel: ContentCardViewModel =
        spyk(ContentCardViewModel(application, componentsItem, 99))

    private val contentCardUseCase: ContentCardUseCase by lazy {
        mockk()
    }

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

    /**************************** useCase test *******************************************/

    @Test
    fun `test for useCase`() {
        val viewModel: ContentCardViewModel =
            spyk(ContentCardViewModel(application, componentsItem, 99))
        val contentCardUseCase = mockk<ContentCardUseCase>()
        viewModel.contentCardUseCase = contentCardUseCase
        assert(viewModel.contentCardUseCase === contentCardUseCase)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun `test for onAttachToViewHolder error case`() {
        viewModel.contentCardUseCase = contentCardUseCase
        coEvery {
            contentCardUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachToViewHolder success when componentList is not empty`() {
        viewModel.contentCardUseCase = contentCardUseCase
        coEvery {
            contentCardUseCase.loadFirstPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns false

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, false)
    }

    /**************************** end of onAttachToViewHolder() *******************************************/

    /****************************************** test for component ****************************************/
    @Test
    fun `test for component passed to ViewModel`() {
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    /****************************************** test for position ****************************************/
    @Test
    fun `test for component position`() {
        TestCase.assertEquals(viewModel.position, 99)
    }

    @Test
    fun `test for checkForDarkMode`() {
        spyk(context.isDarkMode())

        viewModel.checkForDarkMode(context)

        verify { context.isDarkMode() }
    }
}
