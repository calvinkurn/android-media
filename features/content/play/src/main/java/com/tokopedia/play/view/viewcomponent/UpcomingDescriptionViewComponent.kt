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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.util.extension.buildSpannedString
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
    private val ctx: Context
        get() = rootView.context

    private val uiModel: DescriptionUiModel = DescriptionUiModel()

    private val animExpand: ObjectAnimator
        get() = ObjectAnimator.ofInt(
            txt,
            PARAM_MAX_LINES,
            if (!uiModel.isExpanded) MIN_LINES else MAX_LINES //state already switched so returned back
        )
            .apply {
                duration = UnifyMotion.T2
                expandText(
                    description = uiModel.originalText,
                    isExpand = uiModel.isExpanded
                )
            }

    fun setupText(description: String) {
        if (uiModel.originalText == description) return

        uiModel.originalText = description
        txt.text = description

        setupExpandable()
    }

    fun setupExpand(isExpanded: Boolean) {
        if (uiModel.isExpanded == isExpanded) return

        uiModel.isExpanded = isExpanded
        resetText()
    }

    private val clickableSpan by lazy(LazyThreadSafetyMode.NONE) {
        object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color = MethodChecker.getColor(ctx, unifyR.color.Unify_GN500)
                tp.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                listener.onTextClicked(this@UpcomingDescriptionViewComponent)
            }
        }
    }

    private fun resetText() {
        animExpand.start()
    }

    private fun String.truncatedText(readMoreText: String): SpannedString {
        val length = this.filter { it.isLetterOrDigit() }.length
        val newText = this.take(length)
        return buildSpannedString {
            append(
                newText,
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
                readMoreText,
                clickableSpan,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    private fun String.toLongText(readLessText: String): SpannedString {
        return buildSpannedString {
            append(uiModel.originalText)
            append(" ")
            append(
                readLessText,
                clickableSpan,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    private fun setupExpandable() {
        txt.doOnLayout {
            val newText = txt.layout.text.toString()
            if (newText == uiModel.originalText) return@doOnLayout
            expandText(description = newText, isExpand = uiModel.isExpanded)
        }
    }

    private fun expandText(description: String, isExpand: Boolean) {
        //scroll to v top of content
        txt.scrollTo(0, 0)
        if (!isExpand) {
            txt.ellipsize = TextUtils.TruncateAt.END
            txt.movementMethod = LinkMovementMethod.getInstance()
            txt.text = if (uiModel.truncatedText.isEmpty()) {
                uiModel.truncatedText =
                    description.truncatedText(getString(playR.string.play_upcoming_description_read_more))
                uiModel.truncatedText
            } else uiModel.truncatedText
        } else {
            txt.text =
                description.toLongText(getString(playR.string.play_upcoming_description_less))
            txt.ellipsize = null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animExpand.cancel()
    }

    interface Listener {
        fun onTextClicked(view: UpcomingDescriptionViewComponent)
    }

    data class DescriptionUiModel(
        var originalText: String = "",
        var isExpanded: Boolean = false,
        var truncatedText: SpannedString = buildSpannedString { }
    )

    companion object {
        private const val PARAM_MAX_LINES = "maxLines"
        private const val MIN_LINES = 2
        private const val MAX_LINES = 50
        private const val END_CHARS = "... "
    }
}