package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel

import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL

class FpmDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textTimestamp: TextView? = null
    private var duration: TextView? = null
    private var metrics: TextView? = null
    private var txtAtrributes: TextView? = null
    private var viewModel: FpmDebuggerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fpm_debugger_detail, container, false)
        textTimestamp = view.findViewById(R.id.fpm_text_timestamp)
        duration = view.findViewById(R.id.fpm_text_duration)
        metrics = view.findViewById(R.id.fpm_text_metrics)
        txtAtrributes = view.findViewById(R.id.fpm_text_attributes)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments!!.getParcelable(DATA_DETAIL)
        if (viewModel != null) {
            textTimestamp!!.text = viewModel!!.timestamp
            duration!!.text = String.format("Duration: %dms", viewModel!!.duration)
            metrics!!.text = viewModel!!.metrics
            txtAtrributes!!.text = viewModel!!.attributes
        }
    }

    override fun getScreenName(): String {
        return FpmDebuggerDetailFragment::class.java.simpleName
    }

    companion object {

        fun newInstance(extras: Bundle): Fragment {
            val fragment = FpmDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
