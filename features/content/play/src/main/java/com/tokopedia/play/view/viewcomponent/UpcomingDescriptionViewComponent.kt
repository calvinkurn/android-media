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
    private val ctx: Context
                get() = rootView.context
    private var isExpand: Boolean = false

    fun setupText(description: String) {
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
                isExpand = !isExpand
                resetText()
            }
        }

    fun resetText() {
        animateText()
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
        append("... ")
        append(
            getString(playR.string.play_upcoming_description_read_more),
            clickableSpan,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun animateText() {
        ObjectAnimator.ofInt(txt, "maxLines", if(isExpand) 14 else 2).apply {
            duration = UnifyMotion.T2
            setupExpandable()
        }.start()
    }

    private fun setupExpandable() {
        txt.maxLines = 2
        txt.ellipsize = TextUtils.TruncateAt.END

        txt.doOnLayout {
            val newText = txt.layout.text
            when {
                originalText == newText -> return@doOnLayout
                isExpand -> {
                    txt.text = originalText
                    txt.ellipsize = null
                }
                else -> {
                    txt.maxLines = 2
                    txt.ellipsize = TextUtils.TruncateAt.END
                    val length = newText.filter { it.isLetterOrDigit() }.length
                    val truncatedTxt = newText.take(length).toString()
                    txt.text = getText(truncatedTxt)
                    txt.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }

    interface Listener {
        fun onTextClicked(isExpand: Boolean, view: UpcomingDescriptionViewComponent)
    }
}