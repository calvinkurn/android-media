package com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDay
import kotlinx.android.synthetic.main.item_umrah_checkout_pilgrims_filled.view.*

class UmrahPilgrimsFilledViewHolder(view: View,val umrahCheckoutPilgrimsListListener: UmrahCheckoutPilgrimsListListener ) : RecyclerView.ViewHolder(view) {


    fun bind(categories: UmrahCheckoutPilgrims) {
        with(itemView) {
            iv_widget_umrah_checkout_green_rect_action.setImageDrawable(resources.getDrawable(R.drawable.umrah_ic_pencil))
            tg_widget_umrah_checkout_green_rect_title.text =  "${categories.title} ${categories.firstName} ${categories.lastName}"
            tg_widget_umrah_checkout_green_rect_first_line.text = getDay("dd MMMM YYYY",categories.dateBirth)
            container_widget_umrah_checkout_green_rect.setOnClickListener {
                umrahCheckoutPilgrimsListListener.onPilgrimsClick(position)
            }
        }
    }

    interface UmrahCheckoutPilgrimsListListener {
        fun onPilgrimsClick(position: Int)
    }
}