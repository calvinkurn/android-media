package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopHomeProductRecommendationCarouselBinding
import com.tokopedia.shop.home.WidgetName.ADD_ONS
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.utils.view.binding.viewBinding

/**
 * author by Rafli Syam on 17/02/2021
 */
class ShopHomeCarouselProductPersonalizationViewHolder (
        itemView: View,
        val shopHomeCarouselProductListener: ShopHomeCarouselProductListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_recommendation_carousel
    }
    private val viewBinding: ItemShopHomeProductRecommendationCarouselBinding? by viewBinding()
    private var tvCarouselTitle : TextView? = null
    private var recyclerView : CarouselProductCardView? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeCarousellProductUiModel) {
        tvCarouselTitle?.text = element.header.title

        // product list
        val carouselProductList = element.productList.map {
            ShopPageHomeMapper.mapToProductCardPersonalizationModel(
                    shopHomeProductViewModel = it,
                    isHasATC = isHasATC(element),
                    isHasOCCButton = (element.name == BUY_AGAIN) || (element.name == REMINDER),
                    occButtonText = if(isAtcOcc(element.name)) {
                        itemView.context.getString(R.string.occ_text)
                    } else "",
                    element.name

            )
        }

        // listeners
        val productAddToCartListener = object : CarouselProductCardListener.OnItemAddToCartListener {
            override fun onItemAddToCart(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                if (element.name == REMINDER) {
                    shopHomeCarouselProductListener.onCarouselPersonalizationReminderProductItemClickAddToCart(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                    )
                } else {
                    shopHomeCarouselProductListener.onCarouselPersonalizationProductItemClickAddToCart(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem,
                            isOcc = isAtcOcc(element.name)
                    )
                }
            }
        }

        val productAddToCartDefaultListener =
            object : CarouselProductCardListener.OnItemAddToCartListener {
                override fun onItemAddToCart(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                    if (productItem.isEnableDirectPurchase) {
                        shopHomeCarouselProductListener.onProductAtcDefaultClick(
                            productItem,
                            productItem.minimumOrder,
                            element.name
                        )
                    } else {
                        if (element.name == REMINDER) {
                            shopHomeCarouselProductListener.onCarouselPersonalizationReminderProductItemClickAddToCart(
                                adapterPosition,
                                carouselProductCardPosition,
                                element,
                                productItem
                            )
                        } else {
                            shopHomeCarouselProductListener.onCarouselPersonalizationProductItemClickAddToCart(
                                adapterPosition,
                                carouselProductCardPosition,
                                element,
                                productItem,
                                isOcc = isAtcOcc(element.name)
                            )
                        }
                    }
                }
            }

        val productAddToCartNonVariantListener =
            object : CarouselProductCardListener.OnATCNonVariantClickListener {
                override fun onATCNonVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                    quantity: Int
                ) {
                    val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                    shopHomeCarouselProductListener.onProductAtcNonVariantQuantityEditorChanged(
                        productItem,
                        quantity,
                        element.name
                    )
                }

            }

        val productAddToCartVariantListener =
            object : CarouselProductCardListener.OnAddVariantClickListener {
                override fun onAddVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                    shopHomeCarouselProductListener.onProductAtcVariantClick(productItem)
                }

            }

        val productClickListener = object : CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                if (element.name == REMINDER) {
                    shopHomeCarouselProductListener.onPersonalizationReminderCarouselProductItemClicked(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                    )
                } else {
                    shopHomeCarouselProductListener.onPersonalizationCarouselProductItemClicked(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                    )
                }
            }
        }

        val productImpressionListener = object : CarouselProductCardListener.OnItemImpressedListener {
            override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                if (element.name == REMINDER) {
                    shopHomeCarouselProductListener.onCarouselProductPersonalizationReminderItemImpression(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                    )
                } else {
                    shopHomeCarouselProductListener.onCarouselProductPersonalizationItemImpression(
                            adapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                    )
                }
                if (element.name == RECENT_ACTIVITY || element.name == REMINDER) {
                    if (productCardModel.isButtonAtcShown()) {
                        shopHomeCarouselProductListener.onImpressionProductAtc(
                            productItem,
                            adapterPosition,
                            element.name
                        )
                    }
                }
            }

            override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                return element.productList.getOrNull(carouselProductCardPosition)
            }
        }
        recyclerView?.isNestedScrollingEnabled = false
        when (element.name) {

            ADD_ONS -> {
                recyclerView?.bindCarouselProductCardViewGrid(
                        productCardModelList = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                )
            }

            RECENT_ACTIVITY -> {
                recyclerView?.bindCarouselProductCardViewGrid(
                    productCardModelList = carouselProductList,
                    carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                    carouselProductCardOnItemClickListener = productClickListener,
                    carouselProductCardOnItemImpressedListener = productImpressionListener,
                    carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                    carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener
                )
            }

            BUY_AGAIN -> {
                recyclerView?.bindCarouselProductCardViewList(
                        productCardModelList = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                )
            }

            REMINDER -> {
                recyclerView?.bindCarouselProductCardViewList(
                    productCardModelList = carouselProductList,
                    carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                    carouselProductCardOnItemClickListener = productClickListener,
                    carouselProductCardOnItemImpressedListener = productImpressionListener,
                    carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                    carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener
                )
            }

        }
        setWidgetImpressionListener(element)
    }

    private fun setWidgetImpressionListener(model: ShopHomeCarousellProductUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            shopHomeCarouselProductListener.onCarouselProductWidgetImpression(adapterPosition, model)
        }
    }

    private fun initView() {
        tvCarouselTitle = viewBinding?.etalaseHeaderContainer?.tvTitle
        recyclerView = viewBinding?.rvCarouselRecommendation
    }

    private fun isAtcOcc(
            widgetName: String
    ) : Boolean = widgetName == BUY_AGAIN

    private fun isHasATC(
       element : ShopHomeCarousellProductUiModel?
    ) : Boolean = (element?.header?.isATC == IS_ATC)
}