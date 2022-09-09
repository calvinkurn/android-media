package com.tokopedia.play.view.custom.interactive.follow

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.databinding.ViewInteractiveFollowBinding
import com.tokopedia.play_common.view.game.GameHeaderView

/**
 * Created by kenny.hadisaputra on 07/04/22
 */
class InteractiveFollowView : ConstraintLayout {

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
        binding.btnFollow.setOnClickListener {
            mListener?.onFollowClicked(this)
        }
        binding.headerView.isEditable = false
    }

    fun getHeader(): GameHeaderView {
        return binding.headerView
    }

    fun setAvatarUrl(imageUrl: String) {
        binding.ivPartner.setImageUrl(imageUrl)
    }

    fun setBadgeUrl(badgeUrl: String) {
        binding.ivBadge.showWithCondition(badgeUrl.isNotEmpty())
        if(badgeUrl.isNotEmpty()) binding.ivBadge.setImageUrl(badgeUrl)
    }

    fun setPartnerName(name: String) {
        binding.tvName.text = name
    }

    fun setLoading(isLoading: Boolean) {
        binding.btnFollow.isLoading = isLoading
        binding.btnFollow.isClickable = !isLoading
    }

    fun setListener(listener: Listener?) {
        mListener = listener

        if(binding.btnFollow.isVisible) mListener?.onFollowImpressed(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    interface Listener {
        fun onFollowImpressed(view: InteractiveFollowView)

        fun onFollowClicked(view: InteractiveFollowView)
    }
}