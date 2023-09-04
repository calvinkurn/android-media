package com.tokopedia.scp_rewards_touchpoints.touchpoints

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.scp_rewards_touchpoints.common.util.ViewUtil.rotate
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpToasterData
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.unifycomponents.R as unifycomponentsR

object ScpRewardsToaster {
    private val emptyClickListener = View.OnClickListener { }

    private var onCTAClick: View.OnClickListener = emptyClickListener
    private var ctaText: String = ""
    private const val WITHOUT_CTA = 0
    private const val WITH_CTA = 1

    /**
     * Toaster normal style
     */
    const val TYPE_NORMAL = 0

    /**
     * Toaster error style
     */

    const val LENGTH_SHORT = Snackbar.LENGTH_SHORT
    const val LENGTH_LONG = Snackbar.LENGTH_LONG
    const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE
    const val DEFAULT_DURATION = 3500

    var toasterLength = Snackbar.LENGTH_SHORT

    var toasterCustomBottomHeight: Int = 0
    var toasterCustomCtaWidth: Int = 0

    @JvmStatic
    fun build(view: View, toasterData: ScpToasterData, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL, clickListener: View.OnClickListener = View.OnClickListener {}): Snackbar {
        toasterLength = duration
        val cta = if (toasterData.ctaIsShown) WITH_CTA else WITHOUT_CTA
        if (cta == WITH_CTA) {
            onCTAClick = clickListener
            ctaText = toasterData.ctaText
        }
        return initToaster(view, toasterData, type, cta)!!
    }

    private fun initToaster(view: View, toasterData: ScpToasterData, type: Int = TYPE_NORMAL, cta: Int = WITHOUT_CTA): Snackbar {
        val viewTarget: View = view

        val tempSnackBar = Snackbar.make(viewTarget, "", toasterLength)

        val viewLayout = View.inflate(viewTarget.context, R.layout.scp_toaster, null)

        val frame_icon = viewLayout.findViewById<ImageUnify>(R.id.frame_icon)
        val badge = viewLayout.findViewById<ImageUnify>(R.id.iv_icon)
        val tv_title = viewLayout.findViewById<TextView>(R.id.scp_toaster_title)
        val tv_desc = viewLayout.findViewById<TextView>(R.id.scp_toaster_description)
        tv_title.text = toasterData.title
        tv_desc.text = toasterData.subtitle

        frame_icon.setImageBitmap(toasterData.sunburstImage)
        badge.setImageBitmap(toasterData.iconImage)

        val constraintLayoutToaster = viewLayout.findViewById<View>(R.id.constraintLayoutToaster)

        if (type == TYPE_NORMAL) {
            constraintLayoutToaster.setBackgroundResource(R.drawable.scp_rewards_toaster_bg_normal)
        } else {
            constraintLayoutToaster.setBackgroundResource(unifycomponentsR.drawable.toaster_bg_error)
        }

        val actionTextButton = viewLayout.findViewById<Typography>(R.id.snackbar_btn)
        if (toasterCustomCtaWidth > 0) {
            actionTextButton.maxWidth = toasterCustomCtaWidth
            toasterCustomCtaWidth = 0
        }

        if (cta == WITH_CTA) {
            actionTextButton.text = ctaText
            actionTextButton.setOnClickListener {
                viewLayout.animate().alpha(0f).setDuration(UnifyMotion.T3).setInterpolator(UnifyMotion.EASE_OUT).start()
                tempSnackBar.dismiss()
                onCTAClick.onClick(it)
            }
            actionTextButton.visibility = View.VISIBLE
        } else {
            actionTextButton.setOnClickListener(null)
            actionTextButton.visibility = View.GONE
        }

        val layout = tempSnackBar.view as Snackbar.SnackbarLayout
        layout.isClickable = true
        layout.setBackgroundColor(Color.TRANSPARENT)
        layout.removeAllViews()
        layout.addView(viewLayout, 0)

        val params = layout.layoutParams
        if (params is FrameLayout.LayoutParams) {
            params.gravity = Gravity.BOTTOM
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
        } else if (params is CoordinatorLayout.LayoutParams) {
            params.gravity = Gravity.BOTTOM
            params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
        }
        layout.layoutParams = params

        layout.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
        layout.isEnabled = false

        if (toasterCustomBottomHeight > 0) {
            layout.setPadding(16.toPx(), 0, 16.toPx(), toasterCustomBottomHeight + 16.toPx())
        }

        viewLayout.visibility = View.INVISIBLE

        tempSnackBar.addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                viewLayout.visibility = View.VISIBLE

                layout.translationY = toasterCustomBottomHeight + layout.measuredHeight + 0f
                layout.animate().translationY(0f).setDuration(UnifyMotion.T3).setInterpolator(UnifyMotion.EASE_OUT).start()

                layout.scaleX = 0.8f
                layout.scaleY = 0.8f

                layout.animate().scaleX(1f).setDuration(UnifyMotion.T3).setInterpolator(UnifyMotion.EASE_OUT).start()
                layout.animate().scaleY(1f).setDuration(UnifyMotion.T3).setInterpolator(UnifyMotion.EASE_OUT).start()

                layout.alpha = 0f
                layout.animate().alpha(1f).setDuration(UnifyMotion.T3).setInterpolator(UnifyMotion.EASE_OUT).start()

                super.onShown(sb)
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                onCTAClick = emptyClickListener
                super.onDismissed(transientBottomBar, event)
            }
        })

        toasterCustomBottomHeight = 0

        frame_icon.rotate()

        return tempSnackBar
    }
}
