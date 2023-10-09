package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

/**
 * Created by @ilhamsuaib on 09/10/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComposeQuestionnaireViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}