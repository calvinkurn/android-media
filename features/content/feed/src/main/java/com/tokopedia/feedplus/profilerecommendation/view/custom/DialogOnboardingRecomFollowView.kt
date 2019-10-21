package com.tokopedia.feedplus.profilerecommendation.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecomAction
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 2019-09-19.
 */
class DialogOnboardingRecomFollowView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    private val ivClose: ImageView
    private val tvTitle: TextView
    private val ivAvatar: ImageView
    private val tvName: TextView
    private val ivBadge: ImageView
    private val tvInstruction: TextView
    private val btnFollow: UnifyButton

    var listener: ActionListener? = null

    init {
        this.orientation = VERTICAL
        val view = View.inflate(context, R.layout.dialog_onboarding_recommendation_follow, this)
        with(view) {
            ivClose = findViewById(R.id.iv_close)
            tvTitle = findViewById(R.id.tv_title)
            ivAvatar = findViewById(R.id.iv_avatar)
            tvName = findViewById(R.id.tv_name)
            ivBadge = findViewById(R.id.iv_badge)
            tvInstruction = findViewById(R.id.tv_instruction)
            btnFollow = findViewById(R.id.btn_follow)
        }

        ivClose.setOnClickListener { listener?.onCloseButtonClicked() }
    }

    fun setupDialog(
            authorId: String,
            name: String,
            avatarUrl: String,
            badgeUrl: String,
            instruction: String,
            isFollowed: Boolean,
            actionFalse: String,
            actionTrue: String
    ) {
        tvTitle.text = name
        ivAvatar.loadImageCircle(avatarUrl)
        ivBadge.loadImageCircle(badgeUrl)
        tvName.text = name
        tvInstruction.text = instruction

        tag = authorId

        btnFollow.apply {
            if (isFollowed) {
                text = actionFalse
                buttonVariant = 2
            } else {
                text = actionTrue
                buttonVariant = 1
            }
            setOnClickListener {
                listener?.onFollowButtonClicked(
                        authorId,
                        if (isFollowed) FollowRecomAction.UNFOLLOW else FollowRecomAction.FOLLOW
                )
            }
        }
    }

    interface ActionListener {
        fun onCloseButtonClicked()
        fun onFollowButtonClicked(authorId: String, action: FollowRecomAction)
    }
}