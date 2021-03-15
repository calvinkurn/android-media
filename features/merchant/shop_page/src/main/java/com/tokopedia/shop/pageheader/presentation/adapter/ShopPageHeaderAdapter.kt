package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_FOLLOW
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel.ComponentName.BUTTON_PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderActionWidgetFollowButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.PLAY
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_ACTION

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
        return getWidgetUiModel(PLAY)?.getComponentUiModel<ShopHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY) != null
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
        val widgetUiModel = getWidgetUiModel(PLAY)
        widgetUiModel?.getComponentUiModel<ShopHeaderPlayWidgetButtonComponentUiModel>(BUTTON_PLAY)?.let {
            it.shopPageHeaderDataModel = shopPageHeaderDataModel
            val playWidgetPosition = visitables.indexOf(widgetUiModel)
            if (playWidgetPosition != -1) {
                notifyItemChanged(playWidgetPosition, shopPageHeaderDataModel)
            }
        }
    }

}