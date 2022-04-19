package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomSheetStatusPenaltyBinding

class PenaltyStatusBottomSheet : BaseBottomSheetShopScore<BottomSheetStatusPenaltyBinding>() {

    override fun bind(view: View) = BottomSheetStatusPenaltyBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_status_penalty

    override fun getTitleBottomSheet(): String = getString(R.string.title_status_penalty)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, PENALTY_STATUS_BOTTOM_SHEET_TAG)
        }
    }

    companion object {
        const val PENALTY_STATUS_BOTTOM_SHEET_TAG = "PenaltyStatusBottomSheetTag"

        fun newInstance(): PenaltyStatusBottomSheet {
            return PenaltyStatusBottomSheet()
        }
    }

}