package com.tokopedia.kol.feature.comment.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.feedcomponent.util.ColorUtil
import com.tokopedia.feedcomponent.util.MentionTextHelper
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.custom.MentionEditText
import com.tokopedia.feedcomponent.view.span.MentionSpan
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.net.URLEncoder

const val SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
var PARAM_SHOP_ID = "{shop_id}"


class KolCommentNewCardView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val comment: Typography
    private val avatar: ImageView
    private val badge: ImageView
    private val time: Typography
    private val btnReply: UnifyButton
    private val mainView: ConstraintLayout

    private val onMentionClicked = object : MentionSpan.OnClickListener {
        override fun onClick(userId: String) {
            listener?.onMentionedProfileClicked(userId)
        }
    }

    private var listener: Listener? = null

    init {
        val view = View.inflate(context, R.layout.card_kol_new_comment, this)
        with(view) {
            avatar = findViewById(R.id.avatar)
            comment = findViewById(R.id.comment)
            badge = findViewById(R.id.badge)
            time = findViewById(R.id.time)
            btnReply = findViewById(R.id.btn_reply)
            mainView = findViewById(R.id.main_view)

        }
        orientation = VERTICAL
        setBackgroundColor(
            MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        )
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setModel(element: KolCommentNewModel, canComment: Boolean) {
        ImageHandler.loadImageCircle2(avatar.context, avatar, element.avatarUrl)
        element.time?.let {
            if (it == context.getString(com.tokopedia.kolcommon.R.string.post_time_just_now))
                time.text =
                    context.getString(com.tokopedia.feedcomponent.R.string.post_time_few_moments_ago)
            else
                time.text = TimeConverter.generateTimeNewForComment(context, it)
        }
        avatar.setOnClickListener {
            val profileUrl = element.userUrl
            listener?.onAvatarClicked(
                if (!profileUrl.isNullOrEmpty()) profileUrl
                else constructProfileApplink(element.isShop, element.userId ?: "0"),
                element.userId
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
                val paramList =
                    UriUtil.destructureUri(ApplinkConst.PROFILE, Uri.parse(element.userUrl))
                paramList.firstOrNull()
            } else element.userId

            userId?.let { id ->
                listener?.onReplyClicked(
                    MentionableUserModel(
                        id,
                        "",
                        element.name ?: "",
                        "",
                        element.isShop
                    )
                )
            }
        }

        when (element) {
            is KolCommentHeaderNewModel -> handleHeaderComment(element)
            else -> handleGeneralComment(element)
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun handleHeaderComment(element: KolCommentHeaderNewModel) {
        btnReply.gone()
        time.gone()
        val tagConverter = TagConverter()
        val commentText: String = buildString {
            if (badge.isVisible) append(SPACE)
            append(getCommentText(element))
        }
        comment.text = tagConverter.convertToLinkifyHashtag(
            SpannableString(MethodChecker.fromHtml(commentText)), colorLinkHashtag
        ) { hashtag -> onHashtagClicked(hashtag) }

        comment.movementMethod = LinkMovementMethod.getInstance()
    }


    private fun onHashtagClicked(hashtag: String) {
        val encodeHashtag = URLEncoder.encode(hashtag)
        RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
        listener?.onHashtagClicked(hashtag)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleGeneralComment(element: KolCommentNewModel) {
        btnReply.visible()
        time.visible()
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
            listener?.onMenuClicked(element.id, element.canDeleteComment())
            false
        }

    }

    private fun getCommentText(element: KolCommentNewModel): String {
        return ("<b>" + element.name + "</b>" + " ")
            .plus(
                "<font color='${
                    ColorUtil.getColorFromResToString(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950
                    )
                }'>"
            )
            .plus(
                element.review.toString()
                    .replace("(\r\n|\n)".toRegex(), "<br />")
            )
    }
    private val colorLinkHashtag: Int
        get() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)

    private fun constructProfileApplink(isShop: Boolean, userId: String): String {
        return if (!isShop) {
            ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, userId)
        } else {
            ApplinkConst.SHOP.replace(PARAM_SHOP_ID, userId)
        }
    }

    interface Listener {
        fun onHashtagClicked(hashtag: String)
        fun onAvatarClicked(profileUrl: String, userId: String?)
        fun onMentionedProfileClicked(authorId: String)
        fun onTokopediaUrlClicked(url: String)
        fun onReplyClicked(mentionableUser: MentionableUserModel)
        fun onMenuClicked(id: String?, canDeleteComment: Boolean)
    }
}
