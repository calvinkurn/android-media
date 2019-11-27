package com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import kotlinx.android.synthetic.main.item_umrah_checkout_pilgrims.view.*

class UmrahPilgrimsEmptyViewHolder(view: View, val umrahCheckoutPilgrimsListListener: UmrahCheckoutPilgrimsListListener)
    : RecyclerView.ViewHolder(view) {

    fun bind(categories: UmrahCheckoutPilgrims) {
        with(itemView) {
            tg_umrah_checkout_pilgrims_name.text = resources.
                    getString(R.string.umrah_checkout_order_pilgrims_count_list,categories.pilgrimsNumber)
            rl_umrah_checkout_order_pilgrims.setOnClickListener {
                umrahCheckoutPilgrimsListListener.onPilgrimsClick(position)
            }
        }
    }

    interface UmrahCheckoutPilgrimsListListener {
        fun onPilgrimsClick(position: Int)
    }
}