package com.tokopedia.review.feature.inbox.buyerreview.view.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by stevenfredian on 8/16/17.
 */
class ShopReputationView : BaseCustomView {
    private var reputationLayout: LinearLayout? = null
    private var dialog: BottomSheetDialog? = null
    private var showTooltip: Boolean = false
    private var medalWidth: Int = 0

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

    fun getDrawable(context: Context, resId: Int): Drawable? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) return context.getResources()
            .getDrawable(
                resId,
                context.getApplicationContext().getTheme()
            ) else return AppCompatResources.getDrawable(context, resId)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val styledAttributes: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.ShopReputationView)
        try {
            showTooltip =
                styledAttributes.getBoolean(R.styleable.ShopReputationView_srv_show_tooltip, false)
            medalWidth = styledAttributes.getDimension(
                R.styleable.ShopReputationView_srv_medal_width,
                getContext().getResources().getDimensionPixelSize(R.dimen.dp_15)
                    .toFloat()
            ).toInt()
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun init() {
        val view: View = inflate(getContext(), R.layout.widget_reputation_shop, this)
        reputationLayout = view.findViewById(R.id.layout_reputation_view)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    fun setValue(medalType: Int, level: Int, point: String) {
        var level: Int = level
        var point: String = point
        reputationLayout!!.removeAllViews()
        val imageResource: Int = getIconResource(medalType)
        if (medalType == MEDAL_TYPE_0) {
            level = 1
            point = "0"
        }
        updateMedalView(reputationLayout, imageResource, level)
        if (showTooltip) {
            setToolTip(point, medalType, level)
        }
    }

    private fun updateMedalView(
        reputationLayout: LinearLayout?,
        @DrawableRes imageResource: Int,
        levelMedal: Int
    ) {
        val medalMargin: Int = getContext().getResources().getDimensionPixelSize(R.dimen.dp_3)
        for (i in 0 until levelMedal) {
            val medal: View = getGeneratedMedalImage(imageResource)
            if (i < levelMedal) {
                val params: LinearLayout.LayoutParams =
                    medal.getLayoutParams() as LinearLayout.LayoutParams
                params.rightMargin = medalMargin
                medal.setLayoutParams(params)
            }
            reputationLayout!!.addView(medal)
        }
    }

    private fun setToolTip(pointValue: String, medalType: Int, level: Int) {
        reputationLayout!!.setOnClickListener(object : OnClickListener {
            public override fun onClick(v: View) {
                dialog = BottomSheetDialog(getContext())
                dialog!!.setContentView(com.tokopedia.design.R.layout.seller_reputation_bottom_sheet_dialog)
                val point: Typography? =
                    dialog!!.findViewById(com.tokopedia.design.R.id.reputation_point)
                val pointText: String =
                    if (TextUtils.isEmpty(pointValue) || (pointValue == "0")) getContext().getString(
                        com.tokopedia.design.R.string.no_reputation_yet
                    ) else pointValue.toString() + " " + getContext().getString(
                        R.string.point
                    )
                if (point != null) {
                    point.setText(pointText)
                }
                val sellerReputation: LinearLayout? =
                    dialog!!.findViewById(com.tokopedia.design.R.id.seller_reputation)
                updateMedalView(sellerReputation, getIconResource(medalType), level)
                val closeButton: Button? = dialog!!.findViewById(R.id.dialog_close_button)
                if (closeButton != null) closeButton.setOnClickListener(object : OnClickListener {
                    public override fun onClick(v: View) {
                        dialog!!.dismiss()
                    }
                })
                dialog!!.show()
            }
        })
    }

    private fun getGeneratedMedalImage(@DrawableRes imageResource: Int): ImageView {
        val imageView: ImageView = ImageView(getContext())
        imageView.setAdjustViewBounds(true)
        val param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(medalWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView.setLayoutParams(param)
        imageView.setImageDrawable(getDrawable(imageView.getContext(), imageResource))
        return imageView
    }

    private fun getIconResource(type: Int): Int {
        when (type) {
            MEDAL_TYPE_1 -> return com.tokopedia.design.R.drawable.ic_badge_bronze
            MEDAL_TYPE_2 -> return com.tokopedia.design.R.drawable.ic_badge_silver
            MEDAL_TYPE_3 -> return com.tokopedia.design.R.drawable.ic_badge_gold
            MEDAL_TYPE_4 -> return com.tokopedia.design.R.drawable.ic_badge_diamond
            else -> return com.tokopedia.design.R.drawable.ic_badge_none
        }
    }

    companion object {
        val MEDAL_TYPE_0: Int = 0
        val MEDAL_TYPE_1: Int = 1
        val MEDAL_TYPE_2: Int = 2
        val MEDAL_TYPE_3: Int = 3
        val MEDAL_TYPE_4: Int = 4
    }
}