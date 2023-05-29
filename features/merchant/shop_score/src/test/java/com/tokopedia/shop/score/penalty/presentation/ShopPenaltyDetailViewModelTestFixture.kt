package com.tokopedia.shop.score.penalty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.score.penalty.domain.old.mapper.PenaltyMapperOld
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

    lateinit var penaltyMapperOld: PenaltyMapperOld

    protected lateinit var shopPenaltyDetailViewModel: ShopPenaltyDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        penaltyMapperOld = mockk(relaxed = true)
        shopPenaltyDetailViewModel = ShopPenaltyDetailViewModel(CoroutineTestDispatchersProvider, penaltyMapperOld)
    }

    @After
    fun finish() {
        unmockkAll()
    }

}
