package com.tokopedia.topads.dashboard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.domain.model.GetVariantByIdResponse
import com.tokopedia.topads.common.domain.usecase.GetVariantByIdUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsTypeSelectionViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val getVariantByIdUseCase: GetVariantByIdUseCase = mockk(relaxed = true)
    private lateinit var viewModel: TopAdsTypeSelectionViewModel

    @Before
    fun setUp() {
        viewModel = TopAdsTypeSelectionViewModel(rule.dispatchers, getVariantByIdUseCase)
    }

    @Test
    fun `getVariantById failure`() {
        val throwable = spyk(Throwable())

        coEvery {
            getVariantByIdUseCase().getVariantById
        } answers {
            throw throwable
        }
        viewModel.getVariantById()
        assertEquals(
            viewModel.shopVariant.value,
            listOf<GetVariantByIdResponse.GetVariantById.ExperimentVariant>()
        )
    }

    @Test
    fun `getVariantById success`() {
        val data = GetVariantByIdResponse.GetVariantById(
            userIdVariants = listOf(
                GetVariantByIdResponse.GetVariantById.ExperimentVariant(
                    experiment = "experiment",
                    variant = "variant"
                )
            ),
            shopIdVariants = listOf(
                GetVariantByIdResponse.GetVariantById.ExperimentVariant(
                    experiment = "experiment",
                    variant = "variant"
                )
            ),
            sessionIdVariants = listOf(
                GetVariantByIdResponse.GetVariantById.ExperimentVariant(
                    experiment = "experiment",
                    variant = "variant"
                )
            )
        )
        coEvery {
            getVariantByIdUseCase().getVariantById
        } answers {
            data
        }
        viewModel.getVariantById()
        assertEquals(viewModel.shopVariant.value, data.shopIdVariants)
    }

}
