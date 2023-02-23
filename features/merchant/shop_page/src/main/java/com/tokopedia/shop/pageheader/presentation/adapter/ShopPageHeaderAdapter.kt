package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.uimodel.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PLAY

class ShopPageHeaderAdapter(
    typeFactory: ShopPageHeaderAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHeaderAdapterTypeFactory>(typeFactory) {

    init {
        typeFactory.attachAdapter(this)
    }

    private var adapterPageHeaderActionButtonWidget: ShopPageHeaderActionButtonWidgetAdapter? = null

    fun setAdapterWidgetButton(adapterPageHeaderActionButtonWidget: ShopPageHeaderActionButtonWidgetAdapter) {
        this.adapterPageHeaderActionButtonWidget = adapterPageHeaderActionButtonWidget
    }

    fun setData(data: List<ShopPageHeaderWidgetUiModel>) {
        addElement(data)
        notifyDataSetChanged()
    }

    fun isFollowButtonPlaceholderAvailable(): Boolean {
        return getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopPageHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW) != null
    }

    fun isPlayWidgetPlaceholderAvailable(): Boolean? {
        return getWidgetUiModel(SHOP_PLAY)?.getComponentUiModel<ShopPageHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY) != null
    }

    private fun getWidgetUiModel(
        widgetName: String
    ): ShopPageHeaderWidgetUiModel? {
        return visitables.filterIsInstance<ShopPageHeaderWidgetUiModel>().firstOrNull {
            it.name.equals(widgetName, true)
        }
    }

    private inline fun <reified T : BaseShopPageHeaderComponentUiModel> ShopPageHeaderWidgetUiModel.getComponentUiModel(
        componentName: String
    ): T? {
        return this.componentPages.filterIsInstance<T>().firstOrNull {
            it.name.equals(componentName, true)
        }
    }

    fun setLoadingFollowButton(loading: Boolean) {
        getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopPageHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW)?.apply {
            this.isButtonLoading = loading
            adapterPageHeaderActionButtonWidget?.notifyButtonWidgetAdapter()
        }
    }

    fun setFollowButtonData(
        label: String? = null,
        leftDrawableUrl: String? = null,
        isFollowing: Boolean? = null,
        isNeverFollow: Boolean? = null
    ) {
        getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopPageHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW)?.apply {
            label?.let {
                this.label = it
            }
            leftDrawableUrl?.let {
                this.leftDrawableUrl = it
            }
            isFollowing?.let {
                this.isFollowing = it
            }
            isNeverFollow?.let {
                this.isNeverFollow = it
            }
            adapterPageHeaderActionButtonWidget?.notifyButtonWidgetAdapter()
        }
    }

    fun getFollowButtonView(): View? {
        return getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopPageHeaderButtonComponentUiModel>(BUTTON_FOLLOW)?.let {
            adapterPageHeaderActionButtonWidget?.getFollowButtonViewHolder(it)
        }
    }

    fun setPlayWidgetData(shopPageHeaderDataModel: ShopPageHeaderDataModel) {
        val widgetUiModel = getWidgetUiModel(SHOP_PLAY)
        widgetUiModel?.getComponentUiModel<ShopPageHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY)?.let {
            it.shopPageHeaderDataModel = shopPageHeaderDataModel
            val playWidgetPosition = visitables.indexOf(widgetUiModel)
            if (playWidgetPosition != -1) {
                notifyItemChanged(playWidgetPosition)
            }
        }
    }

    fun setShopName(shopName: String) {
        val shopBasicInfoWidget = getWidgetUiModel(SHOP_BASIC_INFO)
        shopBasicInfoWidget?.getComponentUiModel<ShopPageHeaderBadgeTextValueComponentUiModel>(SHOP_NAME)?.let {
            it.text.getOrNull(0)?.textHtml = shopName
            val shopBasicInfoWidgetPosition = visitables.indexOf(shopBasicInfoWidget)
            if (shopBasicInfoWidgetPosition != -1) {
                notifyItemChanged(shopBasicInfoWidgetPosition)
            }
        }
    }
}
