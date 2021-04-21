package com.tokopedia.sellerorder.common.presenter

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class SomBottomSheet(context: Context) : View(context) {

    companion object {
        private const val TAG_OVERLAY_VIEW = "tag_overlay_view"

        private const val OVERLAY_LAYOUT_ANIMATION_DURATION = 300L
    }

    private var overlayLayout: View? = null
    private var bottomSheetLayout: View? = null
    private var bottomSheetBehavior: BottomSheetBehavior<out View>? = null
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    private var overlayFadeOutAnimation: ValueAnimator? = null
    private var overlayFadeInAnimation: ValueAnimator? = null

    protected var dismissOnClickOverlay: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    private fun showOverlay(view: ViewGroup) {
        val overlayLayout = getOverlayLayout(view)
        this.overlayLayout = overlayLayout
        addOverlayLayoutToParent(view, overlayLayout)
    }

    private fun getOverlayLayout(view: ViewGroup): View {
        return overlayLayout ?: view.findViewWithTag(TAG_OVERLAY_VIEW) ?: View(context).apply {
            tag = TAG_OVERLAY_VIEW
            setOnOverlayClickListener(this)
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun addOverlayLayoutToParent(view: ViewGroup, overlayLayout: View) {
        if (view.indexOfChild(overlayLayout) == -1) {
            view.addView(overlayLayout)
        }
    }

    private fun setOnOverlayClickListener(overlayLayout: View) {
        overlayLayout.setOnClickListener {
            if (dismissOnClickOverlay) {
                dismiss()
            }
        }
    }

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        return bottomSheetCallback ?: object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // noop
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> overlayLayout?.animateFadeOut()
                    else -> overlayLayout?.animateFadeIn()
                }
            }
        }
    }

    private fun View?.animateFade(start: Float, end: Float): ValueAnimator {
        return ValueAnimator.ofFloat(start, end).apply {
            duration = OVERLAY_LAYOUT_ANIMATION_DURATION
            addUpdateListener { value ->
                this@animateFade?.alpha = value.animatedValue as Float
            }
            start()
        }
    }

    private fun View?.animateFadeOut() {
        this?.run {
            if (overlayFadeOutAnimation?.isRunning == true) return
            overlayFadeInAnimation?.cancel()
            overlayFadeOutAnimation = animateFade(alpha, 0f).apply {
                addListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        // noop
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        this@run?.gone()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        // noop
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        // noop
                    }
                })
            }
        }
    }

    private fun View?.animateFadeIn() {
        this?.run {
            if (overlayFadeInAnimation?.isRunning == true) return
            overlayFadeOutAnimation?.cancel()
            show()
            overlayFadeInAnimation = animateFade(alpha, 1f)
        }
    }

    protected fun init(view: ViewGroup, childView: View, showOverlay: Boolean) {
        childView.tag = this::class.java.simpleName
        if (showOverlay) {
            showOverlay(view)
        }
        val bottomSheetLayout: View = bottomSheetLayout
                ?: view.findViewById<ViewGroup>(com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper)?.parent as? View
                ?: inflate(context, com.tokopedia.unifycomponents.R.layout.bottom_sheet_layout, view)
        val bottomSheetWrapper: ViewGroup? = bottomSheetLayout.findViewById<ViewGroup>(com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper)?.apply {
            isClickable = true
            getChildAt(childCount - 1)?.let {
                if (it.tag != null && it.tag != this::class.java.simpleName) {
                    removeView(it)
                }
                addView(childView)
            }
        }
        val bottomSheetBehavior = bottomSheetBehavior
                ?: BottomSheetBehavior.from(requireNotNull(bottomSheetWrapper)).apply {
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
        this.bottomSheetLayout = bottomSheetLayout
        this.bottomSheetBehavior = bottomSheetBehavior
    }

    fun setTitle(title: String) {
        BottomSheetUnify.bottomSheetBehaviorTitle(requireNotNull(bottomSheetLayout), title)
    }

    fun showCloseButton() {
        bottomSheetLayout?.findViewById<ImageView>(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)?.apply {
            show()
            setOnClickListener { dismiss() }
        }
    }

    fun hideKnob() {
        bottomSheetLayout?.findViewById<View>(com.tokopedia.unifycomponents.R.id.bottom_sheet_knob)?.gone()
    }

    fun show() {
        val bottomSheetCallback = getBottomSheetCallback()
        this.bottomSheetCallback = bottomSheetCallback
        bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun dismiss() {
        requireNotNull(bottomSheetBehavior).apply {
            if (state != BottomSheetBehavior.STATE_HIDDEN) state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}