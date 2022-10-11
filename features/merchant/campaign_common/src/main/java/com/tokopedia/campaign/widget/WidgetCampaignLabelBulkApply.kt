package com.tokopedia.campaign.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.databinding.LayoutCampaignWidgetLabelBulkApplyBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class WidgetCampaignLabelBulkApply(
    context:
    Context, attrs: AttributeSet
) : CardUnify2(context, attrs) {

    private var labelBackgroundDisabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
    private var labelBackgroundEnabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    private var labelIconDisabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
    private var labelIconEnabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    private var labelTitleDisabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
    private var labelTitleEnabledColor: Int = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    private var viewBinding: LayoutCampaignWidgetLabelBulkApplyBinding? = null
    private var textTitle: Typography? = null
    private var labelBackground: View? = null
    private var labelIcon: ImageUnify? = null
    private var chevronIcon: ImageUnify? = null
    private var cardView: CardUnify2? = null

    init {
        viewBinding = LayoutCampaignWidgetLabelBulkApplyBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        initView()
        configCardView()
        isEnabled = true
    }

    fun setTitle(labelTitle: String) {
        textTitle?.text = labelTitle
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        cardView?.setOnClickListener(listener)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        configLabelState(enabled)
    }

    private fun configLabelState(enabled: Boolean) {
        if (enabled) {
            labelBackground?.setBackgroundColor(labelBackgroundEnabledColor)
            labelIcon?.setColorFilter(labelIconEnabledColor)
            textTitle?.setTextColor(labelTitleEnabledColor)
            chevronIcon?.show()
        }else{
            labelBackground?.setBackgroundColor(labelBackgroundDisabledColor)
            labelIcon?.setColorFilter(labelIconDisabledColor)
            textTitle?.setTextColor(labelTitleDisabledColor)
            chevronIcon?.hide()
        }
    }

    private fun configCardView() {
        cardType = TYPE_CLEAR
    }

    private fun initView() {
        viewBinding?.let {
            cardView = it.cardLabelBulkApply
            textTitle = it.textLabelTitle
            labelBackground = it.labelBackground
            labelIcon = it.labelIcon
            chevronIcon = it.chevronIcon
        }
    }

}
