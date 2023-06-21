package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetPenaltyCalculationBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.ItemPenaltyCalculationScoreAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationFormulaUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationInformationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationSubtitleUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableUiModel
import kotlin.math.absoluteValue

class PenaltyCalculationBottomSheet :
    BaseBottomSheetShopScore<BottomsheetPenaltyCalculationBinding>(),
    ItemPenaltyCalculationScoreAdapter.Listener {

    override fun bind(view: View): BottomsheetPenaltyCalculationBinding = BottomsheetPenaltyCalculationBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottomsheet_penalty_calculation

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_calculation)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onIconClicked() {
        PenaltyShopLevelBottomSheet().show(childFragmentManager)
    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.rvPenaltyCalculation?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter =
                PenaltyCalculationAdapter(
                    getAdapterData(),
                    PenaltyCalculationAdapterFactory(this@PenaltyCalculationBottomSheet)
                )
        }
    }

    private fun getAdapterData(): List<Visitable<*>> {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_ID_PENALTY_CALCULATION)
            )
        }

        val cacheResult =
            cacheManager?.get<ItemPenaltyPointCardUiModel>(
                KEY_PENALTY_CALCULATION,
                ItemPenaltyPointCardUiModel::class.java
            )

        return cacheResult?.mapToUiModels().orEmpty()
    }

    private fun ItemPenaltyPointCardUiModel.mapToUiModels(): List<Visitable<*>> {
        return if (result.orderVerified > Int.ZERO) {
            getFullCalculationDetailUiModels(this)
        } else {
            getPartialCalculationDetailUiModels(this)
        }
    }

    private fun getFullCalculationDetailUiModels(uiModel: ItemPenaltyPointCardUiModel): List<Visitable<*>> {
        return listOf(
            ItemPenaltyCalculationSubtitleUiModel(
                getString(R.string.title_penalty_calculation_summary)
            ),
            ItemPenaltyCalculationScoreUiModel(
                uiModel.result.penaltyDynamic,
                uiModel.date,
                uiModel.result.penalty.absoluteValue,
                uiModel.result.orderVerified,
                uiModel.result.shopLevel
            ),
            ItemPenaltyCalculationSubtitleUiModel(
                getString(R.string.title_penalty_calculation_percentage_calculate)
            ),
            ItemPenaltyCalculationFormulaUiModel(
                uiModel.result.penalty.absoluteValue,
                uiModel.result.orderVerified,
                uiModel.result.penaltyCumulativePercentageFormatted,
                uiModel.result.penaltyDynamic
            ),
            ItemPenaltyCalculationSubtitleUiModel(
                getString(R.string.title_penalty_calculation_shop_level),
                getString(R.string.title_penalty_calculation_level, uiModel.result.shopLevel.toString())
            ),
            ItemPenaltyCalculationTableUiModel(
                uiModel.result.conversionData.map {
                    Triple(it.cumulativePercentageFormatted, it.penaltyPoint, it.conversionRateFlag)
                }
            )
        )
    }

    private fun getPartialCalculationDetailUiModels(uiModel: ItemPenaltyPointCardUiModel): List<Visitable<*>> {
        return listOf(
            ItemPenaltyCalculationSubtitleUiModel(
                getString(R.string.title_penalty_calculation_summary)
            ),
            ItemPenaltyCalculationScoreUiModel(
                uiModel.result.penaltyDynamic,
                uiModel.date,
                uiModel.result.penaltyAmount,
                uiModel.result.orderVerified,
                uiModel.result.shopLevel
            ),
            ItemPenaltyCalculationSubtitleUiModel(
                getString(R.string.title_penalty_calculation_percentage_calculate)
            ),
            ItemPenaltyCalculationInformationUiModel(
                getString(R.string.desc_penalty_calculation_information)
            )
        )
    }

    companion object {
        private const val TAG = "PenaltyCalculationBottomSheet"
        const val KEY_PENALTY_CALCULATION = "penalty_calculation"
        const val KEY_CACHE_ID_PENALTY_CALCULATION = "cache_id_penalty_calculation"

        @JvmStatic
        fun createInstance(cacheManagerId: String): PenaltyCalculationBottomSheet {
            val args = Bundle().apply {
                putString(KEY_CACHE_ID_PENALTY_CALCULATION, cacheManagerId)
            }
            return PenaltyCalculationBottomSheet().apply {
                arguments = args
            }
        }
    }

}
