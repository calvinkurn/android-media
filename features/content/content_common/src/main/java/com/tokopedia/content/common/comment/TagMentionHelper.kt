package com.tokopedia.content.common.comment

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.feedcomponent.util.buildSpannedString

/**
 * @author by astidhiyaa on 23/02/23
 */

class MentionedSpanned(
    @ColorInt val color: Int,
    private val userType: String = "",
    val userName: String,
    val id: String,
    val listener: Listener,
) : ClickableSpan() {

    override fun onClick(p0: View) {
        listener.onClicked(id, userType)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.typeface = Typeface.DEFAULT_BOLD
        ds.isUnderlineText = false
    }

    interface Listener {
        fun onClicked(id: String, userType: String) //appLink
    }
}

object TagMentionBuilder {
    private const val MENTION_CHAR = "@"

    fun getMentionTag(
        item: CommentUiModel.Item,
        @ColorInt mentionColor: Int,
        @ColorInt parentColor: Int,
        mentionListener: MentionedSpanned.Listener,
        parentListener: MentionedSpanned.Listener
    ): SpannedString {
        val parentSpanned = MentionedSpanned(
            color = parentColor,
            id = item.id,
            userName = item.username,
            listener = parentListener
        )
        return try {
            val regex =
                """((?<=\{)(@\d+)\@|(@user|@seller)\@|(@.*)\@(?=\}))""".toRegex()
            var (id, type, name) = Triple("", "", "")
            val find = regex.findAll(item.content)
            if (find.count() > 0) {
                id = find.elementAt(0).value.replace(MENTION_CHAR,"")
                type = find.elementAt(1).value.replace(MENTION_CHAR,"")
                name = find.elementAt(2).value.removeSuffix(MENTION_CHAR)
                val length = find.sumOf { it.value.length } + 10 //total escape character [{}|@]

                val mentionSpanned = MentionedSpanned(
                    color = mentionColor,
                    id = id,
                    userName = name,
                    userType = type,
                    listener = mentionListener
                )

                buildSpannedString {
                    append(
                        item.username,
                        parentSpanned,
                        Spanned.SPAN_COMPOSING
                    )
                    append(' ')
                    append(
                        name,
                        mentionSpanned,
                        Spanned.SPAN_COMPOSING
                    )
                    append(' ')
                    append(item.content.removeRange(0, length))
                }
            } else throw Exception()
        } catch (e: java.lang.Exception) {
            buildSpannedString {
                append(
                    item.username,
                    parentSpanned,
                    Spanned.SPAN_COMPOSING
                )
                append(' ')
                append(item.content)
            }
        }
    }
}
