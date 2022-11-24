package com.tokopedia.shop.campaign.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.campaign.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.campaign.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.campaign.WidgetName.PRODUCT_BUNDLE_MULTIPLE
import com.tokopedia.shop.campaign.WidgetName.PRODUCT_BUNDLE_SINGLE
import com.tokopedia.shop.campaign.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignFlashSaleViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignNplPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignNplViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignProductBundleParentWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignThematicWidgetLoadingStateViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignThematicWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.WidgetConfigListener
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyListener
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.listener.*
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
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
    private val shopHomeProductListSellerEmptyListener: ShopHomeProductListSellerEmptyListener,
    private val widgetConfigListener: WidgetConfigListener,
    private val bundlingParentListener: ShopCampaignProductBundleParentWidgetViewHolder.Listener,
    private val shopHomeListener: ShopHomeListener
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
    shopHomeProductListSellerEmptyListener,
    shopHomeListener
) {

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            VOUCHER_STATIC -> ShopCampaignVoucherViewHolder.LAYOUT
            FLASH_SALE_TOKO -> ShopCampaignFlashSaleViewHolder.LAYOUT
            NEW_PRODUCT_LAUNCH_CAMPAIGN -> getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel)
            PRODUCT_BUNDLE_SINGLE, PRODUCT_BUNDLE_MULTIPLE -> ShopCampaignProductBundleParentWidgetViewHolder.LAYOUT
            else -> HideViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder = when (type) {
            ShopCampaignVoucherViewHolder.LAYOUT -> ShopCampaignVoucherViewHolder(parent, onMerchantVoucherListWidgetListener)
            ShopCampaignNplPlaceholderViewHolder.LAYOUT -> ShopCampaignNplPlaceholderViewHolder(parent)
            ShopCampaignThematicWidgetLoadingStateViewHolder.LAYOUT -> ShopCampaignThematicWidgetLoadingStateViewHolder(parent)
            ShopCampaignThematicWidgetViewHolder.LAYOUT -> ShopCampaignThematicWidgetViewHolder(
                parent,
                thematicWidgetListener,
                widgetConfigListener
            )
            ShopCampaignNplViewHolder.LAYOUT -> ShopCampaignNplViewHolder(
                parent,
                shopHomeCampaignNplWidgetListener,
                widgetConfigListener
            )
            ShopCampaignFlashSaleViewHolder.LAYOUT -> ShopCampaignFlashSaleViewHolder(
                parent,
                shopHomeFlashSaleWidgetListener,
                widgetConfigListener
            )
            ShopCampaignProductBundleParentWidgetViewHolder.LAYOUT -> ShopCampaignProductBundleParentWidgetViewHolder(
                parent,
                multipleProductBundleListener,
                singleProductBundleListener,
                widgetConfigListener,
                bundlingParentListener
            )
            else -> return super.createViewHolder(parent, type)
        }
        return viewHolder
    }

    override fun getShopHomeNplCampaignViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignNplPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignNplViewHolder.LAYOUT
    }

    override fun type(uiModel: ThematicWidgetUiModel): Int {
        return if (isShowThematicWidgetPlaceHolder(uiModel))
            ShopCampaignThematicWidgetLoadingStateViewHolder.LAYOUT
        else
            ShopCampaignThematicWidgetViewHolder.LAYOUT
    }
}
