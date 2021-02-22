package com.tokopedia.flight.promo_chips.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.promo_chips.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.model.AirlinePrice
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.item_flight_promo_chips.view.*

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsViewHolder (itemView: View, private val onFlightPromoChipsListener: OnFlightPromoChipsListener)
: AbstractViewHolder<AirlinePrice>(itemView) {

    lateinit var adapter: FlightPromoChipsAdapter

    companion object {
        val LAYOUT = R.layout.item_flight_promo_chips
    }

    override fun bind(element: AirlinePrice) {
        with(itemView){
            tv_flight_promo_chips_price.text = element.price
            tv_flight_promo_chips_line2.text = context.resources.getString(R.string.flight_srp_promo_chips_content_line2, element.shortName)
            iv_multiairline_logo.setAirlineLogo(element.logo)
            setSelectedItem(element)
        }
    }

    private fun changePromoChipsState(selected: Boolean){
        with(itemView){
            if (selected){
                card_promo_chips.changeTypeWithTransition(CardUnify.TYPE_BORDER_ACTIVE)
            }else{
                card_promo_chips.changeTypeWithTransition(CardUnify.TYPE_BORDER)
            }
        }
    }

    private fun setSelectedItem(element: AirlinePrice) {
        with(itemView) {
            if (::adapter.isInitialized) {
                if (adapter.selectedPosition == FlightPromoChipsAdapter.SELECTED_POSITION_INIT) {
                    changePromoChipsState(false)
                } else {
                    if (adapter.selectedPosition == adapterPosition) {
                        changePromoChipsState(true)
                    }
                    else {
                        changePromoChipsState(false)
                    }
                }
                card_promo_chips.setOnClickListener {
                    if (adapter.selectedPosition != adapterPosition) {
                        onFlightPromoChipsListener.onItemClicked(element, adapterPosition)
                        adapter.setSelectedProduct(adapterPosition)
                    }
                }
            }
        }
    }

    interface OnFlightPromoChipsListener{
        fun onItemClicked(airlinePrice: AirlinePrice, adapterPosition: Int)
    }
}