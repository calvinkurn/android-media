package com.tokopedia.seller.menu.common.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel
import com.tokopedia.seller.menu.common.view.viewholder.*
import com.tokopedia.user.session.UserSessionInterface

class OtherMenuAdapterTypeFactory(
    private val trackingListener: SettingTrackingListener,
    private val shopInfoListener: ShopInfoViewHolder.ShopInfoListener? = null,
    private val shopInfoErrorListener: ShopInfoErrorViewHolder.ShopInfoErrorListener? = null,
    private val sellerMenuTracker: SellerMenuTracker? = null,
    private val userSession: UserSessionInterface? = null
) : BaseAdapterTypeFactory(), OtherMenuTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            DividerViewHolder.THICK_LAYOUT -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_FULL -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_PARTIAL -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_INDENTED -> DividerViewHolder(parent)
            SettingTitleViewHolder.LAYOUT -> SettingTitleViewHolder(parent)
            IndentedSettingTitleViewHolder.LAYOUT -> IndentedSettingTitleViewHolder(parent)
            MenuItemsViewHolder.LAYOUT -> MenuItemsViewHolder(parent, userSession, trackingListener, sellerMenuTracker)
            MenuItemsViewHolder.LAYOUT_NO_ICON -> MenuItemsViewHolder(parent, userSession, trackingListener, sellerMenuTracker)
            SettingTitleMenuViewHolder.LAYOUT -> SettingTitleMenuViewHolder(parent)
            ShopInfoViewHolder.LAYOUT -> ShopInfoViewHolder(parent, shopInfoListener, trackingListener, userSession, sellerMenuTracker)
            ShopInfoLoadingViewHolder.LAYOUT -> ShopInfoLoadingViewHolder(parent)
            ShopInfoErrorViewHolder.LAYOUT -> ShopInfoErrorViewHolder(parent, shopInfoErrorListener)
            ShopOrderViewHolder.LAYOUT -> ShopOrderViewHolder(parent, sellerMenuTracker)
            SellerMenuTitleViewHolder.SECTION_WITH_CTA_LAYOUT -> SellerMenuTitleViewHolder(parent, sellerMenuTracker)
            SellerMenuTitleViewHolder.SECTION_OTHER_LAYOUT -> SellerMenuTitleViewHolder(parent, sellerMenuTracker)
            ShopProductViewHolder.LAYOUT -> ShopProductViewHolder(parent, sellerMenuTracker)
            SellerFeatureViewHolder.LAYOUT -> SellerFeatureViewHolder(parent, sellerMenuTracker)
            SellerSettingsTitleViewHolder.LAYOUT -> SellerSettingsTitleViewHolder(parent)
            LoadingViewholder.LAYOUT -> SellerLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(shopInfoUiModel: ShopInfoUiModel): Int {
        return ShopInfoViewHolder.LAYOUT
    }

    override fun type(shopInfoLoadingUiModel: ShopInfoLoadingUiModel): Int {
        return ShopInfoLoadingViewHolder.LAYOUT
    }

    override fun type(shopInfoErrorUiModel: ShopInfoErrorUiModel): Int {
        return ShopInfoErrorViewHolder.LAYOUT
    }

    override fun type(shopOrderUiModel: ShopOrderUiModel): Int {
        return ShopOrderViewHolder.LAYOUT
    }

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SellerMenuTitleViewHolder.getLayout(sectionTitleUiModel.type)
    }

    override fun type(shopProductUiModel: ShopProductUiModel): Int {
        return ShopProductViewHolder.LAYOUT
    }

    override fun type(sellerFeatureUiModel: SellerFeatureUiModel): Int {
        return SellerFeatureViewHolder.LAYOUT
    }

    override fun type(dividerUiModel: DividerUiModel): Int {
        return DividerViewHolder.getDividerView(dividerUiModel.dividerType)
    }

    override fun type(settingTitleUiModel: SettingTitleUiModel): Int {
        return SettingTitleViewHolder.LAYOUT
    }

    override fun type(sellerSettingsTitleUiModel: SellerSettingsTitleUiModel): Int {
        return SellerSettingsTitleViewHolder.LAYOUT
    }

    override fun type(menuItemUiModel: MenuItemUiModel): Int {
        return MenuItemsViewHolder.getLayoutRes(isNoIcon = menuItemUiModel.isNoIcon)
    }

    override fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int {
        return SettingTitleMenuViewHolder.LAYOUT
    }

    override fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int {
        return IndentedSettingTitleViewHolder.LAYOUT
    }

    override fun type(settingLoadingUiModel: SettingLoadingUiModel): Int {
        return LoadingViewholder.LAYOUT
    }
}