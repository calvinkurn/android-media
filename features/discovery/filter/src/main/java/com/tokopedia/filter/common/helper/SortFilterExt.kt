package com.tokopedia.filter.common.helper

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify

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
    gradientDrawable.setStroke(2, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
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

internal fun BottomSheetUnify.configureBottomSheetHeight() {
    val screenHeight = getScreenHeight()
    val maxHeight = (screenHeight * 0.9f).toInt()

    bottomSheetWrapper.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight)
}

internal fun View.setMargin(marginLeft: Int = -1, marginTop: Int = -1, marginRight: Int = -1, marginBottom: Int = -1) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    val actualMarginLeft = if (marginLeft == -1) layoutParams.leftMargin else marginLeft
    val actualMarginTop = if (marginTop == -1) layoutParams.topMargin else marginTop
    val actualMarginRight = if (marginRight == -1) layoutParams.rightMargin else marginRight
    val actualMarginBottom = if (marginBottom == -1) layoutParams.bottomMargin else marginBottom

    layoutParams.setMargins(actualMarginLeft, actualMarginTop, actualMarginRight, actualMarginBottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = actualMarginLeft
        layoutParams.marginEnd = actualMarginRight
    }
}

internal fun createFilterDividerItemDecoration(context: Context, orientation: Int, leftMargin: Int): RecyclerView.ItemDecoration {
    val itemDecoration = DividerItemDecoration(context, orientation)

    val itemDecorationDrawable = itemDecoration.drawable
    val insetDrawable = InsetDrawable(itemDecorationDrawable, leftMargin, 0, 0, 0)
    itemDecoration.setDrawable(insetDrawable)

    return itemDecoration
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