package com.tokopedia.play.view.viewcomponent

import android.animation.ObjectAnimator
import android.content.Context
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.text.buildSpannedString
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 08/08/22
 */
class UpcomingDescriptionViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val txt = (rootView as Typography)
    private var originalText: String = ""
    private var truncatedText: SpannedString? = null
    private val ctx: Context
        get() = rootView.context
    var isExpand: Boolean = false

    init {
        txt.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private val animExpand: ObjectAnimator
        get() = ObjectAnimator.ofInt(txt, PARAM_MAX_LINES, if (!isExpand) MIN_LINES else MAX_LINES)
            .apply {
                duration = UnifyMotion.T2
                setupExpandable()
            }

    fun setupText(description: String) {
        if(description.isBlank()) return

        originalText = description
        txt.text = originalText

        setupExpandable()
    }

    private val clickableSpan: ClickableSpan
        get() = object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color = MethodChecker.getColor(ctx, unifyR.color.Unify_GN500)
                tp.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                resetText()
            }
        }

    fun resetText() {
        if(originalText.isBlank()) return

        isExpand = !isExpand
        listener.onTextClicked(isExpand, this)
        animateText()
    }

    private fun animateText() {
        animExpand.start()
    }

    private fun getText(text: String): SpannedString = buildSpannedString {
        append(
            text,
            ForegroundColorSpan(
                MethodChecker.getColor(
                    ctx,
                    unifyR.color.Unify_Static_White
                )
            ),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        append(END_CHARS)
        append(
            getString(playR.string.play_upcoming_description_read_more),
            clickableSpan,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun setupExpandable() {
        txt.doOnLayout {
            val newText = txt.layout.text
            when {
                !isExpand -> {
                    txt.maxLines = 2
                    txt.ellipsize = TextUtils.TruncateAt.END
                    val length = newText.filter { it.isLetterOrDigit() }.length
                    val truncatedTxt = newText.take(length - TRIMMED_CHARS).toString()
                    if(truncatedText == null) truncatedText = getText(truncatedTxt)
                    txt.text = if(truncatedText == null) getText(truncatedTxt) else truncatedText
                    txt.movementMethod = LinkMovementMethod.getInstance()
                }
                else -> {
                    txt.text = originalText
                    txt.ellipsize = null
                    txt.maxLines = MAX_LINES
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        animExpand.cancel()
    }

    interface Listener {
        fun onTextClicked(isExpand: Boolean, view: UpcomingDescriptionViewComponent)
    }

    companion object {
        private const val PARAM_MAX_LINES = "maxLines"
        private const val MIN_LINES = 2
        private const val MAX_LINES = 14
        private const val TRIMMED_CHARS = 10
        private const val END_CHARS = "... "
    }
}