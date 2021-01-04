package com.tokopedia.product.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule

/**
 * Created by Yehezkiel on 17/11/20
 */
abstract class BaseProductViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}