package com.tokopedia.play_common.util.extension

import android.app.Dialog
import android.content.ContentResolver
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.data.model.GraphqlError
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by jegul on 03/08/20
 */
inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}

//TODO("Check this as sometimes this causes memory leak, maybe check both old and new vto?")
suspend inline fun View.awaitMeasured() = suspendCancellableCoroutine<Unit> { cont ->
    if (measuredWidth > 0 && measuredHeight > 0) cont.resume(Unit)
    else {
        val vto = viewTreeObserver
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    when {
                        vto.isAlive -> vto.removeOnGlobalLayoutListener(this)
                        else -> viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    if (cont.isActive) cont.resume(Unit)
                }
            }
        }
        cont.invokeOnCancellation {
            when {
                vto.isAlive -> vto.removeOnGlobalLayoutListener(listener)
                else -> viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }
        vto.addOnGlobalLayoutListener(listener)
    }
}

suspend inline fun View.awaitNextGlobalLayout() = suspendCancellableCoroutine<Unit> { cont ->
    val vto = viewTreeObserver
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            when {
                vto.isAlive -> vto.removeOnGlobalLayoutListener(this)
                else -> viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
            if (cont.isActive) cont.resume(Unit)
        }
    }
    cont.invokeOnCancellation {
        when {
            vto.isAlive -> vto.removeOnGlobalLayoutListener(listener)
            else -> viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    vto.addOnGlobalLayoutListener(listener)
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

suspend inline fun View.awaitLayout() = suspendCancellableCoroutine<Unit> { cont ->
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        cont.resume(Unit)
    } else {
        val listener = object : View.OnLayoutChangeListener {
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
                if (cont.isActive) cont.resume(Unit)
            }
        }

        cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
        addOnLayoutChangeListener(listener)
    }
}

val View.globalVisibleRect: Rect
    get() {
        val rect = Rect()
        getGlobalVisibleRect(rect)
        return rect
    }

var View.compatTransitionName: String?
    get() {
        return ViewCompat.getTransitionName(this)
    }
    set(value) {
        ViewCompat.setTransitionName(this, value)
    }

val <T> T.exhaustive: T
    get() = this

fun EditText.setTextFieldColor(@ColorRes color: Int) {
    val drawable: Drawable = background
    drawable.setColorFilter(
            MethodChecker.getColor(
                    context,
                    color
            ), PorterDuff.Mode.SRC_ATOP)

    background = drawable
}

fun Fragment.cleanBackstack() {
    val backStackCount = childFragmentManager.backStackEntryCount
    for (i in 0 until backStackCount) {
        childFragmentManager.popBackStackImmediate()
    }
}

fun Uri.isLocal(): Boolean {
    return scheme in arrayOf(ContentResolver.SCHEME_CONTENT, ContentResolver.SCHEME_FILE)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.setNavigationBarColors(colorResArray: IntArray) {
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
fun Dialog.setNavigationBarColor(@ColorRes colorRes: Int) {
    setNavigationBarColors(intArrayOf(colorRes))
}

@RequiresApi(Build.VERSION_CODES.O)
fun Dialog.setDarkNavigationIcon() {
    val decorView = window?.decorView
    if (decorView != null) {
        decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.updateNavigationBarColors(colorResArray: IntArray, useDarkIcon: Boolean = true) {
    setNavigationBarColors(colorResArray)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && useDarkIcon) setDarkNavigationIcon()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Dialog.updateNavigationBarColor(@ColorRes colorRes: Int, useDarkIcon: Boolean = true) {
    updateNavigationBarColors(intArrayOf(colorRes), useDarkIcon)
}

fun Fragment.recreateView() {
    fragmentManager?.beginTransaction()
            ?.setReorderingAllowed(false)
            ?.detach(this)
            ?.attach(this)
            ?.commit()
}

inline fun FragmentManager.commit(
        allowStateLoss: Boolean = false,
        body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.body()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

val List<GraphqlError>.defaultErrorMessage: String
    get() = mapNotNull { it.message }.joinToString(separator = ", ")
