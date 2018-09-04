package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R

class SettingListLoadingPaymentViewHolder(view : View?) : AbstractViewHolder<LoadingModel>(view) {
    override fun bind(element: LoadingModel?) {
        //do nothing
    }

    companion object {
        val LAYOUT = R.layout.item_shimmering_list_card_payment
    }
}