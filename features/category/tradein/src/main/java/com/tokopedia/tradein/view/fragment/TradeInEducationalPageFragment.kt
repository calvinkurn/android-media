package com.tokopedia.tradein.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.tradein.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TradeInEducationalPageFragment : TkpdBaseV4Fragment() {

    companion object {

        const val TRADE_IN_BM_LINK = "tokopedia://category/tradein_black_market"
        const val TRADE_IN_BM_STRING = "Jika terbukti HP-mu termasuk barang BM, transaksi akan dibatalkan. Selengkapnya"
        const val TRADE_IN_BM_STRING_CLICK = "Selengkapnya"
        const val TRADE_IN_TNC_LINK = "tokopedia://category/tradein_tnc"
        const val TRADE_IN_TNC_STRING = "Dengan mulai Tukar Tambah, kamu setuju dengan\n" +
                "Syarat & Ketentuan yang berlaku."
        const val TRADE_IN_TNC_STRING_CLICK = "Syarat & Ketentuan"

        fun getFragmentInstance(): Fragment {
            return TradeInEducationalPageFragment()
        }
    }

    var onDoTradeInClick : OnDoTradeInClick? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tradein_educational_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    fun setUpTradeInClick(onDoTradeInClick: OnDoTradeInClick){
        this.onDoTradeInClick = onDoTradeInClick
    }

    private fun setUpView() {
        view?.findViewById<UnifyButton>(R.id.btn_continue)?.setOnClickListener {
            onDoTradeInClick?.onClick()
        }
        view?.findViewById<NavToolbar>(R.id.edu_navToolbar)?.setOnBackButtonClickListener {
            onDoTradeInClick?.onBackClick()
        }
        view?.findViewById<Typography>(R.id.edu_subtext_5)?.apply {
            text = getSpan(TRADE_IN_BM_STRING, TRADE_IN_BM_STRING_CLICK, TRADE_IN_BM_LINK)
            movementMethod = LinkMovementMethod.getInstance()
        }
        view?.findViewById<Typography>(R.id.edu_subtext_6)?.apply {
            text = getSpan(TRADE_IN_TNC_STRING, TRADE_IN_TNC_STRING_CLICK, TRADE_IN_TNC_LINK)
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun getSpan(spanString: String, spanClickString: String, link: String): CharSequence? {
        val spannableString = SpannableString(spanString)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, link)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            }
        }
        val start = spanString.indexOf(spanClickString)
        val end = spanString.indexOf(spanClickString) + spanClickString.length
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    override fun getScreenName(): String {
        return ""
    }


    interface OnDoTradeInClick{
        fun onClick()
        fun onBackClick()
    }
}