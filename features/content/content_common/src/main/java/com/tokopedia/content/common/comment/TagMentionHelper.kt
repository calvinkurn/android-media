package com.tokopedia.content.common.comment

import android.content.Context
import android.text.Spanned
import android.text.SpannedString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.text.getSpans
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.content.common.util.buildSpannedString
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 23/02/23
 */

class MentionedSpanned(
    @ColorInt val colorRes: Int,
    private val userType: UserType = UserType.Unknown,
    private val appLink: String = "",
    val id: String,
    val listener: Listener,
    val ctx: Context
) : ClickableSpan() {

    private val newAppLink: String
        get() {
            return appLink.ifBlank {
                when (userType) {
                    UserType.People -> ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, id)
                    UserType.Shop -> ApplinkConst.SHOP.replace("{shop_id}", id)
                    else -> { ""}
                }
            }
        }

    override fun onClick(view: View) {
        listener.onClicked(newAppLink)
    }

    override fun updateDrawState(tp: TextPaint) {
        super.updateDrawState(tp)
        tp.apply {
            color = colorRes
            isUnderlineText = false
            typeface = com.tokopedia.unifyprinciples.Typography.getFontType(
                ctx,
                appLink.isNotBlank(),
                com.tokopedia.unifyprinciples.Typography.PARAGRAPH_2
            )
        }
    }

    interface Listener {
        fun onClicked(appLink: String)
    }
}

class BaseSpan(val fullText: String, val content: String, val shortName: String, val ctx: Context) :
    ForegroundColorSpan(MethodChecker.getColor(ctx, unifyprinciplesR.color.Unify_NN1000)) {
    val sentText: String get() = fullText + content
}

object TagMentionBuilder {
    private const val MENTION_CHAR = "@"
    private val regex = """((?<=\{)(@\d+)\@|(@user|@shop)\@|(@.*)\@(?=\}))""".toRegex()
    private const val MENTION_VALUE = 3

    fun createNewMentionTag(item: CommentUiModel.Item): String {
        return "{$MENTION_CHAR${item.userId}$MENTION_CHAR|$MENTION_CHAR${item.userType.value}$MENTION_CHAR|$MENTION_CHAR${item.username}$MENTION_CHAR} "
    }

    fun isChildOrParent(text: Spanned?, commentId: String): CommentType {
        if (text == null) return CommentType.Parent
        val find = text.getSpans<BaseSpan>(0, text.length)
        val isAvailable = find.joinToString { item -> if (text.contains(item.shortName)) text.toString() else "" }
        return if (isAvailable.isBlank()) {
            CommentType.Parent
        } else {
            CommentType.Child(commentId)
        }
    }

    fun spanText(text: Spanned, textLength: Int, ctx: Context): Spanned {
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
            val span = BaseSpan(fullText = restructured, content = comment, shortName = name, ctx = ctx)
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
            convert.joinToString { txt ->
                if (!text.contains(txt.shortName)) text.toString()
                else txt.sentText + text.toString().substring(txt.shortName.length, text.length)
            }.ifBlank {
                text.toString()
            }
        } catch (e: Exception) {
            text.toString()
        }
    }

    fun getMentionTag(
        item: CommentUiModel.Item,
        @ColorInt mentionColor: Int,
        @ColorInt parentColor: Int,
        mentionListener: MentionedSpanned.Listener,
        parentListener: MentionedSpanned.Listener,
        context: Context
    ): SpannedString {
        val parentSpanned = MentionedSpanned(
            colorRes = parentColor,
            id = item.id,
            appLink = item.appLink,
            listener = parentListener,
            userType = item.userType,
            ctx = context
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
                    colorRes = mentionColor,
                    id = id,
                    userType = UserType.getByValue(type),
                    listener = mentionListener,
                    ctx = context
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
                    append(content)
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
