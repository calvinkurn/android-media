package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.sheet.AutoAdsCreateSheet
import kotlinx.android.synthetic.main.topads_autoads_create_auto_ad_layout.*

class CreateAutoAdsFragment : AutoAdsBaseBudgetFragment(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.topads_autoads_create_auto_ad_layout
    }

    override fun setUpView(view: View) {}

    private var MORE_INFO = " Info Selengkapnya"

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun setListener() {
        btnSubmit.setOnClickListener(this)
        tipBtn.setOnClickListener(this)
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
        btn_submit.isEnabled = false
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
        btn_submit.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(ContextCompat.getColor(context!!, com.tokopedia.design.R.color.tkpd_main_green), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))

            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text.movementMethod = LinkMovementMethod.getInstance()
        info_text.append(spannableText)

    }

    override fun getScreenName(): String? {
        return CreateAutoAdsFragment::class.java.name
    }

    companion object {

        fun newInstance(): CreateAutoAdsFragment {

            val args = Bundle()
            val fragment = CreateAutoAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_submit) {
            activatedAds()
        }
        if (v?.id == R.id.tip_btn) {
            AutoAdsCreateSheet.newInstance(context!!).show()
        }
    }
}