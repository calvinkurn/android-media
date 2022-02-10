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
    val voucher: MerchantVoucherViewModel = MerchantVoucherViewModel(voucherModel)

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isPublic(): Boolean {
        return isPublic == 1
    }

    class Builder : SendableUiModel.Builder<Builder, TopChatVoucherUiModel>() {

        internal lateinit var voucherModel: MerchantVoucherModel
        internal var isPublic: Int = 1

        fun withVoucherModel(voucherModel: MerchantVoucherModel): Builder {
            this.voucherModel = voucherModel
            return self()
        }

        fun withIsPublic(isPublic: Int): Builder {
            this.isPublic = isPublic
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
