package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomSheetStatusPenaltyBinding
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType

class PenaltyStatusBottomSheet : BaseBottomSheetShopScore<BottomSheetStatusPenaltyBinding>() {

    override fun bind(view: View) = BottomSheetStatusPenaltyBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_status_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_status_penalty)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, PENALTY_STATUS_BOTTOM_SHEET_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        when (arguments?.getString(PENALTY_TYPE_TAG)) {
            ShopPenaltyPageType.ONGOING -> {
                binding?.tvTitlePenaltyOver?.gone()
                binding?.tvTitlePointHaveNotDeduction?.gone()
                binding?.tvDescPenaltyOver?.gone()
                binding?.tvDescPointHaveNotDeduction?.gone()
            }
            ShopPenaltyPageType.NOT_YET_DEDUCTED -> {
                binding?.tvTitlePenaltyOver?.gone()
                binding?.tvTitleOnGoing?.gone()
                binding?.tvDescPenaltyOver?.gone()
                binding?.tvDescOnGoing?.gone()
            }
            ShopPenaltyPageType.HISTORY -> {
                binding?.tvTitleOnGoing?.gone()
                binding?.tvTitlePointHaveNotDeduction?.gone()
                binding?.tvDescOnGoing?.gone()
                binding?.tvDescPointHaveNotDeduction?.gone()
            }
        }
    }

    companion object {
        const val PENALTY_STATUS_BOTTOM_SHEET_TAG = "PenaltyStatusBottomSheetTag"

        private const val PENALTY_TYPE_TAG = "penaltyTypeTag"

        fun newInstance(@ShopPenaltyPageType penaltyPageType: String? = null): PenaltyStatusBottomSheet {
            return PenaltyStatusBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PENALTY_TYPE_TAG, penaltyPageType)
                }
            }
        }
    }

}
