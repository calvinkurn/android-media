package com.tokopedia.sellerorder.common.presenter

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.listener.SingleTapListener
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx

abstract class SomBottomSheet <T: ViewBinding> (
    childViewsLayoutResourceId: Int,
    private val showOverlay: Boolean,
    private val showCloseButton: Boolean,
    private val showKnob: Boolean,
    private val clearPadding: Boolean,
    private val draggable: Boolean,
    private val bottomSheetTitle: String,
    protected val context: Context,
    protected var dismissOnClickOverlay: Boolean
) {

    companion object {
        private const val TAG_OVERLAY_VIEW = "tag_overlay_view"
        private const val OVERLAY_LAYOUT_ANIMATION_DURATION = 300L
        const val BOTTOM_SHEET_GAP_DEFAULT = 16
    }

    private var overlayLayout: View? = null
    private var overlayFadeOutAnimation: ValueAnimator? = null
    private var overlayFadeInAnimation: ValueAnimator? = null
    private var bottomSheetBehavior: BottomSheetBehavior<out View>? = null
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var onDismissed: () -> Unit = {}
    private var oneTimeOnDismissed: (() -> Unit)? = {}

    protected var binding: T? = null
    protected var bottomSheetLayout: View? = null
    protected var dismissing: Boolean = false

    protected val hideKeyboardHandler = createHideKeyboardHandler()

    private fun createHideKeyboardHandler(): SingleTapListener {
        return SingleTapListener(context) {
            binding?.root?.hideKeyboard()
            false
        }
    }

    init {
        inflateChildView(childViewsLayoutResourceId)
        binding?.root?.run { hideKeyboardHandler.attachListener(this) }
    }

    abstract fun bind(view: View): T
    abstract fun setupChildView()

    private fun inflateChildView(layoutId: Int) {
        if (binding == null) {
            View.inflate(context, layoutId, null).run {
                binding = bind(this)
            }
        }
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
            overlayView.setBackgroundColor(ContextCompat.getColor(context, R.color._dms_bottomsheet_overlay_color))
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
            overlayFadeOutAnimation = animateFade(alpha, Float.ZERO).apply {
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        // noop
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        this@run?.gone()
                        onDismissed()
                        oneTimeOnDismissed?.invoke()
                        oneTimeOnDismissed = null
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
                if (newState == BottomSheetBehavior.STATE_EXPANDED) onBottomSheetExpanded()
                else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    onBottomSheetHidden()
                    return
                }
                dismissing = false
                overlayLayout?.animateFadeIn()
            }
        }
    }

    private fun setupBottomSheetContent(view: ViewGroup) {
        val bottomSheetLayout = this.bottomSheetLayout ?: (View.inflate(context, com.tokopedia.unifycomponents.R.layout.bottom_sheet_layout, view) as ViewGroup).getChildAt(view.childCount - 1)?.apply {
            this as ViewGroup
            isClickable = true
            binding?.run {
                if (root.parent != null) {
                    (root.parent as? ViewGroup)?.removeView(root)
                }
                addView(root)
            }
            val bottomSheetBehavior = bottomSheetBehavior ?: BottomSheetBehavior.from(this).apply {
                isDraggable = draggable
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            val bottomSheetCallback = bottomSheetCallback ?: createBottomSheetCallback()
            bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
            this@SomBottomSheet.bottomSheetCallback = bottomSheetCallback
            this@SomBottomSheet.bottomSheetBehavior = bottomSheetBehavior
        }
        this.bottomSheetLayout = bottomSheetLayout
    }

    protected open fun onBottomSheetExpanded() {}

    protected open fun onBottomSheetHidden() {
        overlayLayout?.animateFadeOut()
    }

    open fun show() {
        binding?.root?.post { bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED }
    }

    open fun dismiss(): Boolean {
        requireNotNull(bottomSheetBehavior).apply {
            return if (state != BottomSheetBehavior.STATE_HIDDEN) {
                dismissing = true
                binding?.root?.hideKeyboard()
                state = BottomSheetBehavior.STATE_HIDDEN
                true
            } else false
        }
    }

    fun setTitle(title: String) {
        bottomSheetLayout?.let {
            BottomSheetUnify.bottomSheetBehaviorTitle(it, title)
        }
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
        setupChildView()
        if (showCloseButton) {
            showCloseButton()
        }
        if (!showKnob) {
            hideKnob()
        }
        if (clearPadding) {
            clearSidePadding()
        }
    }

    private fun clearSidePadding() {
        bottomSheetLayout?.findViewById<View>(com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper)
            ?.setPadding(Int.ZERO, BOTTOM_SHEET_GAP_DEFAULT.toPx(), Int.ZERO, Int.ZERO)
        (bottomSheetLayout?.findViewById<View>(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)?.layoutParams as? LinearLayout.LayoutParams)?.setMargins(
            BOTTOM_SHEET_GAP_DEFAULT.toPx(),
            Int.ZERO,
            BOTTOM_SHEET_GAP_DEFAULT.toPx(),
            BOTTOM_SHEET_GAP_DEFAULT.toPx()
        )
    }

    fun setOnDismiss(onDismissed: () -> Unit) {
        this.onDismissed = onDismissed
    }

    fun setOneTimeOnDismiss(onDismissed: () -> Unit) {
        this.oneTimeOnDismissed = onDismissed
    }

    fun clearViewBinding() {
        binding = null
    }
}