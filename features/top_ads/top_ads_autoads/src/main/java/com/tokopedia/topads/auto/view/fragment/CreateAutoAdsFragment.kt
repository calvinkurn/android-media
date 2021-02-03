package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_autoads_create_auto_ad_layout.*

private const val CLICK_IKLANKAN_BUTTON = "click-iklankan auto"
private const val MORE_INFO = " Info Selengkapnya"

class CreateAutoAdsFragment : AutoAdsBaseBudgetFragment(), View.OnClickListener {

    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null
    override fun getLayoutId(): Int {
        return R.layout.topads_autoads_create_auto_ad_layout
    }

    override fun setUpView(view: View) {}

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

    override fun showButtonLayout() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_auto_bg.setImageDrawable(context?.getResDrawable(R.drawable.card_auto_ads_create))
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.tip_title)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_ic_tips))
        }

        tipBtn.addItem(tooltipView)
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(ContextCompat.getColor(context!!, com.tokopedia.unifyprinciples.R.color.Unify_G500), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_IKLANKAN_BUTTON, "")
            activatedAds()
        }
        if (v?.id == R.id.tip_btn) {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc3, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.isDragable = false
            tipsListSheet?.isHideable = false
            tipsListSheet?.setTitle(getString(R.string.tip_title))
            tipsListSheet?.show(childFragmentManager, "")
        }
    }
}