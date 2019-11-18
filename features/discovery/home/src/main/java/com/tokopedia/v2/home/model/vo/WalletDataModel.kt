package com.tokopedia.v2.home.model.vo

import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.wallet.WalletData

data class WalletDataModel(
    val walletData: WalletData? = null,
    val status: Resource.Status = Resource.Status.LOADING
) : ModelViewType {
    override fun getPrimaryKey(): Int {
        return 3
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is WalletDataModel
    }
}

class WalletNonLoginDataModel : ModelViewType{
    override fun getPrimaryKey(): Int {
        return 3
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is WalletNonLoginDataModel
    }
}