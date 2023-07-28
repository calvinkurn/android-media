package com.tokopedia.feedplus.presentation.uiview

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.tokopedia.feedcomponent.util.buildSpannedString
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.customview.FeedCaptionFadingLayout
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 06/03/23
 */
@SuppressLint("ClickableViewAccessibility")
class FeedCaptionView(
    private val textView: TextView,
    private val fadingLayout: FeedCaptionFadingLayout,
    private val listener: FeedListener,
    private val captionListener: Listener
) {

    var fullText = ""
    var isCollapsed = true

    private var isScrolling = false

    private val gestureDetector = GestureDetector(
        textView.context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                textView.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                isScrolling = true
                textView.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }
        }
    )

    init {
        textView.setOnTouchListener { v, event ->
            val actionMasked = event.actionMasked
            if (actionMasked == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
                if (!v.isClickable || isScrolling) {
                    isScrolling = false
                    return@setOnTouchListener true
                }
            }

            gestureDetector.onTouchEvent(event)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setOnScrollChangeListener { view, _, _, _, _ ->
                if (isCollapsed) return@setOnScrollChangeListener
                fadingLayout.setBottomFadingEdgeBounds(
                    if (textView.canScrollVertically(1)) FADING_EDGE_HEIGHT else 0
                )
                fadingLayout.setTopFadingEdgeBounds(
                    if (textView.canScrollVertically(-1)) FADING_EDGE_HEIGHT else 0
                )
            }
        }
    }

    fun bind(text: String, trackerDataModel: FeedTrackerDataModel?) {
        fullText = text

        textView.setOnClickListener {
            listener.onCaptionClicked(trackerDataModel)

            if (isCollapsed) {
                showFull()
            } else {
                showLess()
            }
        }

        showLess()
    }

    private fun showLess() {
        textView.invisible()
        textView.text = fullText
        textView.movementMethod = EmptyMovementMethod

        textView.requestLayout()
        textView.doOnLayout {
            textView.isClickable = textView.lineCount > MAX_LINES_COLLAPSED
            textView.post { setupSelengkapnya() }
        }
        textView.scrollTo(0, 0)
        isCollapsed = true

        fadingLayout.setBottomFadingEdgeBounds(0)
        fadingLayout.setTopFadingEdgeBounds(0)
    }

    private fun showFull() {
        textView.text = buildSpannedString {
            appendLine(fullText)
            append(getSpannedActionText(textView.context.getString(R.string.feed_lihat_sedikit_label)))
        }
        textView.movementMethod = ScrollingMovementMethod.getInstance()
        textView.isClickable = true
        captionListener.onExpanded(this)

        textView.scrollTo(0, 0)
        isCollapsed = false

        textView.requestLayout()
        textView.doOnLayout {
            if (textView.lineCount > MAX_LINES_COLLAPSED) {
                fadingLayout.setBottomFadingEdgeBounds(
                    if (textView.canScrollVertically(1)) FADING_EDGE_HEIGHT else 0
                )
                fadingLayout.setTopFadingEdgeBounds(
                    if (textView.canScrollVertically(-1)) FADING_EDGE_HEIGHT else 0
                )
            }
        }
    }

    private fun getSpannedActionText(text: String): SpannableString =
        SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(RelativeSizeSpan(0.85f), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    @SuppressLint("SetTextI18n")
    private fun setupSelengkapnya() {
        if (textView.lineCount <= MAX_LINES_COLLAPSED) {
            textView.show()
            return
        }

        val selengkapnyaText = textView.context.getString(R.string.feed_selengkapnya_label)
        val readMoreText = getSpannedActionText("$ELLIPSIS $selengkapnyaText")
        val cutCharAmountOnLastIndex = getAmountOfCutChar(MAX_LINES_COLLAPSED, readMoreText)
        val lastLineEndIndex = textView.layout.getLineVisibleEnd(MAX_LINES_COLLAPSED - 1)
        val ellipsizedText = textView.text.substring(0, lastLineEndIndex - cutCharAmountOnLastIndex)

        textView.text = buildSpannedString {
            append(ellipsizedText)
            append(readMoreText)
        }

        textView.show()
    }

    private fun getAmountOfCutChar(maxLines: Int, readMoreText: CharSequence): Int {
        val lastLineStartIndex = textView.layout.getLineVisibleEnd(maxLines - 2) + 1
        val lastLineEndIndex = textView.layout.getLineVisibleEnd(maxLines - 1)

        if (lastLineStartIndex > lastLineEndIndex) return 0

        return try {
            val lastLineText = textView.text.substring(lastLineStartIndex, lastLineEndIndex)

            val bounds = Rect()
            textView.paint.getTextBounds(lastLineText, 0, lastLineText.length, bounds)

            var cutTextAmount = -1
            do {
                cutTextAmount++
                val subText = lastLineText.substring(0, lastLineText.length - cutTextAmount)
                val replacedText = subText + readMoreText
                textView.paint.getTextBounds(replacedText, 0, replacedText.length, bounds)
                val replacedTextWidth = bounds.width()
            } while (replacedTextWidth > textView.width)

            cutTextAmount
        } catch (_: IndexOutOfBoundsException) {
            0
        }
    }

    companion object {
        private const val MAX_LINES_COLLAPSED = 2

        private const val FADING_EDGE_HEIGHT = 20

        private const val ELLIPSIS = "..."
    }

    interface Listener {
        fun onExpanded(view: FeedCaptionView)
    }
}

object EmptyMovementMethod : MovementMethod {

    override fun initialize(p0: TextView?, p1: Spannable?) {
    }

    override fun onKeyDown(p0: TextView?, p1: Spannable?, p2: Int, p3: KeyEvent?): Boolean {
        return false
    }

    override fun onKeyUp(p0: TextView?, p1: Spannable?, p2: Int, p3: KeyEvent?): Boolean {
        return false
    }

    override fun onKeyOther(p0: TextView?, p1: Spannable?, p2: KeyEvent?): Boolean {
        return false
    }

    override fun onTakeFocus(p0: TextView?, p1: Spannable?, p2: Int) {
    }

    override fun onTrackballEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }

    override fun onGenericMotionEvent(p0: TextView?, p1: Spannable?, p2: MotionEvent?): Boolean {
        return false
    }

    override fun canSelectArbitrarily(): Boolean {
        return false
    }
}
