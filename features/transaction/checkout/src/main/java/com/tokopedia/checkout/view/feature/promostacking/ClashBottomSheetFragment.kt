package com.tokopedia.checkout.view.feature.promostacking

import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.design.component.BottomSheets

/**
 * Created by fwidjaja on 10/03/19.
 */

open class ClashBottomSheetFragment : BottomSheets() {

    private var mTitle: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): ClashBottomSheetFragment {
            return ClashBottomSheetFragment()
        }
    }

    override fun initView(view: View?) {

    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_clash_voucher
    }

    override fun title(): String {
        return getString(R.string.clash_bottomsheet_title)
    }

}