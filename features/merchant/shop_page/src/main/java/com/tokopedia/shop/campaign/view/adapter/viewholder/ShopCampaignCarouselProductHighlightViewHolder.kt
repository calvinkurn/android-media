package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignCarouselProductHighlightAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignCarouselProductHighlightAdapterTypeFactory
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignCarouselProductListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignProductCarouselBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

//need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopCampaignCarouselProductHighlightViewHolder(
    itemView: View,
    private val listener: ShopCampaignCarouselProductListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopCampaignWidgetCarouselProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_product_carousel
        private const val TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD = 2
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
    }

    private val viewBinding: ItemShopCampaignProductCarouselBinding? by viewBinding()
    private var headerView: ShopCampaignTabWidgetHeaderView? = null
    private var recyclerView: CarouselProductCardView? = null
    private var recyclerViewForSingleOrDoubleProductCard: RecyclerView? = null
    private var productCarouselSingleOrDoubleAdapter: ShopCampaignCarouselProductHighlightAdapter? = null
    private var carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? =
        null
    private var carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? =
        null

    init {
        initView()
    }

    private fun initView() {
        recyclerView = viewBinding?.recyclerViewCarousel
        recyclerViewForSingleOrDoubleProductCard =
            viewBinding?.recyclerViewCarouselSingleOrDoubleProductCard
        headerView = viewBinding?.headerView
    }

    override fun bind(uiModel: ShopCampaignWidgetCarouselProductUiModel) {
        setHeader(uiModel)
        setProductData(uiModel)
        setWidgetImpressionListener(uiModel)
    }

    private fun setProductData(uiModel: ShopCampaignWidgetCarouselProductUiModel) {
        recyclerView?.isNestedScrollingEnabled = false
        recyclerViewForSingleOrDoubleProductCard?.isNestedScrollingEnabled = false
        initProductCardListener(uiModel)
        val listProductCardModel = uiModel.productList.map {
            val stockBarLabel = it.stockLabel
            var stockBarLabelColor = ""
            if (stockBarLabel.equals(RED_STOCK_BAR_LABEL_MATCH_VALUE, ignoreCase = true)) {
                stockBarLabelColor = ShopUtil.getColorHexString(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN600
                )
            }
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = it,
                widgetName = it.name,
                statusCampaign = uiModel.statusCampaign
            ).copy(
                stockBarLabelColor = stockBarLabelColor
            )
        }
        if (isProductCardSingleOrDouble(uiModel.productList)) {
            configRecyclerViewSingleOrDoubleProductCard(uiModel, listProductCardModel)
        } else {
            configRecyclerView(listProductCardModel)
        }
    }

    private fun setHeader(uiModel: ShopCampaignWidgetCarouselProductUiModel) {
        val title = uiModel.header.title
        val ctaText = uiModel.header.ctaText
        if (title.isEmpty() && ctaText.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.setCta(ctaText) {
                listener.onCtaClicked(uiModel)
            }
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        }
    }

    private fun setWidgetImpressionListener(model: ShopCampaignWidgetCarouselProductUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            listener.onCampaignCarouselProductWidgetImpression(bindingAdapterPosition, model)
        }
    }

    private fun initProductCardListener(uiModel: ShopCampaignWidgetCarouselProductUiModel) {
        carouselProductCardOnItemClickListener = object :
            CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val shopProductViewModel =
                    uiModel.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                listener.onCampaignCarouselProductItemClicked(
                    bindingAdapterPosition,
                    carouselProductCardPosition,
                    uiModel,
                    shopProductViewModel
                )
            }
        }
        carouselProductCardOnItemImpressedListener = object :
            CarouselProductCardListener.OnItemImpressedListener {
            override fun onItemImpressed(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int
            ) {
                val shopProductViewModel =
                    uiModel.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                listener.onCampaignCarouselProductItemImpression(
                    bindingAdapterPosition,
                    carouselProductCardPosition,
                    uiModel,
                    shopProductViewModel
                )
            }

            override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                return uiModel.productList.getOrNull(carouselProductCardPosition)
            }
        }
    }

    private fun configRecyclerView(
        listProductCardModel: List<ProductCardModel>
    ) {
        recyclerView?.show()
        recyclerViewForSingleOrDoubleProductCard?.hide()
        recyclerView?.bindCarouselProductCardViewGrid(
            productCardModelList = listProductCardModel,
            carouselProductCardOnItemClickListener = carouselProductCardOnItemClickListener,
            carouselProductCardOnItemImpressedListener = carouselProductCardOnItemImpressedListener
        )
    }

    private fun configRecyclerViewSingleOrDoubleProductCard(
        uiModel: ShopCampaignWidgetCarouselProductUiModel,
        listProductCardModel: List<ProductCardModel>
    ) {
        uiModel.let {
            recyclerView?.hide()
            recyclerViewForSingleOrDoubleProductCard?.show()
            productCarouselSingleOrDoubleAdapter = ShopCampaignCarouselProductHighlightAdapter(
                ShopCampaignCarouselProductHighlightAdapterTypeFactory(
                    it,
                    listProductCardModel,
                    carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener,
                )
            )
            val totalProductSize = uiModel.productList.size
            val layoutManager = GridLayoutManager(itemView.context, totalProductSize)
            productCarouselSingleOrDoubleAdapter?.clearAllElements()
            productCarouselSingleOrDoubleAdapter?.addElement(uiModel.productList)
            recyclerViewForSingleOrDoubleProductCard?.adapter =
                productCarouselSingleOrDoubleAdapter
            recyclerViewForSingleOrDoubleProductCard?.layoutManager = layoutManager
        }
    }

    private fun isProductCardSingleOrDouble(
        shopHomeProductViewModelList: List<ShopHomeProductUiModel>
    ): Boolean {
        return shopHomeProductViewModelList.size == TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD || shopHomeProductViewModelList.size == Int.ONE
    }

}
