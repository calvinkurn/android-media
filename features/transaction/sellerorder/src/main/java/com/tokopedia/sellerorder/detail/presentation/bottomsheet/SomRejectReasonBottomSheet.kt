package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*

class SomRejectReasonBottomSheet(context: Context,
                                 actionListener: SomBottomSheetRejectReasonsAdapter.ActionListener
) : SomBottomSheet(LAYOUT, true, true, false, SomConsts.TITLE_PILIH_PENOLAKAN, context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_reject_reason_list
    }

    private var somBottomSheetRejectReasonsAdapter: SomBottomSheetRejectReasonsAdapter = SomBottomSheetRejectReasonsAdapter(actionListener)

    override fun setupChildView() {
        childViews?.run {
            findViewById<RecyclerView>(R.id.rvBottomSheetRejectReasonList)?.run {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetRejectReasonsAdapter
            }
        }
    }

    fun setupTicker(isPenaltyReject: Boolean, penaltyRejectWording: String) {
        childViews?.run {
            findViewById<Ticker>(R.id.tickerBottomSheetRejectReasonList)?.run {
                if (isPenaltyReject) {
                    show()
                    setHtmlDescription(penaltyRejectWording)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            RouteManager.route(
                                context,
                                String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl)
                            )
                        }

                        override fun onDismiss() {}
                    })
                } else {
                    gone()
                }
            }
        }
    }

    fun setReasons(rejectReasons: MutableList<SomReasonRejectData.Data.SomRejectReason>) {
        somBottomSheetRejectReasonsAdapter.listRejectReasons = rejectReasons
        somBottomSheetRejectReasonsAdapter.notifyDataSetChanged()
    }
}