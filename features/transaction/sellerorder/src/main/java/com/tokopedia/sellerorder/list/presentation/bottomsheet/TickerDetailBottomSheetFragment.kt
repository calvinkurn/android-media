package com.tokopedia.sellerorder.list.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created by fwidjaja on 2019-09-10.
 */
class TickerDetailBottomSheetFragment : BottomSheets() {
    private lateinit var tickerDetail: Ticker
    private var descriptionDetail: String? = ""

    companion object {
        private const val DESC = "DEC"

        @JvmStatic
        fun newInstance(descDetail: String?): TickerDetailBottomSheetFragment {
            return TickerDetailBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    descDetail?.let { putString(DESC, it) }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            descriptionDetail = arguments?.getString("DESC")
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_ticker_detail
    }

    override fun initView(view: View) {
        tickerDetail = view.findViewById(R.id.ticker_detail)
        tickerDetail.setHtmlDescription(descriptionDetail)
    }

    override fun title(): String {
        return getString(R.string.seller_info)
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }
}