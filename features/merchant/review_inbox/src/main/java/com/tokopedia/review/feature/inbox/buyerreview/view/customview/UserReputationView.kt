package com.tokopedia.review.feature.inbox.buyerreview.view.customview

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
    private var dialog: BottomSheetDialog? = null
    private var showTooltip: Boolean = false

    constructor(context: Context?) : super((context)!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super((context)!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        (context)!!, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val styledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.UserReputationView)
        try {
            showTooltip =
                styledAttributes.getBoolean(R.styleable.UserReputationView_usv_show_tooltip, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun init() {
        val view: View = inflate(getContext(), R.layout.widget_reputation_user, this)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) return context.getResources()
            .getDrawable(
                resId,
                context.getApplicationContext().getTheme()
            ) else return AppCompatResources.getDrawable(context, resId)
    }

    fun setValue(
        percentageValue: String?, noReputation: Boolean,
        positiveValue: Int, neutralValue: Int,
        negativeValue: Int
    ) {
        if (TextUtils.isEmpty(percentageValue)) {
            percentageTextView!!.setVisibility(GONE)
        } else {
            percentageTextView!!.setVisibility(VISIBLE)
            percentageTextView!!.setText(percentageValue)
        }
        if (noReputation) {
            imageViewIcon!!.setImageDrawable(
                MethodChecker.getDrawable(
                    imageViewIcon!!.getContext(), R.drawable.review_ic_smiley_empty
                )
            )
        } else {
            imageViewIcon!!.setImageDrawable(
                MethodChecker.getDrawable(
                    imageViewIcon!!.getContext(), R.drawable.review_ic_smiley_good
                )
            )
            if (showTooltip) {
                setBottomDialog(positiveValue, neutralValue, negativeValue)
            }
        }
    }

    private fun setBottomDialog(positiveValue: Int, neutralValue: Int, negativeValue: Int) {
        layout!!.setOnClickListener(object : OnClickListener {
            public override fun onClick(v: View) {
                dialog = BottomSheetDialog(getContext())
                dialog!!.setContentView(R.layout.buyer_reputation_bottom_sheet_dialog)
                val positiveText: Typography? = dialog!!.findViewById(R.id.score_good)
                if (positiveText != null) {
                    positiveText.setText(positiveValue.toString())
                }
                val neutralText: Typography? = dialog!!.findViewById(R.id.score_netral)
                if (neutralText != null) {
                    neutralText.setText(neutralValue.toString())
                }
                val negativeText: Typography? = dialog!!.findViewById(R.id.score_bad)
                if (negativeText != null) {
                    negativeText.setText(negativeValue.toString())
                }
                val closeButton: UnifyButton? = dialog!!.findViewById(R.id.dialog_close_button)
                if (closeButton != null) {
                    closeButton.setOnClickListener(object : OnClickListener {
                        public override fun onClick(v: View) {
                            dialog!!.dismiss()
                        }
                    })
                }
                dialog!!.show()
            }
        })
    }
}