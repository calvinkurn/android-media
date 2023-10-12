package com.tokopedia.review.feature.inbox.buyerreview.view.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by stevenfredian on 8/16/17.
 */
class ShopReputationView : BaseCustomView {

    private var reputationLayout: LinearLayout? = null
    private var ivReputationBadge: ImageUnify? = null
    private var showTooltip: Boolean = false
    private var medalWidth: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    fun getDrawable(context: Context, resId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) context.resources
            .getDrawable(
                resId,
                context.applicationContext.theme
            ) else AppCompatResources.getDrawable(context, resId)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val styledAttributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ShopReputationView)
        try {
            showTooltip =
                styledAttributes.getBoolean(R.styleable.ShopReputationView_srv_show_tooltip, false)
            medalWidth = styledAttributes.getDimension(
                R.styleable.ShopReputationView_srv_medal_width,
                context.resources.getDimensionPixelSize(R.dimen.dp_15)
                    .toFloat()
            ).toInt()
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun init() {
        val view: View = inflate(context, R.layout.widget_reputation_shop, this)
        reputationLayout = view.findViewById(R.id.layout_reputation_view)
        ivReputationBadge = view.findViewById(R.id.iv_reputation_badge)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    fun setValue(badgeUrl: String, score: Int) {
        ivReputationBadge?.loadImage(badgeUrl)
        if (showTooltip) {
            setToolTip(badgeUrl, score)
        }
    }

    private fun setToolTip(badgeUrl: String, score: Int) {
        reputationLayout?.setOnClickListener {
            val dialog = BottomSheetDialog(context)
            dialog.apply {
                dialog.setContentView(com.tokopedia.review.inbox.R.layout.seller_reputation_bottom_sheet_dialog)
                val point: Typography? =
                    dialog.findViewById(com.tokopedia.review.inbox.R.id.reputation_point)
                val pointText: String =
                    if (score.isZero()) context.getString(
                        com.tokopedia.review.inbox.R.string.no_reputation_yet
                    ) else "$score " + context.getString(
                        R.string.point
                    )
                if (point != null) {
                    point.text = pointText
                }
                val ivReputationBadge: ImageUnify? =
                    dialog.findViewById(com.tokopedia.review.inbox.R.id.iv_reputation_badge)
                ivReputationBadge?.loadImage(badgeUrl)
                val closeButton: Button? = dialog.findViewById(R.id.dialog_close_button)
                closeButton?.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }
        }
    }
}
