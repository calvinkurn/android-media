package com.tokopedia.play.broadcaster.util

import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
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
        actionListener: View.OnClickListener = View.OnClickListener { },
        bottomMargin: Int? = null
) {
    if (actionLabel.isNotEmpty()) Toaster.toasterCustomCtaWidth = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
    if (bottomMargin != null) Toaster.toasterCustomBottomHeight = bottomMargin
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

internal fun Fragment.cleanBackstack() {
    val backStackCount = childFragmentManager.backStackEntryCount
    for (i in 0 until backStackCount) {
        childFragmentManager.popBackStackImmediate()
    }
}

internal fun Uri.isLocal(): Boolean {
    return scheme in arrayOf(ContentResolver.SCHEME_CONTENT, ContentResolver.SCHEME_FILE)
}

@RequiresApi(Build.VERSION_CODES.M)
internal fun Dialog.setNavigationBarColors(colorResArray: IntArray) {
    val theWindow: Window? = window
    if (theWindow != null) {
        val metrics = android.util.DisplayMetrics()
        theWindow.windowManager.defaultDisplay.getMetrics(metrics)
        val dimDrawable = GradientDrawable()

        val drawableList = colorResArray.map {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(MethodChecker.getColor(context, it))
            return@map drawable
        }

        val layers = arrayOf<Drawable>(dimDrawable) + drawableList
        val windowBackground = LayerDrawable(layers)
        windowBackground.setLayerInsetTop(1, metrics.heightPixels)
        theWindow.setBackgroundDrawable(windowBackground)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
internal fun Dialog.setNavigationBarColor(@ColorRes colorRes: Int) {
    setNavigationBarColors(intArrayOf(colorRes))
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun Dialog.setDarkNavigationIcon() {
    val decorView = window?.decorView
    if (decorView != null) {
        decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

@RequiresApi(Build.VERSION_CODES.M)
internal fun Dialog.updateNavigationBarColors(colorResArray: IntArray, useDarkIcon: Boolean = true) {
    setNavigationBarColors(colorResArray)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && useDarkIcon) setDarkNavigationIcon()
}

@RequiresApi(Build.VERSION_CODES.M)
internal fun Dialog.updateNavigationBarColor(@ColorRes colorRes: Int, useDarkIcon: Boolean = true) {
    updateNavigationBarColors(intArrayOf(colorRes), useDarkIcon)
}