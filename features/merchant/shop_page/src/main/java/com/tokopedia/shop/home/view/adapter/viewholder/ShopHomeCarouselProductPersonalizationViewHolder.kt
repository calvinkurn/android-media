package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeProductRecommendationCarouselBinding
import com.tokopedia.shop.home.WidgetName.ADD_ONS
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.WidgetName.TRENDING
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * author by Rafli Syam on 17/02/2021
 */
//need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopHomeCarouselProductPersonalizationViewHolder(
    itemView: View,
    val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_recommendation_carousel
        private const val TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD = 2
    }
    private val viewBinding: ItemShopHomeProductRecommendationCarouselBinding? by viewBinding()
    private var tvCarouselTitle: TextView? = null
    private var tvCarouselSubTitle: TextView? = null
    private var recyclerView: CarouselProductCardView? = null
    private var recyclerViewCarouselSingleOrDoubleProduct: RecyclerView? = null
    private var productCarouselSingleOrDoubleAdapter: ShopHomeCarouselProductAdapter? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeCarousellProductUiModel) {
        setHeaderSection(element)
        // product list
        bindProductCardData(element)
        setWidgetImpressionListener(element)
        configColorTheme(element)
    }

    private fun bindProductCardData(element: ShopHomeCarousellProductUiModel) {
        val carouselProductList = element.productList.map {
            ShopPageHomeMapper.mapToProductCardPersonalizationModel(
                shopHomeProductViewModel = it,
                isHasATC = isHasATC(element),
                isHasOCCButton = (element.name == BUY_AGAIN) || (element.name == REMINDER),
                occButtonText = if (isAtcOcc(element.name)) {
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
                        bindingAdapterPosition,
                        carouselProductCardPosition,
                        element,
                        productItem
                    )
                } else {
                    shopHomeCarouselProductListener.onCarouselPersonalizationProductItemClickAddToCart(
                        bindingAdapterPosition,
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
                        saveScrollPosition()
                        shopHomeCarouselProductListener.onProductAtcDefaultClick(
                            productItem,
                            productItem.minimumOrder,
                            element.name
                        )
                    } else {
                        if (element.name == REMINDER) {
                            shopHomeCarouselProductListener.onCarouselPersonalizationReminderProductItemClickAddToCart(
                                bindingAdapterPosition,
                                carouselProductCardPosition,
                                element,
                                productItem
                            )
                        } else {
                            shopHomeCarouselProductListener.onCarouselPersonalizationProductItemClickAddToCart(
                                bindingAdapterPosition,
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
                    saveScrollPosition()
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
                    saveScrollPosition()
                    val productItem = element.productList.getOrNull(carouselProductCardPosition)
                        ?: return
                    shopHomeCarouselProductListener.onProductAtcVariantClick(productItem)
                }
            }

        val productClickListener = object : CarouselProductCardListener.OnItemClickListener {
            override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                    ?: return
                when (element.name) {
                    REMINDER -> {
                        shopHomeCarouselProductListener.onPersonalizationReminderCarouselProductItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                    TRENDING -> {
                        shopHomeCarouselProductListener.onPersonalizationTrendingCarouselProductItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                    else -> {
                        shopHomeCarouselProductListener.onPersonalizationCarouselProductItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                }
            }
        }

        val productImpressionListener = object : CarouselProductCardListener.OnItemImpressedListener {
            override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                    ?: return
                when (element.name) {
                    REMINDER -> {
                        shopHomeCarouselProductListener.onCarouselProductPersonalizationReminderItemImpression(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                    TRENDING -> {
                        shopHomeCarouselProductListener.onCarouselProductPersonalizationTrendingItemImpression(
                            bindingAdapterPosition,
                            productItem
                        )
                    }
                    else -> {
                        shopHomeCarouselProductListener.onCarouselProductPersonalizationItemImpression(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                }
                if (element.name == RECENT_ACTIVITY || element.name == REMINDER) {
                    if (productCardModel.isButtonAtcShown()) {
                        shopHomeCarouselProductListener.onImpressionProductAtc(
                            productItem,
                            bindingAdapterPosition,
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
                when (carouselProductList.size) {
                    Int.ONE, TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD -> {
                        bindSingleOrDoubleProductCard(
                            element = element,
                            listShopHomeProductUiModel = element.productList,
                            listProductCardModel = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener
                        )
                    }
                    else -> {
                        recyclerView?.show()
                        recyclerViewCarouselSingleOrDoubleProduct?.hide()
                        recyclerView?.bindCarouselProductCardViewGrid(
                            scrollToPosition = getScrollPosition(),
                            productCardModelList = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener
                        )
                    }
                }
            }

            RECENT_ACTIVITY, TRENDING -> {
                when (carouselProductList.size) {
                    Int.ONE, TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD -> {
                        bindSingleOrDoubleProductCard(
                            element = element,
                            listShopHomeProductUiModel = element.productList,
                            listProductCardModel = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener,
                            carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener
                        )
                    }
                    else -> {
                        recyclerView?.show()
                        recyclerViewCarouselSingleOrDoubleProduct?.hide()
                        recyclerView?.bindCarouselProductCardViewGrid(
                            scrollToPosition = getScrollPosition(),
                            productCardModelList = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener,
                            carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener
                        )
                    }
                }
            }

            BUY_AGAIN -> {
                if (carouselProductList.size == Int.ONE) {
                    bindSingleOrDoubleProductCard(
                        element = element,
                        listShopHomeProductUiModel = element.productList,
                        listProductCardModel = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                    )
                } else {
                    recyclerView?.show()
                    recyclerViewCarouselSingleOrDoubleProduct?.hide()
                    recyclerView?.bindCarouselProductCardViewList(
                        productCardModelList = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener
                    )
                }
            }

            REMINDER -> {
                if (carouselProductList.size == Int.ONE) {
                    bindSingleOrDoubleProductCard(
                        element = element,
                        listShopHomeProductUiModel = element.productList,
                        listProductCardModel = carouselProductList,
                        carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                        carouselProductCardOnItemClickListener = productClickListener,
                        carouselProductCardOnItemImpressedListener = productImpressionListener,
                        carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                        carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener
                    )
                } else {
                    recyclerView?.show()
                    recyclerViewCarouselSingleOrDoubleProduct?.hide()
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
        }
    }

    private fun bindSingleOrDoubleProductCard(
        element: ShopHomeCarousellProductUiModel,
        listShopHomeProductUiModel: List<ShopHomeProductUiModel>,
        listProductCardModel: List<ProductCardModel>,
        carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
        carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
        carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
        carouselProductCardOnItemATCNonVariantClickListener: CarouselProductCardListener.OnATCNonVariantClickListener? = null,
        carouselProductCardOnItemAddVariantClickListener: CarouselProductCardListener.OnAddVariantClickListener? = null
    ) {
        recyclerView?.hide()
        recyclerViewCarouselSingleOrDoubleProduct?.show()
        productCarouselSingleOrDoubleAdapter = ShopHomeCarouselProductAdapter(
            ShopHomeCarouselProductAdapterTypeFactory(
                element,
                listProductCardModel,
                carouselProductCardOnItemAddToCartListener,
                carouselProductCardOnItemClickListener,
                carouselProductCardOnItemImpressedListener,
                carouselProductCardOnItemATCNonVariantClickListener,
                carouselProductCardOnItemAddVariantClickListener
            )
        )
        val totalProductSize = listShopHomeProductUiModel.size
        val layoutManager = GridLayoutManager(itemView.context, totalProductSize)
        productCarouselSingleOrDoubleAdapter?.clearAllElements()
        productCarouselSingleOrDoubleAdapter?.addElement(listShopHomeProductUiModel)
        recyclerViewCarouselSingleOrDoubleProduct?.adapter = productCarouselSingleOrDoubleAdapter
        recyclerViewCarouselSingleOrDoubleProduct?.layoutManager = layoutManager
    }

    private fun configColorTheme(element: ShopHomeCarousellProductUiModel) {
        if (element.isFestivity) {
            configFestivity()
        } else {
            if (element.header.isOverrideTheme) {
                configReimaginedColor(element.header.colorSchema)
            } else {
                configDefaultColor()
            }
        }
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        val subTitleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        tvCarouselTitle?.setTextColor(titleColor)
        tvCarouselSubTitle?.setTextColor(subTitleColor)

    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )
        tvCarouselTitle?.setTextColor(festivityTextColor)
        tvCarouselSubTitle?.setTextColor(festivityTextColor)
    }

    private fun configDefaultColor() {
        val defaultTitleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        tvCarouselTitle?.setTextColor(defaultTitleColor)
        tvCarouselSubTitle?.setTextColor(defaultTitleColor)
    }

    private fun setHeaderSection(element: ShopHomeCarousellProductUiModel) {
        tvCarouselTitle?.text = element.header.title
        tvCarouselSubTitle?.shouldShowWithAction(element.header.subtitle.isNotEmpty()) {
            tvCarouselSubTitle?.text = element.header.subtitle
        }
    }

    private fun setWidgetImpressionListener(model: ShopHomeCarousellProductUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            if (model.name == TRENDING) {
                shopHomeCarouselProductListener.onCarouselProductPersonalizationTrendingWidgetImpression()
            } else {
                shopHomeCarouselProductListener.onCarouselProductWidgetImpression(
                    bindingAdapterPosition,
                    model
                )
            }
        }
    }

    private fun initView() {
        tvCarouselTitle = viewBinding?.etalaseHeaderContainer?.tvTitle
        tvCarouselSubTitle = viewBinding?.etalaseHeaderContainer?.textSubTitle
        recyclerView = viewBinding?.rvCarouselRecommendation
        recyclerViewCarouselSingleOrDoubleProduct = viewBinding?.rvCarouselSingleOrDoubleProduct
    }

    private fun isAtcOcc(
        widgetName: String
    ): Boolean = widgetName == BUY_AGAIN

    private fun isHasATC(
        element: ShopHomeCarousellProductUiModel?
    ): Boolean = (element?.header?.isATC == IS_ATC)

    fun saveScrollPosition() {
        shopHomeListener.getWidgetCarouselPositionSavedState().put(
            bindingAdapterPosition,
            recyclerView?.getCurrentPosition().orZero()
        )
    }

    private fun getScrollPosition(): Int {
        return shopHomeListener.getWidgetCarouselPositionSavedState().get(bindingAdapterPosition)
    }
}
