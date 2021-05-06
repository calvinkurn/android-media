package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.model.AnalyticsDebuggerViewModel

import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL

class AnalyticsDebuggerDetailFragment : TkpdBaseV4Fragment() {
    private var textName: TextView? = null
    private var textTimestamp: TextView? = null
    private var textData: TextView? = null
    private var viewModel: AnalyticsDebuggerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_analytics_debugger_detail, container, false)
        textName = view.findViewById(R.id.text_name)
        textTimestamp = view.findViewById(R.id.text_timestamp)
        textData = view.findViewById(R.id.text_data)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = arguments!!.getParcelable(DATA_DETAIL)
        if (viewModel != null) {
            textName!!.text = viewModel!!.category
            textTimestamp!!.text = viewModel!!.timestamp
            textData!!.text = viewModel!!.data
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_analytics_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel != null) {
            if (item.itemId == R.id.action_share_text) {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, viewModel!!.data)
                startActivity(Intent.createChooser(sharingIntent, null))
                return true
            } else if (item.itemId == R.id.action_copy_text) {
                val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("tokopedia_analytics_debugger", viewModel!!.data)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String {
        return AnalyticsDebuggerDetailFragment::class.java.simpleName
    }

    companion object {

        fun newInstance(extras: Bundle): Fragment {
            val fragment = AnalyticsDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}
