package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetPenaltyShopLevelBinding
import com.tokopedia.utils.htmltags.HtmlUtil

class PenaltyShopLevelBottomSheet: BaseBottomSheetShopScore<BottomsheetPenaltyShopLevelBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getLayoutResId(): Int = R.layout.bottomsheet_penalty_shop_level

    override fun getTitleBottomSheet(): String = getString(R.string.title_penalty_calculation_shop_level)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    override fun bind(view: View): BottomsheetPenaltyShopLevelBinding =
        BottomsheetPenaltyShopLevelBinding.bind(view)

    private fun setupView() {
        binding?.tvPenaltyShopLevel?.text =
            HtmlUtil.fromHtml(getString(R.string.desc_penalty_shop_level))
    }

    companion object {
        private const val TAG = "PenaltyShopLevelBottomSheet"
    }
}
