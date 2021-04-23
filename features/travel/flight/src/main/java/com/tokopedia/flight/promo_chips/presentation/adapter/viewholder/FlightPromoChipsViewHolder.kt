package com.tokopedia.flight.promo_chips.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.promo_chips.presentation.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label
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
            tv_flight_promo_chips_title.text = element.shortName
            iv_multiairline_logo.setAirlineLogo(element.logo)
            setSelectedItem(element)
        }
    }

    private fun changePromoChipsState(selected: Boolean){
        with(itemView){
            if (selected){
                card_promo_chips.changeTypeWithTransition(CardUnify.TYPE_BORDER_ACTIVE)
                flight_promochips_side_line.setLabelType(com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }else{
                card_promo_chips.changeTypeWithTransition(CardUnify.TYPE_BORDER)
                flight_promochips_side_line.setLabelType(Label.GENERAL_DARK_GREEN)
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
                    }else{
                        adapter.selectedPosition = FlightPromoChipsAdapter.SELECTED_POSITION_INIT
                        changePromoChipsState(false)
                        onFlightPromoChipsListener.onItemUnselected()
                    }
                }
            }
        }
    }

    interface OnFlightPromoChipsListener{
        fun onItemClicked(airlinePrice: AirlinePrice, adapterPosition: Int)
        fun onItemUnselected()
    }
}