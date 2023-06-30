package com.tokopedia.shop.score.penalty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyDetailMapper
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyDetailViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ShopPenaltyDetailViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var penaltyDetailMapper: PenaltyDetailMapper

    protected lateinit var shopPenaltyDetailViewModel: ShopPenaltyDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        penaltyDetailMapper = mockk(relaxed = true)
        shopPenaltyDetailViewModel = ShopPenaltyDetailViewModel(CoroutineTestDispatchersProvider, penaltyDetailMapper)
    }

    @After
    fun finish() {
        unmockkAll()
    }

}
