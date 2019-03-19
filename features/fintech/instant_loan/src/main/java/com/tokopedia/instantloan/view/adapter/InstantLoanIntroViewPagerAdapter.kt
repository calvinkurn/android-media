package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.common.analytics.InstantLoanAnalytics
import com.tokopedia.instantloan.common.analytics.InstantLoanEventConstants
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.WEB_LINK_LEARN_MORE
import com.tokopedia.instantloan.network.InstantLoanUrl.COMMON_URL.WEB_LINK_TNC
import com.tokopedia.instantloan.view.activity.InstantLoanActivity
import com.tokopedia.instantloan.view.presenter.InstantLoanPresenter


class InstantLoanIntroViewPagerAdapter(private val mActivity: InstantLoanActivity, private val mLayouts: IntArray, private val mPresenter: InstantLoanPresenter) : PagerAdapter() {
    private val mLayoutInflater: LayoutInflater
    private val link = mActivity.resources.getString(R.string.instant_loan_clickable_link)
    internal var instantLoanAnalytics: InstantLoanAnalytics? = null

    init {
        this.mLayoutInflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mLayoutInflater.inflate(mLayouts[position], container, false)

        instantLoanAnalytics = InstantLoanAnalytics()

        if (position == mLayouts.size - 2) {

            val textView = view.findViewById<TextView>(R.id.text_label_processing_time)

            val clickableSpan = object : ClickableSpan() {

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = mActivity.resources.getColor(R.color.tkpd_green_header)
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_LEARN_MORE)
                    mActivity.openWebView(WEB_LINK_LEARN_MORE)
                }
            }

            val spannableString = SpannableString(textView.text.toString())

            spannableString.setSpan(clickableSpan,
                    mActivity.resources.getString(R.string.text_intro_slide_2).length + 1,
                    textView.text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textView.text = spannableString

            textView.movementMethod = LinkMovementMethod.getInstance()


        } else if (position == mLayouts.size - 1) {
            val textTnC = view.findViewById<TextView>(R.id.text_tnc)
            val startIndexOfLink = textTnC.text.toString().indexOf(link)

            val spannableString = SpannableString(textTnC.text.toString())

            val clickableSpan = object : ClickableSpan() {

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = mActivity.resources.getColor(R.color.tkpd_green_header)
                    ds.isUnderlineText = false
                }

                override fun onClick(view: View) {
                    sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_TNC)
                    mActivity.openWebView(WEB_LINK_TNC)
                }
            }

            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textTnC.text = spannableString
            textTnC.movementMethod = LinkMovementMethod.getInstance()
            view.findViewById<View>(R.id.button_connect_device).setOnClickListener { v ->
                sendLoanPopupClickEvent(InstantLoanEventConstants.EventLabel.PL_POPUP_CONNECT_DEVICE)
                mPresenter.startDataCollection()
            }

        }
        view.tag = position
        container.addView(view)

        return view
    }

    private fun sendLoanPopupClickEvent(label: String) {
        if (instantLoanAnalytics == null) {
            return
        }
        instantLoanAnalytics!!.eventLoanPopupClick(label)
    }

    override fun getCount(): Int {
        return mLayouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}