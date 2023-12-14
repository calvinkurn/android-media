package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

/**
 * Created by @ilhamsuaib on 09/10/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest<State, UiEffect> {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    protected val coroutineTestDispatchersProvider = CoroutineTestDispatchersProvider
    protected val states = mutableListOf<State>()
    protected val uiEffects = mutableListOf<UiEffect>()

    private lateinit var stateFlow: StateFlow<State>
    private lateinit var uiEffectFlow: SharedFlow<UiEffect>

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
    }

    protected fun initStateAndUiEffect(
        stateFlow: StateFlow<State>,
        uiEffectFlow: SharedFlow<UiEffect>
    ) {
        this.stateFlow = stateFlow
        this.uiEffectFlow = uiEffectFlow
    }

    protected fun runStateAndUiEffectTest(testBody: TestScope.() -> Unit) {
        runTest {
            val uiStateJob = launch(UnconfinedTestDispatcher()) {
                stateFlow.collect {
                    states.add(it)
                }
            }
            val uiEffectJob = launch(UnconfinedTestDispatcher()) {
                uiEffectFlow.collectLatest {
                    uiEffects.add(it)
                }
            }

            testBody()

            uiStateJob.cancel()
            uiEffectJob.cancel()

            states.clear()
            uiEffects.clear()
        }
    }
}