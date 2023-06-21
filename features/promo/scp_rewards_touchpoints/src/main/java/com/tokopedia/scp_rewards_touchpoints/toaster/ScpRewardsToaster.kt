package com.tokopedia.scp_rewards_touchpoints.view.toaster

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.findSuitableParent
import com.tokopedia.unifycomponents.utils.doOnPreDraw
import com.tokopedia.unifycomponents.utils.updateMargin
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.scp_rewards_touchpoints.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx


object ScpRewardsToaster {
    private val emptyClickListener = View.OnClickListener { }

    var onCTAClick: View.OnClickListener = emptyClickListener
    lateinit var ctaText: String
    private val WITHOUT_CTA = 0
    private val WITH_CTA = 1


    /**
     * Toaster normal style
     */
    const val TYPE_NORMAL = 0

    /**
     * Toaster error style
     */
    const val TYPE_ERROR = 1

    const val LENGTH_SHORT = Snackbar.LENGTH_SHORT
    const val LENGTH_LONG = Snackbar.LENGTH_LONG
    const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE

    var toasterLength = Snackbar.LENGTH_SHORT

    var toasterCustomBottomHeight: Int = 0
    var toasterCustomCtaWidth: Int = 0

    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, Toaster.TYPE_NORMAL).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    fun showNormal(view: View, title: String, description: String, duration: Int) {
        build(view.findSuitableParent() ?: return, title, description, duration, TYPE_NORMAL)
            .show()
    }

    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, Toaster.TYPE_NORMAL, actionText, clickListener).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    fun showNormalWithAction(view: View, title: String, description: String, duration: Int, actionText: String, clickListener: View.OnClickListener) {
        build(view.findSuitableParent() ?: return, title, description, duration, TYPE_NORMAL, actionText, clickListener)
            .show()
    }

    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, Toaster.TYPE_ERROR).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    fun showError(view: View, title: String, description: String, duration: Int) {
        build(view.findSuitableParent() ?: return, title, description, duration, TYPE_ERROR)
            .show()
    }

    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, Toaster.TYPE_ERROR, actionText, clickListener).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    fun showErrorWithAction(view: View, title: String, description: String, duration: Int, actionText: String, clickListener: View.OnClickListener) {
        build(view.findSuitableParent() ?: return, title, description, duration, TYPE_ERROR, actionText, clickListener)
            .show()
    }

    /**
     * Make a Toaster to display a message. Toaster will try and find a parent view to hold Toaster's view
     * from the value given to view.
     * @param view for finding suitable parent to hold Toaster's view.
     * @param text The text to show
     * @param duration How long to display the message. Either Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG or Snackbar.LENGTH_INDEFINITE
     * @param type What style to apply for Toaster. Either [TYPE_NORMAL] or [TYPE_ERROR]
     * @param actionText Text to display for the action
     * @param clickListener callback to be invoked when the action is clicked
     */
    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, type, actionText, clickListener).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    @JvmStatic
    fun make(view: View, title: String, description: String, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL, actionText: String = "", clickListener: View.OnClickListener = View.OnClickListener {}) {
        build(view.findSuitableParent() ?: return, title, description, duration, type, actionText, clickListener)
            .show()
    }

    @Deprecated(
        message = "Please use build() method instead",
        replaceWith = ReplaceWith("Toaster.build(view, text, duration, type).show()", "com.tokopedia.unifycomponents.Toaster")
    )
    @JvmStatic
    fun make(view: View,title: String, description: String, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL) {
        build(view.findSuitableParent() ?: return, title, description, duration, type)
            .show()
    }

    @JvmStatic
    fun build(view: View, title: String, description: String, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL, actionText: String = "", clickListener: View.OnClickListener = View.OnClickListener {}): Snackbar {
        toasterLength = duration
        val cta: Int = if (actionText.isNotEmpty()) WITH_CTA else WITHOUT_CTA
        if (cta == WITH_CTA) {
            onCTAClick = clickListener
            ctaText = actionText
        }
        return initToaster(view, title, description, type, cta)!!
    }

    @JvmStatic
    fun build(view: View, title: String, description: String, duration: Int = LENGTH_SHORT, type: Int = TYPE_NORMAL): Snackbar {
        toasterLength = duration
        val cta: Int = WITHOUT_CTA
        return initToaster(view, title, description, type, cta)!!
    }

    private fun initToaster(view: View, title: String, description: String, type: Int = TYPE_NORMAL, cta: Int = WITHOUT_CTA): Snackbar? {
        val viewTarget : View = view

        val tempSnackBar = Snackbar.make(viewTarget, "", toasterLength)

        val viewLayout = View.inflate(viewTarget.context, R.layout.scp_toaster, null)

        val frame_icon = viewLayout.findViewById<ImageUnify>(R.id.frame_icon)
        val tv_title = viewLayout.findViewById<TextView>(R.id.scp_toaster_title)
        val tv_desc = viewLayout.findViewById<TextView>(R.id.scp_toaster_description)
        tv_title.text = title
        tv_title.typeface = Typography.getFontType(viewTarget.context, false, Typography.DISPLAY_2)
        tv_desc.text = description
        tv_desc.typeface = Typography.getFontType(viewTarget.context, false, Typography.DISPLAY_3)

        val constraintLayoutToaster = viewLayout.findViewById<View>(R.id.constraintLayoutToaster)

        if (type == TYPE_NORMAL) {
            constraintLayoutToaster.setBackgroundResource(R.drawable.scp_rewards_toaster_bg_normal)
        } else {
            constraintLayoutToaster.setBackgroundResource(R.drawable.toaster_bg_error)
        }

        val actionTextButton = viewLayout.findViewById<Typography>(R.id.snackbar_btn)
        if(toasterCustomCtaWidth > 0) {
            actionTextButton.maxWidth = toasterCustomCtaWidth
            toasterCustomCtaWidth = 0
        }

        if (cta == WITH_CTA) {
            actionTextButton.text = ctaText
            actionTextButton.setOnClickListener{
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
        layout.isClickable = false
        layout.setBackgroundColor(Color.TRANSPARENT)
        layout.removeAllViews()
        layout.addView(viewLayout, 0)

        val params = layout.layoutParams
        if(params is FrameLayout.LayoutParams) {
            params.gravity = Gravity.BOTTOM
            params.width = FrameLayout.LayoutParams.MATCH_PARENT
        } else if(params is CoordinatorLayout.LayoutParams) {
            params.gravity = Gravity.BOTTOM
            params.width = CoordinatorLayout.LayoutParams.MATCH_PARENT
        }
        layout.layoutParams = params

        layout.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
        layout.isEnabled = false

        if (toasterCustomBottomHeight > 0) {
            layout.setPadding(16.toPx(),0,16.toPx(), toasterCustomBottomHeight +16.toPx())
        }

        viewLayout.visibility = View.INVISIBLE

        tempSnackBar.addCallback(object: Snackbar.Callback() {
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

        rotateSunflare(frame_icon)

        return tempSnackBar
    }

    private fun rotateSunflare(frame_icon: ImageUnify) {
        frame_icon.apply {
            ObjectAnimator.ofFloat(this, View.ROTATION, 0f, 360f).apply {
                duration = 5000
                interpolator = null
                repeatCount = ValueAnimator.INFINITE
                start()
            }
        }
    }
}

