package com.tokopedia.flight.promo_chips.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.ItemFlightPromoChipsBinding
import com.tokopedia.flight.promo_chips.presentation.adapter.FlightPromoChipsAdapter
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsViewHolder (val binding: ItemFlightPromoChipsBinding, private val onFlightPromoChipsListener: OnFlightPromoChipsListener)
: AbstractViewHolder<AirlinePrice>(binding.root) {

    lateinit var adapter: FlightPromoChipsAdapter

    companion object {
        val LAYOUT = R.layout.item_flight_promo_chips
    }

    override fun bind(element: AirlinePrice) {
        with(binding){
            tvFlightPromoChipsPrice.text = element.price
            tvFlightPromoChipsTitle.text = element.shortName
            ivMultiairlineLogo.setAirlineLogo(element.logo)
            setSelectedItem(element)
        }
    }

    private fun changePromoChipsState(selected: Boolean){
        with(binding){
            if (selected){
                cardPromoChips.changeTypeWithTransition(CardUnify.TYPE_BORDER_ACTIVE)
                flightPromochipsSideLine.setLabelType(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }else{
                cardPromoChips.changeTypeWithTransition(CardUnify.TYPE_BORDER)
                flightPromochipsSideLine.setLabelType(Label.GENERAL_DARK_GREEN)
            }
        }
    }

    private fun setSelectedItem(element: AirlinePrice) {
        with(binding) {
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
                cardPromoChips.setOnClickListener {
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