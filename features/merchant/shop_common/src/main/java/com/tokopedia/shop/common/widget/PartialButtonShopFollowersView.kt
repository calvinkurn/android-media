package com.tokopedia.shop.common.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.util.RoundedShadowUtill
import com.tokopedia.shop.common.util.loadLeftDrawable
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_button_npl_follow.view.*

/**
 * Created by Yehezkiel on 06/10/20
 *
 * After get the data simply call renderView()
 * if want to hide the view, set setupVisibility to false
 */
class PartialButtonShopFollowersView private constructor(val view: View, private val listener: PartialButtonShopFollowersListener) {

    companion object {
        const val SHOP_FOLLOWERS_IMG_ASSET = "https://ecs7.tokopedia.net/android/other/il_pdp%20bts_follower.png"
        const val GONE_ANIMATION_DURATION = 300L
        fun build(_view: View, _buttonListener: PartialButtonShopFollowersListener) = PartialButtonShopFollowersView(_view, _buttonListener)
    }

    val shadowDrawable: Drawable by lazy {
        RoundedShadowUtill.generateBackgroundWithShadow(view,
                com.tokopedia.unifyprinciples.R.color.Unify_N0,
                R.dimen.dp_12,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_2,
                Gravity.TOP)
    }

    var setupVisibility: Boolean = false
        set(value) {
            field = value
            if (value) {
                animateSlideUp()
            } else {
                animateSlideDown()
            }
        }

    private var followersImageAsset: ImageView? = null
    private var followersTitle: Typography? = null
    private var followersDesc: Typography? = null
    private var followersBtn: UnifyButton? = null

    init {
        with(view) {
            followersImageAsset = findViewById(R.id.shop_followers_image)
            followersTitle = findViewById(R.id.shop_followers_title)
            followersDesc = findViewById(R.id.shop_followers_desc)
            followersBtn = findViewById(R.id.shop_followers_btn)
        }
    }

    fun renderView(title: String, desc: String, alreadyFollowShop: Boolean = true,
                   buttonLabel: String? = null,
                   voucherIconUrl: String? = null,
                   iconUrl: String = "",
                   hideButton: Boolean = false,
                   maxLine: Int = 1,
                   centerImage: Boolean = false) = with(view) {
        if (alreadyFollowShop) {
            setupVisibility = false
            return@with
        }

        shop_followers_desc?.maxLines = maxLine
        shop_followers_title.text = title
        shop_followers_desc.text = desc

        setupButtonFollowers(buttonLabel, voucherIconUrl)
        setOnClickListener {}
        setupRoundedTopShadow()

        followersImageAsset?.run {
            val params = layoutParams as ViewGroup.MarginLayoutParams

            if (centerImage) {
                params.bottomMargin = 16.toPx()
            } else {
                params.bottomMargin = 0
            }

            if (iconUrl.isNotEmpty()) {
                ImageHandler.loadImageWithoutPlaceholderAndError(this, iconUrl)
            } else {
                ImageHandler.loadImageWithoutPlaceholderAndError(this, SHOP_FOLLOWERS_IMG_ASSET)
            }
        }

        if (hideButton) {
            followersBtn?.visibility = View.GONE
        } else {
            followersBtn?.visibility = View.VISIBLE
        }

        setupVisibility = true
    }

    fun stopLoading() {
        followersBtn?.run {
            if (isLoading) {
                isLoading = false
            }
        }
    }

    fun startLoading() {
        followersBtn?.run {
            if (!isLoading) {
                isLoading = true
            }
        }
    }

    private fun animateSlideUp() = with(view) {
        visibility = View.VISIBLE
        animate().translationY(0F).setDuration(GONE_ANIMATION_DURATION).setListener(null)
    }

    private fun animateSlideDown() = with(view) {
        animate().translationY(view.height.toFloat()).setDuration(GONE_ANIMATION_DURATION).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
                stopLoading()
            }
        })
    }

    private fun setupButtonFollowers(buttonLabel: String?, voucherIconUrl: String?) {
        voucherIconUrl?.run {
            followersBtn?.layoutParams?.width = 110.toPx()
            followersBtn?.loadLeftDrawable(
                    context = view.context,
                    url = voucherIconUrl,
                    convertIntoSize = 20.toPx()
            )
        }
        followersBtn?.run {
            if (!buttonLabel.isNullOrBlank()) {
                text = buttonLabel
            }
            setOnClickListener {
                if (!isLoading) {
                    listener.onButtonFollowNplClick()
                }
                voucherIconUrl?.run {
                    removeCompoundDrawableFollowButton()
                }
            }
        }
    }

    private fun removeCompoundDrawableFollowButton() {
        if (!followersBtn?.compoundDrawables.isNullOrEmpty()) {
            followersBtn?.removeDrawable()
        }
    }

    private fun setupRoundedTopShadow() = with(view) {
        background = shadowDrawable
    }

}

interface PartialButtonShopFollowersListener {
    fun onButtonFollowNplClick()
}