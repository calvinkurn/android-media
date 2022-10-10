package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.activity.AffiliatePromoSearchActivity
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class AffiliateBottomSheetPromoCopyPasteInfo : BottomSheetUnify() {
    private var contentView: View? = null

    companion object {

        private const val RECOMMENDED_DESC_SPAN_LENGTH = 12
        private const val PASTE_ACTION_SPAN_LENGTH = 30
        private const val ZERO = 0

        fun newInstance(): AffiliateBottomSheetPromoCopyPasteInfo {
            return AffiliateBottomSheetPromoCopyPasteInfo()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        setUpView()
        setData()
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setUpView() {
        contentView =
            View.inflate(context, R.layout.affiliate_bottom_sheet_promo_copy_paste_info, null)
        setChild(contentView)
        setTitle(getString(R.string.copy_paste_info_title))
        clearContentPadding = true
        isFullpage = true
    }

    private fun setData() {
        val recommendedDesc = getString(R.string.copy_paste_recommend_desc)
        val pasteActionDesc = getString(R.string.copy_paste_action_desc)
        val promosikanIndex = recommendedDesc.indexOf("promosikan")

        contentView?.findViewById<Typography>(R.id.recommend_desc)?.text =
            boldSpan(recommendedDesc, promosikanIndex, RECOMMENDED_DESC_SPAN_LENGTH)

        contentView?.findViewById<Typography>(R.id.copy_paste_action_text)?.text =
            boldSpan(pasteActionDesc, ZERO, PASTE_ACTION_SPAN_LENGTH)

        contentView?.findViewById<UnifyButton>(R.id.paste_link_button)?.setOnClickListener {
            startActivity(Intent(context, AffiliatePromoSearchActivity::class.java))
        }
    }

    private fun boldSpan(str: String, start: Int, end: Int): SpannableString {
        val sb = SpannableString(str)
        if (start >= 0 && end < str.length) {
            context?.let { ctx ->
                val boldFont = Typography.getFontType(ctx, true, Typography.PARAGRAPH_2)
                sb.setSpan(
                    CustomTypefaceSpan(boldFont!!),
                    start,
                    start + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return sb
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseIconColor()
    }

    private fun changeCloseIconColor() {
        context?.let { ctx ->
            val color =
                MethodChecker.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            bottomSheetClose.drawable?.apply {
                mutate()
                colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    color,
                    BlendModeCompat.SRC_ATOP
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initInject() {
        getComponent().injectPromoCopyPasteInfoBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    private inner class CustomTypefaceSpan(private val typeface: Typeface?) :
        MetricAffectingSpan() {
        override fun updateDrawState(paint: TextPaint) {
            paint.typeface = typeface
        }

        override fun updateMeasureState(paint: TextPaint) {
            paint.typeface = typeface
        }
    }
}
