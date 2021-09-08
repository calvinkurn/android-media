package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.uimodel.component.*
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.SHOP_NAME
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO

class ShopPageHeaderAdapter(
        typeFactory: ShopPageHeaderAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHeaderAdapterTypeFactory>(typeFactory) {

    init {
        typeFactory.attachAdapter(this)
    }

    private var adapterActionButtonWidget: ShopActionButtonWidgetAdapter? = null

    fun setAdapterWidgetButton(adapterActionButtonWidget: ShopActionButtonWidgetAdapter) {
        this.adapterActionButtonWidget = adapterActionButtonWidget
    }

    fun setData(data: List<ShopHeaderWidgetUiModel>) {
        addElement(data)
        notifyDataSetChanged()
    }

    fun isFollowButtonPlaceholderAvailable(): Boolean {
        return getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW) != null
    }

    fun isPlayWidgetPlaceholderAvailable(): Boolean? {
        return getWidgetUiModel(SHOP_PLAY)?.getComponentUiModel<ShopHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY) != null
    }


    private fun getWidgetUiModel(
            widgetName: String
    ): ShopHeaderWidgetUiModel? {
        return visitables.filterIsInstance<ShopHeaderWidgetUiModel>().firstOrNull {
            it.name.equals(widgetName, true)
        }
    }

    private inline fun <reified T : BaseShopHeaderComponentUiModel> ShopHeaderWidgetUiModel.getComponentUiModel(
            componentName: String
    ): T? {
        return this.components.filterIsInstance<T>().firstOrNull {
            it.name.equals(componentName, true)
        }
    }

    fun setLoadingFollowButton(loading: Boolean) {
        getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW)?.apply {
            this.isButtonLoading = loading
            adapterActionButtonWidget?.notifyButtonWidgetAdapter()
        }
    }

    fun setFollowButtonData(
            label: String? = null,
            leftDrawableUrl: String? = null,
            isFollowing: Boolean? = null
    ) {
        getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopHeaderActionWidgetFollowButtonComponentUiModel>(BUTTON_FOLLOW)?.apply {
            label?.let {
                this.label = it
            }
            leftDrawableUrl?.let {
                this.leftDrawableUrl = it
            }
            isFollowing?.let {
                this.isFollowing = it
            }
            adapterActionButtonWidget?.notifyButtonWidgetAdapter()
        }
    }

    fun getFollowButtonView(): View? {
        return getWidgetUiModel(SHOP_ACTION)?.getComponentUiModel<ShopHeaderButtonComponentUiModel>(BUTTON_FOLLOW)?.let {
            adapterActionButtonWidget?.getFollowButtonViewHolder(it)
        }
    }

    fun setPlayWidgetData(shopPageHeaderDataModel: ShopPageHeaderDataModel) {
        val widgetUiModel = getWidgetUiModel(SHOP_PLAY)
        widgetUiModel?.getComponentUiModel<ShopHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY)?.let {
            it.shopPageHeaderDataModel = shopPageHeaderDataModel
            val playWidgetPosition = visitables.indexOf(widgetUiModel)
            if (playWidgetPosition != -1) {
                notifyItemChanged(playWidgetPosition)
            }
        }
    }

    fun setShopName(shopName: String) {
        val shopBasicInfoWidget = getWidgetUiModel(SHOP_BASIC_INFO)
        shopBasicInfoWidget?.getComponentUiModel<ShopHeaderBadgeTextValueComponentUiModel>(SHOP_NAME)?.let {
            it.text.getOrNull(0)?.textHtml = shopName
            val shopBasicInfoWidgetPosition = visitables.indexOf(shopBasicInfoWidget)
            if (shopBasicInfoWidgetPosition != -1) {
                notifyItemChanged(shopBasicInfoWidgetPosition)
            }
        }
    }

}