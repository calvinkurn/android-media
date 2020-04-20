package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel

import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL

class ApplinkDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textApplink: TextView? = null
    private var textTimestamp: TextView? = null
    private var textTraces: TextView? = null
    private var viewModel: ApplinkDebuggerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_applink_debugger_detail, container, false)
        textApplink = view.findViewById(R.id.applink_text_name)
        textTimestamp = view.findViewById(R.id.applink_text_timestamp)
        textTraces = view.findViewById(R.id.applink_text_traces)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments!!.getParcelable(DATA_DETAIL)
        if (viewModel != null) {
            textApplink!!.text = viewModel!!.applink
            textTimestamp!!.text = viewModel!!.timestamp
            textTraces!!.text = viewModel!!.trace
        }
    }

    override fun getScreenName(): String {
        return ApplinkDebuggerDetailFragment::class.java.simpleName
    }

    companion object {

        fun newInstance(extras: Bundle): Fragment {
            val fragment = ApplinkDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
