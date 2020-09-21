package com.tokopedia.developer_options.presentation.feedbackpage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.developer_options.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class TicketCreatedFragment: Fragment() {

    private lateinit var imageCreated: ImageView
    private lateinit var tickerLink: Typography
    private lateinit var imageCopy: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_ticket_result, container, false)
        initView(mainView)
        return  mainView
    }

    private fun initView(mainView: View) {
        imageCreated = mainView.findViewById(R.id.iv_ticket_created)
        tickerLink = mainView.findViewById(R.id.ticket_url)
        imageCopy = mainView.findViewById(R.id.copy_ticket)

        val issueId = arguments?.getString("EXTRA_IS_TICKET_LINK")

        tickerLink.text = "https://tokopedia.atlassian.net/browse/$issueId"

        imageCopy.setOnClickListener {
            onTextCopied(mainView, "label", tickerLink.text.toString())
        }

    }

    private fun onTextCopied(view: View, label: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        Toaster.make(view, "Text berhasil di copy", Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
    }

    companion object {

        @JvmStatic
        fun newInstance(extra: Bundle): TicketCreatedFragment {
            return TicketCreatedFragment().apply {
                arguments = Bundle().apply {
                    putString("EXTRA_IS_TICKET_LINK", extra.getString("EXTRA_IS_TICKET_LINK"))
                }
            }
        }
    }

}