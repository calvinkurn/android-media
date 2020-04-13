package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel

class TopAdsDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textTimestamp: TextView? = null
    private var textEventType: TextView? = null
    private var textSourceName: TextView? = null
    private var viewModel: TopAdsDebuggerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topads_debugger_detail, container, false)
        textTimestamp = view.findViewById(R.id.topads_text_timestamp)
        textEventType = view.findViewById(R.id.topads_text_event_type)
        textSourceName = view.findViewById(R.id.topads_text_source_name)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments!!.getParcelable(DATA_DETAIL)

        textTimestamp?.text = viewModel?.timestamp
        textEventType?.text = viewModel?.eventType
        textSourceName?.text = viewModel?.sourceName

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
