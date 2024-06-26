package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.addHorizontalTrackListener
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeProductRecommendationCarouselBinding
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.util.mapper.TrackShopRecommendationMapper.asProductTrackModel
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber
import com.tokopedia.carouselproductcard.R as carouselproductcardR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * author by Rafli Syam on 17/02/2021
 */
// need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopHomeCarouselProductPersonalizationViewHolder(
    itemView: View,
    val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
    private val shopHomeListener: ShopHomeListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_recommendation_carousel
        private const val TOTAL_PRODUCT_FOR_DOUBLE_PRODUCT_CARD = 2
    }
    private val viewBinding: ItemShopHomeProductRecommendationCarouselBinding? by viewBinding()
    private var tvCarouselTitle: TextView? = null
    private var tvCarouselSubTitle: TextView? = null
    private var carouselProductCardView: CarouselProductCardView? = null
    private var recyclerViewCarouselSingleOrDoubleProduct: RecyclerView? = null
    private var productCarouselSingleOrDoubleAdapter: ShopHomeCarouselProductAdapter? = null
    private var hasApplogScrollListener = false
    private val snapHelper = StartSnapHelper()

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
        carouselProductCardView?.findViewById<RecyclerView>(carouselproductcardR.id.carouselProductCardRecyclerView)?.isNestedScrollingEnabled = false
        recyclerViewCarouselSingleOrDoubleProduct?.isNestedScrollingEnabled = false
        val carouselProductList = element.productList.map {
            ShopPageHomeMapper.mapToProductCardPersonalizationModel(
                shopHomeProductViewModel = it,
                isHasATC = isHasATC(element),
                isHasOCCButton = (element.name == WidgetNameEnum.BUY_AGAIN.value) || (element.name == WidgetNameEnum.REMINDER.value),
                occButtonText = if (isAtcOcc(element.name)) {
                    itemView.context.getString(R.string.occ_text)
                } else {
                    ""
                },
                element.name,
                isOverrideTheme = shopHomeListener.isOverrideTheme(),
                patternColorType = shopHomeListener.getPatternColorType(),
                backgroundColor = shopHomeListener.getBackgroundColor(),
                isFestivity = element.isFestivity,
                atcButtonText = carouselProductCardView?.context?.getString(R.string.shop_atc).orEmpty()
            )
        }

        // listeners
        val productAddToCartListener = object : CarouselProductCardListener.OnItemAddToCartListener {
            override fun onItemAddToCart(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                    ?: return
                if (element.name == WidgetNameEnum.REMINDER.value) {
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
                        saveScrollPosition(element)
                        shopHomeCarouselProductListener.onProductAtcDefaultClick(
                            productItem,
                            productItem.minimumOrder,
                            element.name
                        )
                    } else {
                        if (element.name == WidgetNameEnum.REMINDER.value) {
                            saveScrollPosition(element)
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
                    saveScrollPosition(element)
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
                    saveScrollPosition(element)
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
                    WidgetNameEnum.REMINDER.value -> {
                        shopHomeCarouselProductListener.onPersonalizationReminderCarouselProductItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                    WidgetNameEnum.TRENDING.value -> {
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
                AppLogRecommendation.sendProductClickAppLog(
                    productItem.asProductTrackModel(
                        carouselProductCardPosition
                    )
                )
            }
        }

        val productImpressionListener = object : CarouselProductCardListener.OnItemImpressedListener {
            override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                val productItem = element.productList.getOrNull(carouselProductCardPosition)
                    ?: return
                when (element.name) {
                    WidgetNameEnum.REMINDER.value -> {
                        shopHomeCarouselProductListener.onCarouselProductPersonalizationReminderItemImpression(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            element,
                            productItem
                        )
                    }
                    WidgetNameEnum.TRENDING.value -> {
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
                if (element.name == WidgetNameEnum.RECENT_ACTIVITY.value || element.name == WidgetNameEnum.REMINDER.value) {
                    if (productCardModel.isButtonAtcShown()) {
                        shopHomeCarouselProductListener.onImpressionProductAtc(
                            productItem,
                            bindingAdapterPosition,
                            element.name
                        )
                    }
                }
                AppLogRecommendation.sendProductShowAppLog(
                    productItem.asProductTrackModel(
                        carouselProductCardPosition
                    )
                )
            }

            override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                return element.productList.getOrNull(carouselProductCardPosition)
            }
        }
        carouselProductCardView?.isNestedScrollingEnabled = false
        when (element.name) {
            WidgetNameEnum.ADD_ONS.value -> {
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
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener,
                            productCardType = ShopHomeCarouselProductAdapterTypeFactory.ProductCardType.GRID
                        )
                        recyclerViewCarouselSingleOrDoubleProduct?.trackHorizontalScroll(element)
                    }
                    else -> {
                        carouselProductCardView?.show()
                        recyclerViewCarouselSingleOrDoubleProduct?.hide()
                        carouselProductCardView?.bindCarouselProductCardViewGrid(
                            scrollToPosition = getScrollPosition(),
                            productCardModelList = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener,
                            carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener,
                            customItemDecoration = itemSpacingDecorator
                        )
                        carouselProductCardView?.trackHorizontalScroll(element)
                    }
                }
            }

            WidgetNameEnum.RECENT_ACTIVITY.value, WidgetNameEnum.TRENDING.value -> {
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
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener,
                            productCardType = ShopHomeCarouselProductAdapterTypeFactory.ProductCardType.GRID
                        )
                    }
                    else -> {
                        carouselProductCardView?.show()
                        recyclerViewCarouselSingleOrDoubleProduct?.hide()
                        carouselProductCardView?.bindCarouselProductCardViewGrid(
                            scrollToPosition = getScrollPosition(),
                            productCardModelList = carouselProductList,
                            carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                            carouselProductCardOnItemClickListener = productClickListener,
                            carouselProductCardOnItemImpressedListener = productImpressionListener,
                            carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                            carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener,
                            customItemDecoration = itemSpacingDecorator
                        )
                        carouselProductCardView?.trackHorizontalScroll(element)
                    }
                }
            }
            WidgetNameEnum.BUY_AGAIN.value, WidgetNameEnum.REMINDER.value -> {
                bindSingleOrDoubleProductCard(
                    scrollToPosition = getScrollPosition(),
                    element = element,
                    listShopHomeProductUiModel = element.productList,
                    listProductCardModel = carouselProductList,
                    carouselProductCardOnItemAddToCartListener = productAddToCartDefaultListener,
                    carouselProductCardOnItemClickListener = productClickListener,
                    carouselProductCardOnItemImpressedListener = productImpressionListener,
                    carouselProductCardOnItemATCNonVariantClickListener = productAddToCartNonVariantListener,
                    carouselProductCardOnItemAddVariantClickListener = productAddToCartVariantListener,
                    productCardType = ShopHomeCarouselProductAdapterTypeFactory.ProductCardType.LIST
                )
                recyclerViewCarouselSingleOrDoubleProduct?.trackHorizontalScroll(element)
            }
        }
    }
    private fun bindSingleOrDoubleProductCard(
        scrollToPosition: Int = 0,
        element: ShopHomeCarousellProductUiModel,
        listShopHomeProductUiModel: List<ShopHomeProductUiModel>,
        listProductCardModel: List<ProductCardModel>,
        carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
        carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
        carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
        carouselProductCardOnItemATCNonVariantClickListener: CarouselProductCardListener.OnATCNonVariantClickListener? = null,
        carouselProductCardOnItemAddVariantClickListener: CarouselProductCardListener.OnAddVariantClickListener? = null,
        productCardType: ShopHomeCarouselProductAdapterTypeFactory.ProductCardType
    ) {
        carouselProductCardView?.hide()
        recyclerViewCarouselSingleOrDoubleProduct?.show()
        productCarouselSingleOrDoubleAdapter = ShopHomeCarouselProductAdapter(
            ShopHomeCarouselProductAdapterTypeFactory(
                shopHomeCarouselProductUiModel = element,
                listProductCardModel = listProductCardModel,
                carouselProductCardOnItemAddToCartListener = carouselProductCardOnItemAddToCartListener,
                carouselProductCardOnItemClickListener = carouselProductCardOnItemClickListener,
                carouselProductCardOnItemImpressedListener = carouselProductCardOnItemImpressedListener,
                carouselProductCardOnItemATCNonVariantClickListener = carouselProductCardOnItemATCNonVariantClickListener,
                carouselProductCardOnItemAddVariantClickListener = carouselProductCardOnItemAddVariantClickListener,
                isOverrideWidgetTheme = element.header.isOverrideTheme,
                productCardType = productCardType
            )
        )
        val totalProductSize = listShopHomeProductUiModel.size
        val layoutManager =
            if (productCardType == ShopHomeCarouselProductAdapterTypeFactory.ProductCardType.GRID) {
                GridLayoutManager(itemView.context, totalProductSize)
            } else {
                LinearLayoutManager(
                    recyclerViewCarouselSingleOrDoubleProduct?.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        productCarouselSingleOrDoubleAdapter?.clearAllElements()
        productCarouselSingleOrDoubleAdapter?.addElement(listShopHomeProductUiModel)
        recyclerViewCarouselSingleOrDoubleProduct?.adapter = productCarouselSingleOrDoubleAdapter
        recyclerViewCarouselSingleOrDoubleProduct?.layoutManager = layoutManager
        recyclerViewCarouselSingleOrDoubleProduct?.layoutManager.apply {
            if (this is LinearLayoutManager) shopHomeListener.getShopPageHomeFragment().context?.applicationContext?.resources?.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16)
                ?.let { scrollToPositionWithOffset(scrollToPosition, it) }
        }
        snapHelper.attachToRecyclerView(recyclerViewCarouselSingleOrDoubleProduct)
        recyclerViewCarouselSingleOrDoubleProduct?.setRecycledViewPool(recyclerviewPoolListener.parentPool)
    }

    private fun RecyclerView.trackHorizontalScroll(
        model: ShopHomeCarousellProductUiModel
    ) {
        if (hasApplogScrollListener) return
        addHorizontalTrackListener(
            slideTrackObject = SlideTrackObject(
                moduleName = model.name,
                barName = model.name,
                shopId = model.shopId
            )
        )
        hasApplogScrollListener = true
    }

    private fun CarouselProductCardView.trackHorizontalScroll(
        model: ShopHomeCarousellProductUiModel
    ) {
        addHorizontalTrackListener(
            SlideTrackObject(
                moduleName = model.name,
                barName = model.name,
                shopId = model.shopId
            )
        )
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
            unifyprinciplesR.color.Unify_Static_White
        )
        tvCarouselTitle?.setTextColor(festivityTextColor)
        tvCarouselSubTitle?.setTextColor(festivityTextColor)
    }

    private fun configDefaultColor() {
        val defaultTitleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950
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
            if (model.name == WidgetNameEnum.TRENDING.value) {
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
        carouselProductCardView = viewBinding?.rvCarouselRecommendation
        recyclerViewCarouselSingleOrDoubleProduct = viewBinding?.rvCarouselSingleOrDoubleProduct
    }

    private fun isAtcOcc(
        widgetName: String
    ): Boolean = widgetName == WidgetNameEnum.BUY_AGAIN.value

    private fun isHasATC(
        element: ShopHomeCarousellProductUiModel?
    ): Boolean = (element?.header?.isATC == IS_ATC)

    fun saveScrollPosition(element: ShopHomeCarousellProductUiModel) {
        val position = if (element.name == WidgetNameEnum.BUY_AGAIN.value || element.name == WidgetNameEnum.REMINDER.value) {
            if (recyclerViewCarouselSingleOrDoubleProduct?.layoutManager is LinearLayoutManager) {
                (recyclerViewCarouselSingleOrDoubleProduct?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            } else {
                Int.ZERO
            }
        } else {
            carouselProductCardView?.getCurrentPosition().orZero()
        }
        shopHomeListener.getWidgetCarouselPositionSavedState().put(
            bindingAdapterPosition,
            position
        )
    }

    private fun getScrollPosition(): Int {
        return shopHomeListener.getWidgetCarouselPositionSavedState().get(bindingAdapterPosition)
    }

    private val itemSpacingDecorator = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition == RecyclerView.NO_POSITION) return

            outRect.left = getLeftOffset()
            outRect.right = getRightOffset()
        }

        private fun getLeftOffset(): Int {
            return marginBetweenCard()
        }

        private fun getRightOffset(): Int {
            return marginBetweenCard()
        }

        private fun marginBetweenCard() = 4.toPx()
    }
}
