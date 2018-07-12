package com.tokopedia.shop.page.view.holder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.util.TextApiUtils
import kotlinx.android.synthetic.main.partial_shop_page_header_2.view.*

class ShopPageHeaderViewHolder(private val view: View){
    fun bind(shopInfo: ShopInfo, isMyShop: Boolean) {
        view.shopName.text = MethodChecker.fromHtml(shopInfo.info.shopName).toString()
        when {
            TextApiUtils.isValueTrue(shopInfo.info.getShopIsOfficial()) -> displayOfficial()
            shopInfo.info.isShopIsGoldBadge -> displayGoldenShop()

        }
    }

    private fun displayGoldenShop() {
        view.shopLabelIcon.setImageResource(R.drawable.ic_badge_shop_gm)
        view.visibility = View.GONE
    }

    private fun displayOfficial() {
        view.shopLabelIcon.setImageResource(R.drawable.ic_badge_shop_official)
        view.visibility = View.VISIBLE
    }

}