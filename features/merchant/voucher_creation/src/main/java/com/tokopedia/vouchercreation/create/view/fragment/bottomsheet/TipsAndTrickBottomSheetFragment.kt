package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView

class TipsAndTrickBottomSheetFragment(context: Context) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context) : TipsAndTrickBottomSheetFragment {
            return TipsAndTrickBottomSheetFragment(context)
        }
    }

    override var bottomSheetViewTitle: String? = context.resources?.getString(R.string.mvc_create_tips_bottomsheet_title)
}