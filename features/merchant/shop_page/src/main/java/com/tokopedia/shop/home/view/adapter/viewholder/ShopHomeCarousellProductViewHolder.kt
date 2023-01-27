package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel.Companion.IS_ATC
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

//need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopHomeCarousellProductViewHolder(
    itemView: View,
    val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel
    }
    private val viewBinding: ItemShopHomeProductCarouselBinding? by viewBinding()
    private var textViewTitle: TextView? = null
    private var textViewCta: TextView? = null
    private var ivBadge: ImageView? = null
    private var etalaseHeaderContainer: View? = null
    private var recyclerView: CarouselProductCardView? = null
    private var recyclerViewForProductLessThanTwo: RecyclerView? = null
    private var shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel? = null
    private var productCarouselLessThanTwoAdapter: ShopHomeCarouselProductAdapter? = null

    init {
        initView()
    }

    private fun initView() {
        textViewTitle = viewBinding?.etalaseHeaderContainer?.tvTitle
        ivBadge = viewBinding?.etalaseHeaderContainer?.imageViewEtalaseBadge
        textViewCta = viewBinding?.etalaseHeaderContainer?.tvSeeAll
        etalaseHeaderContainer = viewBinding?.etalaseHeaderContainer?.root
        recyclerView = viewBinding?.recyclerViewCarousel
        recyclerViewForProductLessThanTwo = viewBinding?.recyclerViewCarouselFroProductLessThanTwo
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
                    if (!isEtalaseCarousel())
                        shopHomeCarouselProductListener.onCtaClicked(shopHomeCarousellProductUiModel)
                    else
                        shopHomeCarouselProductListener.onCarouselProductShowcaseCtaClicked(shopHomeCarousellProductUiModel)
                }
            } else {
                hide()
            }
        }
        setWidgetImpressionListener(shopHomeCarousellProductUiModel)
        checkFestivity(shopHomeCarousellProductUiModel)
    }

    private fun checkFestivity(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel) {
        if (shopHomeCarousellProductUiModel.isFestivity) {
            configFestivity()
        } else {
            configNonFestivity()
        }
    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.shop.common.R.color.dms_shop_festivity_text_color
        )
        textViewTitle?.setTextColor(festivityTextColor)
    }

    private fun configNonFestivity() {
        val defaultTitleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        textViewTitle?.setTextColor(defaultTitleColor)
    }

    private fun setWidgetImpressionListener(model: ShopHomeCarousellProductUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            shopHomeCarouselProductListener.onCarouselProductWidgetImpression(bindingAdapterPosition, model)
        }
    }

    private fun bindShopProductCarousel(shopHomeProductViewModelList: List<ShopHomeProductUiModel>) {
        recyclerView?.isNestedScrollingEnabled = false
        recyclerViewForProductLessThanTwo?.isNestedScrollingEnabled = false
        if (isProductSizeLessThanTwo(shopHomeProductViewModelList)) {
            configRecyclerViewForProductLessThanTwo(shopHomeProductViewModelList)
        } else {
            configRecyclerView(shopHomeProductViewModelList)
        }
    }

    private fun configRecyclerView(shopHomeProductViewModelList: List<ShopHomeProductUiModel>) {
        recyclerView?.show()
        recyclerViewForProductLessThanTwo?.hide()
        recyclerView?.bindCarouselProductCardViewGrid(
            scrollToPosition = getScrollPosition(),
            productCardModelList = shopHomeProductViewModelList.map {
                ShopPageHomeMapper.mapToProductCardModel(
                    isHasAtc(),
                    false,
                    it,
                    false
                )
            },
            carouselProductCardOnItemAddToCartListener = object :
                CarouselProductCardListener.OnItemAddToCartListener {
                override fun onItemAddToCart(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val shopProductViewModel =
                        shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                            ?: return
                    if (shopProductViewModel.isEnableDirectPurchase) {
                        saveScrollPosition()
                        shopHomeCarouselProductListener.onProductAtcDefaultClick(
                            shopProductViewModel,
                            shopProductViewModel.minimumOrder,
                            shopHomeCarousellProductUiModel?.name.orEmpty()
                        )
                    } else {
                        if (!isEtalaseCarousel()) {
                            shopHomeCarouselProductListener.onCarouselProductItemClickAddToCart(
                                bindingAdapterPosition,
                                carouselProductCardPosition,
                                shopHomeCarousellProductUiModel,
                                shopProductViewModel
                            )
                        } else {
                            shopHomeCarouselProductListener.onCarouselProductShowcaseItemClickAddToCart(
                                bindingAdapterPosition,
                                carouselProductCardPosition,
                                shopHomeCarousellProductUiModel,
                                shopProductViewModel
                            )
                        }
                    }
                }
            },
            carouselProductCardOnItemClickListener = object :
                CarouselProductCardListener.OnItemClickListener {
                override fun onItemClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val shopProductViewModel =
                        shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                            ?: return
                    if (!isEtalaseCarousel()) {
                        shopHomeCarouselProductListener.onCarouselProductItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            shopHomeCarousellProductUiModel,
                            shopProductViewModel
                        )
                    } else {
                        shopHomeCarouselProductListener.onCarouselProductShowcaseItemClicked(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            shopHomeCarousellProductUiModel,
                            shopProductViewModel
                        )
                    }
                }
            },
            carouselProductCardOnItemATCNonVariantClickListener = object :
                CarouselProductCardListener.OnATCNonVariantClickListener {
                override fun onATCNonVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                    quantity: Int
                ) {
                    saveScrollPosition()
                    val shopProductViewModel =
                        shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                            ?: return
                    shopHomeCarouselProductListener.onProductAtcNonVariantQuantityEditorChanged(
                        shopProductViewModel,
                        quantity,
                        shopHomeCarousellProductUiModel?.name.orEmpty()
                    )
                }
            },
            carouselProductCardOnItemAddVariantClickListener = object :
                CarouselProductCardListener.OnAddVariantClickListener {
                override fun onAddVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    saveScrollPosition()
                    val shopProductViewModel =
                        shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                            ?: return
                    shopHomeCarouselProductListener.onProductAtcVariantClick(
                        shopProductViewModel
                    )
                }
            },
            carouselProductCardOnItemImpressedListener = object :
                CarouselProductCardListener.OnItemImpressedListener {
                override fun onItemImpressed(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val shopProductViewModel =
                        shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                            ?: return
                    if (!isEtalaseCarousel()) {
                        shopHomeCarouselProductListener.onCarouselProductItemImpression(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            shopHomeCarousellProductUiModel,
                            shopProductViewModel
                        )
                    } else {
                        shopHomeCarouselProductListener.onCarouselProductShowcaseItemImpression(
                            bindingAdapterPosition,
                            carouselProductCardPosition,
                            shopHomeCarousellProductUiModel,
                            shopProductViewModel
                        )
                    }

                    if (productCardModel.isButtonAtcShown()) {
                        shopHomeCarouselProductListener.onImpressionProductAtc(
                            shopProductViewModel,
                            bindingAdapterPosition,
                            shopHomeCarousellProductUiModel?.name.orEmpty()
                        )
                    }
                }

                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return shopHomeProductViewModelList.getOrNull(carouselProductCardPosition)
                }
            }
        )
    }

    private fun configRecyclerViewForProductLessThanTwo(shopHomeProductViewModelList: List<ShopHomeProductUiModel>) {
        recyclerView?.hide()
        recyclerViewForProductLessThanTwo?.show()
        productCarouselLessThanTwoAdapter = ShopHomeCarouselProductAdapter(
            ShopHomeCarouselProductAdapterTypeFactory(
                shopHomeCarousellProductUiModel!!,
                shopHomeCarouselProductListener,
                bindingAdapterPosition
            )
        )
        val totalProductSize = shopHomeProductViewModelList.size
        val layoutManager = GridLayoutManager(itemView.context, totalProductSize)
        productCarouselLessThanTwoAdapter?.clearAllElements()
        productCarouselLessThanTwoAdapter?.addElement(shopHomeProductViewModelList)
        recyclerViewForProductLessThanTwo?.adapter = productCarouselLessThanTwoAdapter
        recyclerViewForProductLessThanTwo?.layoutManager = layoutManager
    }

    private fun isProductSizeLessThanTwo(
        shopHomeProductViewModelList: List<ShopHomeProductUiModel>
    ): Boolean {
        return shopHomeProductViewModelList.size <= 2
    }

    private fun isEtalaseCarousel(): Boolean {
        val etalaseId = shopHomeCarousellProductUiModel?.header?.etalaseId
        return etalaseId?.isNotEmpty() == true && etalaseId != "0"
    }

    private fun isHasAtc(): Boolean {
        return (shopHomeCarousellProductUiModel?.header?.isATC ?: 0) == IS_ATC
    }

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
