package com.tokopedia.flight.promo_chips.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.promo_chips.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.model.AirlinePrice
import kotlinx.android.synthetic.main.item_flight_promo_chips.view.*

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsViewHolder (itemView: View, private val onFlightPromoChipsListener: OnFlightPromoChipsListener)
: AbstractViewHolder<AirlinePrice>(itemView) {

    lateinit var adapter: FlightPromoChipsAdapter
    var lastID: String = ""

    companion object {
        val LAYOUT = R.layout.item_flight_promo_chips
    }

    override fun bind(element: AirlinePrice) {
        with(itemView){
            tv_flight_promo_chips_price.text = element.price
            iv_multiairline_logo.setAirlineLogo(null)
            card_promo_chips.setOnClickListener {
                if (lastID != element.airlineID){
                    selected()
                    onFlightPromoChipsListener.onItemClicked(element, adapterPosition)
                } else{
                  unselect()
                }
                lastID = element.airlineID
            }
        }
    }

    fun selected(){
        itemView.card_promo_chips.changeTypeWithTransition(3)
    }

    fun unselect(){
        itemView.card_promo_chips.changeTypeWithTransition(1)
    }

    interface OnFlightPromoChipsListener{
        fun onItemClicked(airlinePrice: AirlinePrice, adapterPosition: Int)
    }
}