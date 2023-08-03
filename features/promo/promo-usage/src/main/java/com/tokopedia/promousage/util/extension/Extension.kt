package com.tokopedia.promousage.util.extension

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx

private const val SUBSTRING_INDEX_NOT_FOUND = -1
private const val PADDING_BOTTOM_IN_DP = 16

fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = colorFilter
}

fun ImageView.removeGrayscale() {
    this.colorFilter = null
}

fun TextView.setHyperlinkText(
    fullText: String,
    hyperlinkSubstring: String,
    ignoreCase: Boolean = true,
    onHyperlinkClick: () -> Unit = {}
) {
    val spannableString = SpannableString(fullText)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onHyperlinkClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            ds.isUnderlineText = false
        }
    }

    val boldSpan = StyleSpan(Typeface.BOLD)
    val substringIndexes = findFirstAndLastIndexOfSubstring(fullText, hyperlinkSubstring, ignoreCase)
    val (substringFirstIndex, substringLastIndex) = substringIndexes

    try {
        spannableString.setSpan(
            boldSpan,
            substringFirstIndex,
            substringLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            substringFirstIndex,
            substringLastIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        this.text = spannableString
        this.movementMethod = LinkMovementMethod.getInstance()
    } catch (e: Exception) {}

}

private fun findFirstAndLastIndexOfSubstring(text: String, textSubstring: String, ignoreCase: Boolean): Pair<Int, Int> {
    val start = text.indexOf(textSubstring, ignoreCase = ignoreCase)
    return if (start != SUBSTRING_INDEX_NOT_FOUND) {
        Pair(start, start + textSubstring.length)
    } else {
        Pair(SUBSTRING_INDEX_NOT_FOUND, SUBSTRING_INDEX_NOT_FOUND)
    }
}

fun RecyclerView.applyPaddingToLastItem(paddingBottomInDp: Int = PADDING_BOTTOM_IN_DP) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildLayoutPosition(view)
            val totalItemCount = state.itemCount
            if (totalItemCount > Int.ZERO && position == totalItemCount - Int.ONE) {
                outRect.bottom = paddingBottomInDp.toPx()
            }
        }
    })
}
fun View.foregroundDrawable(drawableResourceId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.foreground = ContextCompat.getDrawable(this.context, drawableResourceId)
    }
}
