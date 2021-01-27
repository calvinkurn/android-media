package com.tokopedia.kol.feature.comment.view.custom

import android.content.Context
import android.net.Uri
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.feedcomponent.util.MentionTextHelper
import com.tokopedia.feedcomponent.view.custom.MentionEditText
import com.tokopedia.feedcomponent.view.span.MentionSpan
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.kol.R
import com.tokopedia.kol.common.util.UrlUtil
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 2019-09-25.
 */
class KolCommentCardView : LinearLayout {

    private companion object {
        const val SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val comment: Typography
    private val time: Typography
    private val avatar: ImageView
    private val badge: ImageView
    private val btnReply: UnifyButton

    private val onMentionClicked = object: MentionSpan.OnClickListener {
        override fun onClick(userId: String) {
            listener?.onMentionedProfileClicked(userId)
        }
    }

    private var listener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.card_kol_comment, this)
        with(view) {
            avatar = findViewById(R.id.avatar)
            time = findViewById(R.id.time)
            comment = findViewById(R.id.comment)
            badge = findViewById(R.id.badge)
            btnReply = findViewById(R.id.btn_reply)
        }
        orientation = VERTICAL
        setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setModel(element: KolCommentViewModel, canComment: Boolean) {
        ImageHandler.loadImageCircle2(avatar.context, avatar, element.avatarUrl)
        time.text = element.time

        avatar.setOnClickListener {
            val profileUrl = element.userUrl
            listener?.onAvatarClicked(
                    if (!profileUrl.isNullOrEmpty()) profileUrl
                    else constructProfileApplink(element.userId)
            )
        }

        badge.visibility = View.GONE
        if (!TextUtils.isEmpty(element.userBadges)) {
            badge.visibility = View.VISIBLE
            ImageHandler.loadImageCircle2(badge.context, badge, element.userBadges)
        }

        if (canComment) btnReply.visible() else btnReply.gone()
        btnReply.setOnClickListener {
            val userId = if (!element.userUrl.isNullOrEmpty()) {
                val paramList = UriUtil.destructureUri(ApplinkConst.PROFILE, Uri.parse(element.userUrl))
                paramList.firstOrNull()
            } else element.userId

            userId?.let { id ->
                listener?.onReplyClicked(
                        MentionableUserViewModel(
                                id,
                                "",
                                element.name,
                                "",
                                element.isShop)
                )
            }
        }

        when (element) {
            is KolCommentHeaderViewModel -> handleHeaderComment(element)
            else -> handleGeneralComment(element)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun handleHeaderComment(element: KolCommentHeaderViewModel) {
        val commentText: String = buildString {
            if (badge.isVisible) append(SPACE)
            append(getCommentText(element))
        }

        if (!TextUtils.isEmpty(element.tagsLink)) {
            UrlUtil.setTextWithClickableTokopediaUrl(comment,
                    commentText,
                    getUrlClickableSpan(element))
        } else {
            UrlUtil.setTextWithClickableTokopediaUrl(comment, commentText)
        }
    }

    private fun handleGeneralComment(element: KolCommentViewModel) {
        comment.movementMethod = LinkMovementMethod.getInstance()

        val commentText: Spanned = if (element.isOfficial) {
            badge.visibility = View.VISIBLE
            MethodChecker.fromHtml(SPACE + getCommentText(element))
        } else {
            badge.visibility = View.GONE
            MethodChecker.fromHtml(getCommentText(element))
        }

        comment.text = MentionTextHelper.spanText(
                commentText,
                MentionEditText.getMentionColor(context),
                onMentionClicked,
                false
        )

        setOnLongClickListener {
            listener?.onDeleteComment(element.id, element.canDeleteComment()) ?: false
        }
    }

    private fun getCommentText(element: KolCommentViewModel): String {
        return ("<b>" + element.name + "</b>" + " "
                + element.review.toString().replace("(\r\n|\n)".toRegex(), "<br />"))
    }

    private fun constructProfileApplink(userId: String): String {
        return ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, userId)
    }

    private fun getUrlClickableSpan(element: KolCommentHeaderViewModel): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.onTokopediaUrlClicked(element.tagsLink)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
    }

    interface Listener {
        fun onAvatarClicked(profileUrl: String)
        fun onMentionedProfileClicked(authorId: String)
        fun onDeleteComment(commentId: String, canDeleteComment: Boolean): Boolean
        fun onTokopediaUrlClicked(url: String)
        fun onReplyClicked(mentionableUser: MentionableUserViewModel)
    }
}