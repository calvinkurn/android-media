package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class SharedRechargeGeneralViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var sharedRechargeGeneralViewModel: SharedRechargeGeneralViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sharedRechargeGeneralViewModel = SharedRechargeGeneralViewModel(Dispatchers.Unconfined)
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