package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.util.ShopBannerProductGroupWidgetTabDependencyProvider
import com.tokopedia.shop.home.util.ShopHomeReimagineShowcaseNavigationDependencyProvider
import com.tokopedia.shop.home.view.adapter.viewholder.CarouselPlayWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ProductGridListPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.bmsm.ShopBmsmWidgetPdViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopCarouselProductWidgetPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCardDonationViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductPersonalizationViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarousellProductViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerTimerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerTimerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeMultipleImageColumnPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeMultipleImageColumnViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeNplCampaignPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeNplCampaignViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePersoProductComparisonPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePersoProductComparisonViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductBundleParentWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductChangeGridSectionViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductEtalaseTitleViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductItemBigGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductItemListViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListEmptyViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyListener
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineDisplayBannerProductHotspotViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeReimagineTerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListBaseWidgetViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderSquarePlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderSquareViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVideoViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopLayoutLoadingShimmerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner.ShopHomeDisplayAdvanceCarouselBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner.ShopHomeDisplayAdvanceCarouselBannerWidgetListener
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.ShopHomeBannerProductGroupViewPagerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.horizontal.ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.vertical.ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.bmsm.ShopBmsmWidgetGwpViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.bmsm.ShopBmsmWidgetPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase.ShopHomeDirectPurchaseByEtalaseWidgetListener
import com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase.ShopHomeDirectPurchasedByEtalaseViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel.ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.carousel.ShopHomeShowCaseNavigationCarouselViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left.ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.left.ShopHomeShowCaseNavigationLeftMainBannerViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top.ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation.top.ShopHomeShowCaseNavigationTopMainBannerViewHolder
import com.tokopedia.shop.home.view.listener.ShopBannerProductGroupListener
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeCardDonationListener
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineShowcaseNavigationListener
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.ProductGridListPlaceholderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductListEmptyUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetEventListener
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ThematicWidgetLoadingStateViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ThematicWidgetViewHolder

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
    private val shopHomeReimagineDisplayBannerTimerWidgetListener: ShopHomeReimagineDisplayBannerTimerWidgetListener,
    private val shopHomeReimagineShowcaseNavigationListener: ShopHomeReimagineShowcaseNavigationListener,
    private val shopHomeReimagineShowcaseNavigationDependencyProvider: ShopHomeReimagineShowcaseNavigationDependencyProvider,
    private val shopHomeDisplayBannerProductHotspotListener: ShopHomeReimagineDisplayBannerProductHotspotViewHolder.Listener,
    private val shopHomeReimagineTerlarisViewHolderListener: ShopHomeReimagineTerlarisViewHolder.Listener,
    private val shopBannerProductGroupListener: ShopBannerProductGroupListener,
    private val shopBannerProductGroupWidgetTabDependencyProvider: ShopBannerProductGroupWidgetTabDependencyProvider,
    private val shopHomeDisplayAdvanceCarouselBannerWidgetListener: ShopHomeDisplayAdvanceCarouselBannerWidgetListener,
    private val shopHomeDirectPurchaseByEtalaseWidgetListener: ShopHomeDirectPurchaseByEtalaseWidgetListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener,
    private val bmsmWidgetDependencyProvider: BmsmWidgetDependencyProvider,
    private val bmsmWidgetListener: BmsmWidgetEventListener
) : BaseAdapterTypeFactory(), TypeFactoryShopHome, ShopWidgetTypeFactory {
    var productCardType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            WidgetNameEnum.DISPLAY_SINGLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_DOUBLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_TRIPLE_COLUMN.value -> getShopHomeMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.SLIDER_SQUARE_BANNER.value -> getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.SLIDER_BANNER.value -> getShopHomeSliderBannerViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.ADVANCED_SLIDER_BANNER.value -> ShopHomeDisplayAdvanceCarouselBannerViewHolder.LAYOUT_RES
            WidgetNameEnum.VIDEO.value -> ShopHomeVideoViewHolder.LAYOUT_RES
            WidgetNameEnum.PRODUCT.value -> getShopHomeCarousellProductViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.VOUCHER_STATIC.value -> ShopHomeVoucherViewHolder.LAYOUT
            WidgetNameEnum.SHOWCASE_NAVIGATION_BANNER.value -> determineShowcaseNavigationBannerWidgetAppearance(baseShopHomeWidgetUiModel)
            WidgetNameEnum.BANNER_PRODUCT_GROUP.value -> determineBannerProductGroupWidgetAppearance(baseShopHomeWidgetUiModel)
            WidgetNameEnum.RECENT_ACTIVITY.value,
            WidgetNameEnum.BUY_AGAIN.value,
            WidgetNameEnum.REMINDER.value,
            WidgetNameEnum.ADD_ONS.value,
            WidgetNameEnum.TRENDING.value -> getShopHomeCarouselProductPersonalizationViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.NEW_PRODUCT_LAUNCH_CAMPAIGN.value -> getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.FLASH_SALE_TOKO.value -> getShopFlashSaleViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.PLAY_CAROUSEL_WIDGET.value -> CarouselPlayWidgetViewHolder.LAYOUT
            WidgetNameEnum.INFO_CARD.value -> ShopHomeCardDonationViewHolder.LAYOUT
            WidgetNameEnum.PRODUCT_BUNDLE_SINGLE.value,
            WidgetNameEnum.PRODUCT_BUNDLE_MULTIPLE.value -> ShopHomeProductBundleParentWidgetViewHolder.LAYOUT
            WidgetNameEnum.SHOWCASE_SLIDER_SMALL.value,
            WidgetNameEnum.SHOWCASE_SLIDER_MEDIUM.value,
            WidgetNameEnum.SHOWCASE_SLIDER_TWO_ROWS.value,
            WidgetNameEnum.SHOWCASE_GRID_SMALL.value,
            WidgetNameEnum.SHOWCASE_GRID_MEDIUM.value,
            WidgetNameEnum.SHOWCASE_GRID_BIG.value -> {
                return ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT
            }
            WidgetNameEnum.PERSO_PRODUCT_COMPARISON.value -> {
                getPersoProductComparisonViewHolder(baseShopHomeWidgetUiModel)
            }
            WidgetNameEnum.BANNER_TIMER.value -> {
                getShopHomeDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel)
            }
            WidgetNameEnum.REIMAGINE_BANNER_PRODUCT_HOTSPOT.value -> {
                getShopHomeDisplayBannerProductHotspotViewHolder()
            }
            WidgetNameEnum.PRODUCT_VERTICAL.value -> getTerlarisViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.DIRECT_PURCHASED_BY_ETALASE.value -> ShopHomeDirectPurchasedByEtalaseViewHolder.LAYOUT
            WidgetNameEnum.BMSM_GWP_OFFERING_GROUP.value -> getBmsmGwpWidgetViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.BMSM_PD_OFFERING_GROUP.value -> getBmsmPdWidgetViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.ETALASE_THEMATIC.value,
            WidgetNameEnum.BIG_CAMPAIGN_THEMATIC.value -> getThematicWidgetViewHolder(baseShopHomeWidgetUiModel)
            else -> HideViewHolder.LAYOUT
        }
    }

    private fun getThematicWidgetViewHolder(uiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowThematicWidgetPlaceHolder(uiModel)) {
            ThematicWidgetLoadingStateViewHolder.LAYOUT
        } else {
            ThematicWidgetViewHolder.LAYOUT
        }
    }

    private fun getBmsmGwpWidgetViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopBmsmWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopBmsmWidgetGwpViewHolder.LAYOUT
        }
    }

    private fun getBmsmPdWidgetViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopBmsmWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopBmsmWidgetPdViewHolder.LAYOUT
        }
    }

    private fun getShopHomeDisplayBannerProductHotspotViewHolder(): Int {
        return ShopHomeReimagineDisplayBannerProductHotspotViewHolder.LAYOUT
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
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomeSliderBannerPlaceholderViewHolder.LAYOUT_RES
        } else {
            ShopHomeSliderBannerViewHolder.LAYOUT_RES
        }
    }

    private fun getShopHomeSliderSquareViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopHomeSliderSquarePlaceholderViewHolder.LAYOUT_RES
        } else {
            ShopHomeSliderSquareViewHolder.LAYOUT_RES
        }
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
            ShopHomeReimagineTerlarisViewHolder.LAYOUT
        }
    }

    private fun isShowTerlarisWidgetPlaceholder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    fun isShowHomeWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    private fun determineShowcaseNavigationBannerWidgetAppearance(model: BaseShopHomeWidgetUiModel): Int {
        val uiModel = (model as? ShowcaseNavigationUiModel) ?: ShowcaseNavigationUiModel()
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
        val uiModel = (model as? BannerProductGroupUiModel) ?: BannerProductGroupUiModel()
        val isVerticalBanner = uiModel.widgetStyle == BannerProductGroupUiModel.WidgetStyle.VERTICAL.id
        val isLoading = uiModel.isWidgetShowPlaceholder().orFalse()

        return when {
            isLoading && isVerticalBanner -> ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder.LAYOUT
            isLoading && !isVerticalBanner -> ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder.LAYOUT
            else -> ShopHomeBannerProductGroupViewPagerViewHolder.LAYOUT
        }
    }

    private fun isShowThematicWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
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
                listener,
                recyclerviewPoolListener
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                parent,
                listener,
                recyclerviewPoolListener
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
                ShopHomeCarousellProductViewHolder(parent, shopHomeCarouselProductListener, shopHomeListener, recyclerviewPoolListener)
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
                    shopHomeReimagineShowcaseNavigationListener,
                    shopHomeReimagineShowcaseNavigationDependencyProvider
                )
            }
            ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder(parent)
            }
            ShopHomeShowCaseNavigationTopMainBannerViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationTopMainBannerViewHolder(parent, shopHomeReimagineShowcaseNavigationListener, recyclerviewPoolListener)
            }
            ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder(parent)
            }
            ShopHomeShowCaseNavigationCarouselViewHolder.LAYOUT -> {
                ShopHomeShowCaseNavigationCarouselViewHolder(parent, shopHomeReimagineShowcaseNavigationListener, recyclerviewPoolListener)
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
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(
                parent,
                shopProductEtalaseListViewHolderListener
            )
            ShopHomeNplCampaignViewHolder.LAYOUT -> {
                ShopHomeNplCampaignViewHolder(parent, shopHomeCampaignNplWidgetListener, recyclerviewPoolListener)
            }
            ShopHomeFlashSaleViewHolder.LAYOUT -> return ShopHomeFlashSaleViewHolder(
                parent,
                shopHomeFlashSaleWidgetListener,
                recyclerviewPoolListener
            )
            ShopHomeProductChangeGridSectionViewHolder.LAYOUT -> ShopHomeProductChangeGridSectionViewHolder(parent, shopProductChangeGridSectionListener, shopHomeListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> CarouselPlayWidgetViewHolder(
                parent,
                PlayWidgetViewHolder(parent.findViewById(R.id.play_widget_view), playWidgetCoordinator),
                shopHomePlayWidgetListener
            )
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT -> ShopHomeCarouselProductPersonalizationViewHolder(parent, shopHomeCarouselProductListener, shopHomeListener, recyclerviewPoolListener)
            ShopHomeProductBundleParentWidgetViewHolder.LAYOUT -> ShopHomeProductBundleParentWidgetViewHolder(parent, multipleProductBundleListener, singleProductBundleListener, shopHomeListener)
            ShopHomeShowcaseListBaseWidgetViewHolder.LAYOUT -> ShopHomeShowcaseListBaseWidgetViewHolder(
                parent,
                ShopHomeShowcaseListWidgetAdapter(showcaseListWidgetListener = shopHomeShowcaseListWidgetListener),
                recyclerviewPoolListener
            )
            ProductGridListPlaceholderViewHolder.LAYOUT -> ProductGridListPlaceholderViewHolder(parent)
            ShopHomeNplCampaignPlaceholderViewHolder.LAYOUT -> ShopHomeNplCampaignPlaceholderViewHolder(parent)
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT -> ShopCarouselProductWidgetPlaceholderViewHolder(parent, recyclerviewPoolListener)
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
                shopHomeReimagineDisplayBannerTimerWidgetListener
            )
            ShopHomeReimagineDisplayBannerProductHotspotViewHolder.LAYOUT -> ShopHomeReimagineDisplayBannerProductHotspotViewHolder(
                parent,
                shopHomeDisplayBannerProductHotspotListener,
                recyclerviewPoolListener
            )
            ShopHomeDisplayAdvanceCarouselBannerViewHolder.LAYOUT_RES -> ShopHomeDisplayAdvanceCarouselBannerViewHolder(
                parent,
                shopHomeDisplayAdvanceCarouselBannerWidgetListener,
                recyclerviewPoolListener
            )
            // ========= Shop Home Revamp V4 - New widgets ========= //
            ShopHomeV4TerlarisPlaceholderViewHolder.LAYOUT -> ShopHomeV4TerlarisPlaceholderViewHolder(parent)
            ShopHomeReimagineTerlarisViewHolder.LAYOUT -> ShopHomeReimagineTerlarisViewHolder(parent, shopHomeReimagineTerlarisViewHolderListener)
            ShopHomeDirectPurchasedByEtalaseViewHolder.LAYOUT -> ShopHomeDirectPurchasedByEtalaseViewHolder(
                parent,
                shopHomeListener,
                shopHomeDirectPurchaseByEtalaseWidgetListener
            )
            ShopBmsmWidgetGwpViewHolder.LAYOUT -> ShopBmsmWidgetGwpViewHolder(
                parent,
                provider = bmsmWidgetDependencyProvider,
                listener = bmsmWidgetListener,
                patternColorType = shopHomeListener.getPatternColorType()
            )
            ShopBmsmWidgetPdViewHolder.LAYOUT -> ShopBmsmWidgetPdViewHolder(
                parent,
                provider = bmsmWidgetDependencyProvider,
                listener = bmsmWidgetListener,
                patternColorType = shopHomeListener.getPatternColorType()
            )
            ShopBmsmWidgetPlaceholderViewHolder.LAYOUT -> ShopBmsmWidgetPlaceholderViewHolder(
                parent
            )
            else -> return super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}
