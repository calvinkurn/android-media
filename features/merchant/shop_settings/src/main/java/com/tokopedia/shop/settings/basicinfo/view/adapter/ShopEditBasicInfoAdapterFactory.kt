package com.tokopedia.shop.settings.basicinfo.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditMainInfoModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditProfilePictureModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditTickerModel

interface ShopEditBasicInfoAdapterFactory: AdapterTypeFactory {
    fun type(shopEditProfilePictureModel: ShopEditProfilePictureModel): Int
    fun type(shopEditTickerModel: ShopEditTickerModel): Int
    fun type(shopEditMainInfoModel: ShopEditMainInfoModel): Int
}