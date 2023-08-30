package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.home.WidgetName.ADD_ONS
import com.tokopedia.shop.home.WidgetName.ADVANCED_SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.BANNER_PRODUCT_HOTSPOT
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.DIRECT_PURCHASED_BY_ETALASE
import com.tokopedia.shop.home.WidgetName.SHOWCASE_NAVIGATION_BANNER
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.BANNER_PRODUCT_GROUP
import com.tokopedia.shop.home.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.home.WidgetName.INFO_CARD
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.PRODUCT_BUNDLE_MULTIPLE
import com.tokopedia.shop.home.WidgetName.PRODUCT_BUNDLE_SINGLE
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
import com.tokopedia.shop.home.WidgetName.TERLARIS
import com.tokopedia.shop.home.WidgetName.TRENDING
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.util.ShopHomeShowcaseNavigationDependencyProvider
import com.tokopedia.shop.home.util.ShopBannerProductGroupWidgetTabDependencyProvider
import com.tokopedia.shop.home.view.adapter.viewholder.CarouselPlayWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ProductGridListPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopCarouselProductWidgetPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCardDonationViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductPersonalizationViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeAdvanceCarouselBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarousellProductViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerProductHotspotViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeMultipleImageColumnPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeMultipleImageColumnViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeNplCampaignPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeNplCampaignViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductBundleParentWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductChangeGridSectionViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductEtalaseTitleViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductItemBigGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductItemListViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListEmptyViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyListener
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListBaseWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderSquarePlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderSquareViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVideoViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopLayoutLoadingShimmerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePersoProductComparisonViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePersoProductComparisonPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerTimerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerTimerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.ShopHomeBannerProductGroupViewPagerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.horizontal.ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.vertical.ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel.ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel.ShopHomeShowCaseNavigationCarouselViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left.ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.listener.*
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.ProductGridListPlaceholderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductListEmptyUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left.ShopHomeShowCaseNavigationLeftMainBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top.ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top.ShopHomeShowCaseNavigationTopMainBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase.ShopHomeDirectPurchasedByEtalaseViewHolder
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop_widget.thematicwidget.typefactory.ThematicWidgetTypeFactory
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetLoadingStateViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance

open class ShopHomeAdapterTypeFactory(
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
    private val shopHomePlayWidgetListener: ShopHomePlayWidgetListener,
    private val shopHomeCardDonationListener: ShopHomeCardDonationListener,
    private val multipleProductBundleListener: MultipleProductBundleListener,
    private val singleProductBundleListener: SingleProductBundleListener,
    private val thematicWidgetListener: ThematicWidgetViewHolder.ThematicWidgetListener,
    private val shopHomeProductListSellerEmptyListener: ShopHomeProductListSellerEmptyListener,
    private val shopHomeListener: ShopHomeListener,
    private val shopPersoProductComparisonListener: ShopHomePersoProductComparisonViewHolder.ShopHomePersoProductComparisonViewHolderListener,
    private val shopHomeDisplayBannerTimerWidgetListener: ShopHomeDisplayBannerTimerWidgetListener,
    private val shopHomeShowcaseNavigationListener: ShopHomeShowcaseNavigationListener,
    private val shopHomeShowcaseNavigationDependencyProvider: ShopHomeShowcaseNavigationDependencyProvider,
    private val shopHomeDisplayBannerProductHotspotListener: ShopHomeDisplayBannerProductHotspotViewHolder.Listener,
    private val shopHomeV4TerlarisViewHolderListener: ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener,
    private val shopBannerProductGroupListener: ShopBannerProductGroupListener,
    private val shopBannerProductGroupWidgetTabDependencyProvider: ShopBannerProductGroupWidgetTabDependencyProvider
) : BaseAdapterTypeFactory(), TypeFactoryShopHome, ThematicWidgetTypeFactory, ShopWidgetTypeFactory {
    var productCardType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    private var showcaseWidgetLayoutType = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_LINEAR_HORIZONTAL
    private var showcaseWidgetGridColumnSize = ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT_TYPE_GRID_DEFAULT_COLUMN_SIZE

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> getShopHomeMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_SQUARE_BANNER -> getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_BANNER -> getShopHomeSliderBannerViewHolder(baseShopHomeWidgetUiModel)
            ADVANCED_SLIDER_BANNER -> ShopHomeAdvanceCarouselBannerViewHolder.LAYOUT_RES
            VIDEO -> ShopHomeVideoViewHolder.LAYOUT_RES
            PRODUCT -> getShopHomeCarousellProductViewHolder(baseShopHomeWidgetUiModel)
            VOUCHER_STATIC -> ShopHomeVoucherViewHolder.LAYOUT
            SHOWCASE_NAVIGATION_BANNER -> determineShowcaseNavigationBannerWidgetAppearance(baseShopHomeWidgetUiModel)
            BANNER_PRODUCT_GROUP -> determineBannerProductGroupWidgetAppearance(baseShopHomeWidgetUiModel)
            RECENT_ACTIVITY, BUY_AGAIN, REMINDER, ADD_ONS, TRENDING -> getShopHomeCarouselProductPersonalizationViewHolder(baseShopHomeWidgetUiModel)
            NEW_PRODUCT_LAUNCH_CAMPAIGN -> getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel)
            FLASH_SALE_TOKO -> getShopFlashSaleViewHolder(baseShopHomeWidgetUiModel)
            PLAY_CAROUSEL_WIDGET -> CarouselPlayWidgetViewHolder.LAYOUT
            INFO_CARD -> ShopHomeCardDonationViewHolder.LAYOUT
            PRODUCT_BUNDLE_SINGLE, PRODUCT_BUNDLE_MULTIPLE -> ShopHomeProductBundleParentWidgetViewHolder.LAYOUT
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
            PERSO_PRODUCT_COMPARISON -> {
                getPersoProductComparisonViewHolder(baseShopHomeWidgetUiModel)
            }
            BANNER_TIMER -> {
                getShopHomeDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel)
            }
            BANNER_PRODUCT_HOTSPOT -> {
                getShopHomeDisplayBannerProductHotspotViewHolder()
            }
            // New widget for Shop Page Revamp V4
            TERLARIS -> getTerlarisViewHolder(baseShopHomeWidgetUiModel)
            DIRECT_PURCHASED_BY_ETALASE -> ShopHomeDirectPurchasedByEtalaseViewHolder.LAYOUT

            else -> HideViewHolder.LAYOUT
        }
    }

    private fun getShopHomeDisplayBannerProductHotspotViewHolder(): Int {
        return ShopHomeDisplayBannerProductHotspotViewHolder.LAYOUT
    }

    open fun getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomeNplCampaignPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomeNplCampaignViewHolder.LAYOUT
        }
    }

    private fun getShopHomeDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomeDisplayBannerTimerPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomeDisplayBannerTimerViewHolder.LAYOUT
        }
    }

    private fun getShopFlashSaleViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomeFlashSaleViewHolder.LAYOUT
        }
    }

    private fun getShopHomeCarouselProductPersonalizationViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT
        }
    }

    private fun getShopHomeCarousellProductViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomeCarousellProductViewHolder.LAYOUT
        }
    }

    private fun getShopHomeSliderBannerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeSliderBannerPlaceholderViewHolder.LAYOUT_RES
        else
            ShopHomeSliderBannerViewHolder.LAYOUT_RES
    }

    private fun getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeSliderSquarePlaceholderViewHolder.LAYOUT_RES
        else
            ShopHomeSliderSquareViewHolder.LAYOUT_RES
    }

    private fun getShopHomeMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomeMultipleImageColumnPlaceholderViewHolder.LAYOUT_RES
        } else {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
        }
    }

    private fun getPersoProductComparisonViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomePersoProductComparisonPlaceholderViewHolder.LAYOUT
        } else {
            ShopHomePersoProductComparisonViewHolder.LAYOUT
        }
    }

    private fun getTerlarisViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowTerlarisWidgetPlaceholder(baseShopHomeWidgetUiModel)) {
            ShopHomeV4TerlarisPlaceholderViewHolder.LAYOUT
        } else {
             ShopHomeV4TerlarisViewHolder.LAYOUT
        }
    }

    private fun isShowTerlarisWidgetPlaceholder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    fun isShowHomeWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    private fun determineShowcaseNavigationBannerWidgetAppearance(model: BaseShopHomeWidgetUiModel): Int {
        val uiModel = (model as? ShowcaseNavigationUiModel)  ?: ShowcaseNavigationUiModel()
        val isLoading = uiModel.isWidgetShowPlaceholder().orFalse()

        return when {
            isLoading && uiModel.appearance is CarouselAppearance -> ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder.LAYOUT
            isLoading && uiModel.appearance is LeftMainBannerAppearance -> ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder.LAYOUT
            isLoading && uiModel.appearance is TopMainBannerAppearance -> ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder.LAYOUT
            !isLoading && uiModel.appearance is CarouselAppearance -> ShopHomeShowCaseNavigationCarouselViewHolder.LAYOUT
            !isLoading && uiModel.appearance is LeftMainBannerAppearance -> ShopHomeShowCaseNavigationLeftMainBannerViewHolder.LAYOUT
            !isLoading && uiModel.appearance is TopMainBannerAppearance -> ShopHomeShowCaseNavigationTopMainBannerViewHolder.LAYOUT
            else -> ShopHomeShowCaseNavigationTopMainBannerViewHolder.LAYOUT
        }
    }

    private fun determineBannerProductGroupWidgetAppearance(model: BaseShopHomeWidgetUiModel): Int {
        val uiModel = (model as? ShopWidgetComponentBannerProductGroupUiModel)  ?: ShopWidgetComponentBannerProductGroupUiModel()
        val isVerticalBanner = uiModel.widgetStyle == ShopWidgetComponentBannerProductGroupUiModel.WidgetStyle.VERTICAL.id
        val isLoading = uiModel.isWidgetShowPlaceholder().orFalse()

        return when {
            isLoading && isVerticalBanner -> ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder.LAYOUT
            isLoading && !isVerticalBanner -> ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder.LAYOUT
            else -> ShopHomeBannerProductGroupViewPagerViewHolder.LAYOUT
        }
    }


    fun isShowThematicWidgetPlaceHolder(model: ThematicWidgetUiModel): Boolean {
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
        return if (isShowThematicWidgetPlaceHolder(uiModel)) {
            ThematicWidgetLoadingStateViewHolder.LAYOUT
        } else {
            ThematicWidgetViewHolder.LAYOUT
        }
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when (productCardType) {
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

    fun type(shopHomeProductListEmptyUiModel: ShopHomeProductListEmptyUiModel): Int {
        return if (shopHomeProductListEmptyUiModel.isOwner) {
            ShopHomeProductListSellerEmptyViewHolder.LAYOUT
        } else {
            ShopHomeProductListEmptyViewHolder.LAYOUT
        }
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopLayoutLoadingShimmerViewHolder.LAYOUT
    }

    fun type(shopHomeProductChangeGridSectionUiModel: ShopHomeProductChangeGridSectionUiModel): Int {
        return ShopHomeProductChangeGridSectionViewHolder.LAYOUT
    }

    fun type(model: ProductGridListPlaceholderUiModel): Int {
        return ProductGridListPlaceholderViewHolder.LAYOUT
    }

    fun type(model: ShopHomeCardDonationUiModel): Int =
        ShopHomeCardDonationViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder = when (type) {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnViewHolder(
                parent,
                listener
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                parent,
                listener
            )
            ShopHomeSliderBannerViewHolder.LAYOUT_RES -> ShopHomeSliderBannerViewHolder(
                parent,
                listener
            )
            ShopHomeVideoViewHolder.LAYOUT_RES -> ShopHomeVideoViewHolder(
                parent,
                listener
            )
            ShopHomeProductViewHolder.LAYOUT -> {
                ShopHomeProductViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot, shopHomeListener)
            }
            ShopHomeProductListEmptyViewHolder.LAYOUT -> {
                ShopHomeProductListEmptyViewHolder(parent, shopHomeListener)
            }
            ShopHomeProductListSellerEmptyViewHolder.LAYOUT -> {
                ShopHomeProductListSellerEmptyViewHolder(parent, shopHomeProductListSellerEmptyListener)
            }
            ShopHomeProductItemBigGridViewHolder.LAYOUT -> {
                ShopHomeProductItemBigGridViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot, shopHomeListener)
            }
            ShopHomeProductItemListViewHolder.LAYOUT -> {
                ShopHomeProductItemListViewHolder(parent, shopHomeEndlessProductListener, isShowTripleDot, shopHomeListener)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent, shopHomeListener)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent, shopHomeCarouselProductListener, shopHomeListener)
            }
            ShopHomeVoucherViewHolder.LAYOUT -> {
                ShopHomeVoucherViewHolder(parent, onMerchantVoucherListWidgetListener, shopHomeListener)
            }
            ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder(parent)
            }
            ShopHomeShowCaseNavigationLeftMainBannerViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationLeftMainBannerViewHolder(
                    parent,
                    shopHomeShowcaseNavigationListener,
                    shopHomeShowcaseNavigationDependencyProvider
                )
            }
            ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder(parent)
            }
            ShopHomeShowCaseNavigationTopMainBannerViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationTopMainBannerViewHolder(parent, shopHomeShowcaseNavigationListener)
            }
            ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder(parent)
            }
            ShopHomeShowCaseNavigationCarouselViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationCarouselViewHolder(parent, shopHomeShowcaseNavigationListener)
            }
            ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder.LAYOUT -> {
                ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder(parent)
            }
            ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder.LAYOUT -> {
                ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder(parent)
            }
            ShopHomeBannerProductGroupViewPagerViewHolder.LAYOUT -> {
                ShopHomeBannerProductGroupViewPagerViewHolder(parent, shopBannerProductGroupListener, shopBannerProductGroupWidgetTabDependencyProvider)
            }
            ShopLayoutLoadingShimmerViewHolder.LAYOUT -> {
                ShopLayoutLoadingShimmerViewHolder(parent)
            }
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)
            ShopHomeNplCampaignViewHolder.LAYOUT -> {
                ShopHomeNplCampaignViewHolder(parent, shopHomeCampaignNplWidgetListener)
            }
            ShopHomeFlashSaleViewHolder.LAYOUT -> return ShopHomeFlashSaleViewHolder(
                parent,
                shopHomeFlashSaleWidgetListener
            )
            ShopHomeProductChangeGridSectionViewHolder.LAYOUT -> ShopHomeProductChangeGridSectionViewHolder(parent, shopProductChangeGridSectionListener, shopHomeListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> CarouselPlayWidgetViewHolder(
                PlayWidgetViewHolder(parent, playWidgetCoordinator),
                shopHomePlayWidgetListener
            )
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT -> ShopHomeCarouselProductPersonalizationViewHolder(parent, shopHomeCarouselProductListener, shopHomeListener)
            ShopHomeProductBundleParentWidgetViewHolder.LAYOUT -> ShopHomeProductBundleParentWidgetViewHolder(parent, multipleProductBundleListener, singleProductBundleListener, shopHomeListener)
            ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT -> ShopHomeShowcaseListBaseWidgetViewHolder(
                parent,
                ShopHomeShowcaseListWidgetAdapter(showcaseListWidgetListener = shopHomeShowcaseListWidgetListener),
                showcaseWidgetLayoutType,
                showcaseWidgetGridColumnSize
            )
            ProductGridListPlaceholderViewHolder.LAYOUT -> ProductGridListPlaceholderViewHolder(parent)
            ShopHomeNplCampaignPlaceholderViewHolder.LAYOUT -> ShopHomeNplCampaignPlaceholderViewHolder(parent)
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT -> ShopCarouselProductWidgetPlaceholderViewHolder(parent)
            ShopHomeSliderBannerPlaceholderViewHolder.LAYOUT_RES -> ShopHomeSliderBannerPlaceholderViewHolder(parent)
            ShopHomeSliderSquarePlaceholderViewHolder.LAYOUT_RES -> ShopHomeSliderSquarePlaceholderViewHolder(parent)
            ShopHomeMultipleImageColumnPlaceholderViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnPlaceholderViewHolder(parent)
            ShopHomeCardDonationViewHolder.LAYOUT -> ShopHomeCardDonationViewHolder(
                parent,
                shopHomeCardDonationListener
            )
            ThematicWidgetViewHolder.LAYOUT -> ThematicWidgetViewHolder(parent, thematicWidgetListener, shopHomeListener.isOverrideTheme())
            ThematicWidgetLoadingStateViewHolder.LAYOUT -> ThematicWidgetLoadingStateViewHolder(parent)
            ShopHomePersoProductComparisonPlaceholderViewHolder.LAYOUT -> ShopHomePersoProductComparisonPlaceholderViewHolder(parent)
            ShopHomePersoProductComparisonViewHolder.LAYOUT -> ShopHomePersoProductComparisonViewHolder(parent, shopPersoProductComparisonListener, shopHomeListener)
            ShopHomeDisplayBannerTimerPlaceholderViewHolder.LAYOUT -> ShopHomeDisplayBannerTimerPlaceholderViewHolder(parent)
            ShopHomeDisplayBannerTimerViewHolder.LAYOUT -> ShopHomeDisplayBannerTimerViewHolder(
                parent,
                shopHomeDisplayBannerTimerWidgetListener
            )
            ShopHomeDisplayBannerProductHotspotViewHolder.LAYOUT -> ShopHomeDisplayBannerProductHotspotViewHolder(
                parent,
                shopHomeDisplayBannerProductHotspotListener
            )
            ShopHomeAdvanceCarouselBannerViewHolder.LAYOUT_RES -> ShopHomeAdvanceCarouselBannerViewHolder(
                parent,
                listener
            )
            // ========= Shop Home Revamp V4 - New widgets ========= //
            ShopHomeV4TerlarisPlaceholderViewHolder.LAYOUT -> ShopHomeV4TerlarisPlaceholderViewHolder(parent)
            ShopHomeV4TerlarisViewHolder.LAYOUT -> ShopHomeV4TerlarisViewHolder(parent, shopHomeV4TerlarisViewHolderListener)
            ShopHomeDirectPurchasedByEtalaseViewHolder.LAYOUT -> ShopHomeDirectPurchasedByEtalaseViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}
