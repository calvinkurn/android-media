package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
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
import com.tokopedia.affiliate_toko.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateBottomSheetPromoCopyPasteInfo : BottomSheetUnify() {
    private var contentView: View? = null

    companion object {

        private const val SPAN_LENGTH = 12

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
        contentView =
            View.inflate(context, R.layout.affiliate_bottom_sheet_promo_copy_paste_info, null)
        setTitle(getString(R.string.copy_paste_info_title))
        setData()
        setChild(contentView)
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setData() {
        val spanStr = getString(R.string.paste_info_step_two)
        context?.let { ctx ->
            val boldFont = Typography.getFontType(ctx, true, Typography.PARAGRAPH_2)

            val fromChar = spanStr.indexOf("Promosikan") - 1
            val sb = SpannableString(spanStr)
            sb.setSpan(
                CustomTypefaceSpan(boldFont!!),
                fromChar,
                fromChar + SPAN_LENGTH,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            contentView?.findViewById<Typography>(R.id.paste_info_step_two)?.setText(sb)
        }

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

    inner class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {
        override fun updateDrawState(paint: TextPaint) {
            paint.typeface = typeface
        }

        override fun updateMeasureState(paint: TextPaint) {
            paint.typeface = typeface
        }
    }
}
