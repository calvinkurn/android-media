package com.tokopedia.shop.settings.basicinfo.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditMainInfoModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditProfilePictureModel
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditTickerModel

class ShopEditBasicInfoAdapter (
        adapterFactory: ShopEditBasicInfoAdapterFactoryImpl
): BaseListAdapter<Visitable<*>, ShopEditBasicInfoAdapterFactory>(adapterFactory) {
    private fun getVisitablePosition(element: Visitable<*>): Int {
        for (position in 0 until dataSize) {
            return when(element) {
                is ShopEditProfilePictureModel -> position
                is ShopEditTickerModel -> position
                is ShopEditMainInfoModel -> position
                else -> -1
            }
        }
        return  -1
    }
}