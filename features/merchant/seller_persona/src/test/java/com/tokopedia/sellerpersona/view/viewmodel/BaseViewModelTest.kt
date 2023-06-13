package com.tokopedia.sellerpersona.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

/**
 * Created by @ilhamsuaib on 17/02/23.
 */

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        initVariables()
    }

    abstract fun initVariables()
}