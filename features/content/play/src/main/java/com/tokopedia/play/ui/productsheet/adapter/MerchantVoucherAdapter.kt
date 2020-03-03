package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.MerchantVoucherAdapterDelegate
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel

/**
 * Created by jegul on 03/03/20
 */
class MerchantVoucherAdapter : BaseDiffUtilAdapter<MerchantVoucherUiModel>() {

    init {
        delegatesManager
                .addDelegate(MerchantVoucherAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: MerchantVoucherUiModel, newItem: MerchantVoucherUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MerchantVoucherUiModel, newItem: MerchantVoucherUiModel): Boolean {
        return oldItem == newItem
    }
}