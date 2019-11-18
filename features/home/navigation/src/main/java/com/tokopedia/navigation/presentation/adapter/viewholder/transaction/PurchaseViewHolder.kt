package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.PurchaseNotification

class PurchaseViewHolder(view: View): AbstractViewHolder<PurchaseNotification>(view) {

    override fun bind(element: PurchaseNotification) {
        Log.d("TAG Purchase", element.title)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_purchese

        fun create(parent: ViewGroup): PurchaseViewHolder {
            val layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_product_recommendation,
                            parent,
                            false)
            return PurchaseViewHolder(layout)
        }
    }

}