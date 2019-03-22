package com.tokopedia.checkout.view.feature.promostacking

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingAdapter
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingInnerAdapter
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import kotlinx.android.synthetic.main.item_clashing_voucher.view.*

/**
 * Created by fwidjaja on 10/03/19.
 */

open class ClashBottomSheetFragment : BottomSheets() {

    private var mTitle: String? = null
    private lateinit var actionListener: ActionListener
    private lateinit var tvClashingInfoTicker: TextView
    private lateinit var uiModel: ClashingInfoDetailUiModel
    private lateinit var rvClashingOption: RecyclerView

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
        rvClashingOption = view.findViewById(R.id.rv_clashing_option)
        tvClashingInfoTicker = view.findViewById(R.id.tv_clashing_info_ticker)
        tvClashingInfoTicker.text = uiModel.clashMessage

        val adapter = ClashingAdapter()
        adapter.data = uiModel.options
        rvClashingOption.adapter = adapter
        rvClashingOption.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter.notifyDataSetChanged()

        updateHeight()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_clash_voucher
    }

    override fun title(): String {
        return getString(R.string.clash_bottomsheet_title)
    }

}