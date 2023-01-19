package com.tokopedia.topads.debit.autotopup.view.sheet

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TopAdsTopUpCreditInterruptSheet : BottomSheetUnify() {

    private var interruptSheetCancelButton: UnifyButton? = null
    private var interruptSheetApplyButton: UnifyButton? = null
    private var interruptSheetLearnMoreLinkTypography: Typography? = null
    private var interruptSheetDescription: Typography? = null
    private var interruptSheetEducationPointThreeDescription: Typography? = null
    var isAutoTopUpActive: Boolean = false
    var topUpCount: Int = 0
    var autoTopUpBonus: Double = 0.0

    companion object {
        fun newInstance() = TopAdsTopUpCreditInterruptSheet()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView =
            View.inflate(context, R.layout.topads_dash_top_up_intrrupt_model_sheet, null)
        showCloseIcon = false
        clearContentPadding = true
        isDragable = true
        showHeader = false
        customPeekHeight = 500
        setChild(contentView)
        initView(contentView)
    }

    private fun initView(contentView: View?) {
        interruptSheetCancelButton = contentView?.findViewById(R.id.interruptSheetCancelButton)
        interruptSheetApplyButton = contentView?.findViewById(R.id.interruptSheetApplyButton)
        interruptSheetLearnMoreLinkTypography =
            contentView?.findViewById(R.id.interruptSheetLearnMoreLinkTypography)
        interruptSheetDescription = contentView?.findViewById(R.id.interruptSheetDescription)
        interruptSheetEducationPointThreeDescription =
            contentView?.findViewById(R.id.interruptSheetEducationPointThreeDescription)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        renderViews()
    }

    private fun renderViews() {
        context?.let {
            interruptSheetDescription?.text = HtmlCompat.fromHtml(
                String.format(
                    it.getString(R.string.topads_dash_top_credit_interrupt_sheet_description),
                    topUpCount
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            interruptSheetEducationPointThreeDescription?.text = HtmlCompat.fromHtml(
                String.format(
                    it.getString(R.string.topads_dash_top_credit_interrupt_sheet_edu_point_three),
                    autoTopUpBonus.toString()
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        }
    }

    private fun setClickListeners() {
        interruptSheetLearnMoreLinkTypography?.setOnClickListener {
            if (context != null) {
                context?.openWebView("https://seller.tokopedia.com/edu/cara-top-up-saldo-topads")
            }
        }

        interruptSheetCancelButton?.setOnClickListener {
            TopAdsChooseCreditBottomSheet.newInstance().also {
                it.isAutoTopUpSelected = false
                it.isAutoTopUpActive = isAutoTopUpActive
                it.isFullpage = true
                it.show((context as FragmentActivity).supportFragmentManager)
                dismiss()
            }
        }

        interruptSheetApplyButton?.setOnClickListener {
            TopAdsChooseCreditBottomSheet.newInstance().also {
                it.isAutoTopUpSelected = true
                it.isAutoTopUpActive = isAutoTopUpActive
                it.isFullpage = true
                it.show((context as FragmentActivity).supportFragmentManager)
                dismiss()
            }
        }
    }

    private fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
        //create span builder and replaces current text with it
        text = SpannableStringBuilder.valueOf(text).apply {
            //search for all URL spans and replace all spans with our own clickable spans
            getSpans(0, length, URLSpan::class.java).forEach {
                //add new clickable span at the same position
                setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            onClicked?.invoke(it.url)
                        }
                    },
                    getSpanStart(it),
                    getSpanEnd(it),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                //remove old URLSpan
                removeSpan(it)
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
    ) {
        show(fragmentManager, "top_up_interrupt_sheet")
    }
}



