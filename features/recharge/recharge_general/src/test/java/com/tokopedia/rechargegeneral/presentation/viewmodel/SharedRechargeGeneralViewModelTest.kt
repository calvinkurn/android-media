package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import io.mockk.MockKAnnotations
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedRechargeGeneralViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var sharedRechargeGeneralViewModel: SharedRechargeGeneralViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sharedRechargeGeneralViewModel = SharedRechargeGeneralViewModel(CoroutineTestDispatchersProvider)
    }

    @Test
    fun setRecommendationItem() {
        val recommendationItem = TopupBillsRecommendation(productId = 1)
        val observer = Observer<TopupBillsRecommendation> {
            assertEquals(it.productId, 1)
        }

        try {
            sharedRechargeGeneralViewModel.recommendationItem.observeForever(observer)
            sharedRechargeGeneralViewModel.setRecommendationItem(recommendationItem)
        } finally {
            sharedRechargeGeneralViewModel.recommendationItem.removeObserver(observer)
        }
    }
}