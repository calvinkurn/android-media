package com.tokopedia.seller.menu.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.SectionTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleUiModel
import com.tokopedia.seller.menu.common.view.viewholder.DividerViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.IndentedSettingTitleViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.MenuItemsViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.SellerLoadingViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.SellerMenuTitleViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.SellerSettingsTitleViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.SettingTitleMenuViewHolder
import com.tokopedia.seller.menu.common.view.viewholder.SettingTitleViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.SellerFeatureViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.ShopInfoErrorViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.ShopInfoLoadingViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.ShopInfoViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.ShopOrderViewHolder
import com.tokopedia.seller.menu.presentation.adapter.viewholder.ShopProductViewHolder
import com.tokopedia.user.session.UserSessionInterface

class SellerMenuAdapterTypeFactory(
    private val trackingListener: SettingTrackingListener,
    private val shopInfoListener: ShopInfoViewHolder.ShopInfoListener? = null,
    private val shopInfoErrorListener: ShopInfoErrorViewHolder.ShopInfoErrorListener? = null,
    private val sellerMenuTracker: SellerMenuTracker? = null,
    private val userSession: UserSessionInterface? = null
) : BaseAdapterTypeFactory(), OtherMenuTypeFactory, SellerMenuTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopInfoViewHolder.LAYOUT -> ShopInfoViewHolder(
                parent,
                shopInfoListener,
                trackingListener,
                userSession,
                sellerMenuTracker
            )
            ShopInfoLoadingViewHolder.LAYOUT -> ShopInfoLoadingViewHolder(
                parent
            )
            ShopInfoErrorViewHolder.LAYOUT -> ShopInfoErrorViewHolder(
                parent,
                shopInfoErrorListener
            )
            ShopOrderViewHolder.LAYOUT -> ShopOrderViewHolder(
                parent,
                sellerMenuTracker
            )
            ShopProductViewHolder.LAYOUT -> ShopProductViewHolder(
                parent,
                sellerMenuTracker
            )
            SellerFeatureViewHolder.LAYOUT -> SellerFeatureViewHolder(
                parent,
                sellerMenuTracker
            )
            DividerViewHolder.THICK_LAYOUT -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_FULL -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_PARTIAL -> DividerViewHolder(parent)
            DividerViewHolder.THIN_LAYOUT_INDENTED -> DividerViewHolder(parent)
            SettingTitleViewHolder.LAYOUT -> SettingTitleViewHolder(parent)
            IndentedSettingTitleViewHolder.LAYOUT -> IndentedSettingTitleViewHolder(parent)
            MenuItemsViewHolder.LAYOUT -> MenuItemsViewHolder(parent, userSession,
                trackingListener,
                sellerMenuTracker
            )
            MenuItemsViewHolder.LAYOUT_NO_ICON -> MenuItemsViewHolder(
                parent,
                userSession,
                trackingListener,
                sellerMenuTracker
            )
            SettingTitleMenuViewHolder.LAYOUT -> SettingTitleMenuViewHolder(parent)
            SellerMenuTitleViewHolder.SECTION_WITH_CTA_LAYOUT -> SellerMenuTitleViewHolder(
                parent,
                sellerMenuTracker
            )
            SellerMenuTitleViewHolder.SECTION_OTHER_LAYOUT -> SellerMenuTitleViewHolder(
                parent,
                sellerMenuTracker
            )
            SellerSettingsTitleViewHolder.LAYOUT -> SellerSettingsTitleViewHolder(parent)
            LoadingViewholder.LAYOUT -> SellerLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
    override fun type(shopInfoUiModel: com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel): Int {
        return ShopInfoViewHolder.LAYOUT
    }

    override fun type(shopInfoLoadingUiModel: com.tokopedia.seller.menu.presentation.uimodel.ShopInfoLoadingUiModel): Int {
        return ShopInfoLoadingViewHolder.LAYOUT
    }

    override fun type(shopInfoErrorUiModel: com.tokopedia.seller.menu.presentation.uimodel.ShopInfoErrorUiModel): Int {
        return ShopInfoErrorViewHolder.LAYOUT
    }

    override fun type(shopOrderUiModel: com.tokopedia.seller.menu.presentation.uimodel.ShopOrderUiModel): Int {
        return ShopOrderViewHolder.LAYOUT
    }

    override fun type(shopProductUiModel: com.tokopedia.seller.menu.presentation.uimodel.ShopProductUiModel): Int {
        return ShopProductViewHolder.LAYOUT
    }

    override fun type(sellerFeatureUiModel: com.tokopedia.seller.menu.presentation.uimodel.SellerFeatureUiModel): Int {
        return SellerFeatureViewHolder.LAYOUT
    }

    override fun type(sectionTitleUiModel: SectionTitleUiModel): Int {
        return SellerMenuTitleViewHolder.getLayout(sectionTitleUiModel.type)
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