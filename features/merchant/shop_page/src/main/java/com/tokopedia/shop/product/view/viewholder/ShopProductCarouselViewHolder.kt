package com.tokopedia.shop.product.view.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.databinding.ItemNewShopProductCarouselBinding
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.EtalaseHighlightCarouselUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductFeaturedUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductCarouselViewHolder(
    itemView: View,
    deviceWidth: Int,
    private val shopProductClickedListener: ShopProductClickedListener?,
    private val shopProductImpressionListener: ShopProductImpressionListener?,
    titleString: String,
    @ShopTrackProductTypeDef private val shopTrackType: Int,
    private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?
) : AbstractViewHolder<Visitable<*>>(itemView) {

    private val viewBinding: ItemNewShopProductCarouselBinding? by viewBinding()
    private var tvTitle: TextView? = null
    private var iconCtaChevron: IconUnify? = null
    private var recyclerView: CarouselProductCardView? = null
    private var ivBadge: ImageView? = null

    init {
        findViews()
        tvTitle!!.text = titleString
    }

    override fun bind(visitable: Visitable<*>) {
        if (visitable is ShopProductFeaturedUiModel) {
            visitable.shopProductFeaturedViewModelList?.let {
                bindShopProductCarousel(it)
            }
            iconCtaChevron?.visibility = View.GONE
        } else if (visitable is EtalaseHighlightCarouselUiModel) {
            visitable.shopProductUiModelList?.let {
                bindShopProductCarousel(it)
            }
            val shopEtalaseViewModel = visitable.shopEtalaseViewModel
            tvTitle!!.text = shopEtalaseViewModel?.etalaseName.orEmpty()
            if (!TextUtils.isEmpty(shopEtalaseViewModel?.etalaseBadge.orEmpty())) {
                ivBadge?.loadIcon(shopEtalaseViewModel?.etalaseBadge)
                ivBadge!!.visibility = View.VISIBLE
            } else {
                ivBadge!!.visibility = View.GONE
            }
            iconCtaChevron?.setOnClickListener {
                shopEtalaseViewModel?.let {
                    shopCarouselSeeAllClickedListener?.onSeeAllClicked(shopEtalaseViewModel)
                }
            }
            iconCtaChevron?.visibility = View.VISIBLE
        }
    }

    private fun bindShopProductCarousel(shopProductUiModelList: List<ShopProductUiModel>) {
        recyclerView?.findViewById<RecyclerView>(com.tokopedia.carouselproductcard.R.id.carouselProductCardRecyclerView)?.isNestedScrollingEnabled = false
        recyclerView?.bindCarouselProductCardViewGrid(
            productCardModelList = shopProductUiModelList.map {
                ShopPageProductListMapper.mapToProductCardModel(it, isWideContent = false, isShowThreeDots = false)
            },
            carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val shopProductViewModel = shopProductUiModelList.getOrNull(carouselProductCardPosition) ?: return

                    shopProductClickedListener?.onProductClicked(shopProductViewModel, shopTrackType, carouselProductCardPosition)
                }
            },
            carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val shopProductViewModel = shopProductUiModelList.getOrNull(carouselProductCardPosition) ?: return

                    shopProductImpressionListener?.onProductImpression(shopProductViewModel, shopTrackType, carouselProductCardPosition)
                }

                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return shopProductUiModelList.getOrNull(carouselProductCardPosition)
                }
            },
            carouselProductCardOnItemThreeDotsClickListener = object : CarouselProductCardListener.OnItemThreeDotsClickListener {
                override fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val shopProductViewModel = shopProductUiModelList.getOrNull(carouselProductCardPosition) ?: return

                    shopProductClickedListener?.onThreeDotsClicked(shopProductViewModel, shopTrackType)
                }
            }
        )
    }

    override fun bind(visitable: Visitable<*>, payloads: MutableList<Any>) {
        if (payloads.getOrNull(0) !is Boolean) return

        val shopProductViewModelList = getShopProductViewModelListFromVisitable(visitable)

        recyclerView?.setCarouselProductCardListeners(
            carouselProductCardOnItemThreeDotsClickListener = object : CarouselProductCardListener.OnItemThreeDotsClickListener {
                override fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val shopProductViewModel = shopProductViewModelList?.getOrNull(carouselProductCardPosition) ?: return

                    shopProductClickedListener?.onThreeDotsClicked(shopProductViewModel, shopTrackType)
                }
            }
        )
    }

    private fun getShopProductViewModelListFromVisitable(visitable: Visitable<*>): List<ShopProductUiModel>? {
        return when (visitable) {
            is ShopProductFeaturedUiModel -> visitable.shopProductFeaturedViewModelList
            is EtalaseHighlightCarouselUiModel -> visitable.shopProductUiModelList
            else -> listOf()
        }
    }

    private fun findViews() {
        tvTitle = viewBinding?.layoutShopProductCarouselTitle?.tvTitle
        ivBadge = viewBinding?.layoutShopProductCarouselTitle?.imageViewEtalaseBadge
        iconCtaChevron = viewBinding?.layoutShopProductCarouselTitle?.iconCtaChevron
        recyclerView = viewBinding?.recyclerViewCarousel
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_carousel
    }
}
