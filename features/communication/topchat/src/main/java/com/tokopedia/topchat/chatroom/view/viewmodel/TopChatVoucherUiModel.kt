package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class TopChatVoucherUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory> {

    private val voucherModel: MerchantVoucherModel = builder.voucherModel
    private val isPublic: Int = builder.isPublic
    private val isLockToProduct: Int = builder.isLockToProduct
    val applink: String = builder.applink
    val voucher: MerchantVoucherViewModel = MerchantVoucherViewModel(voucherModel).apply {
        this.isPublic = isPublic()
        this.isLockToProduct = isLockToProduct()
    }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    private fun isPublic(): Boolean {
        return isPublic == 1
    }

    fun isLockToProduct(): Boolean {
        return isLockToProduct == 1
    }

    class Builder : SendableUiModel.Builder<Builder, TopChatVoucherUiModel>() {

        internal lateinit var voucherModel: MerchantVoucherModel
        internal var isPublic: Int = 1
        internal var isLockToProduct: Int = 0
        internal var applink: String = ""

        fun withVoucherModel(voucherModel: MerchantVoucherModel): Builder {
            this.voucherModel = voucherModel
            return self()
        }

        fun withIsPublic(isPublic: Int): Builder {
            this.isPublic = isPublic
            return self()
        }

        fun withIsLockToProduct(isLockToProduct: Int): Builder {
            this.isLockToProduct = isLockToProduct
            return self()
        }

        fun withApplink(applink: String): Builder {
            this.applink = applink
            return self()
        }

        override fun build(): TopChatVoucherUiModel {
            if (!::voucherModel.isInitialized) {
                throw IllegalStateException("MerchantVoucherModel must be initialized")
            }
            return TopChatVoucherUiModel(this)
        }
    }
}
