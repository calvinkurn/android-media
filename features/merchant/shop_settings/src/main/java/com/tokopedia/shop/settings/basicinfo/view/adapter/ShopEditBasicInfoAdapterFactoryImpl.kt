package com.tokopedia.shop.settings.basicinfo.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditMainInfoModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditProfilePictureModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditTickerModel
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditMainInfoViewHolder
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditProfilePictureViewHolder
import com.tokopedia.shop.settings.basicinfo.view.viewholder.ShopEditTickerViewHolder

class ShopEditBasicInfoAdapterFactoryImpl(
        private val profilePictureListener: ShopEditProfilePictureViewHolder.ShopEditProfilePictureListener? = null,
        private val tickerListener: ShopEditTickerViewHolder.ShopEditTickerListener? = null,
        private val mainInfoListener: ShopEditMainInfoViewHolder.ShopEditMainInfoListener? = null
) : BaseAdapterTypeFactory(), ShopEditBasicInfoAdapterFactory {
    override fun type(shopEditProfilePictureModel: ShopEditProfilePictureModel): Int = ShopEditProfilePictureViewHolder.LAYOUT

    override fun type(shopEditTickerModel: ShopEditTickerModel): Int = ShopEditTickerViewHolder.LAYOUT

    override fun type(shopEditMainInfoModel: ShopEditMainInfoModel): Int = ShopEditMainInfoViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopEditProfilePictureViewHolder.LAYOUT -> ShopEditProfilePictureViewHolder(parent, profilePictureListener)
            ShopEditTickerViewHolder.LAYOUT -> ShopEditTickerViewHolder(parent, tickerListener)
            ShopEditMainInfoViewHolder.LAYOUT -> ShopEditMainInfoViewHolder(parent, mainInfoListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}