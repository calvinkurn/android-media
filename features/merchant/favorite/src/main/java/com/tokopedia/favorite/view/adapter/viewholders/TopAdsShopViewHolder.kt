package com.tokopedia.favorite.view.adapter.viewholders

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.favorite.R
import com.tokopedia.favorite.view.adapter.TopAdsShopAdapter
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.favorite.view.viewmodel.TopAdsShopUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_13
import com.tokopedia.topads.sdk.domain.model.ShopProductModel
import com.tokopedia.topads.sdk.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.ShopAdsWithOneProductView
import com.tokopedia.unifycomponents.Toaster

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

    private val topAdsUrlHitter by lazy { TopAdsUrlHitter(itemView.context) }

    private var recShopRecyclerView: RecyclerView =
        itemView.findViewById(R.id.rec_shop_recycler_view)

    private var recShopRecyclerViewContainer: RelativeLayout =
        itemView.findViewById(R.id.recShopRecyclerViewContainer)

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
            recShopRecyclerViewContainer.show()
            shopAdsProductView.hide()
        } else {
            shopAdsProductView.setShopProductModel(
                ShopProductModel(
                    title = "",
                    items = getShopProductItem(element)
                ),
                object : ShopAdsProductListener {
                    override fun onItemImpressed(position: Int) {
                        val item = element.adsShopItems?.getOrNull(position)
                        topAdsUrlHitter.hitImpressionUrl(
                            this.javaClass.name,
                            item?.shopImageUrl,
                            item?.shopId,
                            item?.shopName,
                            item?.shopImageUrl
                        )
                    }

                    override fun onItemClicked(position: Int) {
                        RouteManager.route(context, element.adsShopItems?.getOrNull(position)?.applink)
                        recordCLick(element.adsShopItems?.getOrNull(position))
                    }

                },
                object : FollowButtonClickListener {
                    override fun onItemClicked(shopProductModelItem: ShopProductModel.ShopProductModelItem) {
                        recordCLick(element.adsShopItems?.getOrNull(shopProductModelItem.position))
                        if (shopProductModelItem.isFollowed) {
                            Toaster.build(
                                itemView,
                                context.getString(R.string.favorite_error_text_for_followed),
                                Toaster.LENGTH_SHORT,
                                Toaster.TYPE_ERROR
                            ) { }.show()
                        } else {
                            shopProductModelItem.isFollowed = true
                            favoriteClickListener.onFavoriteShopClicked(
                                null,
                                getShopItem(shopProductModelItem)
                            )
                        }

                    }

                }
            )
            recShopRecyclerViewContainer.hide()
            shopAdsProductView.show()
        }
    }

    private fun recordCLick(item: TopAdsShopItem?) {
        topAdsUrlHitter.hitClickUrl(
            this.javaClass.name,
            item?.shopClickUrl,
            item?.shopId,
            item?.shopName,
            item?.shopImageUrl
        )
    }

    private fun getShopItem(shopProductModelItem: ShopProductModel.ShopProductModelItem): TopAdsShopItem {

        return TopAdsShopItem(
            shopId = shopProductModelItem.shopId,
            shopName = shopProductModelItem.shopName,
            shopImageUrl = shopProductModelItem.shopIcon,
            shopLocation = shopProductModelItem.location,
            isFav = shopProductModelItem.isFollowed
        )
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
                goldShop = if (it.isPowerMerchant) Int.ONE else Int.ZERO,
                impressHolder = it.imageShop,
                location = it.shopLocation ?: "",
                position = index,
                layoutType = it.layout,
                shopId = it.shopId ?: "",
                isFollowed = it.isFollowed
            )
            list.add(item)
        }
        return list
    }

}
