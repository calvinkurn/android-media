package com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getTime
import kotlinx.android.synthetic.main.item_umrah_checkout_pilgrims_filled.view.*

/**
 * @author by firman on 27/11/2019
 */

class UmrahPilgrimsFilledViewHolder(view: View, private val umrahCheckoutPilgrimsListListener: UmrahCheckoutPilgrimsListListener ) : RecyclerView.ViewHolder(view) {


    fun bind(categories: UmrahCheckoutPilgrims) {
        with(itemView) {
            @Suppress("DEPRECATION")
            iv_widget_umrah_checkout_green_rect_action.setImageDrawable(resources.getDrawable(R.drawable.umrah_ic_pencil))
            tg_widget_umrah_checkout_green_rect_title.text =  resources.getString(R.string.umrah_checkout_pilgrims_title,categories.title,categories.firstName,categories.lastName)
            tg_widget_umrah_checkout_green_rect_first_line.text = getTime(UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT,categories.dateBirth)
            container_widget_umrah_checkout_green_rect.setOnClickListener {
                @Suppress("DEPRECATION")
                umrahCheckoutPilgrimsListListener.onPilgrimsClick(position)
            }
        }
    }

    interface UmrahCheckoutPilgrimsListListener {
        fun onPilgrimsClick(position: Int)
    }
}