package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetPenaltyCalculationBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.ItemPenaltyCalculationScoreAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.calculation.PenaltyCalculationAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationFormulaUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationInformationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationSubtitleUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableUiModel

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

    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.rvPenaltyCalculation?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter =
                PenaltyCalculationAdapter(
                    getDummyUiModels(),
                    PenaltyCalculationAdapterFactory(this@PenaltyCalculationBottomSheet)
                )
        }
    }

    private fun getDummyUiModels(): List<Visitable<*>> {
        return listOf(
            ItemPenaltyCalculationSubtitleUiModel(
                "Ringkasan Penalty Toko"
            ),
            ItemPenaltyCalculationScoreUiModel(
                -15,
                "5 Jun - 6 Agu 2020",
                5,
                10,
                1
            ),
            ItemPenaltyCalculationSubtitleUiModel(
                "Perhitungan Potongan Skor Performa Toko"
            ),
            ItemPenaltyCalculationFormulaUiModel(
                5,
                10,
                10,
                -15
            ),
            ItemPenaltyCalculationInformationUiModel(
                "Tes aja"
            ),
            ItemPenaltyCalculationSubtitleUiModel(
                "Tabel Konversi",
                "(Level 1)"
            ),
            ItemPenaltyCalculationTableUiModel(
                listOf(
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1,
                    "0%" to -1
                ),
                10
            )

        )
    }

    companion object {
        private const val TAG = "PenaltyCalculationBottomSheet"

        @JvmStatic
        fun createInstance(): PenaltyCalculationBottomSheet {
            return PenaltyCalculationBottomSheet()
        }
    }

}
