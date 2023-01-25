package com.tokopedia.play_common.util.extension

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.data.model.GraphqlError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KProperty1

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

suspend inline fun View.awaitPreDraw() = suspendCancellableCoroutine<Unit> { cont ->
    val vto = viewTreeObserver
    val listener = object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            cont.resume(Unit)
            when {
                vto.isAlive -> vto.removeOnPreDrawListener(this)
                else -> viewTreeObserver.removeOnPreDrawListener(this)
            }
            return true
        }
    }
    cont.invokeOnCancellation {
        when {
            vto.isAlive -> vto.removeOnPreDrawListener(listener)
            else -> viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    vto.addOnPreDrawListener(listener)
}

val View.globalVisibleRect: Rect
    get() {
        val rect = Rect()
        getGlobalVisibleRect(rect)
        return rect
    }

val View.visibleHeight: Int
    get() = if (visibility == View.GONE) 0 else height

val View.marginLp: ViewGroup.MarginLayoutParams
    get() = layoutParams as ViewGroup.MarginLayoutParams

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
    parentFragmentManager.commit {
        detach(this@recreateView)
    }

    parentFragmentManager.commit {
        attach(this@recreateView)
    }
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


fun dismissToaster() {
}

fun SpannableStringBuilder.append(
    text: CharSequence,
    flags: Int,
    vararg spans: Any,
): SpannableStringBuilder {
    val start = length
    append(text)

    spans.forEach { span ->
        setSpan(span, start, length, flags)
    }

    return this
}

@Deprecated(
    message = "please use hideKeyboard() from content_common module",
    replaceWith = ReplaceWith(
        expression = "hideKeyboard()",
        imports = ["com.tokopedia.content.common.util.hideKeyboard"]
    )
)
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

@Deprecated(
    message = "please use hideKeyboard() from content_common module",
    replaceWith = ReplaceWith(
        expression = "hideKeyboard()",
        imports = ["com.tokopedia.content.common.util.hideKeyboard"]
    )
)
fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

@Deprecated(
    message = "please use showKeyboard() from content_common module",
    replaceWith = ReplaceWith(
        expression = "showKeyboard()",
        imports = ["com.tokopedia.content.common.util.showKeyboard"]
    )
)
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

@Deprecated(
    message = "please use showKeyboard(isShow: Boolean) from content_common module",
    replaceWith = ReplaceWith(
        expression = "showKeyboard(isShow)",
        imports = ["com.tokopedia.content.common.util.showKeyboard"]
    )
)
fun EditText.showKeyboard(isShow: Boolean) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (isShow) imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    else imm.hideSoftInputFromWindow(this.windowToken, 0)
}

data class CachedState<T>(val prevValue: T? = null, val value: T) {

    fun <V> isValueChanged(prop: KProperty1<T, V>): Boolean {
        val prevState = this.prevValue
        val currState = this.value

        return when {
            currState == null -> false
            prevState == null -> true
            else -> {
                val prevValue = prop.get(prevState)
                val currentValue = prop.get(currState)
                prevValue != currentValue
            }
        }
    }
}

fun <T: Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue : T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

@Deprecated("Use MutableStateFlow.update")
fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}

fun <T: Any> MutableStateFlow<T?>.setValueIfNotNull(fn: T.() -> T) {
    val value = this.value ?: return
    this.value = value.fn()
}

fun Boolean.switch() : Boolean = !this

inline fun buildSpannedString(builderAction: SpannableStringBuilder.() -> Unit): SpannedString {
    val builder = SpannableStringBuilder()
    builder.builderAction()
    return SpannedString(builder)
}

inline fun SpannableStringBuilder.inSpans(
    span: Any,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    setSpan(span, start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}

inline fun SpannableStringBuilder.bold(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(StyleSpan(Typeface.BOLD), builderAction = builderAction)

suspend fun getBitmapFromUrl(
    context: Context,
    url: String,
): Bitmap = suspendCancellableCoroutine { cont ->
    val target = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            if (cont.isActive) cont.resume(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            if (!cont.isActive || cont.isCompleted) return
            cont.resumeWithException(
                IllegalStateException("Failed to load image from url: $url")
            )
        }
    }

    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(target)

    cont.invokeOnCancellation {
        Glide.with(context).clear(target)
    }
}

inline fun View.updateLayoutParams(block: ViewGroup.LayoutParams.() -> Unit) {
    updateLayoutParams<ViewGroup.LayoutParams>(block)
}

@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(
    block: T.() -> Unit
) {
    val params = layoutParams as T
    block(params)
    layoutParams = params
}
