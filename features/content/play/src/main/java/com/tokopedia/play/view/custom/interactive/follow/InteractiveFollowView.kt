package com.tokopedia.play.view.custom.interactive.follow

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.play.databinding.ViewInteractiveFollowBinding
import com.tokopedia.play_common.view.game.GameHeaderView

/**
 * Created by kenny.hadisaputra on 07/04/22
 */
class InteractiveFollowView : LinearLayout {

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

    private var mListener: Listener? = null

    init {
        orientation = VERTICAL

        binding.btnFollow.setOnClickListener {
            mListener?.onFollowClicked(this)
        }
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

    fun setLoading(isLoading: Boolean) {
        binding.btnFollow.isLoading = isLoading
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    interface Listener {

        fun onFollowClicked(view: InteractiveFollowView)
    }
}