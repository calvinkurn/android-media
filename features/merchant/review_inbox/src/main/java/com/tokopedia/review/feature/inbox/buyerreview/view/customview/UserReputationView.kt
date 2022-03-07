package com.tokopedia.review.feature.inbox.buyerreview.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by stevenfredian on 8/16/17.
 */
class UserReputationView : BaseCustomView {

    private var percentageTextView: Typography? = null
    private var imageViewIcon: ImageView? = null
    private var layout: LinearLayout? = null
    private var showTooltip: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val styledAttributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.UserReputationView)
        try {
            showTooltip =
                styledAttributes.getBoolean(R.styleable.UserReputationView_usv_show_tooltip, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun init() {
        val view: View = inflate(context, R.layout.widget_reputation_user, this)
        imageViewIcon = view.findViewById(R.id.image_view_icon)
        percentageTextView = view.findViewById(R.id.text_view_percentage)
        layout = view.findViewById(R.id.buyer_reputation)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    fun getDrawable(context: Context, resId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) context.resources
            .getDrawable(
                resId,
                context.applicationContext.theme
            ) else AppCompatResources.getDrawable(context, resId)
    }

    fun setValue(
        percentageValue: String?, noReputation: Boolean,
        positiveValue: Int, neutralValue: Int,
        negativeValue: Int
    ) {
        if (TextUtils.isEmpty(percentageValue)) {
            percentageTextView?.visibility = GONE
        } else {
            percentageTextView?.visibility = VISIBLE
            percentageTextView?.text = percentageValue
        }
        if (noReputation) {
            imageViewIcon?.setImageDrawable(
                MethodChecker.getDrawable(
                    imageViewIcon?.context, R.drawable.review_ic_smiley_empty
                )
            )
        } else {
            imageViewIcon?.setImageDrawable(
                MethodChecker.getDrawable(
                    imageViewIcon?.context, R.drawable.review_ic_smiley_good
                )
            )
            if (showTooltip) {
                setBottomDialog(positiveValue, neutralValue, negativeValue)
            }
        }
    }

    @SuppressLint("UnifyComponentUsage")
    private fun setBottomDialog(positiveValue: Int, neutralValue: Int, negativeValue: Int) {
        layout?.setOnClickListener {
            BottomSheetDialog(context).apply {
                setContentView(R.layout.buyer_reputation_bottom_sheet_dialog)
                findViewById<Typography>(R.id.score_good)?.text = positiveValue.toString()
                findViewById<Typography>(R.id.score_netral)?.text = neutralValue.toString()
                findViewById<Typography>(R.id.score_bad)?.text = negativeValue.toString()
                findViewById<UnifyButton>(R.id.dialog_close_button)?.setOnClickListener { dismiss() }
                show()
            }
        }
    }
}