package com.tokopedia.topads.sdk.v2.shopadslayout5.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.v2.shopadslayout5.uimodel.ShopProductModel
import com.tokopedia.topads.sdk.v2.shopadslayout5.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.v2.shopadslayout5.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.v2.shopadslayout5.adapter.ShopAdsProductAdapter
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

    fun setShopProductModel(shopProductModel: ShopProductModel, shopAdsProductListener: ShopAdsProductListener, followButtonClickListener: FollowButtonClickListener?) {

        initTShopProductTitle(shopProductModel)
        initTopAdsCarouselItem(shopProductModel, shopAdsProductListener, followButtonClickListener)

    }

    private fun initTopAdsCarouselItem(shopProductModel: ShopProductModel, shopAdsProductListener: ShopAdsProductListener,
                                       followButtonClickListener: FollowButtonClickListener?) {
        shopAdsProductAdapter = ShopAdsProductAdapter(shopAdsProductListener, followButtonClickListener)
        val list = findViewById<RecyclerView>(R.id.shopAdsProductRv)
        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = shopAdsProductAdapter
        shopAdsProductAdapter?.setList(shopProductModel.items)

    }

    private fun initTShopProductTitle(shopProductModel: ShopProductModel) {
        val title  = findViewById<Typography>(R.id.shopProductTitle)
        title.text = shopProductModel.title
        title.showWithCondition(shopProductModel.title.isNotEmpty())
    }
}
