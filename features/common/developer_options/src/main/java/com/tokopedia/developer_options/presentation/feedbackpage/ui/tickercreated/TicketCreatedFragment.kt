package com.tokopedia.developer_options.presentation.feedbackpage.ui.tickercreated

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
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.feedbackpage.analytics.FeedbackPageAnalytics
import com.tokopedia.developer_options.presentation.feedbackpage.utils.EXTRA_IS_TICKET_LINK
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class TicketCreatedFragment: Fragment() {

    private lateinit var imageCreated: DeferredImageView
    private lateinit var ticketText1: Typography
    private lateinit var ticketText2: Typography
    private lateinit var tickerLink: Typography
    private lateinit var imageCopy: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_ticket_result, container, false)
        initView(mainView)
        return  mainView
    }

    private fun initView(mainView: View) {
        imageCreated = mainView.findViewById(R.id.iv_ticket_created)
        ticketText1 = mainView.findViewById(R.id.ticket_text_1)
        ticketText2 = mainView.findViewById(R.id.ticket_text_2)
        tickerLink = mainView.findViewById(R.id.ticket_url)
        imageCopy = mainView.findViewById(R.id.copy_ticket)

        val issueUrl = arguments?.getString(EXTRA_IS_TICKET_LINK)

        imageCreated.loadRemoteImageDrawable(ADDRESS_INVALID)
        ticketText1.text = context?.let { HtmlLinkHelper(it, getString(R.string.ticket_text_1)).spannedString }
        ticketText2.text = context?.let { HtmlLinkHelper(it, getString(R.string.ticket_text_2)).spannedString }
        tickerLink.text = issueUrl
        tickerLink.setOnClickListener {
            FeedbackPageAnalytics.eventClickJiraLink()
            goToJiraLinkWebView(issueUrl)
        }
        imageCopy.setOnClickListener {
            onTextCopied(mainView, "label", tickerLink.text.toString())
        }

    }

    private fun goToJiraLinkWebView(issueUrl: String?) {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, issueUrl))
            this.startActivity(intent)
        }
    }

    private fun onTextCopied(view: View, label: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        Toaster.make(view, getString(R.string.copy_success), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
    }

    companion object {

        @JvmStatic
        fun newInstance(extra: Bundle): TicketCreatedFragment {
            return TicketCreatedFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_IS_TICKET_LINK, extra.getString(EXTRA_IS_TICKET_LINK))
                }
            }
        }

        private const val ADDRESS_INVALID = "ic_invalid_location.png"
    }

}