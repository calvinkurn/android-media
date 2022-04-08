package com.tokopedia.play_common.view.game.follow

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewInteractiveFollowBinding
import com.tokopedia.play_common.view.RoundedLinearLayout
import com.tokopedia.play_common.view.game.GameHeaderView

/**
 * Created by kenny.hadisaputra on 07/04/22
 */
class InteractiveFollowView : RoundedLinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewInteractiveFollowBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    init {
        clipChildren = false
        clipToPadding = false

        orientation = VERTICAL
        setCornerRadius(
            resources.getDimensionPixelSize(R.dimen.play_dp_12).toFloat()
        )
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    fun setAvatarUrl(imageUrl: String) {
        binding.ivPartner.setImageUrl(imageUrl)
    }

    fun setBadgeUrl(badgeUrl: String) {
        binding.ivBadge.setImageUrl(badgeUrl)
    }

    fun setPartnerName(name: String) {
        binding.tvName.text = name
    }
}