package com.tokopedia.home.account.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.revamp.domain.GetBuyerAccountDataUseCase
import com.tokopedia.home.account.revamp.viewmodel.BuyerAccountViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class BuyerAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context: Context = mockk(relaxed = true)
    private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase = mockk(relaxed = true)

    private lateinit var viewModel: BuyerAccountViewModel
    private val dispatcherProvider = TestDispatcherProvider()

    @Before
    fun setUp() {

    }
}