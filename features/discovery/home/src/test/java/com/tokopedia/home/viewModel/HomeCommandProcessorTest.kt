package com.tokopedia.home.viewModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.util.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Lukas on 04/09/20.
 */
class HomeCommandProcessorTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val homeProcessor = HomeCommandProcessor(Dispatchers.Unconfined)
    private val callback = mockk<ResultCommandProcessor>(relaxed = true)

    @Before
    fun setup() {
        // Sets the given [dispatcher] as an underlying dispatcher of [Dispatchers.Main].
        // All consecutive usages of [Dispatchers.Main] will use given [dispatcher] under the hood.
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Resets state of the [Dispatchers.Main] to the original main dispatcher.
        // For example, in Android Main thread dispatcher will be set as [Dispatchers.Main].
        Dispatchers.resetMain()

        // Clean up the TestCoroutineDispatcher to make sure no other work is running.
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test update widget`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledUpdateWidget = false
        coEvery { callback.updateWidget(any(), any()) } answers {
            calledUpdateWidget = true
        }
        val widgetCommand = UpdateWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledUpdateWidget)
    }

    @Test
    fun `Test add widget`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledAddWidget = false
        coEvery { callback.addWidget(any(), any()) } answers {
            calledAddWidget = true
        }
        val widgetCommand = AddWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledAddWidget)
    }

    @Test
    fun `Test remove widget`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledRemoveWidget = false
        coEvery { callback.deleteWidget(any(), any()) } answers {
            calledRemoveWidget = true
        }
        val widgetCommand = DeleteWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledRemoveWidget)
    }

    @Test
    fun `Test update home data`() = runBlockingTest {
        val mockVisitable = mockk<HomeDataModel>()
        var calledUpdateHomeData = false
        coEvery { callback.updateHomeData(any()) } answers {
            calledUpdateHomeData = true
        }
        val widgetCommand = UpdateHomeData(mockVisitable, callback)
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledUpdateHomeData)
    }

    @Test
    fun `Test update home data but channel closed`() = runBlockingTest {
        val mockVisitable = mockk<HomeDataModel>()
        var calledUpdateHomeData = false
        coEvery { callback.updateHomeData(any()) } answers {
            calledUpdateHomeData = true
        }
        val widgetCommand = UpdateHomeData(mockVisitable, callback)
        homeProcessor.onClear()
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledUpdateHomeData)
    }

    @Test
    fun `Test update widget but channel closed`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledUpdateWidget = false
        coEvery { callback.updateWidget(any(), any()) } answers {
            calledUpdateWidget = true
        }
        val widgetCommand = UpdateWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.onClear()
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledUpdateWidget)
    }

    @Test
    fun `Test add widget but channel closed`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledAddWidget = false
        coEvery { callback.addWidget(any(), any()) } answers {
            calledAddWidget = true
        }
        val widgetCommand = AddWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.onClear()
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledAddWidget)
    }

    @Test
    fun `Test remove widget but channel closed`() = runBlockingTest {
        val mockVisitable = mockk<Visitable<*>>()
        var calledRemoveWidget = false
        coEvery { callback.deleteWidget(any(), any()) } answers {
            calledRemoveWidget = true
        }
        val widgetCommand = DeleteWidgetCommand(mockVisitable, -1, callback)
        homeProcessor.onClear()
        homeProcessor.sendWithQueueMethod(widgetCommand)
        assert(calledRemoveWidget)
    }
}