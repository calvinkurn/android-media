package com.tokopedia.feedcomponent.view.widget.shoprecom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.feedcomponent.R.string.btn_text_follow
import com.tokopedia.feedcomponent.R.string.btn_text_following
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.databinding.ItemShopRecommendationBinding
import com.tokopedia.feedcomponent.view.widget.shoprecom.listener.ShopRecommendationCallback
import com.tokopedia.unifycomponents.UnifyButton.Type.ALTERNATE
import com.tokopedia.unifycomponents.UnifyButton.Type.MAIN
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST

/**
 * created by fachrizalmrsln on 07/07/22
 **/
class ShopRecomView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mListener: ShopRecommendationCallback? = null

    private var binding = ItemShopRecommendationBinding.inflate(
        LayoutInflater.from(this.context),
        this,
        true
    )

    fun setListener(listener: ShopRecommendationCallback?) {
        mListener = listener
    }

    fun setData(data: ShopRecomUiModelItem) = with(binding) {
        txtItemShopName.text = data.name
        txtItemShopUsername.text = data.nickname
        imgItemShopImage.setImageUrl(data.logoImageURL)
        imgItemShopBadge.setImageUrl(data.badgeImageURL)

        buttonFollowState(data.isFollow)
        onClickListener(data)
    }

    private fun buttonFollowState(isFollow: Boolean) = with(binding) {
        btnItemShop.apply {
            text = context.getString(if (isFollow) btn_text_following else btn_text_follow)
            buttonVariant = if (isFollow) GHOST else FILLED
            buttonType = if (isFollow) ALTERNATE else MAIN
        }
    }

    private fun onClickListener(data: ShopRecomUiModelItem) = with(binding) {
        clItemShopContainer.setOnClickListener { mListener?.onShopRecomItemClicked(data.applink) }
        imgItemShopClose.setOnClickListener { mListener?.onShopRecomCloseClicked(data.id) }
        btnItemShop.setOnClickListener {
            mListener?.onShopRecomFollowClicked(
                data.id,
                data.encryptedID,
                data.isFollow
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mListener = null
    }

}