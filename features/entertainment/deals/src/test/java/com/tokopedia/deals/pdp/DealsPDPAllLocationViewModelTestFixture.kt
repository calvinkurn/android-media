package com.tokopedia.deals.pdp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPAllLocationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule

open class DealsPDPAllLocationViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: DealsPDPAllLocationViewModel
    protected val keyWord = "Aeon Mall"
    protected val districtKeyWord = "Bsd Raya Utama"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DealsPDPAllLocationViewModel(
            CoroutineTestDispatchersProvider
        )
    }
}
