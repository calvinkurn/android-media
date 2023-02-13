package com.tokopedia.feedcomponent.shoprecom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.feedcomponent.R.string.btn_text_follow
import com.tokopedia.feedcomponent.R.string.btn_text_following
import com.tokopedia.feedcomponent.databinding.ItemShopRecommendationBinding
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.UnifyButton.Type.ALTERNATE
import com.tokopedia.unifycomponents.UnifyButton.Type.MAIN
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST

/**
 * created by fachrizalmrsln on 07/07/22
 **/
class ShopRecomView : FrameLayout, LifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mListener: ShopRecomWidgetCallback? = null

    private var binding = ItemShopRecommendationBinding.inflate(
        LayoutInflater.from(this.context),
        this,
        true
    )

    init {
        if (context is LifecycleOwner) (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    fun setListener(listener: ShopRecomWidgetCallback?) {
        mListener = listener
    }

    fun setData(data: ShopRecomUiModelItem, position: Int) = with(binding) {
        txtItemShopName.text = data.name
        txtItemShopUsername.text = data.nickname
        imgItemShopImage.setImageUrl(data.logoImageURL)
        imgItemShopBadge.shouldShowWithAction(data.badgeImageURL.isNotEmpty()) {
            imgItemShopBadge.setImageUrl(data.badgeImageURL)
        }
        addOnImpressionListener(data.impressHolder) {
            mListener?.onShopRecomItemImpress(
                data,
                position + 1
            )
        }

        buttonFollowState(data.state)
        onClickListener(data, position)
    }

    private fun buttonFollowState(state: ShopRecomFollowState) = with(binding) {
        when (state) {
            ShopRecomFollowState.FOLLOW -> {
                btnItemShop.apply {
                    text = context.getString(btn_text_following)
                    buttonVariant = GHOST
                    buttonType = ALTERNATE
                    isLoading = false
                }
            }
            ShopRecomFollowState.UNFOLLOW -> {
                btnItemShop.apply {
                    text = context.getString(btn_text_follow)
                    buttonVariant = FILLED
                    buttonType = MAIN
                    isLoading = false
                }
            }
            ShopRecomFollowState.LOADING_FOLLOW -> {
                btnItemShop.apply {
                    text = context.getString(btn_text_following)
                    buttonVariant = GHOST
                    buttonType = ALTERNATE
                    isLoading = true
                }
            }
            ShopRecomFollowState.LOADING_UNFOLLOW -> {
                btnItemShop.apply {
                    text = context.getString(btn_text_follow)
                    buttonVariant = FILLED
                    buttonType = MAIN
                    isLoading = true
                }
            }
        }
    }

    private fun onClickListener(data: ShopRecomUiModelItem, position: Int) = with(binding) {
        clItemShopContainer.setOnClickListener {
            mListener?.onShopRecomItemClicked(
                data,
                position + 1
            )
        }
        imgItemShopClose.setOnClickListener { mListener?.onShopRecomCloseClicked(data) }
        btnItemShop.setOnClickListener { mListener?.onShopRecomFollowClicked(data) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mListener = null
    }
}
