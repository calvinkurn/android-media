package com.tokopedia.sellerorder.common.presenter

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify

abstract class SomBottomSheet(
        childViewsLayoutResourceId: Int,
        private val showOverlay: Boolean,
        private val showCloseButton: Boolean,
        private val showKnob: Boolean,
        private val bottomSheetTitle: String,
        protected val context: Context,
        protected var dismissOnClickOverlay: Boolean
) {

    companion object {
        private const val TAG_OVERLAY_VIEW = "tag_overlay_view"
        private const val OVERLAY_LAYOUT_ANIMATION_DURATION = 300L
    }

    private var overlayLayout: View? = null
    private var overlayFadeOutAnimation: ValueAnimator? = null
    private var overlayFadeInAnimation: ValueAnimator? = null
    private var bottomSheetBehavior: BottomSheetBehavior<out View>? = null
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    protected var bottomSheetLayout: View? = null
    protected var childViews: View? = null

    abstract fun setupChildView()

    init {
        val childView = View.inflate(context, childViewsLayoutResourceId, null)
        this.childViews = childView
    }

    private fun showOverlay(view: ViewGroup) {
        val overlayLayout = getOverlayLayout(view)
        this.overlayLayout = overlayLayout
        addOverlayLayoutToParent(view, overlayLayout)
    }

    private fun getOverlayLayout(view: ViewGroup): View {
        return overlayLayout ?: View(context).apply {
            tag = TAG_OVERLAY_VIEW
            setOnOverlayClickListener(this)
            setupOverlayBackgroundColor(view, this)
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun setupOverlayBackgroundColor(fragmentView: ViewGroup, overlayView: View) {
        if (!hasVisibleTransparentOverlay(fragmentView)) {
            overlayView.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        } else {
            overlayView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    private fun hasVisibleTransparentOverlay(fragmentView: ViewGroup): Boolean {
        for (i in 0 until fragmentView.childCount) {
            val view = fragmentView.getChildAt(i)
            if (view?.tag == TAG_OVERLAY_VIEW && view.visibility == View.VISIBLE) {
                return true
            }
        }
        return false
    }

    private fun addOverlayLayoutToParent(view: ViewGroup, overlayLayout: View) {
        if (overlayLayout.parent == null) {
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
                addListener(object : Animator.AnimatorListener {
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

    private fun createBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        return object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // noop
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> onBottomSheetHidden()
                    else -> overlayLayout?.animateFadeIn()
                }
            }
        }
    }

    private fun setupBottomSheetContent(view: ViewGroup) {
        val bottomSheetLayout = this.bottomSheetLayout ?: (View.inflate(context, com.tokopedia.unifycomponents.R.layout.bottom_sheet_layout, view) as ViewGroup).getChildAt(view.childCount - 1)?.apply {
            this as ViewGroup
            isClickable = true
            if (childViews?.parent != null) {
                (childViews?.parent as? ViewGroup)?.removeView(childViews)
            }
            addView(childViews)
            val bottomSheetBehavior = bottomSheetBehavior ?: BottomSheetBehavior.from(requireNotNull(this)).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            val bottomSheetCallback = bottomSheetCallback ?: createBottomSheetCallback()
            bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
            this@SomBottomSheet.bottomSheetCallback = bottomSheetCallback
            this@SomBottomSheet.bottomSheetBehavior = bottomSheetBehavior
        }
        this.bottomSheetLayout = bottomSheetLayout
    }

    protected open fun onBottomSheetHidden() {
        overlayLayout?.animateFadeOut()
    }

    open fun show() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun dismiss() {
        requireNotNull(bottomSheetBehavior).apply {
            if (state != BottomSheetBehavior.STATE_HIDDEN) state = BottomSheetBehavior.STATE_HIDDEN
        }
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

    fun isShowing(): Boolean {
        return bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN
    }

    fun init(view: ViewGroup) {
        if (showOverlay) {
            showOverlay(view)
        }
        setupBottomSheetContent(view)
        setTitle(bottomSheetTitle)
        if (showCloseButton) {
            showCloseButton()
        }
        if (!showKnob) {
            hideKnob()
        }
        setupChildView()
    }
}