package com.tokopedia.checkout.view.feature.promostacking

import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel

/**
 * Created by fwidjaja on 10/03/19.
 */

open class ClashBottomSheetFragment : BottomSheets() {

    private var mTitle: String? = null
    private lateinit var actionListener: ActionListener
    private lateinit var tvClashingInfoTicker: TextView
    private lateinit var uiModel: ClashingInfoDetailUiModel

    interface ActionListener {

    }

    companion object {
        @JvmStatic
        fun newInstance(): ClashBottomSheetFragment {
            return ClashBottomSheetFragment()
        }
    }

    fun setData(uiModel: ClashingInfoDetailUiModel) {
        this.uiModel = uiModel
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    override fun initView(view: View) {
        tvClashingInfoTicker = view.findViewById(R.id.tv_clashing_info_ticker)
        tvClashingInfoTicker.text = uiModel.clashMessage

        updateHeight()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_clash_voucher
    }

    override fun title(): String {
        return getString(R.string.clash_bottomsheet_title)
    }

}