package com.tokopedia.content.common.comment

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.text.getSpans
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.feedcomponent.util.buildSpannedString

/**
 * @author by astidhiyaa on 23/02/23
 */

class MentionedSpanned(
    @ColorInt val color: Int,
    private val userType: UserType = UserType.Unknown,
    private val appLink: String = "",
    val userName: String,
    val id: String,
    val listener: Listener
) : ClickableSpan() {

    private val newAppLink: String
        get() {
            return appLink.ifBlank {
                when (userType) {
                    UserType.People -> ApplinkConst.PROFILE.replace(
                        ApplinkConst.Profile.PARAM_USER_ID,
                        id
                    )
                    UserType.Shop -> ApplinkConst.SHOP.replace("{shop_id}", id)
                    else -> ""
                }
            }
        }

    override fun onClick(p0: View) {
        listener.onClicked(newAppLink)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.typeface = Typeface.DEFAULT_BOLD
        ds.isUnderlineText = false
    }

    interface Listener {
        fun onClicked(appLink: String)
    }
}

class BaseSpan(val fullText: String, val content: String, val shortName: String) : ForegroundColorSpan(Color.BLACK) {
    val sentText: String get() = fullText + content
}

object TagMentionBuilder {
    private const val MENTION_CHAR = "@"
    private val regex = """((?<=\{)(@\d+)\@|(@user|@shop)\@|(@.*)\@(?=\}))""".toRegex()
    private const val MENTION_VALUE = 3

    fun createNewMentionTag(item: CommentUiModel.Item): String {
        return "{$MENTION_CHAR${item.userId}$MENTION_CHAR|$MENTION_CHAR${item.userType.value}$MENTION_CHAR|$MENTION_CHAR${item.username}$MENTION_CHAR}"
    }

    fun isChildOrParent(text: Spanned?, commentId: String): CommentType {
        if (text == null) return CommentType.Parent
        val find = text.getSpans<BaseSpan>(0, text.length)
        return if (find.isNotEmpty()) {
            CommentType.Child(commentId)
        } else {
            CommentType.Parent
        }
    }

    fun spanText(text: Spanned, textLength: Int): Spanned {
        if (text.isBlank()) return buildSpannedString { append(text) }

        val find = regex.findAll(text)
        val length = find.sumOf { it.value.length } + 4 // for escape char
        return if (find.count() >= MENTION_VALUE) {
            val name = find.elementAt(2).value.removeSuffix(MENTION_CHAR)
            val comment = text.substring(
                length,
                textLength
            )
            val restructured =
                "{${find.elementAt(0).value}|${find.elementAt(1).value}|${find.elementAt(2).value}}"
            val span = BaseSpan(fullText = restructured, content = comment, shortName = name)
            buildSpannedString {
                append(name, span, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                append(comment)
            }
        } else {
            buildSpannedString { append(text) }
        }
    }

    fun getRawText(text: Spanned?): String {
        if (text.isNullOrBlank()) return ""
        val convert = text.getSpans<BaseSpan>(0, text.length)
        return try {
            convert.joinToString { txt -> txt.sentText + text.toString().substring(txt.shortName.length, text.length) }.ifBlank {
                text.toString()
            }
        } catch (e: Exception){
            text.toString()
        }
    }

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
            appLink = item.appLink,
            listener = parentListener,
            userType = item.userType,
        )
        return try {
            var (id, type, name) = Triple("", "", "")
            val find = regex.findAll(item.content)
            if (find.count() > 0) {
                id = find.elementAt(0).value.replace(MENTION_CHAR, "")
                type = find.elementAt(1).value.replace(MENTION_CHAR, "")
                name = find.elementAt(2).value.removeSuffix(MENTION_CHAR)
                val length = find.sumOf { it.value.length } + 4 // total escape character [{}|||]

                val mentionSpanned = MentionedSpanned(
                    color = mentionColor,
                    id = id,
                    userName = name,
                    userType = UserType.getByValue(type),
                    listener = mentionListener
                )

                val content = item.content.substring(length, item.content.length)

                buildSpannedString {
                    append(
                        item.username,
                        parentSpanned,
                        Spanned.SPAN_COMPOSING
                    )
                    append(
                        " $name",
                        mentionSpanned,
                        Spanned.SPAN_COMPOSING
                    )
                    append(" $content")
                }
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            buildSpannedString {
                append(
                    item.username,
                    parentSpanned,
                    Spanned.SPAN_COMPOSING
                )
                append(" ${item.content}")
            }
        }
    }
}
