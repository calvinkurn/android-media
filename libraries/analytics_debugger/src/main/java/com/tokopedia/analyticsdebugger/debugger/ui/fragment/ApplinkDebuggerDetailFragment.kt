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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.Toaster

class ApplinkDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textApplink: TextView? = null
    private var textMappedDeeplink: TextView? = null
    private var textTitleMappedDeeplink: TextView? = null
    private var textTimestamp: TextView? = null
    private var textTraces: TextView? = null
    private var iconApplink: IconUnify? = null
    private var iconMappedDeeplink: IconUnify? = null
    private var viewModel: ApplinkDebuggerViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_applink_debugger_detail, container, false)
        textApplink = view.findViewById(R.id.applink_text_name)
        textMappedDeeplink = view.findViewById(R.id.mapped_deeplink)
        textTitleMappedDeeplink= view.findViewById(R.id.title_mapped_deeplink)
        textTimestamp = view.findViewById(R.id.applink_text_timestamp)
        textTraces = view.findViewById(R.id.applink_text_traces)
        iconApplink = view.findViewById(R.id.icon_copy_main_applink)
        iconMappedDeeplink = view.findViewById(R.id.icon_copy_mapped_deeplink)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = requireArguments().getParcelable(DATA_DETAIL)
        if (viewModel != null) {
            textApplink!!.text = viewModel!!.applink
            textTimestamp!!.text = viewModel!!.timestamp
            textTraces!!.text = viewModel!!.trace

            val mappedDeeplink = getMappedDeeplink(viewModel!!.trace)
            textMappedDeeplink?.let { view ->
                if (!mappedDeeplink.isNullOrEmpty()) {
                    view.text = mappedDeeplink
                } else {
                    iconMappedDeeplink?.hide()
                    textMappedDeeplink?.hide()
                    textTitleMappedDeeplink?.hide()
                }
            }

            iconApplink?.setOnClickListener {
                viewModel?.let {
                    copyToClipBoard(it.applink)
                }
            }

            iconMappedDeeplink?.setOnClickListener {
                copyToClipBoard(mappedDeeplink)
            }
        }
    }

    override fun getScreenName(): String {
        return ApplinkDebuggerDetailFragment::class.java.simpleName
    }

    private fun copyToClipBoard(applink: String?) {
        if (!applink.isNullOrEmpty()) {
            activity?.let {
                val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    CLIP_DATA_APPLINK, applink
                )

                clipboard.setPrimaryClip(clip)
                view?.run {
                    Toaster.build(
                        this,
                        getString(com.tokopedia.analyticsdebugger.R.string.applink_already_copied),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            view?.run {
                Toaster.build(
                    this,
                    getString(com.tokopedia.analyticsdebugger.R.string.applink_not_copied),
                    Snackbar.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    private fun getMappedDeeplink(trace: String?): String? {
        var mappedApplink: String? = null
        try {
            mappedApplink = trace?.substringAfterLast(MAPPED_APPLINK_DIVIDER_START)?.substringBeforeLast(
                MAPPED_APPLINK_DIVIDER_END)
        } catch (e: Exception) {
            mappedApplink = null
        }

        return mappedApplink
    }

    companion object {

        private const val CLIP_DATA_APPLINK = "clip_data_applink"
        private const val MAPPED_APPLINK_DIVIDER_START = "Mapped Deeplink:\n"
        private const val MAPPED_APPLINK_DIVIDER_END = "\n\nDF Deeplink:"

        fun newInstance(extras: Bundle): Fragment {
            val fragment = ApplinkDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
