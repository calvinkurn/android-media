package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
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
    val coroutineTestRule = UnconfinedTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        initVariables()
    }

    abstract fun initVariables()

    protected fun <T : Any> BmgmState<T>.verifyErrorEquals(expected: BmgmState.Error) {
        val expectedResult = expected.t::class.java
        val actualResult = (this as? BmgmState.Error)?.let {
            it.t::class.java
        }
        Assert.assertEquals(expectedResult, actualResult)
    }
}