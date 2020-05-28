package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL
import com.tokopedia.analyticsdebugger.debugger.helper.getTopAdsStatusColor
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel
import kotlinx.android.synthetic.main.fragment_topads_debugger_detail.*

class TopAdsDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var viewModel: TopAdsDebuggerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topads_debugger_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments?.getParcelable(DATA_DETAIL)
        timestampText.text = viewModel?.timestamp
        eventTypeText.text = viewModel?.eventType
        sourceNameText.text = viewModel?.sourceName
        urlText.text = viewModel?.url
        statusText.text = viewModel?.eventStatus
        statusText.setTextColor(getTopAdsStatusColor(context, viewModel?.eventStatus))
        fullResponseText.text = viewModel?.fullResponse
    }

    override fun getScreenName(): String {
        return TopAdsDebuggerDetailFragment::class.java.simpleName
    }

    companion object {

        fun newInstance(extras: Bundle): Fragment {
            val fragment = TopAdsDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
