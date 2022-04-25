package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData

class ProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder) {

    var productBundling: ProductBundlingData = builder.productBundling
        private set

    open class Builder : SendableUiModel.Builder<Builder, ProductBundlingUiModel>() {

        internal var productBundling: ProductBundlingData = ProductBundlingData()

        override fun build(): ProductBundlingUiModel {
            return ProductBundlingUiModel(this)
        }

        fun withProductBundling(productBundling: ProductBundlingData): Builder {
            this.productBundling = productBundling
            return self()
        }
    }

    companion object {
        const val BUNDLE_TYPE_MULTIPLE = 2
    }
}