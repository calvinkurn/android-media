package com.tokopedia.feedcomponent.view.widget.shoprecom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.people.ShopRecomItemUI
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

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
        fun onCloseClicked(data: ShopRecomItemUI)
        fun onFollowClicked(encryptedID: String)
        fun onShopItemClicked(appLink: String)
    }

    private var mListener: Listener? = null

    private val clItemShopContainer: ConstraintLayout
    private val txtItemShopName: Typography
    private val txtItemShopUsername: Typography
    private val imgItemShopImage: ImageUnify
    private val imgItemShopBadge: ImageUnify
    private val imgItemShopClose: ImageUnify
    private val btnItemShopCta: UnifyButton

    init {
        val view = View.inflate(context, R.layout.item_shop_recommendation, this)
        clItemShopContainer = view.findViewById(R.id.cl_item_shop_container)
        txtItemShopName = view.findViewById(R.id.txt_item_shop_name)
        txtItemShopUsername = view.findViewById(R.id.txt_item_shop_username)
        imgItemShopImage = view.findViewById(R.id.img_item_shop_image)
        imgItemShopBadge = view.findViewById(R.id.img_item_shop_badge)
        imgItemShopClose = view.findViewById(R.id.img_item_shop_close)
        btnItemShopCta = view.findViewById(R.id.btn_item_shop_cta)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(data: ShopRecomItemUI) {
        txtItemShopName.text = data.name
        txtItemShopUsername.text = data.nickname
        imgItemShopImage.setImageUrl(data.logoImageURL)
        imgItemShopBadge.setImageUrl(data.badgeImageURL)

        clItemShopContainer.setOnClickListener { mListener?.onShopItemClicked(data.applink) }
        imgItemShopClose.setOnClickListener { mListener?.onCloseClicked(data) }
        btnItemShopCta.setOnClickListener { mListener?.onFollowClicked(data.encryptedID) }
    }

}