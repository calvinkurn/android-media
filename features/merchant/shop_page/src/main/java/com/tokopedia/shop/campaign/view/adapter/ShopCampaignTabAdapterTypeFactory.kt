package com.tokopedia.shop.campaign.view.adapter

import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyListener
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.listener.*
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder

class ShopCampaignTabAdapterTypeFactory(
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
    private val shopHomeProductListSellerEmptyListener: ShopHomeProductListSellerEmptyListener
) : ShopHomeAdapterTypeFactory(
    listener,
    onMerchantVoucherListWidgetListener,
    shopHomeEndlessProductListener,
    shopHomeCarouselProductListener,
    shopProductEtalaseListViewHolderListener,
    shopHomeCampaignNplWidgetListener,
    shopHomeFlashSaleWidgetListener,
    shopProductChangeGridSectionListener,
    playWidgetCoordinator,
    isShowTripleDot,
    shopHomeShowcaseListWidgetListener,
    shopHomePlayWidgetListener,
    shopHomeCardDonationListener,
    multipleProductBundleListener,
    singleProductBundleListener,
    thematicWidgetListener,
    shopHomeProductListSellerEmptyListener
) {

}