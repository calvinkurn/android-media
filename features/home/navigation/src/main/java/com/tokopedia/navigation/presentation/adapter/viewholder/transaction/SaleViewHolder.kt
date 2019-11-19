package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.SaleNotification

class SaleViewHolder(view: View): AbstractViewHolder<SaleNotification>(view) {

    override fun bind(element: SaleNotification) {
        Log.d("TAG Sale", element.title)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_sale
    }

}