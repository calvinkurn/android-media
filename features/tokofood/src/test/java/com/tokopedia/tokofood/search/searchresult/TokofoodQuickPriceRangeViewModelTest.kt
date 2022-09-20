package com.tokopedia.tokofood.search.searchresult

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.utils.collectFromSharedFlow
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokofoodQuickPriceRangeViewModelTest: TokofoodQuickPriceRangeViewModelTestFixture() {

    @Test
    fun `when resetUiModels, should set all ui models isSelected to false`() {
        val currentUiModels = getPriceFilterUiModels().onEach {
            it.isSelected = true
        }

        viewModel.currentUiModelsFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels)
                viewModel.resetUiModels()
            },
            then = { uiModels ->
                assert(uiModels?.all { !it.isSelected } == true)
            }
        )
    }

    @Test
    fun `when resetUiModels but no current ui models, the current ui models should still be empty`() {
        viewModel.currentUiModelsFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.resetUiModels()
            },
            then = { uiModels ->
                assert(uiModels.isNullOrEmpty())
            }
        )
    }

    @Test
    fun `when setPriceRangeUiModel to true state, should show reset button`() {
        val currentUiModels = getPriceFilterUiModels().onEach {
            it.isSelected = false
        }
        val uiModel = currentUiModels[Int.ZERO]

        every {
            helper.getAppliedCount(any())
        } returns Int.ONE

        viewModel.shouldShowResetButton.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels)
                viewModel.setPriceRangeUiModel(uiModel, true)
            },
            then = {
                assert(it == true)
            }
        )
    }

    @Test
    fun `when setPriceRangeUiModel so all ui models unselected, should hide reset button`() {
        val updatedIndex = Int.ZERO
        val currentUiModels = getPriceFilterUiModels().onEachIndexed { index, uiModel ->
            val isSelected = index == updatedIndex
            uiModel.isSelected = isSelected
            uiModel.option.inputState = isSelected.toString()
        }
        val uiModel = currentUiModels[Int.ZERO]

        viewModel.shouldShowResetButton.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels)
                viewModel.setPriceRangeUiModel(uiModel, false)
            },
            then = {
                assert(it == false)
            }
        )
    }

    @Test
    fun `when setPriceRangeUiModel into different states than initial, should show apply button`() {
        val currentUiModels = getPriceFilterUiModels()
        val uiModel = currentUiModels[Int.ZERO]

        viewModel.shouldShowApplyButton.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels, true)
                viewModel.setPriceRangeUiModel(uiModel, !uiModel.isSelected)
            },
            then = {
                assert(it == true)
            }
        )
    }

    @Test
    fun `when setPriceRangeUiModel into same states like initial, should hide apply button`() {
        val currentUiModels = getPriceFilterUiModels()
        val uiModel = currentUiModels[Int.ZERO]

        every {
            helper.getIsOptionsSameAsInitial(any(), any())
        } returns true

        viewModel.shouldShowApplyButton.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels, true)
                viewModel.setPriceRangeUiModel(uiModel, !uiModel.isSelected)
                viewModel.setPriceRangeUiModel(uiModel, uiModel.isSelected)
            },
            then = {
                assert(it == false)
            }
        )
    }

    @Test
    fun `when setPriceRangeUiModel but no initial states, should show apply button`() {
        val currentUiModels = getPriceFilterUiModels()
        val uiModel = currentUiModels[Int.ZERO]

        every {
            helper.getIsOptionsSameAsInitial(any(), any())
        } returns true

        viewModel.shouldShowApplyButton.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModel(uiModel, !uiModel.isSelected)
            },
            then = {
                assert(it == false)
            }
        )
    }

    @Test
    fun `when clickApplyButton, should set apply button clicked`() {
        val currentUiModels = getPriceFilterUiModels()
        val uiModel = currentUiModels[Int.ZERO]

        viewModel.applyButtonClicked.collectFromSharedFlow(
            whenAction = {
                viewModel.setPriceRangeUiModels(currentUiModels)
                viewModel.setPriceRangeUiModel(uiModel, true)
                viewModel.clickApplyButton()
            },
            then = {
                assert(it?.getOrNull(Int.ZERO)?.key == uiModel.option.key)
            }
        )
    }

}