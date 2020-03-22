package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopHomeCarousellProductViewHolder(
        itemView: View,
        val shopPageHomeProductClickListener: ShopPageHomeProductClickListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel
    }

    private var textViewTitle: TextView? = null
    private var textViewCta: TextView? = null
    private var ivBadge: ImageView? = null
    private var etalaseHeaderContainer: View? = null
    private var recyclerView: CarouselProductCardView? = null
    private var shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel? = null

    init {
        initView(itemView)
    }

    private fun initView(view: View) {
        textViewTitle = view.findViewById(R.id.tv_title)
        ivBadge = view.findViewById(R.id.image_view_etalase_badge)
        textViewCta = view.findViewById(R.id.tvSeeAll)
        etalaseHeaderContainer = view.findViewById(R.id.etalase_header_container)
        recyclerView = view.findViewById(R.id.recyclerViewCarousel)
    }

    override fun bind(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel) {
        this.shopHomeCarousellProductUiModel = shopHomeCarousellProductUiModel
        bindShopProductCarousel(shopHomeCarousellProductUiModel.productList)
        val title = shopHomeCarousellProductUiModel.header.title
        val ctaText = shopHomeCarousellProductUiModel.header.ctaText
        if (title.isEmpty() && ctaText.isEmpty()) {
            etalaseHeaderContainer?.hide()
        }
        ivBadge?.visibility = View.GONE
        textViewTitle?.text = MethodChecker.fromHtml(title)
        textViewCta?.apply {
            if (ctaText.isNotEmpty()) {
                show()
                text = MethodChecker.fromHtml(shopHomeCarousellProductUiModel.header.ctaText)
                setOnClickListener {
                    shopPageHomeProductClickListener.onCtaClicked(shopHomeCarousellProductUiModel)
                }
            } else {
                hide()
            }
        }
    }

    private fun bindShopProductCarousel(shopHomeProductViewModelList: List<ShopHomeProductViewModel>) {
        recyclerView?.bindCarouselProductCardViewGrid(
                productCardModelList = shopHomeProductViewModelList.map {
                    ShopPageHomeMapper.mapToProductCardModel(
                            isHasAtc(),
                            it
                    )
                },
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val shopProductViewModel = shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                                ?: return
                        shopPageHomeProductClickListener.onCarouselProductItemClicked(
                                adapterPosition,
                                carouselProductCardPosition,
                                shopHomeCarousellProductUiModel,
                                shopProductViewModel
                        )
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val shopProductViewModel = shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                                ?: return

                        shopPageHomeProductClickListener.onCarouselProductItemImpression(
                                adapterPosition,
                                carouselProductCardPosition,
                                shopHomeCarousellProductUiModel,
                                shopProductViewModel
                        )
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                    }
                },
                carouselProductCardOnItemThreeDotsClickListener = object : CarouselProductCardListener.OnItemThreeDotsClickListener {
                    override fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val shopProductViewModel = shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                                ?: return

                        shopPageHomeProductClickListener.onThreeDotsCarouselProductItemClicked(
                                adapterPosition,
                                carouselProductCardPosition,
                                shopHomeCarousellProductUiModel,
                                shopProductViewModel
                        )
                    }
                }
        )
    }

    private fun isHasAtc(): Boolean {
        return (shopHomeCarousellProductUiModel?.header?.isATC ?: 0) == IS_ATC
    }
}
