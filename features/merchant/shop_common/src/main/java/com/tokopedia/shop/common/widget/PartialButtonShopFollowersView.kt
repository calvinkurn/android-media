package com.tokopedia.shop.common.widget

import com.tokopedia.imageassets.TokopediaImageUrl

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.generateBackgroundWithShadow
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.util.loadLeftDrawable
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 06/10/20
 *
 * After get the data simply call renderView()
 * if want to hide the view, set setupVisibility to false
 */
class PartialButtonShopFollowersView private constructor(val view: View, private val listener: PartialButtonShopFollowersListener) {

    companion object {
        const val SHOP_FOLLOWERS_IMG_ASSET = TokopediaImageUrl.SHOP_FOLLOWERS_IMG_ASSET
        const val GONE_ANIMATION_DURATION = 300L
        fun build(_view: View, _buttonListener: PartialButtonShopFollowersListener) = PartialButtonShopFollowersView(_view, _buttonListener)
    }

    val shadowDrawable: Drawable? by lazy {
        view.generateBackgroundWithShadow(
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_32,
            R.dimen.dp_12,
            R.dimen.dp_12,
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0,
            com.tokopedia.unifyprinciples.R.dimen.layout_lvl0,
            R.dimen.dp_2,
            R.dimen.dp_2,
            Gravity.TOP
        )
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

    fun renderView(
        title: String,
        desc: String,
        alreadyFollowShop: Boolean = true,
        buttonLabel: String? = null,
        voucherIconUrl: String? = null,
        iconUrl: String = "",
        hideButton: Boolean = false,
        maxLine: Int = 1,
        centerImage: Boolean = false,
        customPaddingBottom: Int = 8
    ) = with(view) {
        if (alreadyFollowShop) {
            setupVisibility = false
            return@with
        }

        followersDesc?.maxLines = maxLine
        followersTitle?.text = title
        followersDesc?.text = desc

        setupButtonFollowers(buttonLabel, voucherIconUrl)
        setOnClickListener {}
        setupRoundedTopShadow()

        val titleParams = followersTitle?.layoutParams as ViewGroup.MarginLayoutParams

        if (centerImage) {
            titleParams.topMargin = 4.toPx()
            view.setPadding(0, 0, 0, customPaddingBottom.toPx())
        } else {
            titleParams.topMargin = 0.toPx()
            view.setPadding(0, 0, 0, 0.toPx())
        }

        followersImageAsset?.run {
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

    fun getButtonLoadingStatus(): Boolean {
        return followersBtn?.isLoading ?: false
    }

    private fun animateSlideUp() = with(view) {
        visibility = View.VISIBLE
        animate().translationY(0F).setDuration(GONE_ANIMATION_DURATION).setListener(null)
    }

    private fun animateSlideDown() = with(view) {
        animate().translationY(view.height.toFloat()).setDuration(GONE_ANIMATION_DURATION).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
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
            text = if (!buttonLabel.isNullOrBlank()) {
                buttonLabel
            } else {
                context.getString(R.string.merchant_shop_common_follow_label)
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
        if (shadowDrawable != null) {
            background = shadowDrawable
        }
    }

}

interface PartialButtonShopFollowersListener {
    fun onButtonFollowNplClick()
}
