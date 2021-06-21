package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomRejectReasonBottomSheet(context: Context,
                                 actionListener: SomBottomSheetRejectReasonsAdapter.ActionListener
) : SomBottomSheet(LAYOUT, true, true, false, SomConsts.TITLE_PILIH_PENOLAKAN, context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_secondary
    }

    private var somBottomSheetRejectReasonsAdapter: SomBottomSheetRejectReasonsAdapter = SomBottomSheetRejectReasonsAdapter(actionListener)

    override fun setupChildView() {
        childViews?.run {
            rv_bottomsheet_secondary?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetRejectReasonsAdapter
            }
            tf_extra_notes?.gone()
        }
    }

    fun setupTicker(isPenaltyReject: Boolean, penaltyRejectWording: String) {
        childViews?.run {
            if (isPenaltyReject) {
                ticker_penalty_secondary?.apply {
                    show()
                    setHtmlDescription(penaltyRejectWording)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                        }

                        override fun onDismiss() {}
                    })
                }
            } else {
                ticker_penalty_secondary?.gone()
            }
        }
    }

    fun setReasons(rejectReasons: MutableList<SomReasonRejectData.Data.SomRejectReason>) {
        somBottomSheetRejectReasonsAdapter.listRejectReasons = rejectReasons
        somBottomSheetRejectReasonsAdapter.notifyDataSetChanged()
    }
}