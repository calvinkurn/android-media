package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.ShopProductModel
import com.tokopedia.topads.sdk.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.utils.ApplyItemDecorationReimagineHelper.setItemDecorationReimagineSearch
import com.tokopedia.topads.sdk.view.adapter.ShopAdsProductAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ShopAdsWithOneProductView : BaseCustomView {

    private var shopAdsProductAdapter: ShopAdsProductAdapter? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.topads_with_one_product_layout, this)
    }

    fun setShopProductModel(shopProductModel: ShopProductModel, shopAdsProductListener: ShopAdsProductListener,
                            followButtonClickListener:FollowButtonClickListener?, isReimagine: Boolean = false,) {

        initTShopProductTitle(shopProductModel)
        initTopAdsCarouselItem(shopProductModel, shopAdsProductListener, followButtonClickListener, isReimagine)

    }

    private fun initTopAdsCarouselItem(shopProductModel: ShopProductModel, shopAdsProductListener: ShopAdsProductListener,
                                       followButtonClickListener: FollowButtonClickListener?, isReimagine: Boolean = false,) {
        shopAdsProductAdapter = ShopAdsProductAdapter(shopAdsProductListener, followButtonClickListener, isReimagine)
        val list = findViewById<RecyclerView>(R.id.shopAdsProductRv)
        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = shopAdsProductAdapter
        list.setItemDecorationReimagineSearch(isReimagine)
        shopAdsProductAdapter?.setList(shopProductModel.items)

    }

    private fun initTShopProductTitle(shopProductModel: ShopProductModel) {
        val title  = findViewById<Typography>(R.id.shopProductTitle)
        title.text = shopProductModel.title
        title.showWithCondition(shopProductModel.title.isNotEmpty())
    }
}
