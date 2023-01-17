package com.tokopedia.sellerpersona.view.bottomsheet

import android.view.View
import com.tokopedia.sellerpersona.R
import com.tokopedia.sellerpersona.databinding.BottomSheetOpeningBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class OpeningBottomSheet : BaseBottomSheet<BottomSheetOpeningBinding>() {

    companion object {
        fun newInstance(): OpeningBottomSheet {
            return OpeningBottomSheet()
        }
    }

    override fun bind(view: View): BottomSheetOpeningBinding {
        return BottomSheetOpeningBinding.bind(view)
    }

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_opening

    override fun setupView() {

    }
}