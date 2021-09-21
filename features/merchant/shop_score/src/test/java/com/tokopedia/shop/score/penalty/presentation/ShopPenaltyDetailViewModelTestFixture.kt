package com.tokopedia.shop.score.penalty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyDetailViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ShopPenaltyDetailViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var penaltyMapper: PenaltyMapper

    protected lateinit var shopPenaltyDetailViewModel: ShopPenaltyDetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        penaltyMapper = mockk(relaxed = true)
        shopPenaltyDetailViewModel = ShopPenaltyDetailViewModel(CoroutineTestDispatchersProvider, penaltyMapper)
    }

    @After
    fun finish() {
        unmockkAll()
    }

}