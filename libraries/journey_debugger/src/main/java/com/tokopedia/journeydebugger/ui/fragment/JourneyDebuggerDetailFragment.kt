package com.tokopedia.journeydebugger.ui.fragment

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
import com.tokopedia.journeydebugger.R

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.journeydebugger.JourneyDebuggerConst.DATA_DETAIL
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel
import com.tokopedia.unifycomponents.Toaster

class JourneyDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textJourney: TextView? = null
    private var textTimestamp: TextView? = null
    private var iconCopyJourney: IconUnify? = null
    private var uiModel: JourneyDebuggerUIModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_journey_debugger_detail, container, false)
        textJourney = view.findViewById(R.id.journey_text_name)
        textTimestamp = view.findViewById(R.id.journey_text_timestamp)
        iconCopyJourney = view.findViewById(R.id.icon_copy_journey)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        uiModel = requireArguments().getParcelable(DATA_DETAIL)
        if (uiModel != null) {
            textJourney!!.text = uiModel!!.journey
            textTimestamp!!.text = uiModel!!.timestamp

            iconCopyJourney?.setOnClickListener {
                uiModel?.let {
                    copyToClipBoard(it.journey)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return JourneyDebuggerDetailFragment::class.java.simpleName
    }

    private fun copyToClipBoard(journey: String?) {
        if (!journey.isNullOrEmpty()) {
            activity?.let {
                val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(
                    CLIP_DATA_JOURNEY, journey
                )

                clipboard.setPrimaryClip(clip)
                view?.run {
                    Toaster.build(
                        this,
                        getString(R.string.journey_already_copied),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            view?.run {
                Toaster.build(
                    this,
                    getString(R.string.journey_not_copied),
                    Snackbar.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    companion object {

        private const val CLIP_DATA_JOURNEY = "clip_data_journey"

        fun newInstance(extras: Bundle): Fragment {
            val fragment = JourneyDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
