package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity

class SaldoIntroFragment : TkpdBaseV4Fragment() {

    private var viewMore: TextView? = null
    private var gotoSaldoPage: Button? = null

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_intro, container, false)
        viewMore = view.findViewById(com.tokopedia.saldodetails.R.id.si_view_more)
        gotoSaldoPage = view.findViewById(com.tokopedia.saldodetails.R.id.si_goto_balance_page)
        (view.findViewById<View>(com.tokopedia.saldodetails.R.id.dana_refund) as TextView).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.saldodetails.R.drawable.ic_refund), null, null, null)
        (view.findViewById<View>(com.tokopedia.saldodetails.R.id.disbursement_fund) as TextView).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.saldodetails.R.drawable.ic_refund_disbursement), null, null, null)
        (view.findViewById<View>(com.tokopedia.saldodetails.R.id.hasil_penjualan) as TextView).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.saldodetails.R.drawable.ic_sales_report), null, null, null)
        (view.findViewById<View>(com.tokopedia.saldodetails.R.id.disbursement_priority_balance) as TextView).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, com.tokopedia.saldodetails.R.drawable.ic_balance_disbursement), null, null, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = resources.getString(com.tokopedia.saldodetails.R.string.saldo_intro_help)

        val spannableString = SpannableString(text)
        val indexOfString = getString(com.tokopedia.saldodetails.R.string.saldo_help_text)
        val startIndexOfLink = text.indexOf(indexOfString)
        if (startIndexOfLink != -1) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    startWebView(SaldoDetailsConstants.SALDO_HELP_URL)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = resources.getColor(com.tokopedia.design.R.color.green_250)
                }
            }, startIndexOfLink, startIndexOfLink + resources.getString(com.tokopedia.saldodetails.R.string.saldo_help_text).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewMore!!.movementMethod = LinkMovementMethod.getInstance()
            viewMore!!.text = spannableString
        }

        gotoSaldoPage!!.setOnClickListener { v ->
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
            if (activity != null) {
                activity!!.finish()
            }
        }
    }

    private fun startWebView(url: String) {
        startActivity(SaldoWebViewActivity.getWebViewIntent(context, url))
    }

    companion object {

        fun newInstance(): Fragment {
            return SaldoIntroFragment()
        }
    }
}

