package com.tokopedia.feedcomponent.view.widget.shoprecom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.feedcomponent.R.string.btn_text_follow
import com.tokopedia.feedcomponent.R.string.btn_text_following
import com.tokopedia.feedcomponent.data.pojo.people.ShopRecomItemUI
import com.tokopedia.feedcomponent.databinding.ItemShopRecommendationBinding
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

    interface Listener {
        fun onShopRecomCloseClicked(item: ShopRecomItemUI)
        fun onShopRecomFollowClicked(item: ShopRecomItemUI)
        fun onShopRecomItemClicked(appLink: String)
    }

    private var mListener: Listener? = null

    private var binding = ItemShopRecommendationBinding.inflate(
        LayoutInflater.from(this.context),
        this,
        true
    )

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(data: ShopRecomItemUI) = with(binding) {
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

    private fun onClickListener(data: ShopRecomItemUI) = with(binding) {
        clItemShopContainer.setOnClickListener { mListener?.onShopRecomItemClicked(data.applink) }
        imgItemShopClose.setOnClickListener { mListener?.onShopRecomCloseClicked(data) }
        btnItemShop.setOnClickListener { mListener?.onShopRecomFollowClicked(data) }
    }

}