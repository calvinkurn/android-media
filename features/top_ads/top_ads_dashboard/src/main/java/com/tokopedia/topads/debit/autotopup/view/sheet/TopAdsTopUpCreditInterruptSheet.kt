package com.tokopedia.topads.debit.autotopup.view.sheet

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.INSUFFICIENT_CREDIT
import com.tokopedia.topads.dashboard.data.utils.Utils.openWebView
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TopAdsTopUpCreditInterruptSheet : BottomSheetUnify() {

    private var interruptSheetCancelButton: UnifyButton? = null
    private var interruptSheetApplyButton: UnifyButton? = null
    private var interruptSheetLearnMoreLinkTypography: Typography? = null
    private var interruptSheetDescription: Typography? = null
    private var interruptSheetTitle: Typography? = null
    private var interruptSheetEducationPointThreeDescription: Typography? = null
    var isAutoTopUpActive: Boolean = false
    var topUpCount: Int = Int.ZERO
    var autoTopUpBonus: Double = 0.0
    var creditPerformance: String = ""
    var onButtonClick: ((isNegativeButtonClicked: Boolean) -> Unit)? = null

    companion object {
        fun newInstance() = TopAdsTopUpCreditInterruptSheet()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (activity is TopAdsCreditTopUpActivity) activity?.finish()
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
        setDefaultConfigs()
        setChild(contentView)
        initView(contentView)
    }

    private fun setDefaultConfigs() {
        showCloseIcon = false
        clearContentPadding = true
        isDragable = true
        showHeader = false
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    private fun initView(contentView: View?) {
        interruptSheetCancelButton = contentView?.findViewById(R.id.interruptSheetCancelButton)
        interruptSheetTitle = contentView?.findViewById(R.id.interruptSheetTitle)
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
            val isInsufficientCredit = creditPerformance.equals(INSUFFICIENT_CREDIT, true)
            renderTitle(it, isInsufficientCredit)
            renderDescription(it, isInsufficientCredit)
            renderBulletPoint(it)
        }
    }

    private fun renderTitle(context: Context, isInsufficientCredit: Boolean) {
       if (isInsufficientCredit){
           interruptSheetTitle?.text = context.getString(R.string.top_ads_interrupt_bottom_sheet_title_two)
       }else{
           interruptSheetTitle?.text = context.getString(R.string.top_ads_interrupt_bottom_sheet_title_one)
       }
    }

    private fun renderBulletPoint(context: Context) {
        interruptSheetEducationPointThreeDescription?.text = HtmlCompat.fromHtml(
            String.format(
                context.getString(R.string.topads_dash_top_credit_interrupt_sheet_edu_point_three),
                autoTopUpBonus.toString()
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun renderDescription(context: Context, isInsufficientCredit: Boolean) {
        if (isInsufficientCredit){
            interruptSheetDescription?.text = context.getString(R.string.topads_dash_top_credit_interrupt_sheet_description_two)
        }else{
            interruptSheetDescription?.text = HtmlCompat.fromHtml(
                String.format(
                    context.getString(R.string.topads_dash_top_credit_interrupt_sheet_description_one),
                    topUpCount
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    private fun setClickListeners() {
        interruptSheetLearnMoreLinkTypography?.setOnClickListener {
            context?.let {
                it.openWebView(it.getString(R.string.topads_credit_top_up_url))
            }
        }

        interruptSheetCancelButton?.setOnClickListener {
           TopAdsChooseCreditBottomSheet.newInstance().also {
                it.isAutoTopUpActive = isAutoTopUpActive
                it.show((context as FragmentActivity).supportFragmentManager)
                onButtonClick?.invoke(true)
                dismiss()
            }
        }

        interruptSheetApplyButton?.setOnClickListener {
            TopAdsChooseCreditBottomSheet.newInstance().also {
                it.isAutoTopUpSelected = true
                it.isAutoTopUpActive = isAutoTopUpActive
                it.isFullpage = true
                it.show((context as FragmentActivity).supportFragmentManager)
                onButtonClick?.invoke(false)
                dismiss()
            }
        }
    }

    fun show(
        fragmentManager: FragmentManager,
    ) {
        show(fragmentManager, "")
    }
}



