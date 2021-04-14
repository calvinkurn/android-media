package com.tokopedia.shop.score.performance.presentation.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore

class BottomSheetNextUpdatePMSection: BaseBottomSheetShopScore() {

    override fun getLayoutResId(): Int = R.layout.bottomsheet_next_update_pm_section

    override fun getTitleBottomSheet(): String = getString(R.string.title_next_update_pm_section)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, NEXT_UPDATE_PM_SECTION_BOTTOM_SHEET_TAG)
            }
        }
    }

    companion object {
        const val NEXT_UPDATE_PM_SECTION_BOTTOM_SHEET_TAG = "NextUpdatePMSectionBottomSheetTag"
        fun newInstance(): BottomSheetNextUpdatePMSection {
            return BottomSheetNextUpdatePMSection()
        }
    }
}