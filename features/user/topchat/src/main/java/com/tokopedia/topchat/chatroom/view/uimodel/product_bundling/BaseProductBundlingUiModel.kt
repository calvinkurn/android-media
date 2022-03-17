package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData

open class BaseProductBundlingUiModel protected constructor(
    builder: Builder
): SendableUiModel(builder) {

    open class Builder : SendableUiModel.Builder<Builder, BaseProductBundlingUiModel>() {
        internal var listBundle: List<ProductBundlingData> = listOf()

        override fun build(): BaseProductBundlingUiModel {
            return BaseProductBundlingUiModel(this)
        }

    }
}