package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.WidgetModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

interface TypeFactoryShopHome {
    fun type(widgetModel: WidgetModel): Int
}