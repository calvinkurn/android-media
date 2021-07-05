package com.tokopedia.analyticsdebugger.cassava.debugger

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerDetailFragment

class DebuggerListDetailFragment : Fragment() {
    private var textName: TextView? = null
    private var textTimestamp: TextView? = null
    private var textData: TextView? = null

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
        requireArguments().let {
            textName!!.text = it.getString("name")
            textTimestamp!!.text = it.getString("timestamp")
            textData!!.text = it.getString("datum")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_analytics_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share_text) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, requireArguments().getString("datum"))
            startActivity(Intent.createChooser(sharingIntent, null))
            return true
        } else if (item.itemId == R.id.action_copy_text) {
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("tokopedia_analytics_debugger", requireArguments().getString("datum"))
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun newInstance(extras: Bundle): Fragment {
            val fragment = AnalyticsDebuggerDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}