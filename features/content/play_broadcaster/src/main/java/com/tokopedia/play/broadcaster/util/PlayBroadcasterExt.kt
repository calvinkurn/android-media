package com.tokopedia.play.broadcaster.util

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.play.broadcaster.R

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