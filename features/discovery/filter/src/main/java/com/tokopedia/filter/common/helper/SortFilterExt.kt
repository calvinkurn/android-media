package com.tokopedia.filter.common.helper

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Parcel
import android.os.Parcelable
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Option.Companion.UID_FIRST_SEPARATOR_SYMBOL
import com.tokopedia.filter.common.data.Option.Companion.UID_SECOND_SEPARATOR_SYMBOL
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration as TkpdAbstractionDividerItemDecoration

internal fun <T: Parcelable> T.copyParcelable(): T? {
    var parcel: Parcel? = null

    return try {
        parcel = Parcel.obtain()
        parcel.writeParcelable(this, 0)
        parcel.setDataPosition(0)
        parcel.readParcelable(this::class.java.classLoader)
    } catch(throwable: Throwable) {
        null
    } finally {
        parcel?.recycle()
    }
}

internal fun createColorSampleDrawable(context: Context, colorString: String): GradientDrawable {
    val gradientDrawable = GradientDrawable()

    gradientDrawable.shape = GradientDrawable.OVAL
    gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    gradientDrawable.setStroke(2, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN200))
    gradientDrawable.setColor(safeParseColor(colorString))

    return gradientDrawable
}

internal fun safeParseColor(color: String): Int {
    return try {
        Color.parseColor(color)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
        0
    }
}

@Suppress("MagicNumber")
internal fun BottomSheetUnify.configureBottomSheetHeight() {
    val screenHeight = getScreenHeight()
    val maxHeight = (screenHeight * 0.9f).toInt()

    bottomSheetWrapper.layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        maxHeight,
    )
}

internal fun View.setMargin(marginLeft: Int = -1, marginTop: Int = -1, marginRight: Int = -1, marginBottom: Int = -1) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    val actualMarginLeft = if (marginLeft == -1) layoutParams.leftMargin else marginLeft
    val actualMarginTop = if (marginTop == -1) layoutParams.topMargin else marginTop
    val actualMarginRight = if (marginRight == -1) layoutParams.rightMargin else marginRight
    val actualMarginBottom = if (marginBottom == -1) layoutParams.bottomMargin else marginBottom

    layoutParams.setMargins(actualMarginLeft, actualMarginTop, actualMarginRight, actualMarginBottom)

    layoutParams.marginStart = actualMarginLeft
    layoutParams.marginEnd = actualMarginRight
}

internal fun createFilterDividerItemDecoration(context: Context, orientation: Int, leftMargin: Int): RecyclerView.ItemDecoration {
    return DividerItemDecoration(context, orientation).apply {
        val insetDrawable = createFilterItemDividerDrawable(drawable, leftMargin)
        setDrawable(insetDrawable)
    }
}

private fun createFilterItemDividerDrawable(itemDecorationDrawable: Drawable?, leftMargin: Int) : Drawable {
    return InsetDrawable(itemDecorationDrawable, leftMargin, 0, 0, 0)
}

internal fun createFilterChildDividerItemDecoration(
    context: Context,
    orientation: Int,
    leftMargin: Int
): RecyclerView.ItemDecoration {
    val insetDrawable = createFilterItemDividerDrawable(
        context.getDrawable(com.tokopedia.abstraction.R.drawable.bg_line_separator_thin),
        leftMargin
    )
    return TkpdAbstractionDividerItemDecoration(orientation, insetDrawable).apply {
        setUsePaddingLeft(false)
    }
}

internal fun RecyclerView.addItemDecorationIfNotExists(itemDecoration: RecyclerView.ItemDecoration) {
    val hasNoItemDecoration = itemDecorationCount == 0
    if (hasNoItemDecoration) addItemDecoration(itemDecoration)
}

internal fun View.expandTouchArea(left: Int, top: Int, right: Int, bottom: Int) {
    val parent = parent

    if (parent is View) {
        val hitRect = Rect()
        getHitRect(hitRect)

        hitRect.left -= left
        hitRect.top -= top
        hitRect.right += right
        hitRect.bottom += bottom

        parent.touchDelegate = TouchDelegate(hitRect, this)
    }
}

internal fun BottomSheetUnify.setBottomSheetActionBold() {
    bottomSheetAction.typeface = Typeface.DEFAULT_BOLD
}

internal fun <T> Collection<T>.equalsIgnoreOrder(elements: Collection<T>) =
    this.size == elements.size && this.toSet() == elements.toSet()

val IOption.isTypeRadio: Boolean
    get() = Option.INPUT_TYPE_RADIO == inputType

val IOption.uniqueId: String
    get() = key + UID_FIRST_SEPARATOR_SYMBOL + value + UID_SECOND_SEPARATOR_SYMBOL + name
