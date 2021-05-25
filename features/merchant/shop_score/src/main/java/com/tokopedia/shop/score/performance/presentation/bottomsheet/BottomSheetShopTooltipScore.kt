package com.tokopedia.shop.score.performance.presentation.bottomsheet


import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore

class BottomSheetShopTooltipScore: BaseBottomSheetShopScore() {

    override fun getLayoutResId(): Int = R.layout.bottomsheet_tooltip_skor_performa

    override fun getTitleBottomSheet(): String = getString(R.string.title_score_performance)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, SHOP_TOOLTIP_SCORE_BOTTOM_SHEET_TAG)
            }
        }
    }

    companion object {
        const val SHOP_TOOLTIP_SCORE_BOTTOM_SHEET_TAG = "ShopTooltipScoreBottomSheetTag"
    }
}