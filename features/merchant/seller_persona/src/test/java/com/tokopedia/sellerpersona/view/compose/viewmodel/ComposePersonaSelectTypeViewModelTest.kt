package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by @ilhamsuaib on 09/10/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComposePersonaSelectTypeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when click submit `() {
        runTest {

        }
    }
}