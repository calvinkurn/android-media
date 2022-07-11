package com.tokopedia.favorite.view.adapter.viewholders

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.favorite.R
import com.tokopedia.favorite.view.adapter.TopAdsShopAdapter
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.TopAdsShopUiModel
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_13
import com.tokopedia.topads.sdk.domain.model.ShopProductModel
import com.tokopedia.topads.sdk.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.widget.ShopAdsWithOneProductView

/**
 * @author kulomady on 1/24/17.
 */
class TopAdsShopViewHolder(
    itemView: View,
    private val favoriteClickListener: FavoriteClickListener,
    private val impressionImageLoadedListener: TopAdsShopAdapter.ImpressionImageLoadedListener
) : AbstractViewHolder<TopAdsShopUiModel?>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.favorite_child_favorite_rec_shop
    }

    private var recShopRecyclerView: RecyclerView =
        itemView.findViewById<View>(R.id.rec_shop_recycler_view) as RecyclerView
    private var shopAdsProductView: ShopAdsWithOneProductView =
        itemView.findViewById<View>(R.id.shopAdsProductView) as ShopAdsWithOneProductView
    private val context: Context = itemView.context

    override fun bind(element: TopAdsShopUiModel?) {
        if (element?.adsShopItems?.firstOrNull()?.layout != LAYOUT_13) {
            val topAdsShopAdapter =
                TopAdsShopAdapter(favoriteClickListener, impressionImageLoadedListener)
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recShopRecyclerView.layoutManager = linearLayoutManager
            recShopRecyclerView.setHasFixedSize(true)
            recShopRecyclerView.adapter = topAdsShopAdapter
            element?.adsShopItems?.let { topAdsShopAdapter.setData(it) }
        } else {
            shopAdsProductView.setShopProductModel(
                ShopProductModel(
                    title = "",
                    items = getShopProductItem(element)
                ),
                object : ShopAdsProductListener {
                    override fun onItemImpressed(position: Int) {
                    }

                    override fun onItemClicked(position: Int) {
                    }

                },
                object : FollowButtonClickListener {
                    override fun onItemClicked(shopId: String) {
                        favoriteClickListener.onFavoriteShopClicked(null, null, shopId)
                    }

                }
            )
        }
    }

    private fun getShopProductItem(element: TopAdsShopUiModel?): List<ShopProductModel.ShopProductModelItem> {
        val list = arrayListOf<ShopProductModel.ShopProductModelItem>()
        element?.adsShopItems?.forEachIndexed { index, it ->
            val item = ShopProductModel.ShopProductModelItem(
                imageUrl = it.imageUrl ?: "",
                shopIcon = it.fullEcs ?: "",
                shopName = it.shopName ?: "",
                isOfficial = it.shopIsOfficial,
                isPMPro = it.isPMPro,
                goldShop = if (it.isPowerMerchant) 1 else 0,
                impressHolder = it.imageShop,
                location = it.shopLocation ?: "",
                position = index,
                layoutType = it.layout,
                shopId = it.shopId ?: ""
            )
            list.add(item)
        }
        return list
    }

}
