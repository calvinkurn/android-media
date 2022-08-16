package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel

import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.Toaster

class ApplinkDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textApplink: TextView? = null
    private var textTimestamp: TextView? = null
    private var textTraces: TextView? = null
    private var iconMainApplink: IconUnify? = null
    private var viewModel: ApplinkDebuggerViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_applink_debugger_detail, container, false)
        textApplink = view.findViewById(R.id.applink_text_name)
        textTimestamp = view.findViewById(R.id.applink_text_timestamp)
        textTraces = view.findViewById(R.id.applink_text_traces)
        iconMainApplink = view.findViewById(R.id.icon_copy_main_applink)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments!!.getParcelable(DATA_DETAIL)
        if (viewModel != null) {
            textApplink!!.text = viewModel!!.applink
            textTimestamp!!.text = viewModel!!.timestamp
            textTraces!!.text = viewModel!!.trace
            iconMainApplink?.setOnClickListener {
                viewModel?.let {
                    copyToClipBoard(it.applink)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return ApplinkDebuggerDetailFragment::class.java.simpleName
    }

    private fun copyToClipBoard(applink: String?) {
        activity?.let {
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                CLIP_DATA_APPLINK, applink
            )

            clipboard.setPrimaryClip(clip)
            view?.run {
                Toaster.build(this,
                    getString(com.tokopedia.analyticsdebugger.R.string.applink_already_copied),
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {

        const val CLIP_DATA_APPLINK = "clip_data_applink"

        fun newInstance(extras: Bundle): Fragment {
            val fragment = ApplinkDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
