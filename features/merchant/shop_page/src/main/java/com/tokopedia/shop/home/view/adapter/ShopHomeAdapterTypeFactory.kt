package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_BIG
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_MEDIUM
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_SMALL
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_MEDIUM
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_SMALL
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_TWO_ROWS
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.listener.*
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop_widget.thematicwidget.typefactory.ThematicWidgetTypeFactory
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetLoadingStateViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder

class ShopHomeAdapterTypeFactory(
        private val listener: ShopHomeDisplayWidgetListener,
        private val onMerchantVoucherListWidgetListener: ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
        private val shopHomeEndlessProductListener: ShopHomeEndlessProductListener,
        private val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
        private val shopProductEtalaseListViewHolderListener: ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener?,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
        private val shopHomeFlashSaleWidgetListener: ShopHomeFlashSaleWidgetListener,
        private val shopProductChangeGridSectionListener: ShopProductChangeGridSectionListener,
        private val playWidgetCoordinator: PlayWidgetCoordinator,
        private val isShowTripleDot: Boolean,
        private val shopHomeShowcaseListWidgetListener: ShopHomeShowcaseListWidgetListener,
        private val shopHomePlayWidgetListener: ShopHomePlayWidgetListener
) : BaseAdapterTypeFactory(), TypeFactoryShopHome, ThematicWidgetTypeFactory {
    var productCardType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    private var previousViewHolder: AbstractViewHolder<*>? = null
    private var showcaseWidgetLayoutType = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_LINEAR_HORIZONTAL
    private var showcaseWidgetGridColumnSize = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_DEFAULT_COLUMN_SIZE

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> getShopHomeMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_SQUARE_BANNER -> getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_BANNER -> getShopHomeSliderBannerViewHolder(baseShopHomeWidgetUiModel)
            VIDEO -> ShopHomeVideoViewHolder.LAYOUT_RES
            PRODUCT -> getShopHomeCarousellProductViewHolder(baseShopHomeWidgetUiModel)
            VOUCHER_STATIC -> ShopHomeVoucherViewHolder.LAYOUT
            RECENT_ACTIVITY, BUY_AGAIN, REMINDER -> getShopHomeCarouselProductPersonalizationViewHolder(baseShopHomeWidgetUiModel)
            NEW_PRODUCT_LAUNCH_CAMPAIGN -> getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel)
            FLASH_SALE_TOKO -> ShopHomeFlashSaleViewHolder.LAYOUT
            PLAY_CAROUSEL_WIDGET -> CarouselPlayWidgetViewHolder.LAYOUT
            SHOWCASE_SLIDER_SMALL, SHOWCASE_SLIDER_MEDIUM -> return ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT
            SHOWCASE_SLIDER_TWO_ROWS -> {
                showcaseWidgetLayoutType = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_HORIZONTAL
                showcaseWidgetGridColumnSize = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_TWO_COLUMN_SIZE
                return ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT
            }
            SHOWCASE_GRID_SMALL -> {
                showcaseWidgetLayoutType = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_VERTICAL
                showcaseWidgetGridColumnSize = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_THREE_COLUMN_SIZE
                return ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT
            }
            SHOWCASE_GRID_MEDIUM, SHOWCASE_GRID_BIG -> {
                showcaseWidgetLayoutType = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_VERTICAL
                showcaseWidgetGridColumnSize = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_TWO_COLUMN_SIZE
                return ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT
            }
            else -> HideViewHolder.LAYOUT
        }
    }

    private fun getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeNplCampaignPlaceholderViewHolder.LAYOUT
        else
            ShopHomeNplCampaignViewHolder.LAYOUT
    }

    private fun getShopHomeCarouselProductPersonalizationViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeCarousellProductPlaceholderViewHolder.LAYOUT
        else
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT
    }

    private fun getShopHomeCarousellProductViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeCarousellProductPlaceholderViewHolder.LAYOUT
        else
            ShopHomeCarousellProductViewHolder.LAYOUT
    }

    private fun getShopHomeSliderBannerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeSliderBannerPlaceholderViewHolder.LAYOUT_RES
        else
            ShopHomeSliderBannerViewHolder.LAYOUT_RES
    }

    private fun getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeSliderSquarePlaceholderViewHolder.LAYOUT_RES
        else
            ShopHomeSliderSquareViewHolder.LAYOUT_RES
    }

    private fun getShopHomeMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeMultipleImageColumnPlaceholderViewHolder.LAYOUT_RES
        else
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
    }

    private fun isShowHomeWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    private fun isShowThematicWidgetPlaceHolder(model: ThematicWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    override fun type(shopHomeProductEtalaseTitleUiModel: ShopHomeProductEtalaseTitleUiModel): Int {
        return ShopHomeProductEtalaseTitleViewHolder.LAYOUT
    }

    override fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int {
        return ShopProductSortFilterViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardViewModel: CarouselPlayWidgetUiModel): Int {
        return CarouselPlayWidgetViewHolder.LAYOUT
    }

    override fun type(uiModel: ThematicWidgetUiModel): Int {
        return if(isShowThematicWidgetPlaceHolder(uiModel))
            ThematicWidgetLoadingStateViewHolder.LAYOUT
        else
            ThematicWidgetViewHolder.LAYOUT
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when(productCardType) {
            ShopProductViewGridType.SMALL_GRID -> {
                ShopHomeProductViewHolder.LAYOUT
            }
            ShopProductViewGridType.BIG_GRID -> {
                ShopHomeProductItemBigGridViewHolder.LAYOUT
            }
            ShopProductViewGridType.LIST -> {
                ShopHomeProductItemListViewHolder.LAYOUT
            }
        }
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopHomeLoadingShimmerViewHolder.LAYOUT
    }

    fun type(shopHomeProductChangeGridSectionUiModel: ShopHomeProductChangeGridSectionUiModel): Int {
        return ShopHomeProductChangeGridSectionViewHolder.LAYOUT
    }

    fun type(model: ProductGridListPlaceholderUiModel): Int {
        return ProductGridListPlaceholderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder = when (type) {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeSliderBannerViewHolder.LAYOUT_RES -> ShopHomeSliderBannerViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeVideoViewHolder.LAYOUT_RES -> ShopHomeVideoViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeProductViewHolder.LAYOUT -> {
                ShopHomeProductViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot)
            }
            ShopHomeProductItemBigGridViewHolder.LAYOUT -> {
                ShopHomeProductItemBigGridViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot)
            }
            ShopHomeProductItemListViewHolder.LAYOUT -> {
                ShopHomeProductItemListViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent, shopHomeCarouselProductListener)
            }
            ShopHomeVoucherViewHolder.LAYOUT -> {
                ShopHomeVoucherViewHolder(parent, onMerchantVoucherListWidgetListener)
            }
            ShopHomeLoadingShimmerViewHolder.LAYOUT -> {
                ShopHomeLoadingShimmerViewHolder(parent)
            }
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)
            ShopHomeNplCampaignViewHolder.LAYOUT -> {
                ShopHomeNplCampaignViewHolder(parent, shopHomeCampaignNplWidgetListener)
            }
            ShopHomeFlashSaleViewHolder.LAYOUT -> return ShopHomeFlashSaleViewHolder(parent, shopHomeFlashSaleWidgetListener)
            ShopHomeProductChangeGridSectionViewHolder.LAYOUT -> ShopHomeProductChangeGridSectionViewHolder(parent, shopProductChangeGridSectionListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> CarouselPlayWidgetViewHolder(PlayWidgetViewHolder(parent, playWidgetCoordinator), shopHomePlayWidgetListener)
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT -> ShopHomeCarouselProductPersonalizationViewHolder(parent, shopHomeCarouselProductListener)
            ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT -> ShopHomeShowcaseListBaseWidgetViewHolder(
                    parent,
                    ShopHomeShowcaseListWidgetAdapter(showcaseListWidgetListener = shopHomeShowcaseListWidgetListener),
                    showcaseWidgetLayoutType,
                    showcaseWidgetGridColumnSize
            )
            ProductGridListPlaceholderViewHolder.LAYOUT -> ProductGridListPlaceholderViewHolder(parent)
            ShopHomeNplCampaignPlaceholderViewHolder.LAYOUT -> ShopHomeNplCampaignPlaceholderViewHolder(parent)
            ShopHomeCarousellProductPlaceholderViewHolder.LAYOUT -> ShopHomeCarousellProductPlaceholderViewHolder(parent)
            ShopHomeSliderBannerPlaceholderViewHolder.LAYOUT_RES -> ShopHomeSliderBannerPlaceholderViewHolder(parent)
            ShopHomeSliderSquarePlaceholderViewHolder.LAYOUT_RES -> ShopHomeSliderSquarePlaceholderViewHolder(parent)
            ShopHomeMultipleImageColumnPlaceholderViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnPlaceholderViewHolder(parent)
            ThematicWidgetViewHolder.LAYOUT -> ThematicWidgetViewHolder(parent)
            ThematicWidgetLoadingStateViewHolder.LAYOUT -> ThematicWidgetLoadingStateViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
        previousViewHolder = viewHolder
        return viewHolder
    }
}