package com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.RecommendationTickerUiModel

class RecommendationTickerViewHolder(itemView: View,
                                     onClickableSpanClickedListener: OnClickableSpanClickedListener?)
    : AbstractViewHolder<RecommendationTickerUiModel>(itemView) {

    interface OnClickableSpanClickedListener {
        fun onClickableSpanClicked()
    }

    private var context: Context? = null
    private var titleView: Typography? = null
    private var iconView: AppCompatImageView? = null
    private var descView: Typography? = null
    private var listener: OnClickableSpanClickedListener? = null

    init {
        context = itemView.context
        titleView = itemView.findViewById(R.id.tv_recommendation_title)
        iconView = itemView.findViewById(R.id.iv_recommendation_icon)
        descView = itemView.findViewById(R.id.tv_recommendation_desc)
        listener = onClickableSpanClickedListener
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_recommendation
        private const val CTA_LENGTH = 8
    }

    override fun bind(element: RecommendationTickerUiModel?) {
        element?.run {
            if (this.isRecommendationApplied) {
                renderVoucherRecommendationTicker()
            } else {
                renderVoucherRecommendationTickerCta(listener)
            }
        }
    }

    private fun renderVoucherRecommendationTicker() {
        titleView?.setText(R.string.mvc_voucher_recommendation_title)
        iconView?.setImageResource(R.drawable.ic_mvc_voucher_img)
        descView?.setText(R.string.mvc_voucher_recommendation)
    }

    private fun renderVoucherRecommendationTickerCta(listener: OnClickableSpanClickedListener?) {
        titleView?.setText(R.string.mvc_voucher_recommendation_cta_title)
        iconView?.setImageResource(R.drawable.ic_tkpd_thumbs_up)
        val text = getString(R.string.mvc_voucher_recommendation_cta)
        val ss = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.run {
                    this.onClickableSpanClicked()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                context?.run {
                    ds.color = ContextCompat.getColor(this, R.color.unify_G500)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ds.underlineColor = 0
                }
            }
        }
        ss.setSpan(clickableSpan, (text.length - CTA_LENGTH), text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        descView?.text = ss
        descView?.movementMethod = LinkMovementMethod.getInstance()
    }
}