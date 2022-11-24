package com.tokopedia.feedcomponent.shoprecom.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

/**
 * created by fachrizalmrsln on 14/10/22
 */
data class ShopRecomWidgetModel(
    val shopRecomUiModel: ShopRecomUiModel = ShopRecomUiModel(),
    val onError: String = "",
) : Visitable<DynamicFeedTypeFactory> {

    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}
