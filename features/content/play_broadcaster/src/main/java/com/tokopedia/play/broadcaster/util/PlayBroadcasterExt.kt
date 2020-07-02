package com.tokopedia.play.broadcaster.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by jegul on 26/05/20
 */
internal inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}

inline fun View.doOnPreDraw(crossinline action: (view: View) -> Unit) {
    val vto = viewTreeObserver
    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            action(this@doOnPreDraw)
            when {
                vto.isAlive -> vto.removeOnPreDrawListener(this)
                else -> viewTreeObserver.removeOnPreDrawListener(this)
            }
            return true
        }
    })
}

inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
        ) {
            view.removeOnLayoutChangeListener(this)
            action(view)
        }
    })
}

inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        action(this)
    } else {
        doOnNextLayout {
            action(it)
        }
    }
}

internal var View.compatTransitionName: String?
    get() {
        return ViewCompat.getTransitionName(this)
    }
    set(value) {
        ViewCompat.setTransitionName(this, value)
    }

internal fun GlobalError.productNotFoundState() {
    errorIllustration.setImageResource(com.tokopedia.resources.common.R.drawable.ic_empty_search_wishlist)
    errorTitle.text = context.getString(R.string.play_product_not_found_title)
    errorDescription.text = context.getString(R.string.play_product_not_found_desc)
    errorAction.gone()
}

internal fun GlobalError.productEtalaseEmpty() {
    errorIllustration.setImageResource(R.drawable.ic_empty_product_etalase)
    errorTitle.text = context.getString(R.string.play_product_etalase_empty_title)
    errorDescription.text = context.getString(R.string.play_product_etalase_empty_desc)
    errorAction.gone()
}

internal fun GlobalError.channelNotFound(onAction: () -> Unit) {
    this.errorTitle.setTextColor(ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
    this.errorDescription.setTextColor(ContextCompat.getColor(this.context, R.color.play_white_68))
    this.setType(GlobalError.PAGE_NOT_FOUND)
    this.setActionClickListener { onAction() }
}

internal fun Context.getDialog(
        title: String,
        desc: String,
        @DialogUnify.ActionType actionType: Int = DialogUnify.SINGLE_ACTION,
        @DialogUnify.ImageType imageType: Int = DialogUnify.NO_IMAGE,
        primaryCta: String,
        primaryListener: (DialogUnify) -> Unit = {},
        secondaryCta: String = "",
        secondaryListener: (DialogUnify) -> Unit = {},
        cancelable: Boolean = false,
        overlayClose: Boolean = false
): DialogUnify = DialogUnify(this, actionType, imageType).apply {
    setTitle(title)
    setDescription(desc)
    setPrimaryCTAText(primaryCta)
    setSecondaryCTAText(secondaryCta)
    setPrimaryCTAClickListener { primaryListener(this) }
    setSecondaryCTAClickListener { secondaryListener(this) }
    setCancelable(cancelable)
    setOverlayClose(overlayClose)
}

internal val <T> T.exhaustive: T
    get() = this

internal fun View.showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        duration: Int = Toaster.LENGTH_LONG,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
) {
    if (actionLabel.isNotEmpty()) Toaster.toasterCustomCtaWidth = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
    Toaster.make(this,
            text = message,
            duration = duration,
            type = type,
            actionText = actionLabel,
            clickListener = actionListener)
}

internal fun EditText.setTextFieldColor(@ColorRes color: Int) {
    val drawable: Drawable = background
    drawable.setColorFilter(
            MethodChecker.getColor(
                    context,
                    color
            ), PorterDuff.Mode.SRC_ATOP)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        setBackgroundDrawable(drawable)
    }
}

internal fun ImageView.loadImageFromUrl(url: String, requestListener: RequestListener<Drawable>) {
    Glide.with(context)
            .load(url)
            .placeholder(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            .dontAnimate()
            .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            .addListener(requestListener)
            .into(this)
}

/**
 * Updates this view's padding. This version of the method allows using named parameters
 * to just set one or more axes.
 *
 * @see View.setPadding
 */
internal fun View.updatePadding(
        @Px left: Int = paddingLeft,
        @Px top: Int = paddingTop,
        @Px right: Int = paddingRight,
        @Px bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}


/**
 * Updates the margins in the [ViewGroup]'s [ViewGroup.MarginLayoutParams].
 * This version of the method allows using named parameters to just set one or more axes.
 *
 * @see ViewGroup.MarginLayoutParams.setMargins
 */
internal fun ViewGroup.MarginLayoutParams.updateMargins(
        @Px left: Int = leftMargin,
        @Px top: Int = topMargin,
        @Px right: Int = rightMargin,
        @Px bottom: Int = bottomMargin
) {
    setMargins(left, top, right, bottom)
}

internal fun View.requestApplyInsetsWhenAttached() {
    val isAttached = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) isAttachedToWindow else false
    if (isAttached) {
        // We're already attached, just request as normal
        invalidateInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.invalidateInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

internal fun View.invalidateInsets() {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) requestApplyInsets()
        else requestFitSystemWindows()
    } catch (e: Exception) {}
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding, InitialMargin) -> Unit) {
    val initialPadding = recordInitialPadding()
    val initialMargin = recordInitialMargin()

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding, initialMargin)
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

data class InitialMargin(val left: Int, val top: Int,
                         val right: Int, val bottom: Int)

internal fun View.recordInitialPadding() = InitialPadding(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) paddingStart else paddingLeft,
        paddingTop,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) paddingEnd else paddingRight,
        paddingBottom)

internal fun View.recordInitialMargin(): InitialMargin {
    val margin = layoutParams as ViewGroup.MarginLayoutParams
    return InitialMargin(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) margin.marginStart else margin.leftMargin,
            margin.topMargin,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) margin.marginEnd else margin.rightMargin,
            margin.bottomMargin
    )
}
