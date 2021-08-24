package com.tokopedia.shop.score.performance.presentation.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore

class BottomSheetShopEndTenure: BaseBottomSheetShopScore() {

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_new_seller_shop_score

    override fun getTitleBottomSheet(): String = ""

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }


    companion object {
        const val TAG = "BottomSheetShopEndTenure"
    }

}